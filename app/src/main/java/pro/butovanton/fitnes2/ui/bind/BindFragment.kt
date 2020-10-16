package pro.butovanton.fitnes2.ui.bind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import pro.butovanton.fitnes2.MainActivity
import pro.butovanton.fitnes2.R

class BindFragment : Fragment() {

    private val model: BindViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       val root = inflater.inflate(R.layout.fragment_bind, container, false)

        (activity as MainActivity).navController.navigate(R.id.nav_find_devices)

        return root
    }
}