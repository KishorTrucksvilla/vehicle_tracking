package com.example.buttraking.fragment

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentBustrackingBinding
import com.example.buttraking.dataclass.VehicleDataAdmin
import com.example.buttraking.repository.GetAdmindataRepository
import com.example.buttraking.viewmodel.GetAdmindataViewModel
import com.example.buttraking.viewmodelfactory.ViewModelFactoryAdmindata
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.util.HashMap
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Bustracking : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentBustrackingBinding
    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var viewModelgetlatlong: GetAdmindataViewModel
    private lateinit var locationCallback: LocationCallback
    private val vehicleMarkers = HashMap<String, Marker>()

    private val markerMap: MutableMap<Int, Marker> = mutableMapOf()
    private val previousLocations: MutableMap<Int, LatLng> = mutableMapOf()
    private val polylines: MutableList<Polyline> = mutableListOf() // List to manage polylines
    private val lastUpdateTimes = mutableMapOf<Int, Long>()
    private val lastUpdateTimeMap = mutableMapOf<Int, Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBustrackingBinding.inflate(inflater, container, false)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize Map Fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        startLocationUpdates()
        return binding.root
    }
    private fun startLocationUpdates() {
        val handler = Handler(Looper.getMainLooper())
        val updateInterval = 15000L // Update every 5 seconds

        val locationUpdater = object : Runnable {
            override fun run() {
                setupViewModelLatLong()
                handler.postDelayed(this, updateInterval)
            }
        }
        handler.post(locationUpdater)
    }


    private fun setupViewModelLatLong() {
        if (!isAdded || activity == null) {
            Log.e("ViewModelError", "Fragment is not attached!")
            return
        }

        val apiService = RetrofitHelper.getApiService()
        val repository = GetAdmindataRepository(apiService)
        val factory = ViewModelFactoryAdmindata(repository)
        viewModelgetlatlong =
            ViewModelProvider(this, factory).get(GetAdmindataViewModel::class.java)

        val schoolId = SchoolId().getSchoolId(requireContext())
       // val driverId = DriverDetail1().Driverid(requireContext())

        viewModelgetlatlong.fetcAdmindata(schoolId.trim()) // Fetch API data
        observeLatLong() // Call AFTER initializing viewModelgetlatlong
    }

    private fun observeLatLong() {
        viewModelgetlatlong.Adminrdata.observe(viewLifecycleOwner, Observer { response ->
            Log.d("LatLongData", "Raw response: $response")
            response?.let {
                if (it.status && it.data!!.isNotEmpty()) {
                    updateMarkers(it.data) // Update markers instead of clearing
                }
            }
        })
    }


    private fun updateMarkers(driverList: List<VehicleDataAdmin>) {
        if (googleMap == null || driverList.isEmpty()) {
            Log.e("updateMarkers", "Google Map is null or driverList is empty")
            return
        }

        val currentTime = System.currentTimeMillis()
        val locationUpdateThreshold = 15000L // 15 seconds

        for (driver in driverList) {
            val driverId = driver.id.toIntOrNull()
            if (driverId == null) {
                Log.e("updateMarkers", "Invalid driver ID: ${driver.id}")
                continue
            }

            val lat = driver.latitude?.toDoubleOrNull()
            val lng = driver.longitude?.toDoubleOrNull()

            if (lat == null || lng == null) {
                Log.e("updateMarkers", "Invalid lat/lng for driver $driverId, skipping...")
                continue
            }

            val latLng = LatLng(lat, lng)
            val lastLocation = previousLocations[driverId]

            // Check if the location has changed
            val locationChanged = lastLocation == null || lastLocation != latLng

            // Determine the last update time
            val lastUpdateTime = lastUpdateTimeMap[driverId] ?: currentTime
            val elapsedTime = currentTime - lastUpdateTime

            // Decide the marker icon
            val iconRes = if (!locationChanged && elapsedTime > locationUpdateThreshold) {
                R.drawable.travel // Red marker if location hasn't changed for 15+ seconds
            } else {
                when (driver.driver_type) {
                    "Bus" -> R.drawable.busyellow
                    "Auto" -> R.drawable.busyellow
                    else -> R.drawable.busyellow
                }
            }

            // Resize the icon
            val originalBitmap = BitmapFactory.decodeResource(resources, iconRes)
            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 50, 120, false)

            if (markerMap.containsKey(driverId)) {
                val marker = markerMap[driverId]

                if (marker != null) {
                    // Draw polyline if the location changed
                    if (locationChanged && lastLocation != null) {
                        drawPolyline(lastLocation, latLng, Color.GREEN)
                    }

                    // Calculate bearing if location changed
                    if (locationChanged && lastLocation != null) {
                        val bearing = getBearing(lastLocation, latLng)
                        animateMarkerPosition(marker, latLng, bearing)
                    }

                    // Update marker icon dynamically
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))

                    // Update previous location and timestamp if location changed
                    if (locationChanged) {
                        previousLocations[driverId] = latLng
                        lastUpdateTimeMap[driverId] = currentTime
                    }
                } else {
                    Log.e("updateMarkers", "Marker for driver $driverId is null")
                }
            } else {
                // Create and add a new marker
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(driver.driver_type)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))

                val newMarker = googleMap?.addMarker(markerOptions)
                if (newMarker != null) {
                    markerMap[driverId] = newMarker
                    previousLocations[driverId] = latLng
                    lastUpdateTimeMap[driverId] = currentTime
                } else {
                    Log.e("updateMarkers", "Failed to create marker for driver $driverId")
                }
            }
        }
    }
    private fun animateMarkerPosition(marker: Marker, toPosition: LatLng, bearing: Float) {
        val startPosition = marker.position
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000
        animator.interpolator = LinearInterpolator()

        animator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction
            val lat = (1 - fraction) * startPosition.latitude + fraction * toPosition.latitude
            val lng = (1 - fraction) * startPosition.longitude + fraction * toPosition.longitude
            marker.position = LatLng(lat, lng)
            marker.rotation = bearing // **Rotate smoothly**
        }
        animator.start()
    }

    /**
     * Calculate bearing between two LatLng points.
     */
    private fun getBearing(start: LatLng, end: LatLng): Float {
        val startLat = Math.toRadians(start.latitude)
        val startLng = Math.toRadians(start.longitude)
        val endLat = Math.toRadians(end.latitude)
        val endLng = Math.toRadians(end.longitude)

        val dLon = endLng - startLng

        val y = sin(dLon) * cos(endLat)
        val x = cos(startLat) * sin(endLat) - sin(startLat) * cos(endLat) * cos(dLon)
        val bearing = Math.toDegrees(atan2(y, x)).toFloat()

        return (bearing + 360) % 360 // Ensure positive angle

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = false
            requestLocationUpdates()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val newLatLng = LatLng(location.latitude, location.longitude)
                    Log.d("LocationUpdate", "New Location: $newLatLng")

                    val lastStoredLocation = getLastKnownLocation()

                    if (lastStoredLocation != null && shouldDrawPolyline(lastStoredLocation, newLatLng)) {
                        drawPolyline(lastStoredLocation, newLatLng, Color.YELLOW)
                        Log.d("LocationUpdate", "Polyline drawn from $lastStoredLocation to $newLatLng")
                    }

                    saveLastKnownLocation(newLatLng)
                    // googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15f))
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }


    private fun shouldDrawPolyline(lastLocation: LatLng, newLocation: LatLng): Boolean {
        val distance = FloatArray(1)
        Location.distanceBetween(
            lastLocation.latitude, lastLocation.longitude,
            newLocation.latitude, newLocation.longitude,
            distance
        )

        Log.d("LocationUpdate", "Distance between locations: ${distance[0]} meters")

        return distance[0] >= 5 // Adjust threshold to prevent unnecessary polylines
    }

    private fun drawPolyline(start: LatLng, end: LatLng, color: Int) {
        googleMap?.addPolyline(
            PolylineOptions()
                .add(start, end)
                .width(8f)
                .color(color) // Dynamically set color
        )?.let { polylines.add(it) }
    }


    private fun saveLastKnownLocation(location: LatLng) {
        context?.let { safeContext ->
            val sharedPref =
                safeContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putFloat("last_lat", location.latitude.toFloat())
                putFloat("last_lng", location.longitude.toFloat())
                apply()
            }
        } ?: Log.e("DashBoard", "Fragment not attached to a context.")
    }

    private fun getLastKnownLocation(): LatLng? {
        return context?.let { safeContext ->
            val sharedPref =
                safeContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
            val lat = sharedPref.getFloat("last_lat", 0f)
            val lng = sharedPref.getFloat("last_lng", 0f)
            if (lat != 0f && lng != 0f) LatLng(lat.toDouble(), lng.toDouble()) else null
        } ?: run {
            Log.e("DashBoard", "Fragment not attached to a context.")
            null
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    override fun onDestroyView() {
        super.onDestroyView()
        clearAllPolylines() // Remove polylines when view is destroyed
    }

    override fun onDestroy() {
        super.onDestroy()
        googleMap?.clear() // Clears all markers, polylines, and overlays from the map
        polylines.clear() // Clear the polyline list
    }

    override fun onPause() {
        super.onPause()
        clearAllPolylines() // Remove polylines when fragment is paused
    }

    override fun onDetach() {
        super.onDetach()
        googleMap = null // Release Google Map instance
    }

    private fun clearAllPolylines() {
        for (polyline in polylines) {
            polyline.remove() // Remove each polyline from the map
        }
        polylines.clear() // Clear the list
    }


}