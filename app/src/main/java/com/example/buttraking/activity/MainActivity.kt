package com.example.buttraking.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.example.buttraking.IDDetail.SchoolId
import com.example.buttraking.R
import com.example.buttraking.api.RetrofitHelper
import com.example.buttraking.databinding.ActivityMainBinding
import com.example.buttraking.fragment.AddStudent
import com.example.buttraking.fragment.AllClass
import com.example.buttraking.fragment.AllDriver
import com.example.buttraking.fragment.AllStudent
import com.example.buttraking.fragment.Bustracking
import com.example.buttraking.fragment.DashBoard
import com.example.buttraking.fragment.NewClass
import com.example.buttraking.fragment.driver
import com.example.buttraking.helper.MethodLibrary
import com.example.buttraking.model.response.LogoutResponse
import com.example.buttraking.model.response.MenuData
import com.example.buttraking.onbording.LoginPage
import com.example.buttraking.student.BusTraking
import com.example.buttraking.viewmodel.MenuViewModel
import com.google.android.material.navigation.NavigationView
import org.xmlpull.v1.sax2.Driver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var isLoweringLayingExpanded = false
    private var isClassExpanded = false
    private var isSubjectExpanded = false
    val toolBox = MethodLibrary()
    private lateinit var instituteIcon: String
    private val InstituteLogo: String = "assetsNew/img/institute_logos/"
    private lateinit var menu: Menu

    //General Setting//
    private var generalSettingMenu: Boolean = true
    private var instituteProfileSubmenu: Boolean = true
    private var feeParticularSubmenu: Boolean = true
    private var accountSettingSubmenu: Boolean = true
    private var addEventSubmenu: Boolean = true

    //Classes//

    private var classesMenu: Boolean = true
    private var allClassesSubmenu: Boolean = true
    private var newClassSubmenu: Boolean = true

    //Subject//

    private var subjectMenu: Boolean = true
    private var subjectWithClassSubmenu: Boolean = true
    private var createSubjectSubmenu: Boolean = true

    //Students//

    private var studentsMenu: Boolean = true
    private var allStudentsSubmenu: Boolean = true
    private var addStudentSubmenu: Boolean = true
    private var promoteStudentsSubmenu: Boolean = true
    private var studentIdCardSubmenu: Boolean = true

    //Employees//
    private var employeesMenu: Boolean = true
    private var allEmployeesSubmenu: Boolean = true
    private var addNewEmployeeSubmenu: Boolean = true
    private var offerLetterSubmenu: Boolean = true

    //Accounts//
    private var accountMenu: Boolean = true
    private var accountHeadSubmenu: Boolean = true
    private var addIncomeSubmenu: Boolean = true
    private var addExpenseSubmenu: Boolean = true
    private var accountStatementSubmenu: Boolean = true

    //Fees//
    private var feesMenu: Boolean = true
    private var collectionFeeSubmenu: Boolean = true
    private var feeSlipSubmenu: Boolean = true
    private var feeDefaulterSubmenu: Boolean = true

    //Salary//
    private var salaryMenu: Boolean = true
    private var paySalarySubmenu: Boolean = true
    private var salarySlipSubmenu: Boolean = true

    //Attendance//
    private var attendanceMenu: Boolean = true
    private var markStudentAttendanceSubmenu: Boolean = true
    private var markEmployeeAttendanceSubmenu: Boolean = true
    private var studentAttendanceReportSubmenu: Boolean = true
    private var employeeAttendanceReportSubmenu: Boolean = true
    private var feesReportSubmenu: Boolean = true

    //Timetable//
    private var timetableMenu: Boolean = true

    //Homework//
    private var homeworkMenu: Boolean = true

    //Message//
    private var messageMenu: Boolean = true

    //Report
    private var reportMenu: Boolean = true
    private var studentReportSubmenu: Boolean = true
    private var employeeReportSubmenu: Boolean = true
    private var feeReportSubmenu: Boolean = true

    private val menuViewModel: MenuViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMenuStatus()


       // fetchInstituteProfile(SchoolId().getSchoolId(this))

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DashBoard())
                .commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        onPrepareOptionsMenu(navigationView.menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)

        when (SchoolId().getLoginRole(this)) {
            "Student" -> studentItemShow(navigationView.menu)
            "Principal" -> principlaItemShow(navigationView.menu)
            "Teacher" -> teacherItemShow(navigationView.menu)
            "Admin" -> adminItemShow(navigationView.menu)
            "Driver" -> driverItemShow(navigationView.menu)

        }

        return true
    }


  /*  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        try {
            super.onCreateOptionsMenu(menu)
            menuInflater.inflate(R.menu.main, menu)
            var rolLogin: String = SchoolId().getLoginRole(this)
            if (rolLogin.equals("Student") || rolLogin.equals("Teacher")) {
                menu?.findItem(R.id.action_sub_item_3_ms)?.isVisible = false
                menu?.findItem(R.id.action_sub_item_2_ms)?.isVisible = true
            } else if (rolLogin.equals("Admin") || rolLogin.equals("Principal")) {
                menu?.findItem(R.id.action_sub_item_3_ms)?.isVisible = true
                menu?.findItem(R.id.action_sub_item_2_ms)?.isVisible = true
            }


            toolBox.setIconItem(
                menu?.findItem(R.id.action_main_item_top),
                "${com.example.schoolerp.ImageUtil.ImageUtil.BASE_URL_IMAGE}$InstituteLogo${
                    toolBox.getDataString(
                        "profileLogo",
                        this
                    )
                }", this
            )
            return true
        } catch (e: Exception) {
            toolBox.showConfirmationDialog(
                context = this,
                title = "Warning !",
                message = "Unknown Error found Please Contact with developer nurkude@gmail.com",
                positiveButtonText = "OK",
                positiveButtonAction = { toolBox.activityChanger(MainActivity(), this) },
                negativeButtonText = "",
                negativeButtonAction = { },
                cancelable = false
            )
        }
        return true
    }*/

