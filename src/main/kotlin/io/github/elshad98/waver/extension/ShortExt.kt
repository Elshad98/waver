// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.extension

fun Short.toByteArray(): ByteArray {
    return byteArrayOf(
        (this.toInt() shr 0).toByte(),
        (this.toInt() shr 8).toByte(),
    )
}