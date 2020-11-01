package pro.butovanton.fitnes2

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.ResponseBody

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import pro.butovanton.fitnes2.net.retrofitDataClass.*
import pro.butovanton.fitnes2.util.Logs
import pro.butovanton.fitnes2.util.Utils
import pro.butovanton.fitness.net.Api
import pro.butovanton.fitness.net.JSONPlaceHolderApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
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
    val testDevice = "00000000-0000-0000-0000-ac2a21b05f39"
    val fakeDatail = Detail(Utils.longDateToString(Date().time),
        testDevice,
        60,
        Pressure(80, 120),
        98,
        3.5F,
        36.6F,
        40,
        Coordinates(34.34, 125.34))

    @Test
    fun detail() {

       val response =  jsonApi.postDetail(fakeDatail).execute()
        assertEquals(response.code(),200)
    }

    @Test
    fun workSshifr() {
        val response = jsonApi.workShift(WorkShift(testDevice, false)).execute()
        assertEquals(response.code(), 200)
    }

    @Test
    fun dateToString() {
        val dateTime = 1604210126716
        val strDate = Utils.longDateToString(dateTime)
        assertEquals(strDate, "2020-11-01T08:55:26")
    }
}