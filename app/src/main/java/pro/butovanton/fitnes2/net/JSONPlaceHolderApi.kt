package pro.butovanton.fitness.net

import okhttp3.ResponseBody
import pro.butovanton.fitnes2.net.retrofitDataClass.AlertResponse
import pro.butovanton.fitnes2.net.retrofitDataClass.Detail
import pro.butovanton.fitnes2.net.retrofitDataClass.WorkShift
import retrofit2.Call
import retrofit2.http.*

interface JSONPlaceHolderApi {
    companion object {
      //  const val GUID = "00000000-0000-0000-0000-eab1d1c37ab2"
        const val GUID = "00000000-0000-0000-0000-ac2a21b05f39" // дает алерты

        val password = "MKB:passmkb"
    }


    @GET("/v1/alerts/{GUID}")
    fun alert(@Path ("GUID") guid: String) : Call<List<AlertResponse>>

    @Headers("Content-Type: application/json")
    @POST("/v1/details")
    fun postDetail(@Body detail: Detail) : Call<ResponseBody>

    @POST("/v1/workshift")
    fun workShift(@Body workShift: WorkShift) : Call<ResponseBody>

}