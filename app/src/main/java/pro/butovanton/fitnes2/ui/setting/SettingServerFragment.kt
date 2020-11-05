package pro.butovanton.fitnes2.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.MainActivity
import pro.butovanton.fitnes2.R
import pro.butovanton.fitnes2.ui.PasswordDialog
import pro.butovanton.fitnes2.ui.PasswordListener

class SettingServerFragment : Fragment() , PasswordListener{

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_setting_server, container, false)
        val ipPortET = root.findViewById<EditText>(R.id.ipPortET)
        ipPortET.setText(App.deviceState.ipPorv)
        val saveButton = root.findViewById<Button>(R.id.buttonSaveBT)
        saveButton.setOnClickListener {
            App.deviceState.ipPorv = ipPortET.text.toString()
        }

        val passwordDialog = PasswordDialog(this)
        passwordDialog.show(requireFragmentManager(), "passwordDialog")
        return root
    }

    override fun passwordFailure() {
        val activity = activity as MainActivity
        activity.navController.popBackStack()
    }
}