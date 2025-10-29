// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.extension

fun Int.toByteArray(): ByteArray {
    return byteArrayOf(
        (this shr 0).toByte(),
        (this shr 8).toByte(),
        (this shr 16).toByte(),
        (this shr 24).toByte(),
    )
}