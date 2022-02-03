package com.mobilesystems.feedme.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobilesystems.feedme.databinding.FragmentSearchListBinding
import dagger.hilt.android.AndroidEntryPoint
/**
 * A simple [Fragment] subclass.
 * Use the [SearchListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class SearchListFragment : Fragment() {

    private val viewModel: SearchSearchViewModel by activityViewModels()

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var adapter: SearchListAdapter

    //view binding
    private var _binding: FragmentSearchListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchListBinding.inflate(inflater, container, false)

        // Setup recycler view
        searchRecyclerView = binding.recyclerviewSearchList
        linearLayoutManager = LinearLayoutManager(context)
        searchRecyclerView.layoutManager = linearLayoutManager
        //adapter = SearchListAdapter(context, dataset, itemClickListener)
        //searchRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SearchListFragment"
        fun newInstance() = SearchListFragment()
    }
}