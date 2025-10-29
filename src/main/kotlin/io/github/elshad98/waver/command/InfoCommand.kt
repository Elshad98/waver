// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.command

import io.github.elshad98.waver.formatter.WaveHeaderFormatter
import io.github.elshad98.waver.util.printError
import io.github.elshad98.waver.util.printInfo
import io.github.elshad98.waver.wave.WaveHeaders
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.File
import java.io.IOException
import java.util.concurrent.Callable

@Command(
    name = "info",
    description = ["Reads the RIFF WAVE header of the specified WAV file and prints all header fields."],
)
class InfoCommand : Callable<Int> {

    @Parameters(
        index = "0",
        description = ["The WAV file to analyze."],
    )
    private lateinit var file: File

    override fun call(): Int {
        if (!file.exists()) {
            printError("File '${file.name}' does not exist.")
            return 1
        }

        if (file.isDirectory) {
            printError("'${file.name}' is a directory, not a WAV file.")
            return 1
        }

        return try {
            file.inputStream().use { inputStream ->
                val waveHeader = WaveHeaders.read(inputStream)
                printInfo(WaveHeaderFormatter.format(waveHeader))
            }
            0
        } catch (exc: IOException) {
            printError("Error reading WAV file: ${exc.message}.")
            1
        }
    }
}