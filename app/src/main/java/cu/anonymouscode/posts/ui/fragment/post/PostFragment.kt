package cu.anonymouscode.posts.ui.fragment.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import cu.anonymouscode.posts.adapters.PostAdapter
import cu.anonymouscode.posts.databinding.FragmentPostBinding
import cu.anonymouscode.posts.util.NetworkResult
import cu.anonymouscode.posts.viewmodels.DefaultViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var defaultViewModel: DefaultViewModel
    private val mAdapter by lazy { PostAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultViewModel = ViewModelProvider(requireActivity()).get(DefaultViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentPostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = defaultViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()

        requestApiData()

        return binding.root
    }

    //Obtengo datos del API, en dependencia de los estados por los que
    //pase la sealed class muestro/oculto efecto
    private fun requestApiData(){
        defaultViewModel.getPosts()
        defaultViewModel.postsResponse.observe(viewLifecycleOwner) { response ->
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
        binding.postsRecyclerView.adapter = mAdapter
        binding.postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect(){
        binding.postsRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding.postsRecyclerView.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}