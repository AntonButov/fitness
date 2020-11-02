package pro.butovanton.fitnes2

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.SharedPreferences
import com.google.gson.Gson
import com.htsmart.wristband2.WristbandApplication
import com.realsil.sdk.core.utility.SharedPrefesHelper.getSharedPreferences
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig


class App : Application() {

    var app = this

    override fun onCreate() {
        super.onCreate()
        WristbandApplication.init(this);
        WristbandApplication.setDebugEnable(true);
        App.app = app
        deviceState = getDeviceFromShared()

        // Creating an extended library configuration.
        // Creating an extended library configuration.
        val config = YandexMetricaConfig.newConfigBuilder("9137a9cd-da3e-46b5-b066-d6f81703c308").build()
        // Initializing the AppMetrica SDK.
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(applicationContext, config)
        // Automatic tracking of user activity.
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this)
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

