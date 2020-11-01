package pro.butovanton.fitnes2

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import pro.butovanton.fitnes2.db.DataClass
import pro.butovanton.fitnes2.net.Convertor
import pro.butovanton.fitnes2.util.Logs
import pro.butovanton.fitness.net.JSONPlaceHolderApi
import java.io.IOException

class MService : Service() {

    var reportToModel : ReportToModel? = null
        set(value) { field = value }
    private val mBinder: IBinder = LocalBinder()
    lateinit var job : Job
    lateinit var jobBase : Job
    lateinit var jobSendData : Job

    lateinit var mStateDisposable : Disposable

    var serverAvialCash : Boolean? = null
    val data = DataClass()

    inner class LocalBinder : Binder() {
        val service: MService
            get() = this@MService
    }

    private val api = InjectorUtils.provideApi()
    private val deviceClass = InjectorUtils.provideDevice()
    private val locationClass = InjectorUtils.provideLocation()
    private val dao = InjectorUtils.provideDao()

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

        jobBase = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                if (deviceClass.isConnected()) {
                    bateryGet()
                    val batary = deviceClass.getBatary().percentage
                    reportToModel?.batary(batary = batary)
                    val location = locationClass.getLocation()
                    Logs.d("Location = " + location)
                    data.add(location)
                        val health = deviceClass.getHealthSuspend()
                         health?.let {
                            //analise this
                            data.add(it)
                            dao.insertLast(data.getMOdelToRoom())
                            Logs.d("Health from device: " + it.toString())
                         }
                        delay(12000)
                } else
                    delay(5000)

            }
        }

        jobSendData = GlobalScope.launch(Dispatchers.IO) {
            val convertor = Convertor()
   //         while (true) {
   //                 val data = dao.getLastData()
   //                 while (data != null && !api.postDetail(convertor.toRetrofit(data)))
  //                       delay(5000)
  //                  dao.deleteLast()
 //                   delay(20000)
  //          }
        }
    }

    suspend private fun bateryGet() {
        val batary = deviceClass.getBatary()
        Logs.d("Batary = " + batary.percentage)
        reportToModel?.batary(batary.percentage)
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
            if (deviceClass.isConnected()) {
                deviceClass.disConnect()
            }
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