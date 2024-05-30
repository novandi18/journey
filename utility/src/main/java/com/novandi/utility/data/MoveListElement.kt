package com.novandi.utility.data

fun <T> MutableList<T>.move(fromIndex: Int, toIndex: Int) {
    if (fromIndex !in indices || toIndex !in indices) {
        throw IndexOutOfBoundsException("Invalid indices")
    }
    val element = this.removeAt(fromIndex)
    this.add(toIndex, element)
}