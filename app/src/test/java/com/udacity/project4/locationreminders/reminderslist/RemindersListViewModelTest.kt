package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.ExpectFailure.assertThat
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsEqual
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//@Config(sdk = [Build.VERSION_CODES.P])
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects
    private lateinit var remindersListViewModel: RemindersListViewModel
    private lateinit var remindersRepository: FakeDataSource
    val reminder = ReminderDTO("My hume", "moraa", "Alexandria", 1.454202, 2.599545)

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        remindersRepository = FakeDataSource()
        remindersListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), remindersRepository)

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun loadReminders_loading() {
        mainCoroutineRule.pauseDispatcher()
        remindersListViewModel.loadReminders()

        Assert.assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()

        Assert.assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun loadReminders_updateSnackBarValue() {
        mainCoroutineRule.pauseDispatcher()

        remindersRepository.setReturnError(true)

        remindersListViewModel.loadReminders()

        mainCoroutineRule.resumeDispatcher()

        Assert.assertThat(
            remindersListViewModel.showSnackBar.getOrAwaitValue(),
            `is`("Error getting reminders")
        )
    }

    @Test
    fun loadReminders_checkListNotEmpty() = mainCoroutineRule.runBlockingTest {
        remindersRepository.saveReminder(reminder)
        remindersListViewModel.loadReminders()
        Assert.assertThat(
            remindersListViewModel.remindersList.getOrAwaitValue().size,
            IsEqual(1)
        )
        Assert.assertThat(remindersListViewModel.showNoData.getOrAwaitValue(), `is`(false))

    }
   @Test
    fun loadReminders_deleteAllReminders_ShowNoDataIsTrue()=mainCoroutineRule.runBlockingTest{
       remindersRepository.saveReminder(reminder)
       remindersRepository.deleteAllReminders()
       remindersListViewModel.loadReminders()

       Assert.assertThat(
           remindersListViewModel.remindersList.getOrAwaitValue().size,
           IsEqual(0)
       )
       Assert.assertThat(remindersListViewModel.showNoData.getOrAwaitValue(), `is`(true))

    }

}