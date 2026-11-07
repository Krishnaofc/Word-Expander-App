package com.wordboard.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.wordboard.app.R
import com.wordboard.app.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Floating Quick-Board Bubble Service.
 * Provides a floating overlay icon that users can position anywhere on their screen.
 * Tapping it gives rapid access to phrase templates without typing shortcuts.
 */
class FloatingBoardService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var windowManager: WindowManager
    private var floatingView: View? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        startForegroundServiceNotification()
        setupFloatingBubble()
    }

    private fun startForegroundServiceNotification() {
        val channelId = "wordboard_floating_service"
        val channelName = "WordBoard Floating Bubble"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("WordBoard Active")
            .setContentText("Tap floating bubble for quick text board")
            .setSmallIcon(android.R.drawable.ic_menu_edit)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(101, notification)
    }

    private fun setupFloatingBubble() {
        if (!Settings.canDrawOverlays(this)) return

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 20
            y = 300
        }

        val bubble = ImageView(this).apply {
            setImageResource(android.R.drawable.ic_input_get)
            setBackgroundResource(R.drawable.bg_button_expand)
            setPadding(24, 24, 24, 24)
            elevation = 12f
        }

        // Draggable touch listener
        bubble.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            private var isDrag = false

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isDrag = false
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = (event.rawX - initialTouchX).toInt()
                        val dy = (event.rawY - initialTouchY).toInt()
                        if (kotlin.math.abs(dx) > 10 || kotlin.math.abs(dy) > 10) {
                            isDrag = true
                            params.x = initialX + dx
                            params.y = initialY + dy
                            windowManager.updateViewLayout(bubble, params)
                        }
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!isDrag) {
                            onBubbleClicked()
                        }
                        return true
                    }
                }
                return false
            }
        })

        try {
            windowManager.addView(bubble, params)
            floatingView = bubble
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onBubbleClicked() {
        serviceScope.launch {
            val db = AppDatabase.getDatabase(this@FloatingBoardService)
            val topSnippets = db.snippetDao().getAllSnippetsList().take(5)
            withContext(Dispatchers.Main) {
                if (topSnippets.isNotEmpty()) {
                    val first = topSnippets.first()
                    copyText(first.expandedText)
                    Toast.makeText(
                        this@FloatingBoardService,
                        "Copied: ${first.label.ifBlank { first.shortcut }}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this@FloatingBoardService, "WordBoard ready", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun copyText(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("WordBoard", text)
        clipboard.setPrimaryClip(clip)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        floatingView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
