package pro.butovanton.fitnes2.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_log.*
import pro.butovanton.fitnes2.R

class LogFragment : Fragment() {

    private val model: LogViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_log, container, false)

/*
        model.log.observe(viewLifecycleOwner, object : Observer<String> {
            override fun onChanged(log: String?) {
                logTV.text = "" + logTV.text + log + "\n"
            }
        })

 */

        val viewManager = LinearLayoutManager(activity)
        viewManager.stackFromEnd = true
        lifecycleScope.launchWhenCreated {
            val blacBoxMessages = model.getBlackBox()
            val adapterLog = LogAdapter(blacBoxMessages = blacBoxMessages)
            root.findViewById<RecyclerView>(R.id.blackBoxRV).apply {
            layoutManager = viewManager
            adapter = adapterLog
            }
           }

        return root
    }
}