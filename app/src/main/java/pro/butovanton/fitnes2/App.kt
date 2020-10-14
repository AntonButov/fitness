package pro.butovanton.fitnes2

import android.app.Application
import com.htsmart.wristband2.WristbandApplication

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        WristbandApplication.init(this);
        WristbandApplication.setDebugEnable(true);
    }

}
