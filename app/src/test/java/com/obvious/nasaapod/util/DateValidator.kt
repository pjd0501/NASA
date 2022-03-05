package com.obvious.nasaapod.util

/**
 * Interface to validate the date format
 */
interface DateValidator {
    fun isValidFormat(date: String) : Boolean
}