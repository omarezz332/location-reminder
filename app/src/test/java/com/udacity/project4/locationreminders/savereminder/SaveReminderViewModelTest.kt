package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.ExpectFailure.assertThat
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import getOrAwaitValue
import com.udacity.project4.R


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.core.Is.`is`
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SaveReminderViewModelTest {
    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var reminderdata: FakeDataSource

    //TODO: provide testing to the SaveReminderView and its live data objects

    private val item1 =
        ReminderDataItem("My home", "my place", "Alexandria", 1.454202, 2.599545, "1")
    private val item2 = ReminderDataItem("", "nice place", "cairo", 1.454201, 2.599542, "2")
    private val item3 = ReminderDataItem("", "nice place", "", 1.454201, 2.599542, "2")

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        stopKoin()
    }

    @Before
    fun setupViewModel() {
        reminderdata = FakeDataSource()
        saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), reminderdata)

    }

    @Test
    fun ReminderLiveData_onClear() {
        saveReminderViewModel.reminderTitle.value = item1.title
        saveReminderViewModel.reminderId.value = item1.id
        saveReminderViewModel.reminderDescription.value = item1.description
        saveReminderViewModel.longitude.value = item1.longitude
        saveReminderViewModel.latitude.value = item1.latitude
        saveReminderViewModel.onClear()
        Assert.assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), `is`(nullValue()))
        Assert.assertThat(saveReminderViewModel.reminderId.getOrAwaitValue(), `is`(nullValue()))
        Assert.assertThat(
            saveReminderViewModel.reminderDescription.getOrAwaitValue(),
            `is`(nullValue())
        )
        Assert.assertThat(saveReminderViewModel.longitude.getOrAwaitValue(), `is`(nullValue()))
        Assert.assertThat(saveReminderViewModel.latitude.getOrAwaitValue(), `is`(nullValue()))

    }

    @Test
    fun ReminderLiveData_editReminder() {
        saveReminderViewModel.editReminder(item1)
        Assert.assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), `is`(item1.title))
        Assert.assertThat(saveReminderViewModel.reminderId.getOrAwaitValue(), `is`(item1.id))
        Assert.assertThat(
            saveReminderViewModel.reminderDescription.getOrAwaitValue(),
            `is`(item1.description)
        )
        Assert.assertThat(saveReminderViewModel.longitude.getOrAwaitValue(), `is`(item1.longitude))
        Assert.assertThat(saveReminderViewModel.latitude.getOrAwaitValue(), `is`(item1.latitude))
    }

    @Test
    fun ReminderLiveData_saveReminder() = mainCoroutineRule.runBlockingTest {
        saveReminderViewModel.saveReminder(item1)
        val reminder = reminderdata.getReminder("1") as Result.Success
        Assert.assertThat(reminder.data.title, `is`(item1.title))
        Assert.assertThat(reminder.data.id, `is`(item1.id))
        Assert.assertThat(reminder.data.description, `is`(item1.description))
        Assert.assertThat(reminder.data.latitude, `is`(item1.latitude))
        Assert.assertThat(reminder.data.longitude, `is`(item1.longitude))
    }

    @Test
    fun ReminderLiveData_validateAndSaveReminder() = mainCoroutineRule.runBlockingTest {
        val validate=saveReminderViewModel.validateEnteredData(item2)
        Assert.assertThat(validate, `is` (false))
    }
    @Test
    fun validateData_missingLocation_showSnackAndReturnFalse(){
        // Calling validateEnteredData and passing no location
        val valid = saveReminderViewModel.validateEnteredData(item3)
        // expect a SnackBar to be shown displaying err_select_location string and validate return false
        Assert.assertThat(valid, `is` (false))
    }
    @Test
    fun ReminderLiveData_checkLoading() = mainCoroutineRule.runBlockingTest {
        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.saveReminder(item1)
        Assert.assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()
        Assert.assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(false))


    }

}