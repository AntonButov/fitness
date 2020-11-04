package pro.butovanton.fitnes2.shift

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.realsil.sdk.core.utility.SharedPrefesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.App.Companion.app
import pro.butovanton.fitnes2.InjectorUtils
import pro.butovanton.fitnes2.utils.Logs
import pro.butovanton.fitnes2.utils.AndPermissionHelper.Utils
import pro.butovanton.fitness.net.Api

class Shift(val context: Context) {

    val shiftOpenLive = MutableLiveData<Int>()
    val shiftCloseLive = MutableLiveData<Int>()

    companion object {
        val SHIFTOFF = 0
        val SHIFTONN = 1
    }

    private val api = InjectorUtils.provideApi()

    private val prefs: SharedPreferences = SharedPrefesHelper.getSharedPreferences(app, "shift")

    var shift : Int = initShift()
        set(value) {
            field = value
            prefs.edit().putInt("shift", value).apply()
        }

    private fun initShift() : Int {
        val shift = prefs.getInt("shift", SHIFTOFF)
    return shift
    }

    fun openShift() : LiveData<Int> {
            GlobalScope.launch(Dispatchers.Main)  {
                if (api.workShift("00000000-0000-0000-0000-" + Utils.del2dot(App.deviceState.device?.address!!),true) == Api.OK) {
                    shift = SHIFTONN
                    shiftOpenLive.postValue(shift)
                }
                else {
                    // Вызов повтора открытия смены
                }
            }
        return shiftOpenLive
        }


    fun closeShift() : LiveData<Int>{
        GlobalScope.launch(Dispatchers.Main)  {
            if (api.workShift("00000000-0000-0000-0000-" + Utils.del2dot(App.deviceState.device?.address!!),false) == Api.OK) {
                shift = SHIFTOFF
                shiftCloseLive.postValue(shift)
            }
            else {
                // Вызов повтора закрытия смены
            }
        }
        return  shiftCloseLive
    }

}
