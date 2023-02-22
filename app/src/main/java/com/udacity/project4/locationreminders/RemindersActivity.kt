package com.udacity.project4.locationreminders

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.NavHostFragment
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityRemindersBinding

/**
 * The RemindersActivity that holds the reminders fragments
 */
class RemindersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemindersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding =
//            DataBindingUtil.setContentView(this,R.layout.activity_reminders)
        binding = ActivityRemindersBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_reminders)
        val view = binding.root
        setContentView(view)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                ( binding.navHostFragment.getFragment()  as NavHostFragment).navController.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
