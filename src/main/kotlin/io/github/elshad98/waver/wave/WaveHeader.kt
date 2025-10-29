// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.wave

data class WaveHeader(
    val chunkId: String,
    val chunkSize: Int,
    val format: String,
    val subchunk1Id: String,
    val subchunk1Size: Int,
    val audioFormat: Short,
    val numChannels: Short,
    val sampleRate: Int,
    val byteRate: Int,
    val blockAlign: Short,
    val bitsPerSample: Short,
    val subchunk2Id: String,
    val subchunk2Size: Int,
) {

    companion object {

        const val AUDIO_FORMAT_PCM: Short = 1
        const val AUDIO_FORMAT_IEEE_FLOAT: Short = 3
        const val AUDIO_FORMAT_ALAW: Short = 6
        const val AUDIO_FORMAT_ULAW: Short = 7
        const val AUDIO_FORMAT_EXTENSIBLE: Short = -2 // 65534

        const val CHUNK_SIZE_PCM = 16
        const val CHUNK_SIZE_EXTENSIBLE = 40
    }
}