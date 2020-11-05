package pro.butovanton.fitnes2

import android.bluetooth.BluetoothDevice
import android.content.SharedPreferences
import com.google.gson.Gson
import com.realsil.sdk.core.utility.SharedPrefesHelper
import pro.butovanton.fitnes2.net.retrofitDataClass.AlertResponse

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

    var ipPorv : String? = ""
        get() = SharedPrefesHelper
            .getSharedPreferences(App.app, "device")
            .getString("ipport", "https://212.248.50.40:8080")
        set(value) {
            field = value
            saveToDiviceIpPort(value)
        }

    var allerts : List<AlertResponse>? = null

    private fun saveToDivice(device: BluetoothDevice?) {
        val prefs: SharedPreferences = SharedPrefesHelper.getSharedPreferences(App.app, "device")
        var deviceString = ""
        if (device != null)
            deviceString = Gson().toJson(device)
        prefs.edit().putString("device", deviceString).apply()
    }

    fun saveToDiviceIpPort(ipport: String?) {
        val prefs: SharedPreferences = SharedPrefesHelper.getSharedPreferences(App.app, "device")
        prefs.edit().putString("ipport", ipport).apply()

    }

    fun isBind() = device != null

    fun bind(device: BluetoothDevice?) {
        this.device = device
    }

    fun unBind() {
        this.device = null
    }
}