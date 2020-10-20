package pro.butovanton.fitnes2.ui.bind_and_find

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pro.butovanton.fitnes2.App

class BindViewModel : ViewModel() {

    fun isBind() : Boolean {
        return (App).device != null
    }

    fun getName() : String {
        return (App).device?.name.toString()
    }

    fun unBind() {
        (App).device = null
    }

}