package com.ambe.adttest.helper

/**
 *  Created by AMBE on 16/8/2019 at 13:38 PM.
 */
open class Event<out T>(private val content: T) {
    private var hasBeenHandled = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}