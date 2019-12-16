package ru.skillbranch.devintensive


import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardClosed
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView

    lateinit var benderObj: Bender

    private lateinit var listener: ViewTreeObserver.OnGlobalLayoutListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        val count = savedInstanceState?.getInt("COUNT") ?: 0
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question), count)

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)
        messageEt.setOnEditorActionListener { _, _, _ ->
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString())
            messageEt.setText("")
            val (red, green, blue) = color
            benderImage.setColorFilter(
                Color.rgb(red, green, blue),
                PorterDuff.Mode.MULTIPLY
            )
            textTxt.text = phrase
            hideKeyboard()
            true
        }

        listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            private var lastState: Boolean = isKeyboardOpen()
            override fun onGlobalLayout() {
                val isOpen = isKeyboardOpen()
                if (isOpen == lastState) {
                    return
                } else {
                    dispatchKeyboardEvent(isOpen)
                    lastState = isOpen
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send) {
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString())
            messageEt.setText("")
            val (r, g, b) = color
            benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = phrase
            hideKeyboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.name)
        outState?.putInt("COUNT", benderObj.count)
    }

    override fun onResume() {
        super.onResume()
        val view = findViewById<View>(android.R.id.content)
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    override fun onPause() {
        super.onPause()
        val view = findViewById<View>(android.R.id.content)
        view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    fun dispatchKeyboardEvent(isOpen: Boolean) {
        if (isOpen) Toast.makeText(this, "Keyboard is showing", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "keyboard closed", Toast.LENGTH_LONG).show()
    }
}