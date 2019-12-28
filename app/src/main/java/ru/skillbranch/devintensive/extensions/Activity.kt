package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Px
import kotlin.math.roundToInt

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}

fun Activity.isKeyboardOpen(): Boolean {
    val activityRootView: View = findViewById<View>(android.R.id.content)
    val heightDiff: Int
    val r = Rect()
    activityRootView.getWindowVisibleDisplayFrame(r)
    heightDiff = activityRootView.height - r.height()
    val marginOfError = this.convertDpToPx(50F).roundToInt()
    return heightDiff > marginOfError
}

fun Activity.isKeyboardClosed(): Boolean {
    val activityRootView: View = findViewById<View>(android.R.id.content)
    val heightDiff: Int
    val r = Rect()
    activityRootView.getWindowVisibleDisplayFrame(r)
    heightDiff = activityRootView.height - r.height()
    val marginOfError = this.convertDpToPx(50F).roundToInt()
    return heightDiff < marginOfError
}

fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}

fun Context.pxToDp (px: Float): Int {
    return (px/this.resources.displayMetrics.density).toInt()
}