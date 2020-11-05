package pro.butovanton.fitnes2.ui.bind_and_find

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_bind.*
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.MainActivity
import pro.butovanton.fitnes2.R
import pro.butovanton.fitnes2.ui.PasswordDialog
import pro.butovanton.fitnes2.ui.PasswordListener
import kotlin.concurrent.fixedRateTimer

class BindFragment : Fragment(), PasswordListener {

    private val model: BindViewModel by viewModels()
    lateinit var activityMy : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val root = inflater.inflate(R.layout.fragment_bind, container, false)
       if (!model.isBind()) {
           (activity as MainActivity).navController.navigate(R.id.action_nav_bind_to_nav_find_devices)
       }
       else {
           val deviceTV = root.findViewById(R.id.deviceTV) as TextView
           deviceTV.text = model.getName()
       }
        val unBindB = root.findViewById<Button>(R.id.unBindB)
        unBindB.setOnClickListener {
            model.unBind(requireActivity())
            (activity as MainActivity).navController.navigate(R.id.action_nav_bind_to_nav_find_devices)
        }

        val passwordDialog = PasswordDialog(this)
        passwordDialog.show(requireFragmentManager(), "passwordDialog")

        return root
    }

    override fun passwordFailure() {
        activityMy.navController.popBackStack()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        activityMy = activity as MainActivity
    }
}