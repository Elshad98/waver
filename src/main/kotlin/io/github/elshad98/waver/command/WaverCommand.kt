// Copyright (c) 2025 Elshad Safarov
//
// SPDX-License-Identifier: MIT

package io.github.elshad98.waver.command

import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Command(
    name = "waver",
    description = ["A CLI tool for analyzing and processing WAV files."],
    subcommands = [
        InfoCommand::class,
    ],
    mixinStandardHelpOptions = true,
)
class WaverCommand : Callable<Int> {

    override fun call(): Int {
        return 0
    }
}