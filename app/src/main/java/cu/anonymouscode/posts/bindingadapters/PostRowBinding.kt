package cu.anonymouscode.posts.bindingadapters

import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import cu.anonymouscode.posts.ui.fragment.post.PostFragmentDirections

class PostRowBinding {

    companion object{

        //Convierto el id del post a string para poder representarlo en la vista
        @BindingAdapter("setPostId")
        @JvmStatic
        fun setPostId(textView: TextView, postId: Int) {
            textView.text = postId.toString()
        }

        //Click para buscar los cometarios asociados un post en específico
        @BindingAdapter("onPostClickListener")
        @JvmStatic
        fun onPostClickListener(recipeRowLayout: ConstraintLayout, postId: Int){
            recipeRowLayout.setOnClickListener {
                try{
                    val action = PostFragmentDirections.actionPostFragment2ToCommentFragment(1)
                    recipeRowLayout.findNavController().navigate(action)
                }catch (e: Exception){
                    Log.d("onPostClickListener", e.toString())
                }
            }
        }
    }

}