package pro.butovanton.fitnes2.ui.home

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.htsmart.wristband2.bean.ConnectionState
import pro.butovanton.fitnes2.InjectorUtils
import pro.butovanton.fitnes2.MService
import pro.butovanton.fitnes2.MService.LocalBinder
import pro.butovanton.fitnes2.ReportToModel
import pro.butovanton.fitnes2.shift.ShiftListener
import pro.butovanton.fitnes2.util.Logs
import pro.butovanton.fitness.net.JSONPlaceHolderApi


class HomeViewModel(application: Application) : AndroidViewModel(application), ReportToModel, ShiftListener {

    private lateinit var mService : MService
    private var mBound = false
    val serverAvialLive : MutableLiveData<Boolean> = MutableLiveData()
    val deviceStateLive : MutableLiveData<ConnectionState> = MutableLiveData()
    val deviceBataryLive : MutableLiveData<Int> = MutableLiveData()
    private val mapplication = application
    val shiftLive = MutableLiveData<Boolean>()

    private  val shift = InjectorUtils.provideShift(this)

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
        val intent = Intent(application, MService::class.java)
        application.startService(intent)
        application.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
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

    override fun shift(shift: Boolean) {
        shiftLive.value = shift
        Logs.d(" Shift = " + shift)
    }

    fun changeShift() {
        shift.changeShift()
    }
}

