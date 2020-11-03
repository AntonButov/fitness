package pro.butovanton.fitnes2.ui.log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pro.butovanton.fitnes2.R
import pro.butovanton.fitnes2.db.blackbox.BlackBox
import pro.butovanton.fitnes2.utils.AndPermissionHelper.Utils

class LogAdapter (val blacBoxMessages: List<BlackBox>) :
    RecyclerView.Adapter<LogAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val timeTV = view.findViewById<TextView>(R.id.timeTV)
        val messageTV = view.findViewById<TextView>(R.id.messageTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        var view : View
        view = LayoutInflater.from(parent.context).inflate(
            R.layout.black_box_item,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.timeTV.text = Utils.longDateToString(blacBoxMessages[position].created)
        holder.messageTV.text = blacBoxMessages[position].message
    }

    override fun getItemCount() = blacBoxMessages.size
}
