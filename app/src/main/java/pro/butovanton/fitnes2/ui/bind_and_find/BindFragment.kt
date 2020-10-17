package pro.butovanton.fitnes2.ui.bind_and_find

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

class BindFragment : Fragment() {

    private val model: BindViewModel by viewModels()
    private var device : BluetoothDevice? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       val root = inflater.inflate(R.layout.fragment_bind, container, false)
       device = (App).device
       if (device == null)
            (activity as MainActivity).navController.navigate(R.id.nav_find_devices)
       else {
           val deviceTV = root.findViewById(R.id.deviceTV) as TextView
           deviceTV.text = device!!.name
       }

        val unBindB = root.findViewById<Button>(R.id.unBindB)
        unBindB.setOnClickListener {
            (App).device = null
            (activity as MainActivity).navController.navigate(R.id.nav_find_devices)
        }
        return root
    }
}