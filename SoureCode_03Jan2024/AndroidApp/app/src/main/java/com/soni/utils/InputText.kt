package com.soni.utils
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.text.InputFilter
//import android.text.Spanned
//import android.util.AttributeSet
//import android.widget.EditText
//
//
//@SuppressLint("AppCompatCustomView")
//class CustomEditText : EditText {
//
//    constructor(context: Context?) : super(context!!) {
//        init()
//    }
//
//    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
//        init()
//    }
//
//    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
//        context!!,
//        attrs,
//        defStyleAttr
//    ) {
//        init()
//    }
//
//    private fun init() {
//        filters = arrayOf(EmojiExcludeFilter())
//    }
//
//    private inner class EmojiExcludeFilter : InputFilter {
//        override fun filter(
//            source: CharSequence,
//            start: Int,
//            end: Int,
//            dest: Spanned,
//            dstart: Int,
//            dend: Int
//        ): CharSequence? {
//            for (i in start until end) {
//                val type = Character.getType(source[i])
//                if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
//                    return ""
//                }
//            }
//            return null
//        }
//    }
//}