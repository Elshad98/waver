// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.command

import picocli.CommandLine.Command

@Command(
    name = "generate",
    description = ["Generates audio files."],
    mixinStandardHelpOptions = true,
    subcommands = [
        SilenceCommand::class,
    ],
)
class GenerateCommand