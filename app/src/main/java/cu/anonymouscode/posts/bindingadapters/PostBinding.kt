package cu.anonymouscode.posts.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import cu.anonymouscode.posts.models.Post
import cu.anonymouscode.posts.ui.fragment.post.PostFragmentDirections
import cu.anonymouscode.posts.util.NetworkResult

class PostBinding {

    companion object{

        @BindingAdapter("readApiResponse")
        @JvmStatic
        fun errorImageViewVisibility(
                imageView: ImageView,
                apiResponse: NetworkResult<List<Post>>?
        ){

            if(apiResponse is NetworkResult.Error){
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading){
                imageView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success){
                imageView.visibility = View.INVISIBLE
            }

        }

        @BindingAdapter("readApiResponse2")
        @JvmStatic
        fun errorTextViewVisibility(
                textView: TextView,
                apiResponse: NetworkResult<List<Post>>?
        ){

            if(apiResponse is NetworkResult.Error){
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading){
                textView.visibility = View.INVISIBLE
            } else if (apiResponse is NetworkResult.Success){
                textView.visibility = View.INVISIBLE
            }

        }

    }
}