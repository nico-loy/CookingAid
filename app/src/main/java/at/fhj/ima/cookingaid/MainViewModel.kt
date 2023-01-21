package at.fhj.ima.cookingaid

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import at.fhj.ima.cookingaid.models.FoodRecipe
import at.fhj.ima.cookingaid.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    var recipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch{
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.getRecipes(queries) //requesting data from API
                recipeResponse.value = handleFoodRecipesResponse(response) //Funktion von unten für Errors
            }catch (e: Exception) {
                recipeResponse.value = NetworkResult.Error("Recipes not found.")
            }
        }else{
            recipeResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when{
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API KEY Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!) //schon oben gehandelt deswegen !!
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    //returns true when Internet Connection, false when not
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?:return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}