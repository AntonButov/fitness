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
        }

    var shotDown = false

    fun shotDown() {
        shotDown = true
    }

    private fun saveToDivice(device: BluetoothDevice?) {
        val prefs: SharedPreferences = SharedPrefesHelper.getSharedPreferences(App.app, "device")
        var deviceString = ""
        if (device != null)
            deviceString = Gson().toJson(device)
        prefs.edit().putString("device", deviceString).apply()
    }

    fun isBind() = device != null

    fun bind(device: BluetoothDevice?) {
        this.device = device
    }

    fun unBind() {
        this.device = null
    }
}