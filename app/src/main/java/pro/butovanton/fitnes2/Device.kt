package pro.butovanton.fitnes2

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.util.Log
import com.htsmart.wristband2.WristbandApplication
import com.htsmart.wristband2.bean.ConnectionError
import com.htsmart.wristband2.bean.ConnectionState
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pro.butovanton.fitnes2.mock.DbMock
import pro.butovanton.fitnes2.mock.User
import pro.butovanton.fitnes2.mock.UserMock
import java.lang.String
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Device {

    private val mWristbandManager = WristbandApplication.getWristbandManager()
    private var mStateDisposable: Disposable? = null
    private var mErrorDisposable: Disposable? = null
    var device: BluetoothDevice? = null
    private val mUser: User = UserMock.getLoginUser()

    private lateinit var context: Context
    private var connectState = ConnectionState.DISCONNECTED

    fun initDevice(context: Context) {
        device = (App).device
        this.context = context
        device?.let {
            connect()
        }
    }

    fun disConnect() {
        mWristbandManager.close()
    }

    private fun connect() {
           mWristbandManager.connect(
                device!!,
                String.valueOf(mUser.getId()),
                false,
                mUser.isSex(),
                mUser.getAge(),
                mUser.getHeight(),
                mUser.getWeight()
            )
    }

    fun flowConectSatate() : Flow<Boolean> = flow {
        emit(isConnect())
    }

    suspend fun isConnect(): Boolean {
        return suspendCoroutine { cont ->
            mStateDisposable = mWristbandManager.observerConnectionState()
                .subscribe { connectionState ->
                    if (connectionState == ConnectionState.DISCONNECTED) {
                        if (mWristbandManager.rxBleDevice == null) {
                            Log.d("DEBUG", "active disconnect")
                        } else {
                            if (connectionState == ConnectionState.CONNECTED) {
                                Log.d("DEBUG", "passive disconnect")
                            } else {
                                Log.d("DEBUG", "connect_failed")
                            }
                        }
                        cont.resume(false)
                    } else if (connectionState == ConnectionState.CONNECTED) {
                        Log.d("DEBUG", "connect_success")
                        cont.resume(true)
                        if (mWristbandManager.isBindOrLogin) {
                            //If connect with bind mode, clear Today Step Data
                            Log.d("DEBUG", "connect_bind_tips")
                        } else {
                            Log.d("DEBUG", "connect_login_tips")
                        }
                    } else {
                        Log.d("DEBUG", "state_connecting")
                    }
                    connectState = connectionState
                }
        }

      suspend fun errors(): Boolean {
            return suspendCoroutine { cont ->
                mErrorDisposable = mWristbandManager.observerConnectionError()
                    .subscribe { connectionError ->
                        Log.d("DEBUG","Connect Error occur and retry:" + connectionError.isRetry, connectionError.throwable)
                        cont.resume(false)
                    }
            }
        }
    }

    fun isConnected(): Boolean {
        return mWristbandManager.isConnected
    }

    fun stop() {
        mErrorDisposable?.dispose()
        mStateDisposable?.dispose()
    }
}

