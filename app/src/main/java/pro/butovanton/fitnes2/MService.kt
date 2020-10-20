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
import pro.butovanton.fitnes2.mock.DbMock
import pro.butovanton.fitnes2.mock.User
import pro.butovanton.fitnes2.mock.UserMock
import pro.butovanton.fitness.net.JSONPlaceHolderApi
import java.lang.String

class MService : Service() {

    private var mStateDisposable: Disposable? = null
    private var mErrorDisposable: Disposable? = null

    private val mWristbandManager = WristbandApplication.getWristbandManager()
    var device : BluetoothDevice? = null
    var isBind = false
    private val mUser: User = UserMock.getLoginUser()

    var reportToModel : ReportToModel? = null
        set(value) { field = value }
    private val mBinder: IBinder = LocalBinder()
    lateinit var job : Job

    var serverAvialCash : Boolean? = null

    inner class LocalBinder : Binder() {
        val service: MService
            get() = this@MService
    }

    private val api = InjectorUtils.provideApi()

    override fun onCreate() {
        super.onCreate()
        job = GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                val serverAvial = serverAvial()
                Log.d("DEBUG", "ServerAvable from service : " + serverAvial)
            outServerAvial(serverAvial)
            delay(60000)
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

    fun initDeviceResponse() {
        device = (App).device
        connect(true)
    }

    fun stopDeviceResponse() {
        //TODO
        connect(false)
    }

    private fun connect(connect : Boolean) {
        isBind = DbMock.isUserBind(this, device, mUser)

        //If previously bind, use login mode
        //If haven't  bind before, use bind mode
        Log.d(
            "DEBUG",
            "Connect device:" + device!!.getAddress() + " with user:" + mUser.getId() + " use " + (if (isBind) "Login" else "Bind") + " mode"
        )
            mWristbandManager.connect(
                device!!,
                String.valueOf(mUser.getId()),
                !connect,
                mUser.isSex(),
                mUser.getAge(),
                mUser.getHeight(),
                mUser.getWeight()
            )
    }

    fun initConnectObserver() {
        mStateDisposable = mWristbandManager.observerConnectionState()
            .subscribe(
                object : Consumer<ConnectionState> {
                    @Throws(Exception::class)
                    override fun accept(connectionState: ConnectionState) {
                     Log.d("DEBUG", connectionState.name)
                    }
                })
        mErrorDisposable = mWristbandManager.observerConnectionError()
            .subscribe(
                object : Consumer<ConnectionError> {
                    @Throws(Exception::class)
                    override fun accept(connectionError: ConnectionError) {
                        Log.w("Debug","Connect Error occur and retry:" + connectionError.isRetry, connectionError.throwable)
                    }
                })
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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
        Log.d("DEBUG", "Service destroy.")
    }
}