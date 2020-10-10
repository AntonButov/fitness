package pro.butovanton.fitnes2.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import pro.butovanton.fitnes2.R
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var model: HomeViewModel
    private lateinit var timerTV : TextView

    @InternalCoroutinesApi
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        model =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

            timerTV = root.findViewById(R.id.timerTV)
            lifecycleScope.launchWhenCreated {
                while (true) {
                    val sdf = SimpleDateFormat("HH:mm")
                    val currentDate = sdf.format(Date())
                    timerTV.text = currentDate
                    delay(1000)
                }
            }

            lifecycleScope.launchWhenCreated {
                model.serverAvial().collect {  serverAvial ->
                    var imageServerAvial = 0
                    if (serverAvial)
                        imageServerAvial = R.drawable.greencircle
                    else
                        imageServerAvial = R.drawable.redcircle
                    Glide
                        .with(this@HomeFragment)
                        .load(imageServerAvial)
                        .into(serverAvialIV);
                }
        }

        return root
    }
}