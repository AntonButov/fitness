package pro.butovanton.fitness.net

import android.app.Activity
import okhttp3.ResponseBody
import pro.butovanton.fitnes2.net.retrofitDataClass.AlertResponse
import pro.butovanton.fitnes2.net.retrofitDataClass.Detail
import pro.butovanton.fitnes2.net.retrofitDataClass.WorkShift
import pro.butovanton.fitnes2.utils.Logs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class Api(private val jSONPlaceHolderApi : JSONPlaceHolderApi ) {

    companion object {
        val OK = 1;
        val ERROR = 0;
    }

    suspend fun alert(guid: String): List<AlertResponse>? {
        return suspendCoroutine { cont ->
            jSONPlaceHolderApi.alert(guid).enqueue(object : Callback<List<AlertResponse>> {
                override fun onResponse(
                    call: Call<List<AlertResponse>>,
                    response: Response<List<AlertResponse>>
                ) {
                    Logs.d("Retrofit response : " + response.toString())
                    cont.resume(response.body())
                }

                override fun onFailure(call: Call<List<AlertResponse>>, t: Throwable) {
                    cont.resumeWithException(t)
                }
            })
        }
    }

    suspend fun postDetail(detail: Detail): Long {
        return suspendCoroutine { cont ->
            jSONPlaceHolderApi.postDetail(detail = detail).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    var timeOut = response.body()?.string()
                    if (timeOut == null)
                        timeOut = "5"
                    Logs.d("Задержка от сервера = " + timeOut)
                    cont.resume(timeOut.toLong())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    cont.resume(99L)
                }
            })
        }
    }

    suspend fun workShift(device: String, shift: Boolean):  Int{
        return suspendCoroutine { cont ->
            jSONPlaceHolderApi.workShift(WorkShift(device, shift))
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.code() == 200)
                            cont.resume(OK)
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        cont.resume(ERROR)
                    }
                })
            }
    }
}



