package com.jtcxw.glcxw.jpush

import android.content.Context
import android.util.Log
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.glcxw.lib.util.CacheUtil
import com.jtcxw.glcxw.base.utils.BaseUtil
import com.jtcxw.glcxw.base.utils.ToastUtil
import com.jtcxw.glcxw.base.utils.UserUtil
import com.jtcxw.glcxw.localbean.MessageBean
import com.jtcxw.glcxw.ui.MainActivity
import com.jtcxw.glcxw.utils.DaoUtilsStore
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.text.SimpleDateFormat
import java.util.*

class JPushReceiver: JPushMessageReceiver() {

    override fun onNotifyMessageArrived(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageArrived(p0, p1)


//        val messageEvent = MessageBean()
//        messageEvent.phone = UserUtil.getUserInfoBean().realTelphoneNo
//        messageEvent.title = p1!!.notificationTitle
//        messageEvent.detail = p1!!.notificationContent
//        messageEvent.type = p1!!.notificationType.toString()
//        messageEvent.time = "1分钟"
//        if (BaseUtil.sTopAct != null && BaseUtil.sTopAct is MainActivity) {
//            (BaseUtil.sTopAct as MainActivity).showNotifycation(messageEvent)
//        }
//
//        DaoUtilsStore.getInstance().userDaoUtils.insert(messageEvent)
//        val manager = p0!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val id = (System.currentTimeMillis() / 1000).toInt()
//        //判断8.0，若为8.0型号的手机进行创下一下的通知栏
//        var pendingIntent: PendingIntent?
//        val intent = Intent(p0, MainActivity::class.java)
//        pendingIntent = PendingIntent.getActivity(p0, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        notifyId ++
//
//        if (Build.VERSION.SDK_INT >= 26) {  //判断8.0，若为8.0型号的手机进行创下一下的通知栏
//            val channel = NotificationChannel(id.toString(), "channel_name", NotificationManager.IMPORTANCE_HIGH)
//            manager?.createNotificationChannel(channel)
//            val builder = Notification.Builder(p0, id.toString())
//            builder.setSmallIcon(R.mipmap.icon_launch)
//                .setWhen(System.currentTimeMillis())
//                .setLargeIcon(BitmapFactory.decodeResource(p0.resources, R.mipmap.icon_launch))
//                .setContentTitle(p1!!.notificationTitle)
//                .setContentText(p1.notificationContent)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
////                    .setDeleteIntent(pendingIntentCancel);
//            manager.notify(id, builder.build())
//        } else {
//            val builder = Notification.Builder(p0)
//            builder.setSmallIcon(R.mipmap.icon_launch)
//                .setWhen(System.currentTimeMillis())
//                .setLargeIcon(BitmapFactory.decodeResource(p0.resources, R.mipmap.icon_launch))
//                .setContentTitle(p1!!.notificationTitle)
//                .setContentText(p1.notificationContent)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
////                    .setDeleteIntent(pendingIntentCancel);;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                manager.notify(id, builder.build())
//            }
//        }

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
    }

}