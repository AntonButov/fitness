package pro.butovanton.fitnes2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pro.butovanton.fitnes2.net.retrofitDataClass.AlertResponse

class AllertAdapter (val allerts: List<AlertResponse>) :
    RecyclerView.Adapter<AllertAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val allertTextView = view.findViewById<TextView>(R.id.messageTV)
        val backGroundLL = view.findViewById<LinearLayout>(R.id.item_background)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        var view : View
        view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_alert_list,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.allertTextView.text = allerts[position].description
       val code = allerts[position].code/ 100
       when (code) {
           5 -> holder.backGroundLL.setBackgroundResource(R.color.fivexx)
           4 -> holder.backGroundLL.setBackgroundResource(R.color.fourxx)
           3 -> holder.backGroundLL.setBackgroundResource(R.color.threexx)
           2 -> holder.backGroundLL.setBackgroundResource(R.color.twoxx)
       }
    }

    override fun getItemCount() = allerts.size
}


