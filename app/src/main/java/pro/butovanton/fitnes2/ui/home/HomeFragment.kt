package pro.butovanton.fitnes2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.htsmart.wristband2.bean.ConnectionState
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import pro.butovanton.fitnes2.R
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var model: HomeViewModel
    private lateinit var timerTV : TextView

    @InternalCoroutinesApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        model =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

            model.serverAvialLive.observe(viewLifecycleOwner , object : Observer<Boolean> {
                override fun onChanged(serverAvial: Boolean?) {
                    var imageServerAvial = 0
                    if (serverAvial != null) {
                        if (serverAvial)
                            imageServerAvial = R.drawable.greencircle
                        else
                            imageServerAvial = R.drawable.redcircle
                    Glide
                        .with(this@HomeFragment)
                        .load(imageServerAvial)
                        .into(serverAvialIV);
                    }
                }
            })

        model.deviceStateLive.observe(viewLifecycleOwner , object : Observer<ConnectionState> {
            override fun onChanged(connectionState: ConnectionState?) {
                var imageDeviceConnectedState = 0
                when (connectionState) {
                    ConnectionState.CONNECTED    -> imageDeviceConnectedState = R.drawable.greencircle
                    ConnectionState.CONNECTING   -> imageDeviceConnectedState = R.drawable.yellowcircle
                    ConnectionState.DISCONNECTED -> imageDeviceConnectedState = R.drawable.redcircle
                }
                Glide
                    .with(this@HomeFragment)
                    .load(imageDeviceConnectedState)
                    .into(deviceStatelIV);

            }

        })

        model.deviceBataryLive.observe(viewLifecycleOwner, object  : Observer<Int> {
            override fun onChanged(bataryDevice: Int?) {
                chargeDeviceTV2.text = "" + bataryDevice
            }

        })

        timerTV = root.findViewById(R.id.timerTV)

        return root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            while (true) {
                chargeSmartTV.text = model.getBataryPercent().toString() + "%"
                delay(60000)
            }
        }

        lifecycleScope.launchWhenResumed {
            while (true) {
                val sdf = SimpleDateFormat("HH:mm")
                val currentDate = sdf.format(Date())
                timerTV.text = currentDate
                delay(1000)
            }
        }

    }
}