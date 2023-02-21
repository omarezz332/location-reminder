package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.get
import org.mockito.Mockito.mock
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.mockito.Mockito.verify
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : AutoCloseKoinTest() {

//    TODO: test the navigation of the fragments.


private lateinit var localRepo: RemindersLocalRepository
    private lateinit var repository: ReminderDataSource
    private val dataSource: ReminderDataSource by inject()
    private lateinit var appContext: Application
    private val item1 = ReminderDTO("Reminder1", "Description1", "Location1", 1.0, 1.0,"1")
    private val item2 = ReminderDTO("Reminder2", "Description2", "location2", 2.0, 2.0, "2")
    // use Architecture Components to concurrently carry out each job.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

@Before
fun init() {
    stopKoin()//stop the original app koin
    appContext = getApplicationContext()
    val myModule = module {
        viewModel {
            RemindersListViewModel(
                appContext,
                get() as ReminderDataSource
            )
        }
        single {
            SaveReminderViewModel(
                appContext,
                get() as ReminderDataSource
            )
        }
        single { RemindersLocalRepository(get()) as ReminderDataSource }
        single { LocalDB.createRemindersDao(appContext) }
    }
    //declare a new koin module
    startKoin {
        modules(listOf(myModule))
    }
    //Get our real repository
    repository = get()

    //clear the data to start fresh
    runBlocking {
        repository.deleteAllReminders()
    }
}

    @After
    fun clean()= runTest{
        dataSource.deleteAllReminders()
    }
// test the displayed data on the UI.

    @Test
    fun reminderListAndDisplayedInUi() = runTest {
        // ADD ACTIVE (INCOMPLETE) TASK TO DB FOR GIVEN
        dataSource.saveReminder(item1)
        dataSource.saveReminder(item2)
        // WHEN - Start Fragment
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment { Navigation.setViewNavController(it.view!!, navController) }
        //THEN - We see 3 items(reminders) in the list, which we added above
        onView(withText(item1.title)).check(matches(isDisplayed()))
        onView(withText(item2.description)).check(matches(isDisplayed()))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))

        onView(withId(R.id.reminderssRecyclerView))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText(item1.title))
                )
            )
    }
    //     add testing for the error messages.
    @Test
    fun reminderList_noDataDisplayed() = runTest {
        // ADD ACTIVE (INCOMPLETE) TASK TO DB FOR GIVEN
        dataSource.saveReminder(item1)
        dataSource.saveReminder(item2)
        dataSource.deleteAllReminders()
        // WHEN - Start Fragment
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment { Navigation.setViewNavController(it.view!!, navController) }
        //THEN - We see No Data on the screen
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        onView(withText(item1.title)).check(doesNotExist())
    }

    @Test
    fun reminderList_ToReminderFragment() = runTest {
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment { Navigation.setViewNavController(it.view!!, navController) }
        //THEN - We see No Data on the screen
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withText(item1.title)).check(doesNotExist())
        verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }



}