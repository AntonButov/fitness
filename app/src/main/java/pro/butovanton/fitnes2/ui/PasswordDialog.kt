package pro.butovanton.fitnes2.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pro.butovanton.fitnes2.R


class PasswordDialog() : DialogFragment() {


    companion object {
        fun newInstance(bundle: Bundle) :  PasswordDialog {
            val fragment = PasswordDialog()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.allert_dialog_view, null)


       return AlertDialog.Builder(requireActivity())
            .setTitle("Введите пароль")
            .setView(v)
            .setPositiveButton("Согласен") { dialog: DialogInterface?, which: Int -> }
            .setCancelable(false)
            .setOnCancelListener {

           }
            .create()
    }
Ъ

