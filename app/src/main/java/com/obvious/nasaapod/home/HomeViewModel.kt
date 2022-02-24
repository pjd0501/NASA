package com.obvious.nasaapod.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.obvious.nasaapod.home.data.ImageDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


class HomeViewModel(private val dispatcher: CoroutineDispatcher) :
    ViewModel() {

    fun getImages(inputData: String) =
        liveData(dispatcher + CoroutineExceptionHandler { coroutineContext, throwable -> throwable.printStackTrace() }) {

            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(List::class.java, ImageDto::class.java)
            val jsonAdapter: JsonAdapter<List<ImageDto>> = moshi.adapter(type)

            val jsonData = jsonAdapter.fromJson(inputData)

            // sort the data by latest date.
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val sortedImagesList = jsonData?.sortedByDescending {
                LocalDate.parse(it.date, dateTimeFormatter)
            }
            emit(sortedImagesList)
        }
}

class HomeViewModelFactory(val dispatcher: CoroutineDispatcher) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dispatcher) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}