package com.tbacademy.nextstep.presentation.extension

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText

fun EditText.getString(): String {
    return this.text.toString().trim()
}

fun EditText.onTextChanged(action: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            action(s.toString())
        }
    })
}

fun EditText.togglePasswordVisibility(isPasswordVisible: Boolean) {
    this.inputType = if (isPasswordVisible) {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    this.setSelection(this.text?.length ?: 0)
}

