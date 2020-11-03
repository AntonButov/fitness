package pro.butovanton.fitnes2

import android.bluetooth.BluetoothDevice
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import pro.butovanton.fitnes2.mock.UserMock
import java.text.DateFormat
import java.util.*

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


    @Test
    fun timeTest() {
        val df: DateFormat =
            DateFormat.getTimeInstance()
        df.timeZone = TimeZone.getTimeZone("gmt")
        val gmtTime: String = df.format(Date())
    }

}