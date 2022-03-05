package com.obvious.nasaapod.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.obvious.nasaapod.ext.getOrAwaitValue
import com.obvious.nasaapod.util.DateValidator
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.io.IOException
import java.lang.Exception

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HomePageUnitTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @OptIn(ObsoleteCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    lateinit var homeViewModel: HomeViewModel

    lateinit var data: String

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        homeViewModel = HomeViewModel(dispatcher = testDispatcher)
        data = readFileWithFromResources("data.json")
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun title_isPresent() {
        runBlocking {
            homeViewModel.getImages(data).getOrAwaitValue()?.forEach {
                assertNotNull(it.title)
                assertNotEquals(it.title, "")
            }
        }
    }

    @Test
    fun imageUrl_isPresent() {
        runBlocking {
            homeViewModel.getImages(data).getOrAwaitValue()?.forEach {
                assertNotNull(it.url)
                assertNotEquals(it.url, "")
            }
        }
    }

    @Test
    fun description_isPresent() {
        runBlocking {
            homeViewModel.getImages(data).getOrAwaitValue()?.forEach {
                assertNotNull(it.explanation)
                assertNotEquals(it.explanation, "")
            }
        }
    }

    @Test
    fun date_hasCorrectFormat() {
        runBlocking {
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val validator = DateFormatValidator(dateTimeFormatter)
            homeViewModel.getImages(data).getOrAwaitValue()?.forEach {
                assertNotNull(it.date)
                assertTrue(validator.isValidFormat(it.date!!))
            }
        }
    }

    @Throws(IOException::class)
    fun readFileWithFromResources(fileName: String): String {
        return javaClass.classLoader?.getResourceAsStream(fileName)?.bufferedReader()
            .use { bufferReader -> bufferReader?.readText() } ?: ""
    }

    inner class DateFormatValidator(private val dateTimeFormatter: DateTimeFormatter) : DateValidator {
        override fun isValidFormat(date: String): Boolean {
            try {
                LocalDate.parse(date, dateTimeFormatter)
            } catch (exception : Exception) {
                return false
            }
            return true
        }
    }
}