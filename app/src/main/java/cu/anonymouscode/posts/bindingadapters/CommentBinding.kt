package cu.anonymouscode.posts.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cu.anonymouscode.posts.models.Comment
import cu.anonymouscode.posts.models.Post
import cu.anonymouscode.posts.util.NetworkResult

class CommentBinding {

    companion object{

        @BindingAdapter("readApiResponse")
        @JvmStatic
        fun errorImageViewVisibility(
                imageView: ImageView,
                apiResponse: NetworkResult<List<Comment>>?
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
                apiResponse: NetworkResult<List<Comment>>?
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