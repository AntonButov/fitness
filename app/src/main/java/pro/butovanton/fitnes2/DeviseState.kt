package pro.butovanton.fitnes2

import android.bluetooth.BluetoothDevice
import android.content.SharedPreferences
import com.google.gson.Gson
import com.realsil.sdk.core.utility.SharedPrefesHelper

class DeviseState {

    var device: BluetoothDevice?= null
        set(value) {
            field = value
            saveToDivice(value)
            //         if (value != null)
            //             startService()
        }

    var state = device == null

    private fun saveToDivice(device: BluetoothDevice?) {
        val prefs: SharedPreferences = SharedPrefesHelper.getSharedPreferences(App.app, "device")
        var deviceString = ""
        if (device != null)
            deviceString = Gson().toJson(device)
        prefs.edit().putString("device", deviceString).apply()
    }
}