package pro.butovanton.fitnes2

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.htsmart.wristband2.bean.ConnectionState
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import pro.butovanton.fitnes2.util.Logs
import pro.butovanton.fitness.net.JSONPlaceHolderApi

class MService : Service() {

    var reportToModel : ReportToModel? = null
        set(value) { field = value }
    private val mBinder: IBinder = LocalBinder()
    lateinit var job : Job
    lateinit var jobBatary : Job

    lateinit var mStateDisposable : Disposable

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
                if ((App).deviceState.isBind()) {
                    val serverAvial = serverAvial()
                    Logs.d("ServerAvable from service = " + serverAvial)
                    outServerAvial(serverAvial)
                }
                delay(60000)
            }
        }

        mStateDisposable = deviceClass.connecterObserver()
            .subscribe { connectionState ->
                Logs.d("connected state " + connectionState.toString())
                reportToModel?.deviceAvial(connectionState)
            }

        jobBatary = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                if (deviceClass.isConnected()) {
                    val batary = deviceClass.getBatary().percentage
                    Logs.d("Batary = " + batary)
                    reportToModel?.batary(batary)
                 //   val dataLast = deviceClass.dataSHealthyingle()
                 //   Logs.d("Health in moment: " + dataLast.toString())
                }
            delay(100000)
            }
        }

        jobBatary = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                if (deviceClass.isConnected()) {
                    val data = deviceClass.data()
                    Logs.d("Health request.")
                    delay(60000)
                }
                else
                   delay(6000)
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
        if ((App).deviceState.isBind()) {
            if (!deviceClass.isConnected()) {
                    deviceClass.connect()
            }
        }
        else
            if (deviceClass.isConnected())
               deviceClass.disConnect()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Logs.d("Service bind.")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        Logs.d("Service unBind.")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Logs.d("Task removed.")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        deviceClass.stop()
        Logs.d("Service destroy.")
    }
}