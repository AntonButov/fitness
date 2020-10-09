package pro.butovanton.fitness.net

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import pro.butovanton.fitnes2.net.responses.AlertResponse
import retrofit2.Call
import retrofit2.http.*

interface JSONPlaceHolderApi {
    companion object {
      //  const val GUID = "00000000-0000-0000-0000-eab1d1c37ab2"
        const val GUID = "00000000-0000-0000-0000-ac2a21b05f39"
    }

    @GET("/v1/alerts/{GUID}")
    fun alert(@Path ("GUID") guid: String) : Call<List<AlertResponse>>

}