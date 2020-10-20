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
import pro.butovanton.fitnes2.InjectorUtils
import pro.butovanton.fitnes2.MService
import pro.butovanton.fitnes2.MService.LocalBinder
import pro.butovanton.fitnes2.ReportToModel
import pro.butovanton.fitness.net.JSONPlaceHolderApi


class HomeViewModel(application: Application) : AndroidViewModel(application), ReportToModel {

    private lateinit var mService : MService
    private var mBound = false
    val serverAvialLive : MutableLiveData<Boolean> = MutableLiveData()
    private val mapplication = application

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

    fun getServerAvial() : LiveData<Boolean> {
        return serverAvialLive
    }

    override fun serverAvial(sevrerAvial: Boolean) {
        serverAvialLive.postValue(sevrerAvial)
    }

    val batary = InjectorUtils.provideBatary()

    fun getBataryPercent() : Int {
        return batary.getBatteryPercentage(mapplication)
    }
}

