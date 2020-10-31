package pro.butovanton.fitnes2

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.ResponseBody

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import pro.butovanton.fitnes2.net.retrofitDataClass.AlertResponse
import pro.butovanton.fitnes2.net.retrofitDataClass.Coordinates
import pro.butovanton.fitnes2.net.retrofitDataClass.Detail
import pro.butovanton.fitnes2.net.retrofitDataClass.Pressure
import pro.butovanton.fitnes2.util.Logs
import pro.butovanton.fitness.net.Api
import pro.butovanton.fitness.net.JSONPlaceHolderApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DetailInstrumentedTest {
    val jsonApi = InjectorUtils.provideJSONPlaceHolderApi()
    val api = InjectorUtils.provideApi()
    val fakeDatail = Detail("2020-06-16T19:25:43",
        "00000000-0000-0000-0000-ac2a21b05f39",
        60,
        Pressure(80, 120),
        98,
        3.5F,
        36.6F,
        40,
        Coordinates(34.34F, 125.34F))

    @Test
    fun detail() {
        val countDetail = CountDownLatch(1)
        jsonApi.postDetail(fakeDatail).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            Logs.d("Detail : " + response.body().toString())

                countDetail.count
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                countDetail.count
            }
        })
        countDetail.await()
    }
}