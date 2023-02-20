package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    private val item1 =
        ReminderDTO("My home", "my place", "Alexandria", 1.454202, 2.599545, "1")
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: RemindersDatabase

    //    TODO: Add testing implementation to the RemindersDao.kt
    @Before
    fun iniDataBase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

    }
    @After
    fun closeDB()=database.close()


    @Test
    fun reminders_getReminders()=runBlockingTest{
        database.reminderDao().saveReminder(item1)
        val loadedReminder=database.reminderDao().getReminderById( item1.id)

        Assert.assertThat<ReminderDTO>(loadedReminder as ReminderDTO, notNullValue())
        Assert.assertThat(loadedReminder.id, `is`(item1.id))
        Assert.assertThat(loadedReminder.title, `is`(item1.title))
        Assert.assertThat(loadedReminder.description, `is`(item1.description))
    }
    @Test
    fun reminders_insertReminders()=runBlockingTest{
        database.reminderDao().saveReminder(item1)
        val loadedReminder=database.reminderDao().getReminderById( item1.id)

        Assert.assertThat<ReminderDTO>(loadedReminder as ReminderDTO, notNullValue())
        Assert.assertThat(loadedReminder.id, `is`(item1.id))
        Assert.assertThat(loadedReminder.title, `is`(item1.title))
        Assert.assertThat(loadedReminder.description, `is`(item1.description))
    }
}