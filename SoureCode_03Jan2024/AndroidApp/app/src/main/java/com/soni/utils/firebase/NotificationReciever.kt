package com.soni.utils.firebase

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BADGE_ICON_LARGE
import com.soni.R
import com.soni.activity.HomeActivity
import com.soni.activity.SplashScreen
import com.soni.utils.Const
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

class NotificationReciever:BroadcastReceiver() {
    override fun onReceive(mcontext: Context?, p1: Intent?) {
        Log.e("NOTIFICATION", "onReceivebbbbbbbbbbbbbbbbbb")
        try {
            var title = p1?.extras?.getString("title", "") ?: ""
            var desc = p1?.extras?.getString("body", "") ?: ""
            if (title != null && desc != null &&
                !title.equals("")&&!title.equals("null") && !desc.equals("")&&!desc.equals("null")
            ) {
                Log.d("NOTIFICATION1",mcontext.toString())
                Log.d("NOTIFICATION1", title)
                Log.d("NOTIFICATION1",desc)
                try {
                    showNormalNotification(mcontext!!,title,desc)

                } catch (e: Exception) {
                    Log.e("NOTIFICATION",e.message.toString())
                }

            }else{
                showNormalNotification(mcontext!!,title,desc)

            }
        } catch (e: Exception) {
            Log.e("NOTIFICATION", "Exception:")
        }

    }
    fun showNormalNotification(context: Context,title:String ,desc:String) {
        val id:Int = (Date().getTime() / 1000L % Int.MAX_VALUE).toInt()
        val runningAppProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(runningAppProcessInfo)
        val appRunningBackground =
            runningAppProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND

        var intent = Intent(context, SplashScreen::class.java)
//                intent.putExtra(Const.BundleKey.FROM_FCM_NOTI, true)
//                intent.putExtra(Const.BundleKey.ID, addvertiseId)
        intent = if (appRunningBackground){
            Intent(context, SplashScreen::class.java)
        }else{
            Intent(context, HomeActivity::class.java)
        }
        intent.putExtra(Const.IntentKey.isFromNotification, true)
        intent.putExtra(Const.IntentKey.notificationId, id)
        val channel_id = "767"
        val channelName = "SoniApp"

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        var notification = NotificationCompat.Builder(context, channel_id)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(desc)
            .setBadgeIconType(BADGE_ICON_LARGE)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .build()


        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager



        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O
        ) {
            val notificationChannel = NotificationChannel(
                channel_id, channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }

//                val id = Random.nextInt(10000);
        Log.e("NOTIFICATION", "notify")
        notificationManager.notify(id, notification)

    }

    class GetImageFromUrl(
        var context: Context,
        var title: String?,
        var message: String?,
        var addvertiseId: String?
    ) :
        AsyncTask<String?, Void?, Bitmap>() {

        override fun doInBackground(vararg url: String?): Bitmap? {
            val stringUrl = url[0]
            var bitmap: Bitmap? = null
            val inputStream: InputStream
            try {
                inputStream = URL(stringUrl).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e:Exception) {
            }
            return bitmap
        }

        override fun onPostExecute(bitmap: Bitmap) {
            super.onPostExecute(bitmap)
            Log.e("NOTIFICATION", "addIddd: ")
            try {
                val intent = Intent(context, SplashScreen::class.java)
//                intent.putExtra(Const.BundleKey.FROM_FCM_NOTI, true)
//                intent.putExtra(Const.BundleKey.ID, addvertiseId)

                val channel_id = "767"
                val channelName = "SoniApp"

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                val pendingIntent = PendingIntent.getActivity(
                    context, System.currentTimeMillis().toInt(), intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                val runningAppProcessInfo = ActivityManager.RunningAppProcessInfo()
                ActivityManager.getMyMemoryState(runningAppProcessInfo)
                val appRunningBackground =
                    runningAppProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                var notification = NotificationCompat.Builder(context, channel_id)
                    .setSmallIcon(R.drawable.add_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setLargeIcon(bitmap)
                    .setAutoCancel(true)
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)

                    )
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setContentIntent(pendingIntent)
                    .build()


                val notificationManager = context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager



                if (Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.O
                ) {
                    val notificationChannel = NotificationChannel(
                        channel_id, channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.RED
                    notificationChannel.enableVibration(true)
                    notificationChannel.vibrationPattern =
                        longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                    notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    notificationManager.createNotificationChannel(
                        notificationChannel
                    )
                }
                val id:Int = (Date().getTime() / 1000L % Int.MAX_VALUE).toInt()
//                val id = Random.nextInt(10000);
                Log.e("NOTIFICATION", "notify")
                notificationManager.notify(id, notification)

            }catch (e:Exception){
                Log.e("NOTIFICATION", e.message.toString())
            }

        }
    }
}