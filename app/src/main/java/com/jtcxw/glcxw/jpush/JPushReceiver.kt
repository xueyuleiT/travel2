package com.jtcxw.glcxw.jpush

import android.app.Notification
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.jtcxw.glcxw.R
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.localbean.MessageBean
import com.jtcxw.glcxw.ui.MainActivity
import com.jtcxw.glcxw.utils.DaoUtilsStore
import org.json.JSONObject

class JPushReceiver: JPushMessageReceiver() {

    companion object{
        var notifyId = 0
    }
    override fun onNotifyMessageArrived(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageArrived(p0, p1)

    }

    override fun onInAppMessageArrived(p0: Context?, p1: NotificationMessage?) {
        super.onInAppMessageArrived(p0, p1)
        Log.d("onMessage",p1!!.inAppMsgTitle)
    }

    override fun onMessage(p0: Context?, p1: CustomMessage?) {

        super.onMessage(p0, p1)
        val messageEvent = MessageBean()
        messageEvent.time = System.currentTimeMillis().toString()
        messageEvent.phone = UserUtil.getUserInfoBean().realTelphoneNo
        messageEvent.title = p1!!.title
        messageEvent.detail = p1!!.message
        try {
            val json = JSONObject(p1!!.extra)
            messageEvent.PushType = json.optString("PushType")
            messageEvent.MessageType = json.optString("MessageType")
            messageEvent.BusinessId = json.optString("BusinessId")
        }catch (e: Exception) {

        }
        if (BaseUtil.sTopAct != null && BaseUtil.sTopAct is MainActivity) {
            (BaseUtil.sTopAct as MainActivity).showNotifycation(messageEvent)
        }

        DaoUtilsStore.getInstance().userDaoUtils.insert(messageEvent)
//


        val manager =
            p0!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = (System.currentTimeMillis() / 1000).toInt()
        //判断8.0，若为8.0型号的手机进行创下一下的通知栏
        var pendingIntent: PendingIntent?
        val intent = Intent(p0, MainActivity::class.java)
        intent.putExtra("type", "message")
        intent.putExtra("pushType", messageEvent.pushType)
        intent.putExtra("messageType", messageEvent.messageType)
        intent.putExtra("businessId", messageEvent.businessId)
        pendingIntent = PendingIntent.getActivity(
            p0,
            notifyId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        notifyId++

        if (Build.VERSION.SDK_INT >= 26) {  //判断8.0，若为8.0型号的手机进行创下一下的通知栏
            val channel =
                NotificationChannel(
                    "消息通知",
                    "消息通知",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.importance = NotificationManager.IMPORTANCE_DEFAULT
            manager?.createNotificationChannel(channel)
            val builder = Notification.Builder(p0, id.toString())
            builder.setSmallIcon(R.mipmap.icon_launch)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        p0.resources,
                        R.mipmap.icon_launch
                    )
                )
                .setContentTitle(p1!!.title)
                .setContentText(p1.message)
                .setAutoCancel(true)
                .setChannelId("消息通知")
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
            //                    .setDeleteIntent(pendingIntentCancel);
            manager.notify(id, builder.build())
        } else {
            val builder = Notification.Builder(p0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.mipmap.icon_launch)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            p0.resources,
                            R.mipmap.icon_launch
                        )
                    )
                    .setContentTitle(p1!!.title)
                    .setContentText(p1.message)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setVisibility(VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
            } else {
                builder.setSmallIcon(R.mipmap.icon_launch)
                    .setWhen(System.currentTimeMillis())
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            p0.resources,
                            R.mipmap.icon_launch
                        )
                    )
                    .setContentTitle(p1!!.title)
                    .setContentText(p1.message)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
            }
            //                    .setDeleteIntent(pendingIntentCancel);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                manager.notify(id, builder.build())
            }
        }
    }


    override fun onNotifyMessageOpened(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageOpened(p0, p1)
//        val intent = Intent(p0, MainActivity::class.java)
//        p0!!.startActivity(intent)
    }
}