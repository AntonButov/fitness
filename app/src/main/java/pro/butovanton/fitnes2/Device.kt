package pro.butovanton.fitnes2

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.htsmart.wristband2.WristbandApplication
import pro.butovanton.fitnes2.mock.DbMock
import pro.butovanton.fitnes2.mock.User
import pro.butovanton.fitnes2.mock.UserMock
import java.lang.String

class Device {

    private val mWristbandManager = WristbandApplication.getWristbandManager()
    var device : BluetoothDevice? = null
    var isBind = false
    private val mUser: User = UserMock.getLoginUser()

   fun initDevice(context: Context) {
       device = (App).device
       connect(context)
   }

    private fun connect(context: Context) {
        isBind = DbMock.isUserBind(context, device, mUser)

        //If previously bind, use login mode
        //If haven't  bind before, use bind mode
        Log.d("DEBUG","Connect device:" + device!!.getAddress() + " with user:" + mUser.getId() + " use " + (if (isBind) "Login" else "Bind") + " mode")
        if (!isBind)
               mWristbandManager.connect(
                    device!!,
                    String.valueOf(mUser.getId()),
                    !isBind,
                    mUser.isSex(),
                    mUser.getAge(),
                    mUser.getHeight(),
                    mUser.getWeight()
        )
    }

}