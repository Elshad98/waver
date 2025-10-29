// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.extension

fun ShortArray.toByteArray(): ByteArray {
    val result = ByteArray(size * 2)
    for ((index, value) in withIndex()) {
        result[index * 2] = (value.toInt() shr 0).toByte()
        result[index * 2 + 1] = (value.toInt() shr 8).toByte()
    }
    return result
}