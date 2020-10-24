package pro.butovanton.fitnes2

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.*
import okhttp3.ResponseBody

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import pro.butovanton.fitness.net.Api
import pro.butovanton.fitness.net.JSONPlaceHolderApi
import java.util.concurrent.CountDownLatch

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AllertInstrumentedTest {
    var api = InjectorUtils.provideApi()

    @Test
    fun alert() {
        runBlocking {
            try {
                val response = api.alert(JSONPlaceHolderApi.GUID)
                assertNotNull(response)
                Logs("DEBUG", "AllertRespose = " + response.toString())
            }
            catch (t: Throwable) {
                assertNotNull(t)
                Logs("DEBUG", "Exeption = " + t.message)
            }
        }
    }



    @Test
    fun getBataryPercent() {
    val batary = InjectorUtils.provideBatary()
    var appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    val bataryPercent = batary.getBatteryPercentage(appContext)
    assertNotNull(bataryPercent)
    }
}