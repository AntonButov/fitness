package pro.butovanton.fitness.net

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.json.JSONException

import org.json.JSONObject
import pro.butovanton.fitnes2.InjectorUtils
import pro.butovanton.fitnes2.net.responses.AlertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class Api(val jSONPlaceHolderApi : JSONPlaceHolderApi ) {

   suspend fun alert(guid : String) : List<AlertResponse>?  {
      return suspendCoroutine {cont ->
          jSONPlaceHolderApi.alert(guid).enqueue(object : Callback<List<AlertResponse>> {
              override fun onResponse( call: Call<List<AlertResponse>>, response: Response<List<AlertResponse>>
              ) {
                  Log.d("DEBUG", "Retrofit response : " + response.toString())
                  cont.resume(response.body())
              }

              override fun onFailure(call: Call<List<AlertResponse>>, t: Throwable) {
                  cont.resumeWithException(t)
              }

          })
      }
    }
}


