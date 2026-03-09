package com.kindaboii.journal.features.entries.impl.export

import java.awt.Desktop
import java.nio.file.Files

actual fun printHtml(html: String) {
    val file = Files.createTempFile("journal_export", ".html").toFile()
    file.deleteOnExit()
    file.writeText(html, Charsets.UTF_8)
    Desktop.getDesktop().browse(file.toURI())
}
