// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.formatter

import io.github.elshad98.waver.wave.WaveHeader

object WaveHeaderFormatter {

    fun format(waveHeader: WaveHeader): String {
        return buildString {
            appendLine("Chunk ID       : ${waveHeader.chunkId}")
            appendLine("Chunk Size     : ${waveHeader.chunkSize}")
            appendLine("Format         : ${waveHeader.format}")
            appendLine("Subchunk1 ID   : ${waveHeader.subchunk1Id}")
            appendLine("Subchunk1 Size : ${waveHeader.subchunk1Size}")
            appendLine("Audio Format   : ${audioFormatToString(waveHeader.audioFormat.toInt())}")
            appendLine("Num Channels   : ${waveHeader.numChannels}")
            appendLine("Sample Rate    : ${waveHeader.sampleRate}")
            appendLine("Byte Rate      : ${waveHeader.byteRate}")
            appendLine("Block Align    : ${waveHeader.blockAlign}")
            appendLine("Bits Per Sample: ${waveHeader.bitsPerSample}")
            appendLine("Subchunk2 ID   : ${waveHeader.subchunk2Id}")
            appendLine("Subchunk2 Size : ${waveHeader.subchunk2Size}")
        }
    }

    private fun audioFormatToString(formatCode: Int): String {
        return when (formatCode) {
            1 -> "PCM"
            3 -> "IEEE Float"
            6 -> "A-LAW"
            7 -> "Mu-LAW"
            65534 -> "Extensible"
            else -> "Unknown"
        } + " ($formatCode)"
    }
}