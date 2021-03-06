package pro.butovanton.fitnes2.ui.info

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_info.*
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.R

class InfoFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_info, container, false)
        val macDev = root.findViewById<TextView>(R.id.macDeviceTV)
        if ((App).deviceState.isBind()) {
            macDev.text = "MAC: " + macDev.text + ((App).deviceState.device?.address)
        }
        val packInfo = context?.packageManager?.getPackageInfo(context?.packageName.toString(),0)
        val appVer = root.findViewById<TextView>(R.id.appVerTV)
        appVer.text = "" + appVer.text + packInfo?.versionName
        return root
    }
}