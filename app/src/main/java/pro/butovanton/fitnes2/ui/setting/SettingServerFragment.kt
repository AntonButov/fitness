package pro.butovanton.fitnes2.ui.setting

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setting_server.*
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.R

class SettingServerFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_setting_server, container, false)
        val ipPortET = root.findViewById<EditText>(R.id.ipPortET)
        ipPortET.setText(App.deviceState.ipPorv)
        val saveButton = root.findViewById<Button>(R.id.buttonSave)
        saveButton.setOnClickListener {
            App.deviceState.ipPorv = ipPortET.text.toString()
        }
        return root
    }
}