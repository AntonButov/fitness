package pro.butovanton.fitnes2

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import com.htsmart.wristband2.WristbandApplication
import com.realsil.sdk.core.utility.SharedPrefesHelper.getSharedPreferences


class App : Application() {

    var app = this

    override fun onCreate() {
        super.onCreate()
        WristbandApplication.init(this);
        WristbandApplication.setDebugEnable(true);
        App.app = app
        device = getDeviceFromShared()
    }

    private fun getDeviceFromShared() : BluetoothDevice? {
        val prefs: SharedPreferences = getSharedPreferences(app, "device")
        val deviceString = prefs.getString("device", "")
        if (deviceString.equals("")) return null
        return Gson().fromJson(deviceString, BluetoothDevice::class.java)
    }

    companion object {
        lateinit var app: Application

        var device: BluetoothDevice? = null
            set(value) {
                field = value
                saveToDivice(value)
       //         if (value != null)
       //             startService()
            }

        private fun saveToDivice(device: BluetoothDevice?) {
            val prefs: SharedPreferences = getSharedPreferences(app, "device")
            var deviceString = ""
            if (device != null)
                deviceString = Gson().toJson(device)
            prefs.edit().putString("device", deviceString).apply()
        }

    }

}

