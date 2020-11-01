package pro.butovanton.fitnes2

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import okhttp3.ResponseBody

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import pro.butovanton.fitnes2.mock.UserMock
import pro.butovanton.fitnes2.util.Utils
import pro.butovanton.fitness.net.Api
import pro.butovanton.fitness.net.JSONPlaceHolderApi
import java.util.*
import java.util.concurrent.CountDownLatch

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DeviceInstrumentedTest {
    var deviceClass = InjectorUtils.provideDevice()
    val user = UserMock()

    var device = mockk<BluetoothDevice>()
    val myTestSmartMac = "AC:2A:21:B0:5F:39"

    @Before
    fun setUp() {
   //     MockKAnnotations.init(this)
   //     every { device.address } returns myTestSmartMac
    }

    @Test
    fun alert() {
      //  runBlocking {
  //      deviceClass.initDevice()
      //  }
    }

}