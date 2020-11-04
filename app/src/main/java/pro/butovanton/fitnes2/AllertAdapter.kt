package pro.butovanton.fitnes2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
/*
class AllertAdapter (val allerts: List<AllertResponse>) :
    RecyclerView.Adapter<AllertAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val guestIV = view.findViewById<CircleImageView>(R.id.guestIV)
        val guestTV = view.findViewById<TextView>(R.id.nameGuestTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        var view : View
        view = LayoutInflater.from(parent.context).inflate(
            R.layout.guests_item,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso
            .get()
            .load(guests[position].urlGuest)
            .into(holder.guestIV)
        holder.guestIV
        holder.guestTV.text = guests[position].name
    }

    override fun getItemCount() = guests.size
}


 */