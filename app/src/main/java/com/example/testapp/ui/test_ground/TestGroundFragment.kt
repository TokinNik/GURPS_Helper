package com.example.testapp.ui.test_ground

import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
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

    private val navController: NavController?
        get() = activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }


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
