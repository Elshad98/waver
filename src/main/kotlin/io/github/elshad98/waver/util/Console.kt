// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.util

private const val ANSI_RESET = "\u001B[0m"
private const val ANSI_RED = "\u001B[31m"

fun printError(message: String) {
    System.err.println("${ANSI_RED}$message${ANSI_RESET}")
}

fun printInfo(message: String) {
    println(message)
}