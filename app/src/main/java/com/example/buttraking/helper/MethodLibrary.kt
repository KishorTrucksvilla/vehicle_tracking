package com.example.buttraking.helper

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.print.PrintAttributes
import android.print.PrintManager
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.CONNECTIVITY_SERVICE
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.buttraking.R
import com.example.buttraking.activity.MainActivity
import com.example.buttraking.fragment.enquiry
import java.util.Calendar

class MethodLibrary {
    private var activeDialog: AlertDialog? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressDialog : ProgressDialog
    var isExpanded : Boolean = false
    private lateinit var context: Context
    //    private lateinit var fragment: Fragment
    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null


  /*  fun displayImage(
        imagePath:String,
        imageId: ImageView,
        context: Context
    ) {
        val imageUrl = imagePath
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.malestudent)
            .transform(CircleCrop())
            .into(imageId)
    }

    fun displayImage(
        drawable: Int,
        imageId: ImageView,
        context: Context
    ){
        Glide.with(context)
            .load(drawable)
            .transform(CircleCrop())
            .into(imageId)
    }

    fun displayImageSquare(
        imagePath:String,
        imageId: ImageView,
        context: Context
    ) {
        val imageUrl = imagePath
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.imagenotfound)
            .into(imageId)
    }
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
*/
    //-----------function for convert point value to int(String Form)
    fun formatPercentage(value: String): String {
        return value.toFloat().toInt().toString()
    }

    fun showSetText(text: String, textView: TextView) {
        textView.text = text
    }
    fun showSetText(text: String, textView: EditText) {
        textView.setText(text)
    }

/*
    fun checkPresentStatus(
        todayPresenty: String,
        yesterdayPresenty: String,
        textViewPresentToday: TextView,
        textViewAbsentYesterday: TextView,
        context: Context
    ) {
        fun setAttendanceStatus(
            textView: TextView,
            status: String,
            context: Context
        ) {
            val (statusText, colorResId) = when (status) {
                "P" -> "Present" to R.color.green
                "A" -> "Absent" to R.color.red
                "L" -> "Leave" to R.color.AttencesLeave
                else -> "" to R.color.black
            }
            textView.text = statusText
            textView.setTextColor(getColor(context, colorResId))
        }

        setAttendanceStatus(textViewPresentToday, todayPresenty, context)
        setAttendanceStatus(textViewAbsentYesterday, yesterdayPresenty, context)
    }
*/


/*
    fun circlePrint(
        presentPercentage: String,
        absentPercentage: String,
        leavePercentage: String,
        circularProgressViewPresent: CircularProgressViewPresent,
        circularProgressViewAbsent: CircularProgressViewAbsent,
        circularProgressViewLeave: CircularProgressViewLeave,
        context: Context
    ) {

        if (context is Activity) {
            val activity = context

            Thread {
                for (i in 0..presentPercentage.toInt()) {
                    Thread.sleep(15) // Simulate delay
                    activity.runOnUiThread {
                        circularProgressViewPresent.setProgress(i.toFloat())
                    }
                }
            }.start()

            Thread {
                for (i in 0..absentPercentage.toInt()) {
                    Thread.sleep(15) // Simulate delay
                    activity.runOnUiThread {
                        circularProgressViewAbsent.setProgress(i.toFloat())
                    }
                }
            }.start()

            Thread {
                for (i in 0..leavePercentage.toInt()) {
                    Thread.sleep(15) // Simulate delay
                    activity.runOnUiThread {
                        circularProgressViewLeave.setProgress(i.toFloat())
                    }
                }
            }.start()
        }
    }

    fun circlePrint(
        boysPercentage: String,
        circularProgressViewTotalStudents: CircularProgressViewTotalStudents,
        context: Context
    ) {

        if (context is Activity) {
            val activity = context

            Thread {
                for (i in 0..boysPercentage.toInt()) {
                    Thread.sleep(20) // Simulate delay
                    activity.runOnUiThread {
                        circularProgressViewTotalStudents.setProgress(i.toFloat())
                    }
                }
            }.start()

//            Thread {
//                for (i in 0..girlsPercentage.toInt()) {
//                    Thread.sleep(15) // Simulate delay
//                    activity.runOnUiThread {
//                        circularProgressViewTotalStudents.setProgress(i.toFloat())
//                    }
//                }
//            }.start()

        }
    }

*/
    fun vibrate(context: Context, duration: Long = 300, delay: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val pattern = longArrayOf(0, duration, delay, duration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createWaveform(pattern, -1) // -1 means no repeat
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(pattern, -1)
        }
    }

