package xyz.tberghuis.floatingtimer.tmp

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.view.WindowManager
import androidx.compose.ui.unit.IntOffset
import androidx.core.app.NotificationCompat
import com.torrydo.screenez.ScreenEz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import xyz.tberghuis.floatingtimer.NOTIFICATION_CHANNEL
import xyz.tberghuis.floatingtimer.R
import xyz.tberghuis.floatingtimer.logd
import kotlin.math.max
import kotlin.math.min

class MaccasService : Service() {
  private val job = SupervisorJob()
  val scope = CoroutineScope(Dispatchers.Default + job)

  private var isStarted = false

  lateinit var maccasOverlayController: MaccasOverlayController

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()

    logd("MaccasService oncreate")
    ScreenEz.with(this.applicationContext)


    maccasOverlayController = MaccasOverlayController(this)


  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    postOngoingActivityNotification()
    return START_STICKY
  }

  private fun postOngoingActivityNotification() {
    if (!isStarted) {
      isStarted = true

      startForeground(MC.FOREGROUND_SERVICE_NOTIFICATION_ID, buildNotification())
    }
  }

  private fun buildNotification(): Notification {
//    val pendingIntent: PendingIntent =
//      Intent(this, MainActivity::class.java).let { notificationIntent ->
//        PendingIntent.getActivity(this, 0, notificationIntent, FLAG_IMMUTABLE)
//      }


    val notification: Notification =
      NotificationCompat.Builder(this, NOTIFICATION_CHANNEL).setContentTitle("Floating Timer")
        .setSmallIcon(R.drawable.ic_alarm)
//        .setContentIntent(pendingIntent)
        .build()
    return notification
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    ScreenEz.refresh()
    maccasOverlayController.updateBubbleParamsWithinScreenBounds()
  }
}
