// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver

import io.github.elshad98.waver.command.WaverCommand
import picocli.CommandLine
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val commandLine = CommandLine(WaverCommand())
    val exitCode = commandLine.execute(*args)
    exitProcess(exitCode)
}