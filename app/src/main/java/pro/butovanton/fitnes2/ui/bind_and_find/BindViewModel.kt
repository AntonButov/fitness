package pro.butovanton.fitnes2.ui.bind_and_find

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.htsmart.wristband2.WristbandApplication
import pro.butovanton.fitnes2.App

class BindViewModel(application: Application) : AndroidViewModel(application) {

    private val mWristbandManager = WristbandApplication.getWristbandManager()

    fun isBind() : Boolean {
        return (App).device != null
    }

    fun getName() : String {
        return (App).device?.name.toString()
    }

    fun unBind() {
        if (mWristbandManager.isConnected)
            mWristbandManager.close()
        (App).device = null
    }

}