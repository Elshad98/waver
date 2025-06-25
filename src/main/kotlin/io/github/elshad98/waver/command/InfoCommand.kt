// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.command

import io.github.elshad98.waver.formatter.WaveHeaderFormatter
import io.github.elshad98.waver.util.printError
import io.github.elshad98.waver.util.printInfo
import io.github.elshad98.waver.wave.WaveHeader
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.File
import java.io.IOException
import java.util.concurrent.Callable

@Command(
    name = "info",
)
class InfoCommand : Callable<Int> {

    @Parameters(
        index = "0",
        description = ["The WAV file to analyze."],
    )
    private lateinit var file: File

    override fun call(): Int {
        if (!file.exists() || file.isDirectory) {
            printError("File '${file.name}' does not exist or is a directory")
            return 1
        }

        return try {
            file.inputStream().use { inputStream ->
                val waveHeader = WaveHeader.read(inputStream)
                printInfo(WaveHeaderFormatter.format(waveHeader))
            }
            0
        } catch (exc: IOException) {
            printError("Error reading WAV file: ${exc.message}")
            1
        }
    }
}