package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.fragment_product.*

fun <T> buildResourceResponse(): Triple<MutableLiveData<T>, MutableLiveData<StateView>, MutableLiveData<Throwable>> =
    Triple(MutableLiveData(), MutableLiveData(), MutableLiveData())


suspend fun execute(block: suspend () -> Unit): Throwable? {
    var error: Throwable? = null
    try {
        block()
    } catch (t: Throwable) {
        error = t
    }
    return error
}

fun showMessage(
    context: Context,
    title: String?,
    message: String?,
    textNegativeButton: String = "",
    onClickNegativeButton: () -> Unit = {},
    textPositiveButton: String = "OK",
    onClickPositiveButton: () -> Unit = {}
) {
    val alertDialog = AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)

    if(textNegativeButton.isNotEmpty()) {
        alertDialog.setNegativeButton(textNegativeButton) { dialog, _ ->
            onClickNegativeButton()
            dialog.dismiss()
        }
    }

    alertDialog.setPositiveButton(textPositiveButton) { dialog, _ ->
            onClickPositiveButton()
            dialog.dismiss()
        }

    alertDialog.show()
}
