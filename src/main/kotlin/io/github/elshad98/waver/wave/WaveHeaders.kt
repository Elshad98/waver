// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.wave

import io.github.elshad98.waver.extension.toByteArray
import io.github.elshad98.waver.extension.toInt
import io.github.elshad98.waver.extension.toShort
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.RandomAccessFile

object WaveHeaders {

    private const val HEADER_LENGTH = 44

    private const val RIFF_HEADER = "RIFF"
    private const val WAVE_FORMAT = "WAVE"
    private const val FMT_CHUNK = "fmt "
    private const val DATA_CHUNK = "data"

    fun read(inputStream: InputStream): WaveHeader {
        val buffer = ByteArray(HEADER_LENGTH)
        val bytesRead = inputStream.read(buffer)
        if (bytesRead != HEADER_LENGTH) {
            throw IOException("Could not read header: expected $HEADER_LENGTH bytes, got $bytesRead.")
        }

        val chunkId = buffer.decodeToString(endIndex = 4)
        if (chunkId != RIFF_HEADER) {
            throw IOException("Invalid Chunk ID: expected $RIFF_HEADER, got $chunkId.")
        }

        val format = buffer.decodeToString(startIndex = 8, endIndex = 12)
        if (format != WAVE_FORMAT) {
            throw IOException("Invalid Format: expected $WAVE_FORMAT, got $format.")
        }

        val subchunk1Id = buffer.decodeToString(startIndex = 12, endIndex = 16)
        if (subchunk1Id != FMT_CHUNK) {
            throw IOException("Invalid Subchunk1 ID: expected $FMT_CHUNK, got $subchunk1Id.")
        }

        val subchunk2Id = buffer.decodeToString(startIndex = 36, endIndex = 40)
        if (subchunk2Id != DATA_CHUNK) {
            throw IOException("Invalid Subchunk2 ID: expected $DATA_CHUNK, got $subchunk2Id.")
        }

        return WaveHeader(
            chunkId = chunkId,
            chunkSize = buffer.toInt(offset = 4),
            format = format,
            subchunk1Id = subchunk1Id,
            subchunk1Size = buffer.toInt(offset = 16),
            audioFormat = buffer.toShort(offset = 20),
            numChannels = buffer.toShort(offset = 22),
            sampleRate = buffer.toInt(offset = 24),
            byteRate = buffer.toInt(offset = 28),
            blockAlign = buffer.toShort(offset = 32),
            bitsPerSample = buffer.toShort(offset = 34),
            subchunk2Id = subchunk2Id,
            subchunk2Size = buffer.toInt(offset = 40),
        )
    }

    fun write(
        outputStream: OutputStream,
        audioFormat: Short,
        numChannels: Short,
        sampleRate: Int,
        bitsPerSample: Short,
    ) {
        val subchunk1Size = if (audioFormat == WaveHeader.AUDIO_FORMAT_EXTENSIBLE) {
            WaveHeader.CHUNK_SIZE_EXTENSIBLE
        } else {
            WaveHeader.CHUNK_SIZE_PCM
        }
        val blockAlign = (numChannels * (bitsPerSample / 8)).toShort()
        val byteRate = sampleRate * blockAlign
        outputStream.apply {
            write(RIFF_HEADER.toByteArray()) // Chunk ID
            write(ByteArray(4)) // Chunk Size
            write(WAVE_FORMAT.toByteArray()) // Format
            write(FMT_CHUNK.toByteArray()) // Subchunk1 ID
            write(subchunk1Size.toByteArray()) // Subchunk1 Size
            write(audioFormat.toByteArray()) // Audio Format
            write(numChannels.toByteArray()) // Number of Channels
            write(sampleRate.toByteArray()) // Sample Rate
            write(byteRate.toByteArray()) // Byte Rate
            write(blockAlign.toByteArray()) // Block Align
            write(bitsPerSample.toByteArray()) // Bits per Sample
            write(DATA_CHUNK.toByteArray()) // Subchunk2 ID
            write(ByteArray(4)) // Subchunk2 Size
        }
    }

    fun update(file: File) {
        val length = file.length()
        if (length > UInt.MAX_VALUE.toLong()) {
            throw IOException("File is too large for standard WAV format (max 4GB). Needs RF64 support.")
        }

        val chunkSize = (length - 8).toInt().toByteArray()
        val subchunk2Size = (length - 44).toInt().toByteArray()

        RandomAccessFile(file, "rw").use {
            it.seek(4)
            it.write(chunkSize, 0, 4)
            it.seek(40)
            it.write(subchunk2Size, 0, 4)
        }
    }
}