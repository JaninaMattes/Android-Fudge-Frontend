package com.mobilesystems.feedme.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobilesystems.feedme.R
import dagger.hilt.android.AndroidEntryPoint
/**
 * A simple [Fragment] subclass.
 * Use the [SearchListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class SearchListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_list, container, false)
    }

    companion object {
        const val TAG = "SearchListFragment"
        fun newInstance() = SearchListFragment()
    }
}