package pro.butovanton.fitnes2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_allert.*

class AllertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allert)

        val allerts = App.deviceState.allerts
        val viewManager = LinearLayoutManager(this)
        val adapterGuest = allerts?.let { AllertAdapter(it) }

        findViewById<RecyclerView>(R.id.allertRV).apply {
            layoutManager = viewManager
            adapter = adapterGuest
        }

        buttonOk.setOnClickListener {
            finish()
        }
    }
}