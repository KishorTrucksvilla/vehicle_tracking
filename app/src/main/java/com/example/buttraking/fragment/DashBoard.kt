package com.example.buttraking.fragment

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buttraking.IDDetail.DriverDetail1
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.FragmentDashBoardBinding
import com.example.buttraking.dataclass.VehicleData
import com.example.buttraking.repository.GetDriverLatLongRepository
import com.example.buttraking.repository.PutDriverLatLongRepository
import com.example.buttraking.utils.LocationTrackingService
import com.example.buttraking.viewmodel.GetDriverLatLongViewModel
import com.example.buttraking.viewmodel.PutDriverLatLongViewModel
import com.example.buttraking.viewmodelfactory.GetDriverlatlongViewModelFactory
import com.example.buttraking.viewmodelfactory.PutDriverLatLongViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.HashSet
import java.util.Locale
import java.util.logging.Handler
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class DashBoard : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentDashBoardBinding
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val LOCATION_REFRESH_INTERVAL = 30000
    private lateinit var handler: Handler
    private lateinit var locationRunnable: Runnable
    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null

    private var locationJob: Job? = null

    private var googleMap: GoogleMap? = null

    private lateinit var viewmodelputlatlong: PutDriverLatLongViewModel

    private lateinit var viewModelgetlatlong: GetDriverLatLongViewModel
    private lateinit var locationCallback: LocationCallback
    private val vehicleMarkers = HashMap<String, Marker>()

    private val markerMap: MutableMap<Int, Marker> = mutableMapOf()
    private val previousLocations: MutableList<LatLng> = mutableListOf()
    private var isTracking = false  // Flag to track ON/OFF state
    private var trackingStartTime: Long = 0L
    private val handler1 = android.os.Handler(Looper.getMainLooper())
    private var updateTimerRunnable: Runnable? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val gpsLossTimeout = 15000L // 15 seconds
    private var lastUpdateTime = mutableMapOf<Int, Long>()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentDashBoardBinding.bind(inflater.inflate(R.layout.fragment_dash_board, null))

        CheckRole()

        return binding.root
    }

    private fun CheckRole() {
        when (SchoolId().getLoginRole(requireContext())) {
            "Admin" -> adminDetails()
           /* "Principal" -> principalDetails()
            "Teacher" -> teacherDetails()*/
            "Student" -> studentDetails()
            "Driver" -> DriverDetail()

        }
    }
    private fun studentDetails() {
        binding.fabLocation.visibility = View.GONE

        /*  binding.scrollViewStudent.visibility = View.VISIBLE
          binding.layoutJoiningDate.visibility = View.GONE
          binding.layoutEmployeeId.visibility = View.GONE
          binding.fabLocation.visibility = View.GONE

          val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment)
          if (mapFragment != null) {
              childFragmentManager.beginTransaction().hide(mapFragment).commit()
          }
          showStudentDetail()
          loadAttencesStudent()
          observeHomeworkList()*/
    }

    private fun DriverDetail() {
        // Your existing UI setup code...
        setupviewmodeldriever()
        initializeLocationTracking()
        setupListeners()
        setupViewModelLatLong()
        //observallatlong()
        binding.fabLocation.visibility = View.VISIBLE
      /*  binding.scrollViewAdmin.visibility = View.GONE
        binding.layoutPrinciple.visibility = View.GONE
        binding.EmployeeAttendanceForPrincipal.visibility = View.GONE
        binding.StudentAttendanceForPrincipal.visibility = View.GONE
*/
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment)
        mapFragment?.let {
            childFragmentManager.beginTransaction().show(it).commit()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            val mapFragment =
                childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }


    }


    private fun adminDetails() {
        binding.fabLocation.visibility = View.GONE

    }
        private fun setupListeners() {
            // Set default state to RED (OFF) when the app starts
            binding.fabLocation.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#F44336")) // Default RED (OFF)

            binding.fabLocation.setOnClickListener {
                if (!isGPSEnabled()) {
                    showGPSDisabledAlert()
                    return@setOnClickListener
                }

                if (!isTracking) {
                    isTracking = true  // Only allow state change when awake

                    // Animate button scale effect
                    binding.fabLocation.animate().scaleX(0.7f).scaleY(0.7f).setDuration(100)
                        .withEndAction {
                            binding.fabLocation.animate().scaleX(1f).scaleY(1f).setDuration(100)
                        }.start()

                    trackingStartTime = System.currentTimeMillis() // Store start time

                    binding.fabLocation.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#4CAF50")) // Green (ON)

                    if (binding.fabLocation is FloatingActionButton) {
                        (binding.fabLocation as FloatingActionButton).setImageResource(R.drawable.power)
                    } else {
                        binding.fabLocation.text = "Tracking ON"
                    }

                    startAutoLocationRefresh()
                    showMessage("Auto-Refresh Started")

                    // Start updating the button text with elapsed time
                    updateTimerRunnable = object : Runnable {
                        override fun run() {
                            val elapsedTime = System.currentTimeMillis() - trackingStartTime
                            val seconds = (elapsedTime / 1000) % 60
                            val minutes = (elapsedTime / (1000 * 60)) % 60

                            binding.fabLocation.text = "Tracking ON\n${minutes}m ${seconds}s"
                            handler1.postDelayed(this, 1000) // Update every second
                        }
                    }
                    handler1.post(updateTimerRunnable!!)

                    // **Start spinning animation**
                    binding.fabLocation.animate().rotationBy(360f).setDuration(1000)
                        .setInterpolator(LinearInterpolator()).start()

                } else {
                    // Tracking is ON, now turn it OFF
                    isTracking = false

                    val elapsedTime = System.currentTimeMillis() - trackingStartTime
                    val seconds = (elapsedTime / 1000) % 60
                    val minutes = (elapsedTime / (1000 * 60)) % 60

                    binding.fabLocation.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#F44336")) // Red (OFF)

                    if (binding.fabLocation is FloatingActionButton) {
                        (binding.fabLocation as FloatingActionButton).setImageResource(R.drawable.power)
                    } else {
                        binding.fabLocation.text = "Tracking OFF"
                    }

                    stopAutoLocationRefresh()
                    showMessage("Tracking stopped. Time: ${minutes}m ${seconds}s")

                    // Stop updating timer
                    handler1.removeCallbacks(updateTimerRunnable!!)

                    // Reset button text
                    binding.fabLocation.text = "Tracking OFF"

                    // **Stop spinning animation**
                    binding.fabLocation.animate().rotation(0f).setDuration(500).start()
                }
            }
        }

    // Function to check if GPS is enabled
    private fun isGPSEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // Function to show an alert if GPS is disabled
    private fun showGPSDisabledAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle("GPS Required")
            .setMessage("GPS is turned off. Please enable it to start tracking.")
            .setPositiveButton("Enable GPS") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupviewmodeldriever() {
        val apiService = RetrofitHelper.getApiService()
        val repository = PutDriverLatLongRepository(apiService)
        val factory = PutDriverLatLongViewModelFactory(repository)
        viewmodelputlatlong =
            ViewModelProvider(this, factory)[PutDriverLatLongViewModel::class.java]
    }

    private fun initializeLocationTracking() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            startLocationUpdates()
            startLocationTrackingService()
        }
    }

    private fun startLocationTrackingService() {
        val intent = Intent(requireContext(), LocationTrackingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        } else {
            showMessage("Location permission denied")
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showMessage("Location permission not granted")
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                latitude = it.latitude
                longitude = it.longitude
                if (shouldUpdateLocation(latitude, longitude)) {
                    submitData()
                } else {
                    showMessage("Location unchanged, skipping update.")
                }
            } ?: run {
                showMessage("Could not get last known location. Trying to get current location...")
                requestNewLocationData()
            }
        }.addOnFailureListener { e ->
            showMessage("Failed to get location: ${e.message}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
            interval = 0
            fastestInterval = 0
            setExpirationTime(System.currentTimeMillis() + 5000)
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val newLocation = locationResult.lastLocation

                if (newLocation != null) {
                    val newLatitude = newLocation.latitude
                    val newLongitude = newLocation.longitude

                    if (shouldUpdateLocation(newLatitude, newLongitude)) {
                        latitude = newLatitude
                        longitude = newLongitude
                        submitData()
                    }
                } else {
                    Log.e("GPS Loss", "No new location data received!")
                    playGpsLossSound()
                    startGpsLossCheck() // Trigger GPS loss check immediately
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                if (!locationAvailability.isLocationAvailable) {
                    Log.e("GPS Loss", "GPS signal lost!")
                    playGpsLossSound()
                    startGpsLossCheck()
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun startGpsLossCheck() {
        Log.e("GPS Loss", "GPS signal lost! Retrying location update...")
        playGpsLossSound()
        requestNewLocationData() // Try fetching location again instead of using a timer
    }

    private fun playGpsLossSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.gps_loss_sound) // Ensure file exists in res/raw
        }
        mediaPlayer?.start()
    }

    private fun submitData() {

        val schoolId = SchoolId().getSchoolId(requireContext())
        val Driverid = DriverDetail1().Driverid(requireContext())
        val currentTimeMillis = System.currentTimeMillis()
        val formattedTime = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date(currentTimeMillis)) // Convert to 12-hour format with seconds


        val driverData = mapOf(
            "latitude" to latitude.toString(),
            "longitude" to longitude.toString(),
            "school_id" to schoolId.toString(),
            "id" to Driverid.toString(),
            "timer" to formattedTime // Store formatted time

        )
        Log.d(
            "SubmitData",
            "Submitting driver data - Latitude: $latitude, Longitude: $longitude, School ID: $schoolId, Driver ID: $driverData"
        )



        viewmodelputlatlong.submitLatLongDriver(driverData)

        viewmodelputlatlong.addDriverResponse.observe(viewLifecycleOwner) { response ->
            if (response?.status == true) {
                // Success: Log the message and show it
                //  val successMessage = response?.message ?: "Driver added successfully!"
                //  Log.d("SubmitData", "Success: $successMessage")
                //  showMessage(successMessage)

                // Store the latitude and longitude
                lastLatitude = latitude
                lastLongitude = longitude
            } else {
                // Failure: Log the error message and show it
              /*  val errorMessage = response?.message ?: "Failed to add driver. Unknown error."
                Log.d("SubmitData", "Failed to add driver: $errorMessage")
                showMessage(errorMessage)*/
            }
        }

    }

    private fun shouldUpdateLocation(newLat: Double, newLon: Double): Boolean {
        val threshold = 0.0001 // Adjust if needed
        if (lastLatitude == null || lastLongitude == null) return true
        return (Math.abs(newLat - lastLatitude!!) > threshold || Math.abs(newLon - lastLongitude!!) > threshold)
    }


    private fun startAutoLocationRefresh() {
        locationJob?.cancel() // Cancel any existing job to prevent multiple instances

        locationJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                getCurrentLocation() // Update location
                //  submitData() // Send data to API
                setupViewModelLatLong()

                delay(15000) // Wait 15 seconds before the next execution
            }
        }

        // Start foreground service for tracking when screen is locked
        startLocationTrackingService()
    }

    private fun stopAutoLocationRefresh() {
        locationJob?.cancel() // Stops the refresh loop
        locationJob = null

        // Stop foreground service when tracking stops
        val intent = Intent(requireContext(), LocationTrackingService::class.java)
        requireContext().stopService(intent) // Stop the service

        // Remove location updates to stop GPS tracking
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

        // Stop and release the MediaPlayer
        handler1.removeCallbacksAndMessages(null)

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationJob?.cancel() // Cancels the coroutine when the fragment is destroyed
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupViewModelLatLong() {
        if (!isAdded || activity == null) {
            Log.e("ViewModelError", "Fragment is not attached!")
            return
        }

        val apiService = RetrofitHelper.getApiService()
        val repository = GetDriverLatLongRepository(apiService)
        val factory = GetDriverlatlongViewModelFactory(repository)
        viewModelgetlatlong =
            ViewModelProvider(this, factory).get(GetDriverLatLongViewModel::class.java)

        val schoolId = SchoolId().getSchoolId(requireContext())
        val driverId = DriverDetail1().Driverid(requireContext())

        viewModelgetlatlong.fetchlatlong(schoolId.trim(), driverId.trim()) // Fetch API data
        observeLatLong() // Call AFTER initializing viewModelgetlatlong
    }

    private fun observeLatLong() {
        viewModelgetlatlong.latlong.observe(viewLifecycleOwner, Observer { response ->
            Log.d("LatLongData", "Raw response: $response")
            response?.let {
                if (it.status && it.data!!.isNotEmpty()) {
                    updateMarkers(it.data) // Update markers instead of clearing
                }
            }
        })
    }



    private fun updateMarkers(driverList: List<VehicleData>) {
        if (googleMap == null || driverList.isEmpty()) {
            Log.e("updateMarkers", "Google Map is null or driverList is empty")
            return
        }

        val currentTime = System.currentTimeMillis()

        for (driver in driverList) {
            val latLng = LatLng(driver.latitude, driver.longitude)
            val driverId = driver.id

            // Select icon based on driver_type
            val iconRes = when (driver.driver_type) {
                "Bus" -> R.drawable.busyellow
                "Auto" -> R.drawable.busyellow
                else -> R.drawable.busyellow
            }

            // Resize the icon
            val originalBitmap = BitmapFactory.decodeResource(resources, iconRes)
            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 50, 120, false)

            if (markerMap.containsKey(driverId)) {
                val marker = markerMap[driverId]
                if (marker != null) {
                    val lastLocation = previousLocations.getOrNull(driverId) ?: latLng
                    val locationChanged = lastLocation != latLng

                    if (locationChanged) {
                        if (shouldDrawPolyline(lastLocation, latLng)) {
                            drawPolyline(lastLocation, latLng)
                        }

                        // Update previous locations safely
                        if (driverId < previousLocations.size) {
                            previousLocations[driverId] = latLng
                        } else {
                            previousLocations.add(latLng)
                        }

                        // Calculate bearing for rotation
                        val bearing = getBearing(lastLocation, latLng)

                        // Animate marker movement and rotation
                        animateMarkerPosition(marker, latLng, bearing)

                        // Update last update time
                        lastUpdateTime[driverId] = currentTime
                    }

                    // Update marker icon dynamically
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))
                } else {
                    Log.e("updateMarkers", "Marker for $driverId is null")
                }
            } else {
                // Create and add a new marker
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(driver.driver_type)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))

                val newMarker = googleMap?.addMarker(markerOptions)
                newMarker?.let {
                    markerMap[driverId] = it
                    previousLocations.add(latLng)
                    lastUpdateTime[driverId] = currentTime // Set initial timestamp
                }
            }
        }

        // Start GPS loss check
        //  startGpsLossCheck()
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
            googleMap.isMyLocationEnabled = true
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

                    if (previousLocations.isNotEmpty()) {
                        val lastLocation =
                            previousLocations.last() // Only access last() if not empty
                        if (shouldDrawPolyline(lastLocation, newLatLng)) {
                            drawPolyline(lastLocation, newLatLng)
                            Log.d(
                                "LocationUpdate",
                                "Polyline drawn from $lastLocation to $newLatLng"
                            )
                        }
                    }

                    previousLocations.add(newLatLng) // Ensure this is a MutableList<LatLng>
                    saveLastKnownLocation(newLatLng)

                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15f))
                    Log.d("LocationUpdate", "Camera moved to $newLatLng")
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun shouldDrawPolyline(lastLocation: LatLng?, newLocation: LatLng): Boolean {
        if (lastLocation == null) return false // No previous location to compare

        val distance = FloatArray(1)
        Location.distanceBetween(
            lastLocation.latitude, lastLocation.longitude,
            newLocation.latitude, newLocation.longitude,
            distance
        )

        Log.d("LocationUpdate", "Distance between locations: ${distance[0]} meters")

        return distance[0] >= 1 // Draw only if distance is >= 1 meter
    }

    private fun drawPolyline(start: LatLng, end: LatLng) {
        if (googleMap == null) return

        googleMap?.addPolyline(
            PolylineOptions()
                .add(start, end)
                .width(8f)
                .color(Color.GREEN)
        )

        // Save the last known location so it doesn't re-draw when unlocking
        saveLastKnownLocation(end)
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
}
