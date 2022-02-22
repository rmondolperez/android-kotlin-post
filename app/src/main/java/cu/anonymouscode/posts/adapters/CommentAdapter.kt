package cu.anonymouscode.posts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cu.anonymouscode.posts.databinding.CommentRowLayoutBinding
import cu.anonymouscode.posts.models.Comment
import cu.anonymouscode.posts.util.DefaultDiffUtil

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    private var comments = emptyList<Comment>()

    class MyViewHolder(private val binding: CommentRowLayoutBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.comment = comment
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CommentRowLayoutBinding.inflate(layoutInflater, parent, false)

                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentComment = comments[position]
        holder.bind(currentComment)
    }

    fun setData(newData: List<Comment>){
        val recipesDiffUtil = DefaultDiffUtil(comments, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        comments = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}