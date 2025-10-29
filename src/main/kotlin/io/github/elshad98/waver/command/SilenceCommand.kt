// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.command

import io.github.elshad98.waver.util.printError
import io.github.elshad98.waver.util.printInfo
import io.github.elshad98.waver.wave.WaveHeader
import io.github.elshad98.waver.wave.WaveHeaders
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import java.io.File
import java.io.IOException
import java.util.concurrent.Callable

@Command(
    name = "silence",
    description = ["Generates a WAV file containing silence."],
)
class SilenceCommand : Callable<Int> {

    companion object {

        private const val BUFFER_SIZE = 4096

        private const val DEFAULT_NUM_CHANNELS: Int = 1
        private const val DEFAULT_SAMPLE_RATE: Long = 8_000
        private const val DEFAULT_BITS_PER_SAMPLE: Short = 16

        private val VALID_CHANNEL_RANGE = 1..65_535
        private val VALID_SAMPLE_RATE_RANGE = 1..4_294_967_295

        private val SUPPORTED_BITS_PER_SAMPLE = shortArrayOf(8, 16, 24, 32, 64)
    }

    @Option(
        names = ["-sr", "--sample-rate"],
        description = [
            "Sample rate in Hz.",
            "Valid range: 1 – 4 294 967 295.",
        ],
    )
    private var sampleRate: Long = DEFAULT_SAMPLE_RATE

    @Option(
        names = ["-c", "--channels"],
        description = [
            "Number of audio channels.",
            "Valid range: 1 – 65 535.",
        ],
    )
    private var numChannels = DEFAULT_NUM_CHANNELS

    @Option(
        names = ["-bps", "--bits-per-sample"],
        description = [
            "Bits per sample.",
            "Supported values:",
            "8  = PCM 8-bit (unsigned integer)",
            "16 = PCM 16-bit (signed integer)",
            "24 = PCM 24-bit (signed integer)",
            "32 = PCM 32-bit (signed integer)",
            "64 = IEEE Float 64-bit",
        ],
    )
    private var bitsPerSample = DEFAULT_BITS_PER_SAMPLE

    @Option(
        required = true,
        names = ["-d", "--duration"],
        description = ["Duration in seconds (e.g 2.5)."],
    )
    private var duration = 0.0

    @Parameters(
        index = "0",
        description = ["The output file path."],
    )
    private lateinit var outputFile: File

    override fun call(): Int {
        if (sampleRate !in VALID_SAMPLE_RATE_RANGE) {
            printError("Invalid sample rate: $sampleRate.")
            return 1
        }
        if (bitsPerSample !in SUPPORTED_BITS_PER_SAMPLE) {
            printError("Invalid bits per sample: $bitsPerSample.")
            return 1
        }
        if (numChannels !in VALID_CHANNEL_RANGE) {
            printError("Invalid number of channels: $numChannels.")
            return 1
        }
        if (duration <= 0) {
            printError("Invalid duration: $duration.")
            return 1
        }

        return try {
            outputFile.createNewFile()
            outputFile.outputStream().use { outputStream ->
                val audioFormat = if (bitsPerSample < 64) WaveHeader.AUDIO_FORMAT_PCM else WaveHeader.AUDIO_FORMAT_IEEE_FLOAT
                WaveHeaders.write(outputStream, audioFormat, numChannels.toShort(), sampleRate.toInt(), bitsPerSample)
                val totalBytes = (sampleRate * numChannels * (bitsPerSample / 8) * duration).toLong()
                val silenceBytes = if (bitsPerSample == 8.toShort()) 128.toByte() else 0
                val buffer = ByteArray(BUFFER_SIZE) { silenceBytes }
                var bytesWritten = 0
                while (bytesWritten < totalBytes) {
                    val remaining = totalBytes - bytesWritten
                    val length = minOf(BUFFER_SIZE.toLong(), remaining).toInt()
                    outputStream.write(buffer, 0, length)
                    bytesWritten += length
                }
            }
            WaveHeaders.update(outputFile)
            printInfo("Created $outputFile ($sampleRate Hz, $bitsPerSample-bit, $numChannels ch, $duration s).")
            0
        } catch (exc: IOException) {
            printError("Failed to generate file: ${exc.message}.")
            if (outputFile.exists()) {
                outputFile.delete()
            }
            1
        }
    }
}