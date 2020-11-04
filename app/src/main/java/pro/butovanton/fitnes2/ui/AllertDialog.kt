package pro.butovanton.fitnes2.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class AllertDialog() : DialogFragment() {

    /*
    companion object {
        fun newInstance(bundle1: Bundle, bundle: List<AlertResponse>): AllertDialog {
            val fragment = AllertDialog(allerts)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.allert_dialog_view, null)

        val viewManager = LinearLayoutManager(this)
        val adapterGuest = AllertAdapter(allerts)

        v.findViewById<RecyclerView>(R.id.allertRV).apply {
            layoutManager = viewManager
            adapter = adapterGuest
        }

       return AlertDialog.Builder(requireActivity())
            .setTitle("Внимание!")
            .setView(v)
            .setPositiveButton("Согласен") { dialog: DialogInterface?, which: Int -> }
            .setCancelable(false)
            .setOnCancelListener {

           }
            .create()
    }


 */
}