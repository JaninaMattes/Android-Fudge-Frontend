package com.mobilesystems.feedme

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mobilesystems.feedme.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_shoppingList,
                R.id.navigation_inventorylist,
                R.id.navigation_dashboard,
                R.id.navigation_recipes
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_search_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.user_profile -> {
                navController.navigateUp() // to clear previous navigation history
                navController.navigate(R.id.userProfileFragment)
            }
            R.id.favorite_recipes -> {
                Toast.makeText(this, "Not yet implemented!!", Toast.LENGTH_SHORT).show()
            }
            R.id.more -> {
                Toast.makeText(this, "Not yet implemented!!", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Search functionality
    override fun onQueryTextSubmit(query: String?): Boolean {
        // TODO Add global search for new query
        // This method can be used when a query is submitted eg. creating search history using SQLite DB
        Toast.makeText(this, "Query Inserted", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        // TODO Add global search for new query
        //Filter for text in Fragment Adapter
        // adapter.filter(query)
        return true
    }

    // Barcode Scanning with ZXing
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // This is important, otherwise the result will not be passed to the fragment
        super.onActivityResult(requestCode, resultCode, data)
    }

}