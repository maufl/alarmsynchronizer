package de.maufl.alarmsynchronizer

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.net.Authenticator
import java.net.PasswordAuthentication
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by maufl on 1/5/18.
 */

class PostAlarmTask(val triggerTimer: Long?, val url: String, val deviceName: String, val userName: String, val password: String)
    : AsyncTask<Void, Void, Boolean>() {
    override fun doInBackground(vararg u: Void): Boolean {
        val request = URL(url).openConnection() as HttpsURLConnection
        try {
            Authenticator.setDefault(object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(userName, password.toCharArray())
                }
            })
            val payload = hashMapOf("device_name" to deviceName, "trigger_time" to triggerTimer?.toString())
            val body = JSONObject(payload).toString().toByteArray()
            request.doOutput = true
            request.setFixedLengthStreamingMode(body.size)
            request.outputStream.write(body)

            if (request.url.toString() != url) {
                // Uh oh, we've been redirected
            }
            if (request.responseCode >= 400) {
                // Unsuccessful
            }
        } catch (e: Throwable) {
            Log.w("PostAlarmTask","Caught exception ${e.message}")
        } finally {
            request.disconnect()
        }
        return true
    }
}
