import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tseaafricaapp.R
import com.example.tseaafricaapp.Recipe
import com.example.tseaafricaapp.RecipePage

class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(R.id.tvRecipeName)
        val totalMinutes: TextView = itemView.findViewById(R.id.tvTotalMinutes)
        val totalServings: TextView = itemView.findViewById(R.id.tvTotalServings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.recipeName.text = recipe.name
        holder.totalMinutes.text = "Time: ${recipe.totalMinutes} minutes"
        holder.totalServings.text = "Servings: ${recipe.totalServings}"
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RecipePage::class.java)
            intent.putExtra("RECIPE_ID", recipe.recipeId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recipes.size
}
