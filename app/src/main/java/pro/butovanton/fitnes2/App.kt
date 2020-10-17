package pro.butovanton.fitnes2

import android.app.Application
import android.bluetooth.BluetoothDevice
import com.htsmart.wristband2.WristbandApplication

class App : Application() {

    companion object {
       var device : BluetoothDevice? = null
    }

    override fun onCreate() {
        super.onCreate()
        WristbandApplication.init(this);
        WristbandApplication.setDebugEnable(true);
    }

}
