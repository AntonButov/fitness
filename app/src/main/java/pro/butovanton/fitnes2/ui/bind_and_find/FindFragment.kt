package pro.butovanton.fitnes2.ui.bind_and_find

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.htsmart.wristband2.WristbandApplication
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.scan.ScanResult
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.MainActivity
import pro.butovanton.fitnes2.R
import pro.butovanton.fitnes2.mock.User
import pro.butovanton.fitnes2.mock.UserMock
import pro.butovanton.fitnes2.utils.AndPermissionHelper
import pro.butovanton.fitnes2.utils.Utils


class FindFragment : Fragment() {

    val model: BindViewModel by viewModels()
    private var mRxBleClient: RxBleClient? = null
    private var mScanDisposable: Disposable? = null
    private var mAdapter: DeviceListAdapter? = null

    private val mUser: User = UserMock.getLoginUser()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_find_devices, container, false)

        mAdapter = DeviceListAdapter()
        val devicesRV = root.findViewById<ListView>(R.id.devicesLV)
        devicesRV.adapter = mAdapter
        devicesRV.setOnItemClickListener { parent, view, position, id ->
            val result = mAdapter!!.getItem(position) as ScanResult
            val device = result.bleDevice.bluetoothDevice
            (App).device = device
            (activity as MainActivity).navController.navigate(R.id.action_nav_find_devices_to_nav_bind)
        }
/*
        val viewManager = LinearLayoutManager(activity)
        val adapterFindDevices = FindDeviceAdapter()

        root.findViewById<RecyclerView>(R.id.findDevicesRV).apply {
            layoutManager = viewManager
            adapter = adapterFindDevices
        }
     */
        //Do not create an RxBleClient instance yourself, please get it like this.

        //Do not create an RxBleClient instance yourself, please get it like this.

        mRxBleClient = WristbandApplication.getRxBleClient()
        startScanning()
        return root
    }

    /**
     * Start scan
     */
    private fun startScanning() {
        mAdapter?.clear()
        if (Utils.checkLocationForBle(activity)) {
            AndPermissionHelper.blePermissionRequest(
                activity as AppCompatActivity?,
                object : AndPermissionHelper.AndPermissionHelperListener1 {
                    override fun onSuccess() {
                        val scanSettings = com.polidea.rxandroidble2.scan.ScanSettings.Builder()
                            .setScanMode(com.polidea.rxandroidble2.scan.ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setCallbackType(com.polidea.rxandroidble2.scan.ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .build()
                        //    mSwipeRefreshLayout.setRefreshing(true)
                        // invalidateOptionsMenu()
                        mScanDisposable = mRxBleClient?.scanBleDevices(scanSettings)
                            ?.subscribe(Consumer<com.polidea.rxandroidble2.scan.ScanResult?> { scanResult ->
                                mAdapter?.add(scanResult)
                                Log.d(
                                    "Debug",
                                    "ScanResult = " + scanResult.bleDevice.macAddress
                                )
                            },
                                Consumer<Throwable?> { stopScanning() })
                    }
                })
        }
    }

    private fun stopScanning() {
     //   if (mScanDisposable != null) mScanDisposable.dispose()
      //  mSwipeRefreshLayout.setRefreshing(false)
      //  invalidateOptionsMenu()
        mScanDisposable?.dispose()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopScanning()
    }

}