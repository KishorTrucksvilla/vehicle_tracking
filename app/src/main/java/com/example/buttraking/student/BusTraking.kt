package com.example.buttraking.student

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentBusTrakingBinding
import com.example.buttraking.dataclass.VehicleDataStudent
import com.example.buttraking.repository.GetDriverStudentdataRepository
import com.example.buttraking.viewmodel.GetDriverLatLongViewModel
import com.example.buttraking.viewmodelfactory.GetDriverStudentdataViewModel
import com.example.buttraking.viewmodelfactory.GetDriverStudentdataViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
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


class BusTraking : Fragment(), OnMapReadyCallback {
    private  lateinit var binding: FragmentBusTrakingBinding
    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModelgetlatlong: GetDriverLatLongViewModel


    private lateinit var viewModelstudnet: GetDriverStudentdataViewModel
    private lateinit var locationCallback: LocationCallback
    private val vehicleMarkers = HashMap<String, Marker>()

    private val markerMap: MutableMap<Int, Marker> = mutableMapOf()
    private val previousLocations: MutableMap<Int, LatLng> = mutableMapOf()
    private val polylines: MutableList<Polyline> = mutableListOf() // List to manage polylines
    private val lastUpdateTimes = mutableMapOf<Int, Long>()
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBusTrakingBinding.bind(inflater.inflate(R.layout.fragment_bus_traking,null))
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
                restartCountdownTimer() // Restart the countdown
                handler.postDelayed(this, updateInterval)
            }
        }
        handler.post(locationUpdater)
    }
    private fun restartCountdownTimer() {
        countDownTimer?.cancel() // Cancel previous timer if running

        countDownTimer = object : CountDownTimer(15000, 1000) { // 15s countdown, updating every second
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.tvCountdownTimer.text = "${secondsRemaining}s" // Update the countdown TextView
            }

            override fun onFinish() {
                binding.tvCountdownTimer.text = "0s" // Ensure it shows 0s when done
            }
        }.start()
    }


    private fun setupViewModelLatLong() {
        if (!isAdded || activity == null) {
            Log.e("ViewModelError", "Fragment is not attached!")
            return
        }

        val apiService = RetrofitHelper.getApiService()
        val repository = GetDriverStudentdataRepository(apiService)
        val factory = GetDriverStudentdataViewModelFactory(repository)
        viewModelstudnet =
            ViewModelProvider(this, factory).get(GetDriverStudentdataViewModel::class.java)

        val schoolId = SchoolId().getSchoolId(requireContext())
        val  studenId = StudentInfo().getStudentId(requireContext())

        viewModelstudnet.fetchLatLongStudent(schoolId.trim(),studenId.trim()) // Fetch API data
        observeLatLong()
    }

    private fun observeLatLong() {
        viewModelstudnet.studentLatLong.observe(viewLifecycleOwner, Observer { response ->
            Log.d("LatLongData", "Raw response: $response")
            response?.let {
                if (it.status && !it.data.isNullOrEmpty()) {
                    updateMarkers(it.data)

                    // Get the first available vehicle safely
                    val latestVehicle = it.data.firstOrNull()

                    if (latestVehicle != null) {
                        binding.tvVehicleUpdatedTime.text = latestVehicle.updatedtime ?: "N/A"
                        binding.tvVehicleNumber.text = latestVehicle.vehiclenumber ?: "Unknown"
                    } else {
                        // Handle case when vehicle data is empty or null
                        binding.tvVehicleUpdatedTime.text = "N/A"
                        binding.tvVehicleNumber.text = "Unknown"
                    }
                } else {
                    // Handle case when response is invalid
                    binding.tvVehicleUpdatedTime.text = "N/A"
                    binding.tvVehicleNumber.text = "Unknown"
                }
            } ?: run {
                // If response itself is null
                binding.tvVehicleUpdatedTime.text = "N/A"
                binding.tvVehicleNumber.text = "Unknown"
            }
        })
    }
    private fun updateMarkers(driverList: List<VehicleDataStudent>) {
        if (googleMap == null) {
            Log.e("updateMarkers", "Google Map is null")
            return
        }

        val currentTime = System.currentTimeMillis()
        val locationUpdateThreshold = 15000L // 15 seconds
        val updatedDriverIds = driverList.mapNotNull { it.id.toIntOrNull() }.toSet()

        // Remove markers for drivers not in the updated list
        val iterator = markerMap.keys.iterator()
        while (iterator.hasNext()) {
            val driverId = iterator.next()
            if (driverId !in updatedDriverIds) {
                previousLocations.remove(driverId)
                lastUpdateTimes.remove(driverId)
                markerMap[driverId]?.remove()
                iterator.remove()
            }
        }

        for (driver in driverList) {
            val lat = driver.latitude?.toDoubleOrNull()
            val lng = driver.longitude?.toDoubleOrNull()

            if (lat == null || lng == null) {
                Log.e("updateMarkers", "Invalid lat/lng for driver ${driver.id}, skipping...")
                continue
            }

            val latLng = LatLng(lat, lng)
            val driverId = driver.id.toIntOrNull() ?: continue

            val lastLocation = previousLocations[driverId]
            val lastUpdateTime = lastUpdateTimes[driverId] ?: currentTime
            val elapsedTime = currentTime - lastUpdateTime
            val locationChanged = lastLocation == null || lastLocation != latLng
            val isLocationStale = elapsedTime > locationUpdateThreshold

            // Choose the right icon based on location status
            val iconRes = when {
                !locationChanged && isLocationStale -> R.drawable.travel // Stale location (Bus is stationary)
                driver.driverType == "Bus" -> R.drawable.busyellow // Moving Bus
                driver.driverType == "Auto" -> R.drawable.busyellow // Auto
                else -> R.drawable.busyellow // Default marker
            }

            val bitmap = BitmapFactory.decodeResource(resources, iconRes)
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 50, 120, false)

            googleMap?.setOnMarkerClickListener { marker ->
                val driverId = markerMap.entries.find { it.value == marker }?.key
                val driver = driverList.find { it.id.toIntOrNull() == driverId }

                driver?.let {
                    when (iconRes) {
                        R.drawable.travel -> Toast.makeText(context, "Stop! (${it.driverType})", Toast.LENGTH_SHORT).show()
                        R.drawable.busyellow -> Toast.makeText(context, "School Bus Started! (${it.driverType})", Toast.LENGTH_SHORT).show()
                    }
                }

                false // Allow default behavior
            }

            if (markerMap.containsKey(driverId)) {
                val marker = markerMap[driverId]

                // **Calculate bearing based on movement**
                val bearing = if (lastLocation != null) getBearing(lastLocation, latLng) else 0f // Default to 0°

                if (marker != null) {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))
                    marker.rotation = bearing // **Set marker direction dynamically**
                }

                if (locationChanged) {
                    if (lastLocation != null) {
                        drawPolyline(lastLocation, latLng, if (isLocationStale) Color.GREEN else Color.GREEN)
                    }

                    previousLocations[driverId] = latLng
                    lastUpdateTimes[driverId] = currentTime

                    Handler(Looper.getMainLooper()).postDelayed({
                        animateMarkerPosition(marker!!, latLng, bearing)
                        marker.isFlat = true
                    }, 1000)
                }
            } else {
                // **Set correct rotation from the start**
                val initialBearing = if (lastLocation != null) getBearing(lastLocation, latLng) else 0f

                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(driver.driverType)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))
                    .flat(true)
                    .rotation(initialBearing) // **Set initial direction**

                val newMarker = googleMap?.addMarker(markerOptions)
                newMarker?.let {
                    markerMap[driverId] = it
                    previousLocations[driverId] = latLng
                    lastUpdateTimes[driverId] = currentTime
                }
            }
        }
    }
    fun getColoredBitmap(context: Context, drawableRes: Int, color: Int, width: Int, height: Int): BitmapDescriptor {
        val drawable = ContextCompat.getDrawable(context, drawableRes) ?: return BitmapDescriptorFactory.defaultMarker()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)

        // **Apply the color filter (tint)**
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        drawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    /**
     * Animate marker movement and rotate it towards the upcoming direction.
     */
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
    private fun animateMarkerPosition(marker: Marker, toPosition: LatLng) {
        val startPosition = marker.position
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction
            val lat = (1 - fraction) * startPosition.latitude + fraction * toPosition.latitude
            val lng = (1 - fraction) * startPosition.longitude + fraction * toPosition.longitude
            marker.position = LatLng(lat, lng)
        }
        animator.start()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = false // **Enable blue dot**
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
                        // drawPolyline(lastStoredLocation, newLatLng, Color.YELLOW)
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
            val sharedPref = safeContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putFloat("last_lat", location.latitude.toFloat())
                putFloat("last_lng", location.longitude.toFloat())
                apply()
            }
            Log.d("LocationUpdate", "Saved last known location: $location")
        } ?: Log.e("DashBoard", "Fragment not attached to a context.")
    }

    private fun getLastKnownLocation(): LatLng? {
        return context?.let { safeContext ->
            val sharedPref = safeContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
            val lat = sharedPref.getFloat("last_lat", 0f)
            val lng = sharedPref.getFloat("last_lng", 0f)

            if (lat != 0f && lng != 0f) {
                LatLng(lat.toDouble(), lng.toDouble()).also {
                    Log.d("LocationUpdate", "Retrieved last known location: $it")
                }
            } else {
                Log.d("LocationUpdate", "No last known location found")
                null
            }
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