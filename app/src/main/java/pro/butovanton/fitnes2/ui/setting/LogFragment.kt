package pro.butovanton.fitnes2.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_setting_server.*
import pro.butovanton.fitnes2.R
import pro.butovanton.fitnes2.ui.bind_and_find.BindViewModel

class LogFragment : Fragment() {

    private val model: LogViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_setting_server, container, false)

        model.log.observe(viewLifecycleOwner, object : Observer<String> {
            override fun onChanged(log: String?) {
                logTV.text = "" + logTV.text + log + "\n"
            }
        })

        return root
    }
}