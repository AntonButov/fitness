package pro.butovanton.fitness.net

import pro.butovanton.fitnes2.net.retrofitDataClass.AlertResponse
import pro.butovanton.fitnes2.util.Logs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class Api(val jSONPlaceHolderApi : JSONPlaceHolderApi ) {

   suspend fun alert(guid : String) : List<AlertResponse>?  {
      return suspendCoroutine { cont ->
          jSONPlaceHolderApi.alert(guid).enqueue(object : Callback<List<AlertResponse>> {
              override fun onResponse( call: Call<List<AlertResponse>>, response: Response<List<AlertResponse>>) {
                  Logs.d("Retrofit response : " + response.toString())
                  cont.resume(response.body())
              }

              override fun onFailure(call: Call<List<AlertResponse>>, t: Throwable) {
                  cont.resumeWithException(t)
              }

          })
      }
    }
}


