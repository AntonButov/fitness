package pro.butovanton.fitnes2.shift

import android.content.Context
import android.content.SharedPreferences
import com.realsil.sdk.core.utility.SharedPrefesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.App.Companion.app
import pro.butovanton.fitnes2.InjectorUtils
import pro.butovanton.fitness.net.JSONPlaceHolderApi

class Shift(val context: Context, val shiftListener : ShiftListener) {

    private val api = InjectorUtils.provideApi()

    private val prefs: SharedPreferences = SharedPrefesHelper.getSharedPreferences(app, "shift")

    private var shift : Boolean = initShift()
        set(value) {
            field = value
            prefs.edit().putBoolean("shift", value).apply()
        }

    private fun initShift() : Boolean {
        val shift = prefs.getBoolean("shift", false)
        shiftListener.shift(shift)
    return shift
    }

    fun changeShift() {
        GlobalScope.launch(Dispatchers.Main)  {
   //         if (api.workShift(App.deviceState.device?.address!!, !shift)) {
            if (api.workShift(JSONPlaceHolderApi.GUID, !shift)) {
                shift = !shift
                shiftListener.shift(shift)
            }
            else {}
        }
    }
}