package pro.butovanton.fitnes2.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import pro.butovanton.fitnes2.R

class InfoFragment : Fragment() {

    private lateinit var infoViewModel: InfoViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        infoViewModel =
                ViewModelProviders.of(this).get(InfoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_info, container, false)

        return root
    }
}