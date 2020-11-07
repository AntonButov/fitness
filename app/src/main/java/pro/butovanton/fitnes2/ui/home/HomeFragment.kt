package pro.butovanton.fitnes2.ui.home

import android.app.AlertDialog
import android.content.*
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
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.R
import pro.butovanton.fitnes2.shift.Shift
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private val model: HomeViewModel by viewModels()
    private lateinit var timerTV : TextView
    private lateinit var smartBataryTV : TextView
    private lateinit var tickReceiver : BroadcastReceiver
    private lateinit var shiftBT : Button

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

        shiftBT = root.findViewById(R.id.shiftB) as Button
        shiftBT.setOnClickListener {
            model.closeShift().observe(viewLifecycleOwner, object : Observer<Int> {
                override fun onChanged(shift: Int) {
                    when (shift) {
                       Shift.SHIFTOFF -> {
                           val dialogFinishWork = AlertDialog
                               .Builder(context)
                               .setTitle("Работа закончена")
                               .setMessage("Смена окончена успешно." + "\n" +
                                           "Можно выключить телефон")
                               .setPositiveButton("Ок", object : DialogInterface.OnClickListener {
                                   override fun onClick(dialog: DialogInterface?, which: Int) {
                                       model.stopService()
                                       activity?.finish()
                                   }
                               })
                               .create()
                           dialogFinishWork.setCancelable(false)
                           dialogFinishWork.show()
                       }
                        Shift.SHIFTONN -> {
                            val dialogFinishWork = AlertDialog
                                .Builder(context)
                                .setTitle("")
                                .setMessage("Окончание смены не завершено." + "\n" +
                                            "Повторить да/нет")
                                .setPositiveButton("Да", object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        model.closeShift()
                                    }
                                })
                                .setNegativeButton("нет",  object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        sevriceGo()
                                    }
                                })
                                .create()
                            dialogFinishWork.setCancelable(false)
                            dialogFinishWork.show()
                        }
                    }
                }
            })
        }
        setShift()

        timerTV = root.findViewById(R.id.timerTV)
        smartBataryTV = root.findViewById(R.id.chargeSmartTV)


        tickReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.action!!.compareTo(Intent.ACTION_TIME_TICK) == 0) {
                  setTimeAndBatary()
                }
            }
        }

        context?.registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))

        setTimeAndBatary()

        return root
    }

    fun setShift() {
        when (App.deviceState.isBind()) {
            true -> shiftBT.visibility = View.VISIBLE;
            false -> shiftBT.visibility = View.INVISIBLE;
        }
    }

    fun setTimeAndBatary() {
        val sdf = SimpleDateFormat("HH:mm")
        val currentDate = sdf.format(Date())
        timerTV.text = currentDate
        smartBataryTV.text = model.getBataryPercent().toString() + "%"
    }

    fun sevriceGo() {
        val dialogFinishWork = AlertDialog
            .Builder(context)
            .setTitle("")
            .setMessage("Смена незакончена." + "\n" +
                    "Необходимо посетить IT отдел")
            .setPositiveButton("Ок", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    activity?.finish()
                }
            })
            .create()
        dialogFinishWork.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(tickReceiver)
    }
}