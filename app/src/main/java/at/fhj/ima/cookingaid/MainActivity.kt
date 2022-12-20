package at.fhj.ima.cookingaid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageButton>(R.id.open_recipe_list).setOnClickListener {
            val intent = Intent(this, RecipeListActivity::class.java)
            startActivity(intent)
        }
    }

}