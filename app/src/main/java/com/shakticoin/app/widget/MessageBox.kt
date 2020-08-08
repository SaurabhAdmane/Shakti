package com.shakticoin.app.widget

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.shakticoin.app.R
import com.shakticoin.app.ShaktiApplication

class MessageBox(val message: String?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message?:ShaktiApplication.getContext().getString(R.string.err_unexpected))
                    .setPositiveButton(R.string.errbox__ok, null)
            builder.create()
        } ?: throw IllegalStateException()
    }
}