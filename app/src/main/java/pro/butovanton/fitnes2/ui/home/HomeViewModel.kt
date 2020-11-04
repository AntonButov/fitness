package pro.butovanton.fitnes2.ui.home

import android.app.Application
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.htsmart.wristband2.bean.ConnectionState
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.App.Companion.app
import pro.butovanton.fitnes2.InjectorUtils
import pro.butovanton.fitnes2.MService
import pro.butovanton.fitnes2.MService.LocalBinder
import pro.butovanton.fitnes2.ReportToModel
import pro.butovanton.fitnes2.shift.ShiftListener
import pro.butovanton.fitnes2.utils.Logs


class HomeViewModel(application: Application) : AndroidViewModel(application), ReportToModel {

    private lateinit var mService : MService
    private var mBound = false
    val serverAvialLive : MutableLiveData<Boolean> = MutableLiveData()
    val deviceStateLive : MutableLiveData<ConnectionState> = MutableLiveData()
    val deviceBataryLive : MutableLiveData<Int> = MutableLiveData()
    private val mapplication = application

    private  val shift = InjectorUtils.provideShift()

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            val binder = service as LocalBinder
            mService = binder.service
            mBound = true
            mService.reportToModel = this@HomeViewModel
            serverAvialLive.value = mService.serverAvialCash
            deviceBataryLive.value = mService.bataryCash
            deviceStateLive.value = mService.deviceStateCash
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
            mService.reportToModel = null
        }
    }

    init {
        if (App.deviceState.isBind()) {
            val intent = Intent(app, MService::class.java)
            app.startService(intent)
            app.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

        }
    }

    fun stopService() {
        App.deviceState.shotDown()
        val intent = Intent(app, MService::class.java)
        app.startService(intent)
        app.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        app.stopService(intent)
        app.unbindService(mConnection)

    }

    override fun serverAvial(sevrerAvial: Boolean) {
        serverAvialLive.postValue(sevrerAvial)
    }

    override fun deviceAvial(deviceConnectState: ConnectionState) {
        deviceStateLive.postValue(deviceConnectState)
    }

    override fun batary(batary: Int) {
        deviceBataryLive.postValue(batary)
    }

    val batary = InjectorUtils.provideBatary()

    fun getBataryPercent() : Int {
        return batary.getBatteryPercentage(mapplication)
    }

    fun getShift() : Int {
        return shift.shift
    }

    fun openShift() : LiveData<Int> {
         return shift.openShift()
    }

    fun closeShift() : LiveData<Int>{
       return shift.closeShift()
       }

}