    fun activityChanger(activityClass : AppCompatActivity, context: Context)
    {
        context.startActivity(Intent(context, activityClass::class.java))
        (context as AppCompatActivity).finish()
    }

    fun fragmentChanger(secondFragment: Fragment, context: Context) {
        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, secondFragment)
            .commit()
    }

    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String,
        positiveButtonAction: () -> Unit,
        negativeButtonText: String? = null,
        negativeButtonAction: (() -> Unit)? = null,
        cancelable: Boolean = false
    ) {
        // Check if the context is still valid (to prevent window leaks)
        if (context is Activity && context.isFinishing) {
            return // Don't show the dialog if the activity is finishing
        }

        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(cancelable)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog properly
                positiveButtonAction()
            }

        if (negativeButtonText != null && negativeButtonAction != null) {
            builder.setNegativeButton(negativeButtonText) { dialog, _ ->
                dialog.dismiss() // Dismiss before calling action
                negativeButtonAction()
            }
        }

        val dialog = builder.create()

    //    dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialog.setOnDismissListener {
            // Optional: Handle any cleanup if needed when the dialog is dismissed
        }

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
           // ?.setBackgroundResource(R.drawable.button_background)

        negativeButtonText?.let {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
             //   ?.setBackgroundResource(R.drawable.button_background)
        }
    }
    fun dismissActiveDialogs() {
        activeDialog?.dismiss()
        activeDialog = null
    }



    fun showConfirmationDialog(
        context: Context,
        title: String,
        positiveButtonText: String,
        positiveButtonAction: () -> Unit,
        negativeButtonText: String? = null,
        negativeButtonAction: (() -> Unit)? = null,
        cancelable: Boolean = false
    ) {
        val builder = AlertDialog.Builder(context)

        // Set the custom title for the dialog
        builder.setTitle(title)

        // Set the cancelable property
        builder.setCancelable(cancelable)

        // Set the positive button with custom drawable and action
        builder.setPositiveButton(positiveButtonText) { dialog, which ->
            positiveButtonAction()
        }

        // If a negative button is provided, set it with the custom drawable and action
        if (negativeButtonText != null && negativeButtonAction != null) {
            builder.setNegativeButton(negativeButtonText) { dialog, which ->
                negativeButtonAction()
            }
        }

        // Create the dialog
        val dialog = builder.create()

      //  dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialog.show()

       // dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.button_background)

        // If a negative button exists, apply the custom background to it
        negativeButtonText?.let {
           // dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.button_background)
        }
    }

    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String,
        positiveButtonAction: () -> Unit,
        negativeButtonText: String? = null,
        negativeButtonAction: (() -> Unit)? = null,
        cancelable: Boolean = true,
        cancelableAction: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(cancelable)
            .setPositiveButton(positiveButtonText) { dialog, which ->
                // Perform action for the positive button
                positiveButtonAction()
            }

        // If a negative button is provided, set it
        if (negativeButtonText != null && negativeButtonAction != null) {
            builder.setNegativeButton(negativeButtonText) { dialog, which ->
                // Perform action for the negative button
                negativeButtonAction()
            }
        }

        // Create the dialog
        val dialog = builder.create()

        // Apply custom background to the dialog
     //   dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialog.show()

        // Apply custom background to the positive button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
          //  .setBackgroundResource(R.drawable.button_background)

        // Apply custom background to the negative button, if present
        negativeButtonText?.let {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
              //  .setBackgroundResource(R.drawable.button_background)
        }

        // Handle the cancel action (tap outside or back press)
        dialog.setOnCancelListener {
            cancelableAction() // This is the action you want to perform on cancel
            // Toast.makeText(context, "Dialog was canceled", Toast.LENGTH_SHORT).show()
        }
    }




    fun replaceFragment(
        fragment: Fragment,
        context: Context
    ) {

        val fragmentManager = (context as AppCompatActivity).supportFragmentManager

        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container, fragment)

        fragmentTransaction.commit()
    }

    fun clearSharedprepared(
        PreparedName: String,
        context: Context
    ){
        sharedPreferences = context.getSharedPreferences(PreparedName, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    fun startLoadingBar(massage:String, cancelable: Boolean, context: Context){
        this.context = context
        try {
            progressDialog = ProgressDialog(context)
            progressDialog.setMessage(massage)
            progressDialog.setCancelable(cancelable)
            progressDialog.show()
        }catch (e : Exception){
            showConfirmationDialog(
                context = context,
                title = "Warning !",
                message = "Unknown Error found Please Contact with developer nurkude@gmail.com",
                positiveButtonText = "OK",
                positiveButtonAction = { activityChanger(MainActivity(), context) },
                negativeButtonText = "",
                negativeButtonAction = { },
                cancelable = false
            )
        }

    }

    fun stopLoadingBar(){
        try {
            progressDialog.dismiss()
        }catch (e : Exception){
            showConfirmationDialog(
                context = context,
                title = "Warning !",
                message = "Unknown Error found Please Contact with developer nurkude@gmail.com",
                positiveButtonText = "OK",
                positiveButtonAction = { activityChanger(MainActivity(), context) },
                negativeButtonText = "",
                negativeButtonAction = { },
                cancelable = false
            )
        }

    }



    /* fun convertImageToBase64(file: File): String {
         val bitmap = BitmapFactory.decodeFile(file.path)
         val byteArrayOutputStream = ByteArrayOutputStream()
         bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
         val byteArray = byteArrayOutputStream.toByteArray()
         return Base64.encodeToString(byteArray, Base64.NO_WRAP)
     }
 */

//    private fun AnimatedLayout(
//        linearLayout: View,
//        startLayoutHeight: Int,
//        targetLayoutHeight: Int,
//        timeDuration: Long
//    ) {
//        val startHeight = linearLayout.height
//        val targetHeight: Int
//        var isExpanded: Boolean = glfalse
//
//        // Toggle between expanded and collapsed states
//        if (isExpanded) {
//            targetHeight = (resources.displayMetrics.density * startLayoutHeight).toInt()
//            binding.showmypersonalinfo.text = "Show My Personal Information"
//        } else {
//            targetHeight = (resources.displayMetrics.density * targetLayoutHeight).toInt()
//            binding.showmypersonalinfo.text = "Hide My persional infromation"
//        }
//
//        val animator = ValueAnimator.ofInt(startHeight, targetHeight)
//        animator.duration = timeDuration
//        animator.addUpdateListener { animation ->
//            val value = animation.animatedValue as Int
//            val params = linearLayout.layoutParams
//            params.height = value
//            linearLayout.layoutParams = params
//        }
//        animator.start()
//        isExpanded = !isExpanded
//    }


    //    private fun circlePresentPrincipal(
//        PresentPercentage: String,
//        AbsentPercentage: String,
//        LeavePercentage: String,
//        circularProgressViewPresent:CircularProgressViewPresent,
//        circularProgressViewAbsent:CircularProgressViewAbsent,
//        circularProgressViewLeave:CircularProgressViewLeave,
//    ){
//
//        Thread {
//            for (i in 0..PresentPercentage.toInt()) {
//                Thread.sleep(15) // Simulate delay0
//                requireActivity().runOnUiThread {
//                    circularProgressViewPresent.setProgress(i.toFloat())
//                }
//            }
//        }.start()
//
//        Thread {
//            for (i in 0..AbsentPercentage.toInt()) {
//                Thread.sleep(15) // Simulate delay0
//                requireActivity().runOnUiThread {
//                    circularProgressViewAbsent.setProgress(i.toFloat())
//                }
//            }
//        }.start()
//
//        Thread {
//            for (i in 0..LeavePercentage.toInt()) {
//                Thread.sleep(15) // Simulate delay0
//                requireActivity().runOnUiThread {
//                    circularProgressViewLeave.setProgress(i.toFloat())
//                }
//            }
//        }.start()
//    }


    fun toggleLayoutExpansion(
        AnimationLayout: LinearLayout,
        PrimaryText: TextView,
        PrimaryTextMessage: String,
        SecondText: TextView,
        SecondTextMessage: String,
        startingHeight: Int,
        EndingHeight: Int,
        Timing: Long,
        DefaultValue: Boolean,
        context: Context
    ) {
        // Starting height of the layout
        val startHeight = AnimationLayout.height
        val targetHeight: Int

        // Determine target height based on defaultValue
        if (DefaultValue) {
            targetHeight = (context.resources.displayMetrics.density * startingHeight).toInt() // Collapse to 200dp
            PrimaryText.text = PrimaryTextMessage // Update primary text message
        } else {
            // Expand to a new height (1450dp)
            targetHeight = (context.resources.displayMetrics.density * EndingHeight).toInt()
            SecondText.text = SecondTextMessage // Update secondary text message
        }

        // Create the ValueAnimator for height change
        val animator = ValueAnimator.ofInt(startHeight, targetHeight)
        animator.duration = Timing

        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val params = AnimationLayout.layoutParams
            params.height = value
            AnimationLayout.layoutParams = params
        }

        animator.start()
        isExpanded = !isExpanded
    }


    fun toggleLayoutExpansion(
        AnimationLayout: LinearLayout,
        PrimaryText: TextView,
        SecondText: TextView,
        startingHeight: Int,
        EndingHeight: Int,
        Timing: Long,
        defaultValue: Boolean,
        context: Context
    ) {
        // Starting height of the layout
        val startHeight = AnimationLayout.height
        val targetHeight: Int

        // Determine target height based on defaultValue
        if (defaultValue) {
            targetHeight = (context.resources.displayMetrics.density * startingHeight).toInt() // Collapse to 200dp
            // Update primary text message
        } else {
            // Expand to a new height (1450dp)
            targetHeight = (context.resources.displayMetrics.density * EndingHeight).toInt()
            // Update secondary text message
        }

        // Create the ValueAnimator for height change
        val animator = ValueAnimator.ofInt(startHeight, targetHeight)
        animator.duration = Timing

        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val params = AnimationLayout.layoutParams
            params.height = value
            AnimationLayout.layoutParams = params
        }

        animator.start()
        isExpanded = !isExpanded
    }



    fun toggleLayoutExpansion(
        AnimationLayout: LinearLayout,
        PrimaryImage: ImageView,
        PrimaryImageResource: Int,
        SecondaryImage: ImageView,
        SecondaryImageResource: Int,
        startingHeight: Int,
        EndingHeight: Int,
        Timing: Long,
        DefaultValue: Boolean,
        context: Context
    ) {
        // Starting height of the layout
        val startHeight = AnimationLayout.height
        val targetHeight: Int

        // Determine target height based on defaultValue
        if (DefaultValue) {
            targetHeight = (context.resources.displayMetrics.density * startingHeight).toInt() // Collapse to starting height
            PrimaryImage.setImageResource(PrimaryImageResource) // Update primary image
        } else {
            // Expand to a new height (EndingHeight)
            targetHeight = (context.resources.displayMetrics.density * EndingHeight).toInt()
            SecondaryImage.setImageResource(SecondaryImageResource) // Update secondary image
        }

        // Create the ValueAnimator for height change
        val animator = ValueAnimator.ofInt(startHeight, targetHeight)
        animator.duration = Timing

        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val params = AnimationLayout.layoutParams
            params.height = value
            AnimationLayout.layoutParams = params
        }

        animator.start()
        isExpanded = !isExpanded
    }

    fun LayoutMarginTopAnimate(
        layout: LinearLayout,
        TopMargin: Int,
        DelaTime: Long,
        context: Context

    ){
        val topMarginInDp = TopMargin
        val topMarginInPixels = (topMarginInDp * context.resources.displayMetrics.density).toInt()
        val params = layout.layoutParams as ConstraintLayout.LayoutParams

        // Create a ValueAnimator to animate the margin change
        val animator = ValueAnimator.ofInt(params.topMargin, topMarginInPixels)
        animator.duration = DelaTime // Duration in milliseconds
        animator.interpolator = AccelerateDecelerateInterpolator() // Smooth easing

        // Update the top margin in the animation's update listener
        animator.addUpdateListener { animation ->
            // Get the animated value and set it to the topMargin
            val animatedValue = animation.animatedValue as Int
            params.topMargin = animatedValue
            layout.layoutParams = params
        }
        animator.start()
    }
