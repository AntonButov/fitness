package pro.butovanton.fitnes2

import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.htsmart.wristband2.WristbandApplication
import com.htsmart.wristband2.bean.ConnectionError
import com.htsmart.wristband2.bean.ConnectionState
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import pro.butovanton.fitnes2.mock.DbMock
import pro.butovanton.fitnes2.mock.User
import pro.butovanton.fitnes2.mock.UserMock
import pro.butovanton.fitness.net.JSONPlaceHolderApi
import java.lang.String

class MService : Service() {

    var isBind = false
    private val mUser: User = UserMock.getLoginUser()

    var reportToModel : ReportToModel? = null
        set(value) { field = value }
    private val mBinder: IBinder = LocalBinder()
    lateinit var job : Job
    lateinit var jobConnectStatus : Job

    var serverAvialCash : Boolean? = null

    inner class LocalBinder : Binder() {
        val service: MService
            get() = this@MService
    }

    private val api = InjectorUtils.provideApi()
    private val deviceClass = InjectorUtils.provideDevice()

    override fun onCreate() {
        super.onCreate()
        job = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                (App).device?.let {
                    val serverAvial = serverAvial()
                    Log.d("DEBUG", "ServerAvable from service : " + serverAvial)
                    outServerAvial(serverAvial)
                }
            delay(60000)
            }
        }

        jobConnectStatus = GlobalScope.launch(Dispatchers.Main) {
            deviceClass.flowConectSatate().collect {
                Log.d("DEBUG", "Flow connect = " + it)
            }
        }
    }

    private suspend fun serverAvial(): Boolean {
        try {
            val response = api.alert(JSONPlaceHolderApi.GUID)
            return true
        }
        catch (t: Throwable) {
            return false
        }
    }

    private fun outServerAvial(serverAvial: Boolean) {
        serverAvialCash = serverAvial
        reportToModel?.serverAvial(serverAvial)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if ((App).device != null)
           deviceClass.initDevice(this)
        else
            deviceClass.stop()
        Log.d("DEBUG", "Service startComand.")
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d("DEBUG", "Service bind.")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        Log.d("DEBUG", "Service unBind.")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("DEBUG", "Task removed.")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        deviceClass.stop()
        Log.d("DEBUG", "Service destroy.")
    }
}