package pro.butovanton.fitnes2.ui.bind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import pro.butovanton.fitnes2.R

class BindFragment : Fragment() {

    private lateinit var bindViewModel: BindViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bindViewModel =
                ViewModelProviders.of(this).get(BindViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bind, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        bindViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}