package pro.butovanton.fitnes2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.htsmart.wristband2.bean.ConnectionState
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import pro.butovanton.fitnes2.MainActivity
import pro.butovanton.fitnes2.R
import pro.butovanton.fitnes2.util.Logs
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private val model: HomeViewModel by viewModels()
    private lateinit var timerTV : TextView

    @InternalCoroutinesApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

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
                chargeDeviceTV2.text = "" + bataryDevice + "%"
            }

        })

        timerTV = root.findViewById(R.id.timerTV)

        val shiftB = root.findViewById(R.id.shiftB) as Button
        shiftB.setOnClickListener {
            model.changeShift()
        }

        model.shiftLive.observe(viewLifecycleOwner, object : Observer<Boolean> {
            override fun onChanged(shiftReal : Boolean?) {
            when (shiftReal) {
                true -> shiftB.setText("Открыть смену.")
                false -> shiftB . setText ("Зактрыть смену.")
            }
        }
        })

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