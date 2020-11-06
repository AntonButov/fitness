package pro.butovanton.fitnes2.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_setting_server.*
import pro.butovanton.fitnes2.R


class PasswordDialog(val mPasswordListener : PasswordListener) : DialogFragment() {

    companion object {
        val PASSWORD = "iHealth"
        fun newInstance(bundle: Bundle, passwordListener : PasswordListener): PasswordDialog {
            val fragment = PasswordDialog(passwordListener)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.password_dialog_view, null)


        return AlertDialog.Builder(requireActivity())
            .setTitle("Введите пароль")
            .setView(v)
            .setPositiveButton("Согласен") { dialog: DialogInterface?, which: Int ->
                val passwordText = v.findViewById<EditText>(R.id.passwordED)
                if (!passwordText.text.toString().equals(PASSWORD))
                    mPasswordListener.passwordFailure()
            }
            .create()
    }
}
