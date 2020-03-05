package com.example.testapp.ui.test_ground

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.testapp.R
import kotlinx.android.synthetic.main.fragment_start.progressBar
import kotlinx.android.synthetic.main.fragment_test_ground.*
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class TestGroundFragment : Fragment() {

    private val viewModel: TestGroundFragmentViewModel by inject()
    private val sc = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("TEST_FRAGMENT", "onServiceDisconnected")
            isBind = false
        }

        override fun onServiceConnected(name: ComponentName?, serviceBind: IBinder?) {
            val binder = serviceBind as TestService.TestBinder
            service = binder.getService()
            Log.d("TEST_FRAGMENT", "onServiceConnected")
            isBind = true
        }

    }
    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }

    private var isBind = false

    private lateinit var service: TestService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_ground, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val scope = Toothpick.openScope("APP")
        scope.installViewModelBinding<TestGroundFragmentViewModel>(this)
        scope.inject(this)

        viewModel.clearEvents()

        initOnClick()
    }

    private fun initOnClick() {
        button_1.setOnClickListener {
            val intent = Intent(activity, TestService::class.java)
            intent.putExtra(TestService.COMMAND_KEY, TestService.Command.CONTINUE.id)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity!!.startForegroundService(intent)
            } else {
                activity!!.startService(intent)
            }
        }
        button_2.setOnClickListener {
            val intent = Intent(activity, TestService::class.java)
            activity!!.stopService(intent)
        }
        button_3.setOnClickListener {
            if(test_progress_bar.visibility == View.VISIBLE)
                hideProgressBar()
            else
                showProgressBar()
            if (this::service.isInitialized) textView.text = service.getText() else "service not bind"
        }
        button_4.setOnClickListener {
            val intent = Intent(activity, TestService::class.java)
            activity!!.bindService(intent, sc, BIND_AUTO_CREATE)
        }
        button_5.setOnClickListener {
           if (isBind) {
               activity!!.unbindService(sc)
               isBind = false
               Log.d("TEST_FRAGMENT", "onClick unbind")
           }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun hideProgressBar() {
        test_progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        test_progress_bar.visibility = View.VISIBLE
    }
}
