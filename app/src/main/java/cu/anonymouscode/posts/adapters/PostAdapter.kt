package cu.anonymouscode.posts.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cu.anonymouscode.posts.databinding.PostRowLayoutBinding
import cu.anonymouscode.posts.models.Post
import cu.anonymouscode.posts.util.DefaultDiffUtil
import kotlinx.android.synthetic.main.contact_row_layout.view.*

class PostAdapter : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    private var posts = emptyList<Post>()

    class MyViewHolder(private val binding: PostRowLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.post = post
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PostRowLayoutBinding.inflate(layoutInflater, parent, false)

                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPost = posts[position]
        holder.bind(currentPost)
    }

    //Compara su ha existido un cambio en el la lista de Comentarios,
    // si es asi actuliza la lista que no es mas que la lista, con ayudal del
    //observable
    fun setData(newData: List<Post>){
        val recipesDiffUtil = DefaultDiffUtil(posts, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        posts = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}