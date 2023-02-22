package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminderList: MutableList<ReminderDTO> = mutableListOf()) :
    ReminderDataSource {
    private var returnError = false
    fun setReturnError(returnErr: Boolean) {
        returnError = returnErr
    }

    //    TODO: Create a fake data source to act as a double to the real data source
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (returnError) {
            return Result.Error("Error getting reminders")
        } else {
            return Result.Success(ArrayList(reminderList))
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminderList.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (returnError) {
            return Result.Error("Error getting reminders")
        } else {
            val reminder = reminderList.find {
                it.id == id
            }
            return if (reminder != null) {
                Result.Success(reminder)

            } else {

                Result.Error("reminders were unable to get retrieved")
            }
        }

    }

    override suspend fun deleteAllReminders() {
        reminderList.clear()
    }

    override suspend fun delete(id: String) {
        reminderList.remove(reminderList.find {
            it.id == id
        })

    }


}