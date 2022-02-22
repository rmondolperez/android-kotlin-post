package cu.anonymouscode.posts.ui.fragment.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import cu.anonymouscode.posts.adapters.CommentAdapter
import cu.anonymouscode.posts.databinding.FragmentCommentBinding
import cu.anonymouscode.posts.util.NetworkResult
import cu.anonymouscode.posts.viewmodels.DefaultViewModel

class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    private lateinit var commentViewModel: DefaultViewModel
    private val mAdapter by lazy { CommentAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commentViewModel = ViewModelProvider(requireActivity()).get(DefaultViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.commentViewModel = commentViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()

        val args = arguments
        val postId: Int? = args?.getInt("postId")

        if (postId != 0){
            requestApiData(postId!!)
        }

        return binding.root
    }

    private fun requestApiData(postId: Int){
        commentViewModel.getComments(postId)
        commentViewModel.commentsResponse.observe(viewLifecycleOwner) { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }

                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

        }
    }

    private fun setupRecyclerView(){
        binding.commentRecyclerView.adapter = mAdapter
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect(){
        binding.commentRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding.commentRecyclerView.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}