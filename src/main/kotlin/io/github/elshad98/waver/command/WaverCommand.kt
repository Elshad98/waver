// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.command

import picocli.CommandLine.Command

@Command(
    name = "waver",
    description = ["A CLI tool for analyzing and processing WAV files."],
    subcommands = [
        InfoCommand::class,
        GenerateCommand::class,
    ],
    mixinStandardHelpOptions = true,
)
class WaverCommand