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
        deviceState = getDeviceFromShared()
    }

    private fun getDeviceFromShared() : DeviseState {
        val prefs: SharedPreferences = getSharedPreferences(app, "device")
        val deviceString = prefs.getString("device", "")
        val deviceState = DeviseState()
        if (deviceString.equals("")) deviceState.unBind()
        else deviceState.bind(Gson().fromJson(deviceString, BluetoothDevice::class.java))
        return deviceState
    }

    companion object {
        lateinit var app: Application

        var deviceState = DeviseState()
    }

}

