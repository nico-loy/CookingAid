package at.fhj.ima.cookingaid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import at.fhj.ima.cookingaid.bindingadapters.RecipesRowBinding
import at.fhj.ima.cookingaid.databinding.RecipeRowLayoutBinding
import at.fhj.ima.cookingaid.models.FoodRecipe
import at.fhj.ima.cookingaid.models.Result
import at.fhj.ima.cookingaid.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes = emptyList<at.fhj.ima.cookingaid.models.Result>()

    class MyViewHolder(private val binding: RecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(result: at.fhj.ima.cookingaid.models.Result) {
                binding.result = result //variable von xml bind
                binding.executePendingBindings()
            }

            companion object {
                fun from(parent: ViewGroup): MyViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = RecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                    return MyViewHolder(binding)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: FoodRecipe){
        val recipesDiffUtil = RecipesDiffUtil(recipes, newData.results) //anstatt NotififyDataSetChanged, updated nur die Views die nicht gleich sind
        val diffUtilResult = DiffUtil.calculateDiff((recipesDiffUtil))
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)

    }
}