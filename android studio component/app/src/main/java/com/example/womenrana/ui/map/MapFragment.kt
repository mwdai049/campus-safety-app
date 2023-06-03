package com.example.womenrana.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.womenrana.R
import com.example.womenrana.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MapFragment : Fragment(), OnMapReadyCallback {


    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (San Diego, CA) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(32.8801, -117.2340)
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }


        Places.initialize(activity?.applicationContext, getString(R.string.maps_api_key))
        placesClient = Places.createClient(activity?.applicationContext)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity().applicationContext)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        map?.uiSettings?.isCompassEnabled = true;
        map?.uiSettings?.isZoomControlsEnabled = true;


        val callBoxesButton: ToggleButton = root.findViewById(R.id.callBoxesButton) as ToggleButton
        var boxButtonStat: Boolean = callBoxesButton.isChecked
        if(!boxButtonStat){
            callBoxesButton.setBackgroundColor(Color.parseColor("#dee7e3"))
            callBoxesButton.setTextColor(Color.BLACK)
        }
        val findRouteButton: ImageButton = root.findViewById(R.id.findRouteButton) as ImageButton

        if(!boxButtonStat){
            callBoxesButton.setBackgroundColor(Color.parseColor("#dee7e3"))
            callBoxesButton.setTextColor(Color.BLACK)
        }

        callBoxesButton.setOnClickListener {
            boxButtonStat = !boxButtonStat
            if(boxButtonStat){
                callBoxesButton.setBackgroundColor(Color.parseColor("#6D77A6"))
                callBoxesButton.setTextColor(Color.parseColor("#F0F3F2"))

                for (i in CALLBOXLATLNGS.indices){
                    map?.addMarker(
                        MarkerOptions()
                            .position(CALLBOXLATLNGS[i])
                            .title(CALLBOXNAMES[i])
                    )
                }

                findRouteButton.visibility = View.VISIBLE

                if(lastKnownLocation == null){
                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15.0f))
                }
                else{
                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude), 15.0f))
                }




            }
            else{
                callBoxesButton.setBackgroundColor(Color.parseColor("#dee7e3"))
                callBoxesButton.setTextColor(Color.BLACK)
                map?.clear()
                findRouteButton.visibility = View.INVISIBLE
            }
        }

        /*val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(OnMapReadyCallback {
            fun onMapReady(googleMap: GoogleMap){
                googleMap.setOnMapClickListener (GoogleMap.OnMapClickListener(){
                    fun onMapClick(latLng: LatLng){
                        val markerOptions = MarkerOptions();
                        markerOptions.position(latLng)
                        markerOptions.title((latLng.latitude as String).plus(" : ").plus(latLng.longitude as String))
                        googleMap.clear()
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            latLng, 10F
                        ))
                        googleMap.addMarker(markerOptions)
                    }
                })
            }
        })*/

        val textView: TextView = binding.textMap
        /*mapViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                    updateLocationUI()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        updateLocationUI()
    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result

                        if (lastKnownLocation != null) {
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }




    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        // ...

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        if (locationPermissionGranted){
            map.isMyLocationEnabled = true
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAG = MapFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
        var CALLBOXLATLNGS = arrayOf(
            LatLng(32.8788827, -117.2323377),
            LatLng(32.8781821, -117.2375546),
            LatLng(32.8758031, -117.2374054),
            LatLng(32.8813858, -117.2375784),
            LatLng(32.8827604, -117.2407143),
            LatLng(32.8788216, -117.2407997),
            LatLng(32.8855568, -117.2406740),
            LatLng(32.8759641, -117.2408109),
            LatLng(32.8758142, -117.2346568),
            LatLng(32.8651637, -117.2538061),
            LatLng(32.8795899, -117.2375645),
            LatLng(32.8810131, -117.2352989),
            LatLng(32.8786887, -117.2357455),
            LatLng(32.8813689, -117.2386275),
            LatLng(32.8806360, -117.2412986)
        )
        var CALLBOXNAMES = arrayOf(
            "Camp Snoopy",
            "Center Hall",
            "CMM E",
            "Geisel Library",
            "Marshall Uppers",
            "Muir",
            "North Campus",
            "Revelle",
            "School of Medicine",
            "SIO",
            "Student Health",
            "Warren Mall",
            "Student Services Center",
            "Marshall Lowers",
            "Sixth College"
        )
    }


}