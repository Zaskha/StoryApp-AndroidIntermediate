package com.zaskha.storyapepe.helper

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.zaskha.storyapepe.R

class MyEditTextRegister : AppCompatEditText {

    var type = ""

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (type == "password") {
                    if (s.length < 6) {
                        error = context.getString(R.string.invalid_password)
                    }
                } else if (type == "email") {
                    if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        error = context.getString(R.string.invalid_email)
                    }
                } else {
                    if (s.isEmpty()) {
                        error = context.getString(R.string.invalid_name)
                    }
                }


            }

            override fun afterTextChanged(s: Editable?) {
                // nothing
            }
        })
    }

}