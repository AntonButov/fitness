package pro.butovanton.fitnes2.ui.bind_and_find

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pro.butovanton.fitnes2.R


class FindDeviceAdapter (val guests: List<Int>) :
    RecyclerView.Adapter<FindDeviceAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
       // val guestIV = view.findViewById<CircleImageView>(R.id.guestIV)
      //  val guestTV = view.findViewById<TextView>(R.id.nameGuestTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        var view : View
        view = LayoutInflater.from(parent.context).inflate(
            R.layout.find_divices_item,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    override fun getItemCount() = guests.size
}