/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {


            R.id.action_sub_item_1_ms -> {
                toolBox.activityChanger(LoginPage(), this)
                true
            }

            R.id.action_sub_item_2_ms -> {

                toolBox.showConfirmationDialog(
                    context = this,
                    title = "Logout !",
                    message = "Are you sure you want to logout?",
                    positiveButtonText = "Logout",
                    positiveButtonAction = {
                        toolBox.clearSharedprepared("onboarding_prefs", this)
                        toolBox.clearSharedprepared("MyPrefs", this)
                        toolBox.activityChanger(LoginPage(), this)
                    },
                    negativeButtonText = "Cancel",
                    negativeButtonAction = { },
                    cancelable = true
                )
                true
            }

            R.id.action_sub_item_3_ms -> {
              //  toolBox.fragmentChanger(AccountSettings(), this)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
*/


    private fun logoutUser() {
        RetrofitHelper.getApiService().logout().enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val logoutResponse = response.body()!!
                    if (logoutResponse.status) {
                        toolBox.clearSharedprepared("onboarding_prefs", this@MainActivity)
                        toolBox.clearSharedprepared("MyPrefs", this@MainActivity)

                        // Ensure all dialogs are dismissed before changing activity
                        toolBox.dismissActiveDialogs()

                        toolBox.activityChanger(LoginPage(), this@MainActivity)
                    } else {
                        toolBox.showConfirmationDialog(
                            context = this@MainActivity,
                            title = "Warning !",
                            message = "Unknown Error found Please Contact with developer nurkude@gmail.com",
                            positiveButtonText = "OK",
                            positiveButtonAction = {
                                toolBox.activityChanger(
                                    MainActivity(),
                                    this@MainActivity
                                )
                            },
                            negativeButtonText = null,
                            cancelable = false
                        )
                    }
                } else {
                    toolBox.showConfirmationDialog(
                        context = this@MainActivity,
                        title = "Warning !",
                        message = "Unknown Error found Please Contact with developer nurkude@gmail.com",
                        positiveButtonText = "OK",
                        positiveButtonAction = {
                            toolBox.activityChanger(
                                MainActivity(),
                                this@MainActivity
                            )
                        },
                        negativeButtonText = null,
                        cancelable = false
                    )
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Logout failed: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    // Handles item selections in the navigation drawer
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val id = menuItem.itemId

        when (id) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DashBoard()).commit()
            }


            R.id.nav_AllClasses -> {
                if (!allClassesSubmenu) {
                    toolBox.showPremiumFeatureDialog(this, this)
                }
                // Load the AllClasses fragment when "All Classes" is clicked
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AllClass()).commit()
            }

            R.id.nav_NewClass -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, NewClass()).commit()
            }


            R.id.nav_AllStudents -> {
                if (!allStudentsSubmenu) {
                    toolBox.showPremiumFeatureDialog(this, this)
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AllStudent()).commit()
            }

            R.id.nav_AddStudent -> {
                if (!addStudentSubmenu) {
                    toolBox.showPremiumFeatureDialog(this, this)
                }
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    AddStudent()
                ).commit()

            }



            R.id.nav_ShowBus -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, Bustracking()).commit()
            }

            R.id.nav_AddDriver -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, driver())
                    .commit()
            }

            R.id.nav_Bustrackingstudent -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, BusTraking()).commit()
            }

            R.id.nav_AllDriver -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AllDriver()).commit()
            }


            // Class section
            R.id.nav_Class -> {
                toggleClassExpandableItems() // Expand/collapse class items
                return true
            }

            // Subject section
               // Student section
            R.id.nav_Student -> {
                toggleStudentExpandableItems() // Expand/collapse student items
                return true
            }
            // Employee section

            R.id.nav_Bustracking -> {
                toggleBusExpandableItems() // Expand/collapse salary items
                return true
            }

            R.id.nav_logout -> {
                toolBox.showConfirmationDialog(

                    context = this@MainActivity,
                    title = "Logout !",
                    message = "Are you sure you want to logout?",
                    positiveButtonText = "Logout",
                    positiveButtonAction = {
                        logoutUser()
                    },
                    negativeButtonText = "Cancel",
                    negativeButtonAction = { },
                    cancelable = true
                )

                return true
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun driverItemShow(menu: Menu?){
        fun setMenuItemVisibility(itemId: Int, isVisible: Boolean) {
            menu?.findItem(itemId)?.isVisible = isVisible
        }

        setMenuItemVisibility(R.id.nav_home, true)
/*
        setMenuItemVisibility(R.id.nav_Gsettings, false)
        setMenuItemVisibility(R.id.nav_Fees, false)
        setMenuItemVisibility(R.id.nav_salary, false)
        setMenuItemVisibility(R.id.nav_Attendance, false)
        setMenuItemVisibility(R.id.nav_Homework, false)
        setMenuItemVisibility(R.id.nav_Messages, false)
*/
        setMenuItemVisibility(R.id.nav_Student, false)
        setMenuItemVisibility(R.id.nav_Class, false)
/*
        setMenuItemVisibility(R.id.nav_Subject, false)
        setMenuItemVisibility(R.id.nav_TimeTable, false)
*/
        setMenuItemVisibility(R.id.nav_Bustracking, false)
     //   setMenuItemVisibility(R.id.nav_Bustrackingstudent, false)


    }

    // Expand/collapse general settings sub-menu items
    private fun toggleGeneralSettingExpandableItems() {
        menu = navigationView.menu
/*
        val subitem1 = menu.findItem(R.id.nav_InstituteProfile)
        val subitem2 = menu.findItem(R.id.nav_FeeParticulars)

//        val subitem3 = menu.findItem(R.id.nav_DetailsforFeesChallahan)
//        val subitem4 = menu.findItem(R.id.nav_MarkingGrading)
        val subitem5 = menu.findItem(R.id.nav_AccountSettings)
        val subitem6 = menu.findItem(R.id.nav_Event)
*/

        // Toggle visibility of each sub-item

        isLoweringLayingExpanded = !isLoweringLayingExpanded

//        if (instituteProfileSubmenu){
//        }
       /* subitem1.isVisible = isLoweringLayingExpanded
        subitem2.isVisible = isLoweringLayingExpanded*/
//        if (feeParticularSubmenu){
//
//        }
        //subitem3.isVisible = isLoweringLayingExpanded
//        subitem4.isVisible = isLoweringLayingExpanded
//        if(accountSettingSubmenu){
//        }
//        if (addEventSubmenu){
//        }
      /*  subitem6.isVisible = isLoweringLayingExpanded
        subitem5.isVisible = isLoweringLayingExpanded
*/
        navigationView.invalidate()
    }

    // Helper function to toggle visibility for any set of menu items
    private fun toggleExpandableItems(
        items: List<Int>,
        roleBasedItems: Map<String, List<Int>>? = null
    ) {
        menu = navigationView.menu
        isClassExpanded = !isClassExpanded

        // Toggle visibility for the provided list of item IDs
        for (itemId in items) {
            val item = menu.findItem(itemId)
            item.isVisible = isClassExpanded
        }

        // Handle role-based items (e.g., "Student" vs. "Admin")
        roleBasedItems?.forEach { (role, itemIds) ->
            if (SchoolId().getLoginRole(this).equals(role, ignoreCase = true)) {
                for (itemId in itemIds) {
                    val item = menu.findItem(itemId)
                    item.isVisible = isClassExpanded
                }
            }
        }
    }
    private fun toggleReportExpandableItems() {
        menu = navigationView.menu
        isSubjectExpanded = !isSubjectExpanded
      /*  menu.findItem(R.id.nav_StudentAttendanceReport).isVisible = isSubjectExpanded
        menu.findItem(R.id.nav_EmployeeAttendanceReport).isVisible = isSubjectExpanded
        menu.findItem(R.id.nav_FeesReport).isVisible = isSubjectExpanded
*/
        if (studentAttendanceReportSubmenu){}
        if (employeeAttendanceReportSubmenu){}
        if (feesReportSubmenu){}
    }
    // Toggle methods for specific categories:

    private fun toggleClassExpandableItems() {
        menu = navigationView.menu
        val subitem1 = menu.findItem(R.id.nav_AllClasses)
        val subitem2 = menu.findItem(R.id.nav_NewClass)


        isLoweringLayingExpanded = !isLoweringLayingExpanded
        subitem1.isVisible = isLoweringLayingExpanded
        subitem2.isVisible = isLoweringLayingExpanded
    }

    /*private fun toggleSubjectExpandableItems() {
        menu = navigationView.menu
        val subitem1 = menu.findItem(R.id.nav_ClassWithSubject)
        val subitem2 = menu.findItem(R.id.nav_AssignSubject)

        isSubjectExpanded = !isSubjectExpanded
        subitem1.isVisible = isLoweringLayingExpanded
        subitem2.isVisible = isLoweringLayingExpanded


        val roleVisibilityMap = mapOf(
            "Student" to listOf(subitem1),
            "Admin" to listOf(subitem1, subitem2),
            "Principal" to listOf(subitem1, subitem2)
        )

        roleVisibilityMap[SchoolId().getLoginRole(this)]?.forEach {
            it.isVisible = isSubjectExpanded
        }
    }*/


    private fun toggleStudentExpandableItems() {
        menu = navigationView.menu

        isSubjectExpanded = !isSubjectExpanded
        menu.findItem(R.id.nav_AllStudents).isVisible = isSubjectExpanded
        menu.findItem(R.id.nav_AddStudent).isVisible = isSubjectExpanded
     //   menu.findItem(R.id.nav_PromoteStudents).isVisible = isSubjectExpanded
      //  menu.findItem(R.id.nav_StudentIDCards).isVisible = isSubjectExpanded

        if (allStudentsSubmenu) {
        }
        if (addStudentSubmenu) {
        }
        if (promoteStudentsSubmenu) {
        }
        if (promoteStudentsSubmenu) {
        }
    }

    private fun toggleEmployeeExpandableItems() {
        menu = navigationView.menu

        isSubjectExpanded = !isSubjectExpanded
      //  menu.findItem(R.id.nav_AllEmployees).isVisible = isSubjectExpanded
     //   menu.findItem(R.id.nav_AddNewEmployees).isVisible = isSubjectExpanded
       // menu.findItem(R.id.nav_JobLetters).isVisible = isSubjectExpanded

        //if (employeesMenu){menu.findItem(R.id.nav_Employee).isVisible = isSubjectExpanded }
        if (allEmployeesSubmenu) {
        }
        if (addNewEmployeeSubmenu) {
        }
        if (offerLetterSubmenu) {
        }

    }

    private fun toggleAccountExpandableItems() {
        menu = navigationView.menu

        isSubjectExpanded = !isSubjectExpanded
       // menu.findItem(R.id.nav_CharofAccount).isVisible = isSubjectExpanded
       // menu.findItem(R.id.nav_AddIncome).isVisible = isSubjectExpanded
      //  menu.findItem(R.id.nav_AddExpense).isVisible = isSubjectExpanded
      //  menu.findItem(R.id.nav_AccountStatement).isVisible = isSubjectExpanded

        if (accountHeadSubmenu) {
        }
        if (addIncomeSubmenu) {
        }
        if (addExpenseSubmenu) {
        }
        if (accountStatementSubmenu) {
        }
//        toggleExpandableItems(
//            items = listOf(
//                R.id.nav_CharofAccount, R.id.nav_AddIncome, R.id.nav_AddExpense, R.id.nav_AccountStatement
//            )
//        )
    }

    private fun toggleFeesExpandableItems() {
        menu = navigationView.menu

        isSubjectExpanded = !isSubjectExpanded
      //  menu.findItem(R.id.nav_Collectionfee).isVisible = isSubjectExpanded
       // menu.findItem(R.id.nav_FeeSlip).isVisible = isSubjectExpanded
      //  menu.findItem(R.id.nav_FeesDefaulter).isVisible = isSubjectExpanded
        if (collectionFeeSubmenu) {
        }
        if (feeSlipSubmenu) {
        }
        if (feeDefaulterSubmenu) {
        }
    }

//        toggleExpandableItems(
//            items = listOf(R.id.nav_Collectionfee, R.id.nav_FeeSlip, R.id.nav_FeesDefaulter)
//        )

    private fun toggleSalaryExpandableItems() {
        menu = navigationView.menu

        isSubjectExpanded = !isSubjectExpanded
            //  menu.findItem(R.id.nav_PaySalary).isVisible = isSubjectExpanded
       // menu.findItem(R.id.nav_SalarySlip).isVisible = isSubjectExpanded
        if (paySalarySubmenu) {
        }
        if (salarySlipSubmenu) {
        }
        // toggleExpandableItems(items = listOf(R.id.nav_PaySalary, R.id.nav_SalarySlip))
    }
    private fun toggleBusExpandableItems() {
        toggleExpandableItems(
            items = listOf(R.id.nav_AllDriver,R.id.nav_AddDriver, R.id.nav_ShowBus),
        )
    }

    private fun toggleAttendanceExpandableItems() {
        menu = navigationView.menu
        isSubjectExpanded = !isSubjectExpanded

        if (SchoolId().getLoginRole(this).equals("Teacher"))
           // toggleExpandableItems(items = listOf(R.id.nav_MarksStudentAttendance,R.id.nav_StudentAttendanceReport))
        else{
           // menu.findItem(R.id.nav_MarksStudentAttendance).isVisible = isSubjectExpanded
           // menu.findItem(R.id.nav_MarksEmpAttendance).isVisible = isSubjectExpanded
            if (markStudentAttendanceSubmenu){}
            if (markEmployeeAttendanceSubmenu){}
            //toggleExpandableItems(items = listOf(R.id.nav_MarksStudentAttendance, R.id.nav_MarksEmpAttendance,R.id.nav_StudentAttendanceReport,R.id.nav_EmployeeAttendanceReport))
        }
    }

    private fun toggleReportsExpandableItems() {
        menu = navigationView.menu
        isSubjectExpanded = !isSubjectExpanded
       // menu.findItem(R.id.nav_CreateInquiry).isVisible = isSubjectExpanded
      //  menu.findItem(R.id.nav_ViewInquiry).isVisible = isSubjectExpanded

    }

    // Override back button to close the drawer if it's open, otherwise perform the default action
    /*  override fun onBackPressed() {
          if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
              drawerLayout.closeDrawer(GravityCompat.START)
          } else {
              super.onBackPressed()
          }
      }*/

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        // Check if the navigation drawer is open, and if so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Handle back press depending on the current fragment
            when {
                currentFragment is DashBoard -> {
                    // If it's the DashBoard fragment, show exit confirmation
                    showExitConfirmationDialog()
                }

                supportFragmentManager.backStackEntryCount > 0 -> {
                    // If there are fragments in the back stack, pop the top one
                    supportFragmentManager.popBackStack()
                }

                else -> {
                    // If no fragments are in the back stack, replace the fragment with DashBoard
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, DashBoard())
                        .commit()
                }
            }
        }
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to exit the app?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Exit the app
                finish()  // Close the current activity (exit the app)
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog without exiting
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun studentItemShow(menu: Menu?) {
        fun setMenuItemVisibility(itemId: Int, isVisible: Boolean) {
            menu?.findItem(itemId)?.isVisible = isVisible
        }

       /* setMenuItemVisibility(R.id.nav_student_Paid_fee_recipt, true)
        setMenuItemVisibility(R.id.nav_student_my_time_table, true)
        setMenuItemVisibility(R.id.nav_student_home_work, true)
        setMenuItemVisibility(R.id.nav_student_message, true)
        setMenuItemVisibility(R.id.nav_student_account_settings, false)
        setMenuItemVisibility(R.id.nav_student_details, true)
        setMenuItemVisibility(R.id.nav_Bustrackingstudent, true)*/

    }

    private fun teacherItemShow(menu: Menu?){
        fun setMenuItemVisibility(itemId: Int, isVisible: Boolean) {
            menu?.findItem(itemId)?.isVisible = isVisible
        }
        setMenuItemVisibility(R.id.nav_home, true)
/*
        setMenuItemVisibility(R.id.nav_Attendance, true)
        setMenuItemVisibility(R.id.nav_Homework, true)
        setMenuItemVisibility(R.id.nav_Messages, true)
        setMenuItemVisibility(R.id.nav_TimeTable, true)
        setMenuItemVisibility(R.id.nav_Bustrackingstudent, false)
*/


    }

    private fun adminItemShow(menu: Menu?){
        fun setMenuItemVisibility(itemId: Int, isVisible: Boolean) {
            menu?.findItem(itemId)?.isVisible = isVisible
        }

       // setMenuItemVisibility(R.id.nav_Employee, true)
        setMenuItemVisibility(R.id.nav_home, true)
/*
        setMenuItemVisibility(R.id.nav_Gsettings, true)
        setMenuItemVisibility(R.id.nav_Fees, true)
        setMenuItemVisibility(R.id.nav_salary, true)
        setMenuItemVisibility(R.id.nav_Attendance, true)
        setMenuItemVisibility(R.id.nav_Homework, true)
        setMenuItemVisibility(R.id.nav_Messages, true)
        setMenuItemVisibility(R.id.nav_Accounts, true)
*/
        setMenuItemVisibility(R.id.nav_Student, true)
        setMenuItemVisibility(R.id.nav_Class, true)
/*
        setMenuItemVisibility(R.id.nav_Subject, true)
        setMenuItemVisibility(R.id.nav_TimeTable, true)
*/
        setMenuItemVisibility(R.id.nav_Bustracking, true)
/*
        setMenuItemVisibility(R.id.nav_Bustrackingstudent, false)

        setMenuItemVisibility(R.id.nav_Gsettings, generalSettingMenu)
        setMenuItemVisibility(R.id.nav_Fees, feesMenu)
        setMenuItemVisibility(R.id.nav_salary, salaryMenu)
        setMenuItemVisibility(R.id.nav_Attendance, attendanceMenu)
        setMenuItemVisibility(R.id.nav_Homework, homeworkMenu)
        setMenuItemVisibility(R.id.nav_Messages, messageMenu)
        setMenuItemVisibility(R.id.nav_Accounts, accountMenu)
*/
        setMenuItemVisibility(R.id.nav_Student, studentsMenu)
        setMenuItemVisibility(R.id.nav_Class, classesMenu)
/*
        setMenuItemVisibility(R.id.nav_Subject, subjectMenu)
        setMenuItemVisibility(R.id.nav_TimeTable, timetableMenu)
        setMenuItemVisibility(R.id.nav_report, reportMenu)
*/
    }


    private fun principlaItemShow(menu: Menu?){
        fun setMenuItemVisibility(itemId: Int, isVisible: Boolean) {
            menu?.findItem(itemId)?.isVisible = isVisible
        }

       // setMenuItemVisibility(R.id.nav_Employee, employeesMenu)
        setMenuItemVisibility(R.id.nav_home, true)
/*
        setMenuItemVisibility(R.id.nav_Gsettings, generalSettingMenu)
        setMenuItemVisibility(R.id.nav_Fees, feesMenu)
        setMenuItemVisibility(R.id.nav_salary, salaryMenu)
        setMenuItemVisibility(R.id.nav_Attendance, attendanceMenu)
        setMenuItemVisibility(R.id.nav_Homework, homeworkMenu)
        setMenuItemVisibility(R.id.nav_Messages, messageMenu)
        setMenuItemVisibility(R.id.nav_Accounts, accountMenu)
*/
        setMenuItemVisibility(R.id.nav_Student, studentsMenu)
        setMenuItemVisibility(R.id.nav_Class, classesMenu)
/*
        setMenuItemVisibility(R.id.nav_Subject, subjectMenu)
        setMenuItemVisibility(R.id.nav_TimeTable, timetableMenu)
        setMenuItemVisibility(R.id.nav_Gsettings, true)
        setMenuItemVisibility(R.id.nav_Fees, true)
        setMenuItemVisibility(R.id.nav_salary, true)
        setMenuItemVisibility(R.id.nav_Attendance, true)
        setMenuItemVisibility(R.id.nav_Homework, true)
        setMenuItemVisibility(R.id.nav_Messages, true)
        setMenuItemVisibility(R.id.nav_Accounts, true)
*/
        setMenuItemVisibility(R.id.nav_Student, true)
        setMenuItemVisibility(R.id.nav_Class, true)
/*
        setMenuItemVisibility(R.id.nav_Subject, true)
        setMenuItemVisibility(R.id.nav_TimeTable, true)
        setMenuItemVisibility(R.id.nav_Bustrackingstudent, false)
        setMenuItemVisibility(R.id.nav_ChateView, true)
*/


    }
    //
