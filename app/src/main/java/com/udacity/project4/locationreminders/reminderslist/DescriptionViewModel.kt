package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.project4.locationreminders.data.ReminderDataSource
import kotlinx.coroutines.launch

class DescriptionViewModel(private val dataSource: ReminderDataSource, application: Application)
    : AndroidViewModel(application) {

    fun deleteReminder(reminderData: ReminderDataItem) {
        viewModelScope.launch {
            dataSource.delete(reminderData.id)
        }
    }
}