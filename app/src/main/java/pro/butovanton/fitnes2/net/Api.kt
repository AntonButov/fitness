package pro.butovanton.fitness.net

import android.bluetooth.BluetoothDevice
import okhttp3.ResponseBody
import pro.butovanton.fitnes2.net.retrofitDataClass.AlertResponse
import pro.butovanton.fitnes2.net.retrofitDataClass.Detail
import pro.butovanton.fitnes2.net.retrofitDataClass.WorkShift
import pro.butovanton.fitnes2.util.Logs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class Api(private val jSONPlaceHolderApi : JSONPlaceHolderApi ) {

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

    suspend fun postDetail(detail: Detail): Boolean {
        return suspendCoroutine { cont ->
            jSONPlaceHolderApi.postDetail(detail = detail).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    cont.resume(response.code() == 200)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    cont.resumeWithException(t)
                }
            })
        }
    }

    suspend fun workShift(device: String, shift: Boolean): Boolean {
        return suspendCoroutine { cont ->
            jSONPlaceHolderApi.workShift(WorkShift(device, shift))
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        cont.resume(response.code() == 200)
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        throw Exception(t)
                    }
                })
            }
    }
}



