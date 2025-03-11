package com.example.buttraking.api

import com.example.buttraking.model.response.AddDriver
import com.example.buttraking.model.response.AllClassNameResponse
import com.example.buttraking.model.response.ApiResponse
import com.example.buttraking.model.response.ClassDataResponse
import com.example.buttraking.model.response.ClassResponseData
import com.example.buttraking.model.response.DeleteResponse
import com.example.buttraking.model.response.DirverdataResponse
import com.example.buttraking.model.response.DriverDataResponse
import com.example.buttraking.model.response.EmployeeResponse
import com.example.buttraking.model.response.GelatandlongResponse
import com.example.buttraking.model.response.GetAdmindataResponse
import com.example.buttraking.model.response.LoginResponse
import com.example.buttraking.model.response.LogoutResponse
import com.example.buttraking.model.response.MenuResponse
import com.example.buttraking.model.response.PutHomeWorkResponse
import com.example.buttraking.model.response.StudentDataResponse
import com.example.buttraking.model.response.StudentEditResponse
import com.example.buttraking.model.response.TeacherNameResponse
import retrofit2.Call
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

    @GET("index.php/api/StudentController/get_vehicles/{schoolId}")
    fun GetAdmindata(@Path("schoolId") schoolId: String): Call<GetAdmindataResponse>
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
    fun getlatandlong(@Path("schoolId") schoolId: String,@Path("Id") Id: String): Call<GelatandlongResponse>


    @FormUrlEncoded
    @POST("api/StudentController/addStudent")
    fun addStudent(@FieldMap studentData: Map<String, String>): Call<ApiResponse>


    @GET("index.php/api/StudentController/get_classes/{school_id}")
    fun getClasses(@Path("school_id") schoolId: String): Call<ClassDataResponse>

    @GET("index.php/api/StudentController/get_vehicles/{schoolId}")
    fun getDirverdata(@Path("schoolId") schoolId: String): Call<DirverdataResponse>
    @GET("index.php/api/StudentController/get_student/{school_id}")
    fun getStudent(
        @Path("school_id") schoolId: String,
        @QueryMap getstudent: Map<String, String>
    ): Call<StudentDataResponse>
    @DELETE("index.php/api/StudentController/delete_student/{school_id}/{id}")
    fun deleteStudent(
        @Path("school_id") schoolId: String,
        @Path("id") studentId: String
    ): Call<Void>

    @FormUrlEncoded
    @PUT("index.php/api/StudentController/update_student")
    fun EditStudent(@FieldMap EditStudentData: Map<String, String>): Call<StudentEditResponse>
    @GET("index.php/api/StudentController/get_student_vehicles/{schoolId}/{Id}")
    fun getDriverStudentData(@Path("schoolId") schoolId: String,@Path("Id") Id: String): Call<DriverDataResponse>
    @FormUrlEncoded
    @POST("api/SubjectController/addClass")
    fun addClasss(@FieldMap classData: Map<String, String>): Call<ClassResponseData>
    @GET("api/EmployeeController/getEmployee/{schoolId}")

    fun getEmployees(@Path("schoolId") schoolId: String): Call<EmployeeResponse>
    // fun getEmployees(@QueryMap getEmployees: Map<String, String>): Call<EmployeeResponse>

    @GET("index.php/api/EmployeeController/get_role/{school_id}")
    fun getEmployeeData(@Path("school_id") schoolId: String): Call<TeacherNameResponse>

    @GET("api/SubjectController/get_Class/{schoolId}")
    fun getClassList(@Path("schoolId") schoolId: String): Call<AllClassNameResponse>
    @FormUrlEncoded
    @POST("api/EmployeeController/addEmployee")
    fun addEmployee(@FieldMap fields: Map<String, Any?>): Call<ApiResponse>
    @DELETE("index.php/api/EmployeeController/delete_employee/{school_id}/{id}")
    fun deleteEmployee(
        @Path("school_id") schoolId: String,
        @Path("id") employeeId: String
    ): Call<DeleteResponse>

}


