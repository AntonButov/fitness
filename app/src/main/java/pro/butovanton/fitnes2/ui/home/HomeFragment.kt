package pro.butovanton.fitnes2.ui.home

import android.content.BroadcastReceiver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.htsmart.wristband2.bean.ConnectionState
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.InternalCoroutinesApi
import pro.butovanton.fitnes2.R


class HomeFragment : Fragment() {

    private val model: HomeViewModel by viewModels()
    private lateinit var timerTV : TextView
    private lateinit var smartBataryTV : TextView

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

            model.serverAvialLive.observe(viewLifecycleOwner, object : Observer<Boolean> {
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

        model.deviceStateLive.observe(viewLifecycleOwner, object : Observer<ConnectionState> {
            override fun onChanged(connectionState: ConnectionState?) {
                if (connectionState != null) {
                    var imageDeviceConnectedState = 0
                    when (connectionState) {
                        ConnectionState.CONNECTED -> imageDeviceConnectedState =
                            R.drawable.greencircle
                        ConnectionState.CONNECTING -> imageDeviceConnectedState =
                            R.drawable.yellowcircle
                        ConnectionState.DISCONNECTED -> imageDeviceConnectedState =
                            R.drawable.redcircle
                    }
                    Glide
                        .with(this@HomeFragment)
                        .load(imageDeviceConnectedState)
                        .into(deviceStatelIV);

                }
            }
        })

        model.deviceBataryLive.observe(viewLifecycleOwner, object : Observer<Int> {
            override fun onChanged(bataryDevice: Int?) {
                if (bataryDevice != null)
                    chargeDeviceTV2.text = "" + bataryDevice + "%"
            }

        })


        val shiftB = root.findViewById(R.id.shiftB) as Button
        shiftB.setOnClickListener {
            model.changeShift()
        }

        model.shiftLive.observe(viewLifecycleOwner, object : Observer<Boolean> {
            override fun onChanged(shiftReal: Boolean?) {
                when (shiftReal) {
                    true -> shiftB.setText("Открытие смены.")
                    false -> shiftB.setText("Окончание смены.")
                }
            }
        })

        timerTV = root.findViewById(R.id.timerTV)
        smartBataryTV = root.findViewById(R.id.chargeSmartTV)
     //   lifecycleScope.launchWhenCreated {
       //     while (true) {
        //        val sdf = SimpleDateFormat("HH:mm")
       //         val currentDate = sdf.format(Date())
       //         timerTV.text = currentDate
      //          smartBataryTV.text = model.getBataryPercent().toString() + "%"
      //          delay(30000)
      //      }
     //   }

        var tickReceiver: BroadcastReceiver

        return root
    }

    override fun onResume() {
        super.onResume()

    }
}