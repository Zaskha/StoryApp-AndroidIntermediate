package com.zaskha.storyapepe.helper

open class Event<out T> {

    private val t: T by lazy { t }

    fun ifNotControlled(): T? = if (controlled) null else {
        controlled = true
        t
    }

    companion object {
        @Suppress("MemberVisibilityCanBePrivate")
        var controlled = false
            private set
    }
}