/*
    private fun printSalarySlip(context: Context, employee: AllEmployee, PrintName: String) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = SearchEmpPrintAdapter(context, employee)
        val printAttributes = PrintAttributes.Builder().build()
        printManager.print(PrintName, printAdapter, printAttributes)
    }
*/

    fun SpinnerValidation(
        spinner: Spinner
    ): Boolean {

        if (!spinner.selectedItem.toString().equals(spinner.selectedItem.toString())){
            return true
        }else{
            return false
        }
    }

    fun fullImageActivity(
        imageId: ImageView,
        imageUrl: String,
        context: Context
    ){
      //  context.startActivity(Intent(imageId.context, FullScreenImageActivity::class.java).putExtra("image_url", imageUrl))
    }

    fun openCamera(
        context: Context,
        CAMERA_REQUEST_CODE: Int,
        startActivityForResult: (Intent, Int) -> Unit
    ) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(context.packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(context, "Camera not available", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveDataString(
        UniqueKey: String,
        StringData: String,
        context: Context
    ){
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(UniqueKey, StringData)
        editor.commit()
    }

    fun saveDataBoolean(
        UniqueKey: String,
        BooleanData: Boolean,
        context: Context
    ){
        sharedPreferences = context.getSharedPreferences("SaveBoolean", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(UniqueKey, BooleanData)
        editor.commit()
    }

    fun getDataBoolean(
        UniqueKey: String,
        context: Context
    ): Boolean {
        sharedPreferences = context.getSharedPreferences("SaveBoolean", MODE_PRIVATE)
        return sharedPreferences.getBoolean(UniqueKey, false) ?: false
    }

    fun getDataString(
        UniqueKey: String,
        context: Context
    ): String {
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPreferences.getString(UniqueKey, null) ?: ""
    }

/*
    fun setIconItem(
        menuItem: MenuItem?,
        ImageURL: String,
        context: Context
    ){
        menuItem.let {
            Glide.with(context)
                .load(ImageURL)
                .placeholder(R.drawable.all_messages)
                .transform(CircleCrop())
                .error(R.drawable.attendance_drawable_selected_a)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        if (menuItem != null) {
                            menuItem.icon = resource
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
    }
*/
    fun clicked(
        binding: View,
        action: () -> Unit
    ) {
        binding.setOnClickListener {
            action()
        }
    }

    fun DatePicker(
        edDateOfBirth: EditText,
        context: Context
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            // Format the date as "dd/MM/yyyy"
            val formattedDay = String.format("%02d", selectedDay)
            val formattedMonth = String.format("%02d", selectedMonth + 1)
            //val date = "$formattedDay/$formattedMonth/$selectedYear"
            val date = "$selectedYear-$formattedMonth-$formattedDay"
            edDateOfBirth.setText(date)
//            if (isBirthDate) {
//
//            } else {
//                edDateOfAdmission.setText(date)
//            }
        }, year, month, day)

        datePickerDialog.show()
    }

    fun SetSpinnerByName(
        spinner: Spinner,
        setDefaultName: String,
        context: Context,
        arrayResourceId: Int
    ) {
        val religionList = context.resources.getStringArray(arrayResourceId)

        val defaultIndex = religionList.indexOf(setDefaultName)

        if (defaultIndex != -1) {
            spinner.setSelection(defaultIndex)
        }
    }

    fun deviceOffLineMassage(
        context: Context
    ){
        showConfirmationDialog(
            context = context,
            title = "Device Offline",
            message = "Please check internet connection and try again.",
            positiveButtonText = "Mobile data",
            positiveButtonAction = { openMobileDataSettings(context) },
            negativeButtonText = "Wifi",
            negativeButtonAction = {
                context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            },
            cancelable = true)
        {
//            fragmentChanger(fragmentName, context)
        }
    }

    fun showPremiumFeatureDialog(
        context: Context,
        activity: MainActivity
    ) {
        showConfirmationDialog(
            context = context,
            title = "Unlock Premium Features",
            message = "Upgrade to premium to access exclusive features and enhance your experience.",
            positiveButtonText = "Contact Now",
            positiveButtonAction = { fragmentChanger(enquiry(), context) },
            negativeButtonText = "Not Yet",
            negativeButtonAction = { activityChanger(activity, context) },
            cancelable = false
        )
    }


    fun deviceOffLineMassage(
        fragmentName: Fragment,
        context: Context
    ){
        showConfirmationDialog(
            context = context,
            title = "Device Offline",
            message = "Please check internet connection and try again.",
            positiveButtonText = "Mobile data",
            positiveButtonAction = { openMobileDataSettings(context) },
            negativeButtonText = "Wifi",
            negativeButtonAction = {
                context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            },
            cancelable = true)
        {
            fragmentChanger(fragmentName, context)
        }
    }

    fun dataFailedLoad(
        fragmentName: Fragment,
        context: Context,
    ){
        showConfirmationDialog(
            context = context,
            title = "Failed to Load !",
            message = "Connection failed please check mobile data or wifi and try again.",
            positiveButtonText = "ok",
            positiveButtonAction = { activityChanger(MainActivity(), context) },
            negativeButtonText = "Refresh",
            negativeButtonAction = {fragmentChanger(fragmentName, context) },
            cancelable = false
        )
    }



//    fun openMobileDataSettings(
//        context: Context
//    ) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            // Android 10 and above
//            val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
//            startActivity(intent)
//        } else {
//            // Android 9 and below
//            val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
//            startActivity(intent)
//        }
//    }

    fun openMobileDataSettings(context: Context) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
        } else {
            Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
        }
        context.startActivity(intent)
    }

    fun isInternetAvailable(
        context: Context
    ): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
        return false
    }

    fun setKeyboardListener(view: View, onKeyboardOpen: () -> Unit, onKeyboardClose: () -> Unit) {
        var isKeyboardOpen = false

        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val rect = Rect()
                view.getWindowVisibleDisplayFrame(rect)
                val screenHeight = view.height
                val keypadHeight = screenHeight - rect.bottom

                // Check if the keyboard is open (based on the height difference)
                val isCurrentlyOpen = keypadHeight > screenHeight * 0.15

                if (isCurrentlyOpen && !isKeyboardOpen) {
                    // Keyboard has just opened
                    isKeyboardOpen = true
                    onKeyboardOpen() // Call your method when the keyboard opens
                } else if (!isCurrentlyOpen && isKeyboardOpen) {
                    // Keyboard has just closed
                    isKeyboardOpen = false
                    onKeyboardClose() // Call your method when the keyboard closes
                }

                return true
            }
        })
    }

    fun rotateImage(imageView: ImageView, angle: Float = 360f, duration: Long = 2000, stopDelay: Long = 3000, shouldStop: Boolean = true, repeatCount: Int = -1) {
        // Get the current rotation of the image
        val currentRotation = imageView.rotation

        // Create the ObjectAnimator to rotate the image from its current angle to the target angle
        val rotateAnimator = ObjectAnimator.ofFloat(imageView, "rotation", currentRotation, currentRotation + angle)
        rotateAnimator.duration = duration // Set the rotation duration

        // Set repeat count (use -1 for infinite rotations)
        rotateAnimator.repeatCount = if (repeatCount == -1) ObjectAnimator.INFINITE else repeatCount - 1
        rotateAnimator.repeatMode = ObjectAnimator.RESTART // Restart the rotation each time it completes

        // Start the rotation animation
        rotateAnimator.start()

        // Stop the rotation after the specified stopDelay, if shouldStop is true
        if (shouldStop) {
            Handler().postDelayed({
                rotateAnimator.cancel()  // Stop the rotation after the delay
            }, stopDelay)
        }
    }

    fun sendDataToFragment(
        activity: Context,
        fragment: Fragment,
        data: Map<String, String>
    ) {
        val bundle = Bundle()

        // Add all key-value pairs from the map to the bundle
        for ((key, value) in data) {
            bundle.putString(key, value)
        }

        fragment.arguments = bundle

        // Ensure activity is a FragmentActivity or AppCompatActivity to access supportFragmentManager
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace with your container's ID
                .addToBackStack(null) // Optional: Add the transaction to the back stack
                .commit()
        } else {
            // Handle the case where the activity is not a FragmentActivity (optional)
            Log.e("FragmentTransaction", "Activity is not a FragmentActivity.")
        }
    }

}