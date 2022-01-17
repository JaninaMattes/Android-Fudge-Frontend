package com.mobilesystems.feedme.ui.logout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobilesystems.feedme.LoginActivity
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.domain.model.User
import com.mobilesystems.feedme.ui.authentication.AuthViewModel
import androidx.lifecycle.Observer


class LogoutFragment : Fragment() {

    private val sharedViewModel: AuthViewModel by activityViewModels()
    private lateinit var username: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.logout_fragment, container, false)

        //elements
        val cancelButton: Button = rootView.findViewById(R.id.cancel_logout)
        val logoutButton: Button = rootView.findViewById(R.id.logout_button)

        val userObserver = Observer<User?>{ user: User? ->
            if(user != null){
                username = user.firstName
                password = user.password
                Log.d("Logout", username)
            }
        }

        sharedViewModel.loggedInUser.observe(viewLifecycleOwner, userObserver)

        cancelButton.setOnClickListener {
            val action = LogoutFragmentDirections.actionLogoutFragmentToNavigationDashboard()
            findNavController().navigate(action)
        }

        logoutButton.setOnClickListener{
            sharedViewModel.logout(username, password)
            val intent = Intent(activity?.applicationContext, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //    _binding = null
        // Persist current state
    }

    companion object {
        const val TAG = "LogoutFragment"
        fun newInstance() = LogoutFragment()
    }

}