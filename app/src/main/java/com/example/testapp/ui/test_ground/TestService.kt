package com.example.testapp.ui.test_ground

import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.testapp.MainActivity
import com.example.testapp.R
import com.example.testapp.RxTest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TestService : Service() {

    companion object{
        val TAG = "TEST_SERVICE"
        val COMMAND_KEY = "command"
    }
    enum class Command(val id: Int) {
        UNKNOWN(-1),
        STOP(0),
        PAUSE(1),
        CONTINUE(2)
    }

    private val rx = RxTest()
    private var notifNum = 0
    private var lastOnNext = 0
    private var notifMessage = ""
        get() {
        return "test message $notifNum, last result $lastOnNext"
    }
    private lateinit var disposeRx: Disposable

    override fun onCreate() {
        super.onCreate()
        //init (call once)
        Log.d(TAG, "onCreate")

        createNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do something
        Log.d(TAG, "onStartCommand |${intent?.getIntExtra(COMMAND_KEY, Command.UNKNOWN.id) ?: "null"}|")
        when(intent?.getIntExtra(COMMAND_KEY, Command.UNKNOWN.id)) {
            Command.STOP.id -> {
                disposeRx.dispose()
                stopSelf()
                return START_STICKY
            }
            Command.PAUSE.id -> {
                disposeRx.dispose()
                return START_STICKY
            }
            Command.CONTINUE.id -> {}
            Command.UNKNOWN.id -> {
                return START_STICKY
            }
        }

        disposeRx = rx.rxTimerRoll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "RXT - onNext $it")
                    notifNum++
                    lastOnNext = it
                    createNotification()
                },
                {
                    Log.d(TAG, "RXT - onError $it")
                },
                {
                    Log.d(TAG, "RXT - onComplete")
                }
            )
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        //clear resources (call once)
        Log.d(TAG, "onDestroy")
        disposeRx.dispose()
    }

    override fun startService(service: Intent?): ComponentName? {
        Log.d(TAG, "startService")

        return super.startService(service)
    }

    override fun stopService(name: Intent?): Boolean {
        Log.d(TAG, "stopService")

        return super.stopService(name)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind")

        return null
    }

    private fun createNotification() {
        val intent = Intent(this, TestService::class.java)
        intent.putExtra(COMMAND_KEY, Command.PAUSE.id)
        val intent2 = Intent(this, TestService::class.java)
        intent2.putExtra(COMMAND_KEY, Command.CONTINUE.id)
        val intent3 = Intent(this, TestService::class.java)
        intent3.putExtra(COMMAND_KEY, Command.STOP.id)
        val pIntent1 = PendingIntent.getActivity(this, 1, Intent(this, MainActivity::class.java), 0)
        val pIntent2 = PendingIntent.getService(this, 2, intent, 0)
        val pIntent3 = PendingIntent.getService(this, 3, intent2, 0)
        val pIntent4 = PendingIntent.getService(this, 4, intent3, 0)

        val notification = NotificationCompat.Builder(this, "chanel_id")
            .setContentTitle("Title")
            .setContentText(notifMessage)
            .setSmallIcon(R.drawable.ic_test)
            .setContentIntent(pIntent1)
            .setOngoing(true)
            .addAction(R.drawable.ic_test, "pause", pIntent2)
            .addAction(R.drawable.ic_test, "continue", pIntent3)
            .addAction(R.drawable.ic_test, "stop", pIntent4)
            .build()

        startForeground(1, notification)
    }
}
