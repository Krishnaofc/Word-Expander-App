package com.wordboard.app.service

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.wordboard.app.R

/**
 * Manages the interactive floating pop-up pill displayed right above the keyboard.
 * Allows 1-tap text expansion for shortcuts and spell corrections.
 */
class OverlayPopupManager(private val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var popupView: View? = null
    private val handler = Handler(Looper.getMainLooper())
    private var dismissRunnable: Runnable? = null

    /**
     * Shows a floating pill above the keyboard.
     * @param shortcut The trigger word or typo detected.
     * @param previewText The expanded text or spelling correction.
     * @param isCorrection True if this was a typo correction, False if a user shortcut.
     * @param onExpandClick Callback invoked when user taps the expand button.
     */
    fun showExpansionPopup(
        shortcut: String,
        previewText: String,
        isCorrection: Boolean = false,
        onExpandClick: () -> Unit
    ) {
        // Overlay permission check
        if (!Settings.canDrawOverlays(context)) {
            return
        }

        handler.post {
            // Remove any existing popup first
            dismissPopup()

            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    @Suppress("DEPRECATION")
                    WindowManager.LayoutParams.TYPE_PHONE
                },
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                y = 520 // Positioned comfortably right above standard keyboards (Gboard/Samsung)
            }

            // Inflate custom popup view dynamically
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.view_keyboard_popup_pill, null)

            val tvShortcut = view.findViewById<TextView>(R.id.tvPopupShortcut)
            val tvPreview = view.findViewById<TextView>(R.id.tvPopupPreview)
            val btnExpand = view.findViewById<Button>(R.id.btnPopupExpand)
            val btnDismiss = view.findViewById<ImageButton>(R.id.btnPopupDismiss)

            tvShortcut.text = if (isCorrection) "Fix: $shortcut" else shortcut
            tvPreview.text = previewText

            btnExpand.setOnClickListener {
                onExpandClick()
                dismissPopup()
            }

            btnDismiss.setOnClickListener {
                dismissPopup()
            }

            try {
                windowManager.addView(view, layoutParams)
                popupView = view

                // Auto-dismiss after 6 seconds of inactivity
                dismissRunnable = Runnable { dismissPopup() }
                handler.postDelayed(dismissRunnable!!, 6000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Dismisses the active floating popup.
     */
    fun dismissPopup() {
        handler.removeCallbacksAndMessages(null)
        popupView?.let { view ->
            try {
                if (view.isAttachedToWindow) {
                    windowManager.removeView(view)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            popupView = null
        }
    }

    fun isShowing(): Boolean = popupView != null
}