/*
    fun fetchInstituteProfile(schoolId: String) {
        try {
            val apiService = RetrofitHelper.getApiService()
            val call = apiService.getInstituteProfile(schoolId)
            call.enqueue(object : Callback<InstituteProfileResponse> {
                override fun onResponse(
                    call: Call<InstituteProfileResponse>,
                    response: Response<InstituteProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        //=============== Successfully received response===========
                        val instituteProfile = response.body()
                        if (instituteProfile != null && !instituteProfile.data.institute_logo.isNullOrEmpty()) {
                            toolBox.saveDataString("profileLogo", instituteProfile.data.institute_logo, this@MainActivity)
                            toolBox.saveDataString("institute_address", instituteProfile.data.institute_address, this@MainActivity)
                            toolBox.saveDataString("school_name", instituteProfile.data.school_name, this@MainActivity)


                        } else {
                            println("Institute logo is null or empty")
                        }
                    } else {
                        println("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<InstituteProfileResponse>, t: Throwable) {
                    t.printStackTrace()
                    println("API Call Failed: ${t.message}")
                }
            })
        }catch (e : Exception){
            toolBox.showConfirmationDialog(
                context = this,
                title = "Warning !",
                message = "Unknown Error found Please Contact with developer nurkude@gmail.com",
                positiveButtonText = "OK",
                positiveButtonAction = { toolBox.activityChanger(MainActivity(), this) },
                negativeButtonText = "",
                negativeButtonAction = { },
                cancelable = false)
        }
    }
*/

    private fun getMenuStatus(){
        // Observe the menu data
        menuViewModel.menuData.observe(this, Observer { menuResponse ->
            // Handle the successful response
            menuResponse?.let {
                if (it.status) {
                    setMenuVisibility(it.data)
                }
            }
        })

        // Observe for error messages
        menuViewModel.errorMessage.observe(this, Observer { error ->
            // Handle the error
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
            }
        })

        // Call API to fetch the menu data for a specific school
        val schoolId = SchoolId().getSchoolId(this) // Pass the actual school ID
        menuViewModel.fetchMenuData(schoolId)
    }

    private fun setMenuVisibility(
        VisibilityData:  List<MenuData>
    ){
        try{
            generalSettingMenu = VisibilityData.get(1).status
            instituteProfileSubmenu = VisibilityData.get(2).status
            feeParticularSubmenu = VisibilityData.get(3).status
            accountSettingSubmenu = VisibilityData.get(4).status
            addEventSubmenu = VisibilityData.get(5).status

            classesMenu = VisibilityData.get(6).status
            allClassesSubmenu = VisibilityData.get(7).status
            newClassSubmenu = VisibilityData.get(8).status

            subjectMenu = VisibilityData.get(9).status
            subjectWithClassSubmenu = VisibilityData.get(10).status
            createSubjectSubmenu = VisibilityData.get(11).status

            studentsMenu = VisibilityData.get(12).status
            allStudentsSubmenu = VisibilityData.get(13).status
            addStudentSubmenu = VisibilityData.get(14).status
            promoteStudentsSubmenu = VisibilityData.get(15).status
            studentIdCardSubmenu = VisibilityData.get(16).status

            employeesMenu = VisibilityData.get(17).status
            allEmployeesSubmenu = VisibilityData.get(18).status
            addNewEmployeeSubmenu = VisibilityData.get(19).status
            offerLetterSubmenu = VisibilityData.get(20).status

            accountMenu = VisibilityData.get(21).status
            accountHeadSubmenu = VisibilityData.get(22).status
            addIncomeSubmenu = VisibilityData.get(23).status
            addExpenseSubmenu = VisibilityData.get(24).status
            accountStatementSubmenu = VisibilityData.get(25).status

            feesMenu = VisibilityData.get(26).status
            collectionFeeSubmenu = VisibilityData.get(27).status
            feeSlipSubmenu = VisibilityData.get(28).status
            feeDefaulterSubmenu = VisibilityData.get(29).status

            salaryMenu = VisibilityData.get(30).status
            paySalarySubmenu = VisibilityData.get(31).status
            salarySlipSubmenu = VisibilityData.get(32).status

            attendanceMenu = VisibilityData.get(33).status
            markStudentAttendanceSubmenu = VisibilityData.get(34).status
            markEmployeeAttendanceSubmenu = VisibilityData.get(35).status

            timetableMenu = VisibilityData.get(36).status
            homeworkMenu = VisibilityData.get(37).status
            messageMenu = VisibilityData.get(38).status

            reportMenu = VisibilityData.get(39).status
            studentReportSubmenu = VisibilityData.get(40).status
            employeeReportSubmenu = VisibilityData.get(41).status
            feeReportSubmenu = VisibilityData.get(42).status

        }catch (e:Exception){
            Toast.makeText(this, e.printStackTrace().toString(), Toast.LENGTH_SHORT).show()

        }
    }
}