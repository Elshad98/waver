// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.formatter

import io.github.elshad98.waver.wave.WaveHeader

object WaveHeaderFormatter {

    fun format(waveHeader: WaveHeader): String {
        return buildString {
            appendLine("Chunk ID          : ${waveHeader.chunkId}")
            appendLine("Chunk Size        : ${waveHeader.chunkSize}")
            appendLine("Format            : ${waveHeader.format}")
            appendLine("Subchunk1 ID      : ${waveHeader.subchunk1Id}")
            appendLine("Subchunk1 Size    : ${waveHeader.subchunk1Size}")
            appendLine("Audio Format      : ${audioFormatToString(waveHeader.audioFormat)}")
            appendLine("Number of Channels: ${waveHeader.numChannels}")
            appendLine("Sample Rate       : ${waveHeader.sampleRate} Hz")
            appendLine("Byte Rate         : ${waveHeader.byteRate} B/s")
            appendLine("Block Align       : ${waveHeader.blockAlign} bytes")
            appendLine("Bits per Sample   : ${waveHeader.bitsPerSample}")
            appendLine("Subchunk2 ID      : ${waveHeader.subchunk2Id}")
            appendLine("Subchunk2 Size    : ${waveHeader.subchunk2Size}")
        }
    }

    private fun audioFormatToString(audioFormat: Short): String {
        return when (audioFormat) {
            WaveHeader.AUDIO_FORMAT_PCM -> "PCM"
            WaveHeader.AUDIO_FORMAT_ALAW -> "A-law"
            WaveHeader.AUDIO_FORMAT_ULAW -> "Mu-law"
            WaveHeader.AUDIO_FORMAT_IEEE_FLOAT -> "IEEE Float"
            WaveHeader.AUDIO_FORMAT_EXTENSIBLE -> "Extensible"
            else -> "Unknown"
        } + " (${audioFormat.toUShort()})"
    }
}