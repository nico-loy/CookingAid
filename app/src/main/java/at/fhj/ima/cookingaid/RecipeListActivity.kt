package at.fhj.ima.cookingaid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RecipeListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)
    }

    override fun onCreate(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
): View? {
        val view = inflater.inflate(R.layout.activity_recipe_list, container, false)

        return view
    }
}