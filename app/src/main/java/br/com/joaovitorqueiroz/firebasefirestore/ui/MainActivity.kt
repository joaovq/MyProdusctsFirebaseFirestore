package br.com.joaovitorqueiroz.firebasefirestore.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.joaovitorqueiroz.firebasefirestore.R
import br.com.joaovitorqueiroz.firebasefirestore.databinding.ActivityMainBinding
import br.com.joaovitorqueiroz.firebasefirestore.extensions.navigateWithAnim

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        The recommended method to create the bindings
//        is to do it while inflating the layout, as shown in the following example:
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//      or
//      val binding: ActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater())
        setupActionbar()
        configNavigation()
    }

    private fun setupActionbar() {
        val toolbar = binding.toolbarMain.toolbar
        toolbar.inflateMenu(R.menu.main_menu)

        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            // supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setIcon(R.drawable.baseline_local_fire_department_24)
        }
    }

    private fun configNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            androidx.navigation.fragment.R.id.nav_host_fragment_container
        ) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navGraph = navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, _, _ ->
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.settings -> {
                navigateUpSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateUpSettings() {
        navController.navigateWithAnim(R.id.settingsFragment)
    }
}