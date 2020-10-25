package pro.butovanton.fitnes2.ui.bind_and_find

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.htsmart.wristband2.WristbandApplication
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.MService

class BindViewModel(application: Application) : AndroidViewModel(application) {

    private val mWristbandManager = WristbandApplication.getWristbandManager()

    fun isBind() : Boolean {
        return (App).deviceState.isBind()
    }

    fun getName() : String {
        return (App).deviceState.device?.address.toString()
    }

    fun unBind(activity: Activity) {
            (App).deviceState.unBind()
            startService(activity)
    }

    private fun startService(activity: Activity) {
        val intent = Intent(activity, MService::class.java)
        activity.startService(intent)
    }

}