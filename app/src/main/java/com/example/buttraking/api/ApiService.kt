package com.example.buttraking.api

import com.example.buttraking.model.response.LoginResponse
import com.example.buttraking.model.response.LogoutResponse
import com.example.buttraking.model.response.MenuResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {
    @FormUrlEncoded
    @POST("/api/Logincontroller/login")
    fun login(
        @Field("number") schoolId: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("index.php/api/Logincontroller/logout") fun logout(): Call<LogoutResponse>
    @GET("index.php/api/AccountsController/get_menu/{school_id}")
    fun getMenuStatus(
        @Path("school_id") schoolId:String,
    ) : Call<MenuResponse>

}

/*
    @FormUrlEncoded
    @POST("api/EmployeeController/addEmployee")
    fun addEmployee(@FieldMap fields: Map<String, String?>): Call<ApiResponse>

    @FormUrlEncoded
    @POST("api/SubjectController/addSubject")
    fun addSubject(@FieldMap fields: Map<String, String?>): Call<ApiResponse>

    @GET("api/EmployeeController/getEmployee/{schoolId}")
    fun getEmployees(@Path("schoolId") schoolId: String): Call<EmployeeResponse>
    // fun getEmployees(@QueryMap getEmployees: Map<String, String>): Call<EmployeeResponse>

    @GET("index.php/api/StudentController/get_classes/{school_id}")
    fun getClasses(@Path("school_id") schoolId: String): Call<ClassDataResponse>

    @FormUrlEncoded
    @POST("api/StudentController/addStudent")
    fun addStudent(@FieldMap studentData: Map<String, String>): Call<ApiResponse>


    @DELETE("index.php/api/StudentController/delete_student/{school_id}/{id}")
    fun deleteStudent(
        @Path("school_id") schoolId: String,
        @Path("id") studentId: String
    ): Call<Void>

    //    ------------------API for Detele Class Starting (hemant)-------------------
    @DELETE("index.php/api/SubjectController/deleteClass/{school_id}/{id}")
    fun deleteClass(
        @Path("school_id") schoolId: String,
        @Path("id") studentId: String
    ): Call<Void>


    @DELETE("index.php/api/EmployeeController/delete_employee/{school_id}/{id}")
    fun deleteEmployee(
        @Path("school_id") schoolId: String,
        @Path("id") employeeId: String
    ): Call<DeleteResponse>

    @GET("index.php/api/StudentController/get_subject/{school_id}")
    fun getSubjeact(@Path("school_id") schoolId: String): Call<SubjectDataResponse>

    //    @GET("index.php/api/StudentController/get_subject/{school_id}")
    //    fun getSubjects(
    //        @Path("school_id") schoolId: String
    //    ): Call<SubjectResponse>

    //    ------------------API for Account Setting Starting (hemant)-------------------
    @GET("index.php/api/AccountsController/get_account/{school_id}")
    suspend fun getAccountSetting(
        @Path("school_id") schoolId: String
    ): Response<GetAccountSettingResponse>

    @GET("index.php/api/EmployeeController/get_role/{school_id}")
    fun getEmployeeData(@Path("school_id") schoolId: String): Call<TeacherNameResponse>

    @FormUrlEncoded
    @POST("index.php/api/AccountsController/update_account")
    fun updatePassword(@FieldMap fields: Map<String, String>): Call<passwordResponse>

    @GET("index.php/api/StudentController/get_student_attendence/{school_id}")
    fun getStudentAttandadance(
        @Path("school_id") schoolId: String
    ): Call<GetStudentAttandadanceResponse>

    @GET("index.php/api/StudentController/get_employee_attendence/{school_id}")
    fun getEmployeesAttandadance(
        @Path("school_id") schoolId: String
    ): Call<GetEmployeesAttandadanceResponse>

    @GET("index.php/api/StudentController/getAttendance/{school_id}/{class_name}")
    fun getStudentAttandadancedateWise(
        @Path("school_id") schoolId: String,
        @Path("class_name") class_name: String
    ): Call<GetStudentAttandadanceDateWiseResponse>

    //    ------------------API for Get class List Starting (hemant)-------------------
    @GET("api/SubjectController/get_Class/{schoolId}")
    fun getClassList(@Path("schoolId") schoolId: String): Call<AllClassNameResponse>
    //    ------------------API for Get class List Endind (hemant)-------------------

    //    ------------------API for update class Starting (hemant)-------------------
    @FormUrlEncoded
    @POST("index.php/api/SubjectController/updateClass/{schoolId}/{class_Id}")
    fun updateClass(
        @Path("schoolId") schoolId: String,
        @Path("class_Id") classId: String,
        @FieldMap updateClassfields: Map<String, String>
    ): Call<classUpdateResponse>
    //    ------------------API for update class Ending (hemant)-------------------

    //    ------------------API for Add class Starting (hemant)-------------------
    @FormUrlEncoded
    @POST("api/SubjectController/addClass")
    fun addClasss(@FieldMap classData: Map<String, String>): Call<ClassResponseData>
    //    ------------------API for Add class Starting (hemant)-------------------

    @FormUrlEncoded
    @POST("index.php/api/StudentController/classes_attendence/2024-11-15/KG-1/eraAbc")
    fun addStudentAttandance(
        @FieldMap StudentAttandanceData: Map<String, String>
    ): Call<StudentAttendanceResponse>


    @FormUrlEncoded
    @POST("index.php/api/AccountsController/vehicles")
    fun Addriver(
        @FieldMap Addriver: Map<String, String>
    ): Call<AddDriver>



    @FormUrlEncoded
    @PUT("index.php/api/AccountsController/update_vehicles")
    fun Putlatandlong(
        @FieldMap Putedriverlatlong: Map<String, String>
    ): Call<AddDriver>



    @GET("index.php/api/AccountsController/get_vehicles/{schoolId}/{Id}")
    fun getlatandlong(@Path("schoolId") schoolId: String, @Path("Id") Id: String): Call<GelatandlongResponse>

    @GET("index.php/api/StudentController/get_vehicles/{schoolId}")
    fun getDirverdata(@Path("schoolId") schoolId: String): Call<DirverdataResponse>

    @GET("index.php/api/StudentController/get_student_vehicles/{schoolId}/{Id}")
    fun getDriverStudentData(@Path("schoolId") schoolId: String, @Path("Id") Id: String): Call<DriverDataResponse>


    @GET("index.php/api/StudentController/get_vehicles/{schoolId}")
    fun GetAdmindata(@Path("schoolId") schoolId: String): Call<GetAdmindataResponse>
    @GET("index.php/api/StudentController/get_fee/{school_id}/{student_id}")
    fun getStudentResponse(
        @Path("school_id") schoolId: String,
        @Path("student_id") employeeId: String
    ): Call<List<StudentFeeDetails>>

    @FormUrlEncoded
    @POST("index.php/api/AccountsController/inquiry")
    fun addInquiry(@FieldMap fields: Map<String, String?>): Call<InquiryResponse>


    @DELETE("index.php/api/AccountsController/delete_vehicle/{school_id}/{id}")
    fun deletedriver(
        @Path("school_id") schoolId: String,
        @Path("id") employeeId: String
    ): Call<DeleteResponse>

    @FormUrlEncoded
    @PUT("index.php/api/AccountsController/update_driver")
    fun putDriver(
        @FieldMap driverDetails: Map<String, String>
    ): Call<PutHomeWorkResponse>

}
*/
