package com.example.womenrana

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.womenrana.databinding.ActivityMainBinding
import com.example.womenrana.ui.dashboard.DashboardFragment
import com.example.womenrana.ui.home.HomeFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), OnMapReadyCallback {



    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sharedPreferences = this.getSharedPreferences("MyContacts", MODE_PRIVATE)



        bottomNav = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.commit {
                        replace<HomeFragment>(R.id.container)
                        setReorderingAllowed(true)
                        supportFragmentManager.saveBackStack("home")
                    }
                }
                R.id.navigation_map -> {
                    supportFragmentManager.commit {
                        replace<com.example.womenrana.ui.map.MapFragment>(R.id.container)
                        setReorderingAllowed(true)
                        supportFragmentManager.saveBackStack("map")
                    }
                }

                R.id.navigation_dashboard -> {
                    supportFragmentManager.commit {
                        replace<DashboardFragment>(R.id.container)
                        setReorderingAllowed(true)
                        supportFragmentManager.saveBackStack("dashboard")
                    }
                }
            }
            true
        }

        /*binding = ActivityMainBinding.inflate(layoutInflater )
        setContentView(binding.root)*/

        /*val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, mapFragment)
            .commit()*/

        /*val fragment = com.example.womenrana.ui.map.MapFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout,fragment)
            .commit()



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/


    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )
    }
}