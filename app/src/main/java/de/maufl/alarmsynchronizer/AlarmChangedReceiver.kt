package de.maufl.alarmsynchronizer

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log

/**
 * Created by maufl on 1/5/18.
 */
class AlarmChangedReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("AlarmChangedReceiver", "Received intent about changed alarm")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmClockInfo = alarmManager.nextAlarmClock

        val sharedPres = PreferenceManager.getDefaultSharedPreferences(context)
        val url = sharedPres.getString("url", null)
        val deviceName = sharedPres.getString("device_name", null)
        val userName = sharedPres.getString("user", null)
        val password = sharedPres.getString("password", null)
        if (url == null || deviceName == null || userName == null || password == null) {
            return
        }
        PostAlarmTask(alarmClockInfo?.triggerTime, url, deviceName, userName, password).execute()
    }
}