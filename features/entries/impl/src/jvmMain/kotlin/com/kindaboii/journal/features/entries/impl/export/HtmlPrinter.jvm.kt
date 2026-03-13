package com.kindaboii.journal.features.entries.impl.export

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.FontStyle as PdfFontStyle
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import java.awt.Desktop
import java.awt.Font as AwtFont
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.SwingUtilities
import org.jsoup.Jsoup
import org.jsoup.helper.W3CDom

actual fun printHtml(html: String, fileName: String) {
    val doSave = Runnable {
        val chooser = JFileChooser().apply {
            dialogTitle = "Сохранить как PDF"
            fileFilter = FileNameExtensionFilter("PDF файл (*.pdf)", "pdf")
            selectedFile = File("$fileName.pdf")
        }
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return@Runnable

        val selected = chooser.selectedFile
        val file = if (selected.name.endsWith(".pdf")) selected else File(selected.absolutePath + ".pdf")

        // CSS overrides: system-ui → Arial (openhtmltopdf doesn't know system-ui),
        // linear-gradient is not supported — mood bar color is set via inline style in HTML
        val pdfHtml = html.replace(
            "</head>",
            "<style>" +
                "body,p,div,span,h1,h2,h3{font-family:Arial,Helvetica,sans-serif!important}" +
                "</style></head>",
        )

        val w3cDoc = W3CDom().fromJsoup(Jsoup.parse(transformMoodBarsForPdf(pdfHtml)))

        file.outputStream().use { os ->
            PdfRendererBuilder()
                .apply { registerSystemFonts() }
                .withW3cDocument(w3cDoc, null)
                .toStream(os)
                .run()
        }

        Desktop.getDesktop().open(file)
    }
    if (SwingUtilities.isEventDispatchThread()) doSave.run()
    else SwingUtilities.invokeLater(doSave)
}

// openhtmltopdf doesn't support linear-gradient; replace the mood bar with a
// table-based gradient (20 cells × 5%) so the PDF has a proper colour ramp.
private fun transformMoodBarsForPdf(html: String): String {
    val markerRe = Regex("""<div class="mood-bar-wrap"><div class="mood-marker" style="left:(\d+)%"></div></div>""")
    return html.replace(markerRe) { m ->
        val pct = m.groupValues[1].toInt()
        val cells = (0 until 20).joinToString("") { i ->
            """<td style="background:${moodColor(i * 5 + 2)};height:8px"></td>"""
        }
        // Wrapper is 14px tall (bar 8px + 3px overflow top/bottom for the marker).
        // Table sits at top:3px, marker is centered via margin-left:-7px.
        """<div style="position:relative;height:14px;margin-bottom:8px"><table style="position:absolute;top:3px;width:100%;height:8px;border-collapse:collapse" cellpadding="0" cellspacing="0"><tr>$cells</tr></table><div style="position:absolute;left:${pct}%;margin-left:-7px;top:0;width:14px;height:14px;border-radius:7px;background:#fff;border:2.5px solid #bbb"></div></div>"""
    }
}

private fun PdfRendererBuilder.registerSystemFonts() {
    val os = System.getProperty("os.name").lowercase()
    val dirs = when {
        "win" in os -> listOf(File("C:/Windows/Fonts"))
        "mac" in os -> listOf(
            File("/System/Library/Fonts"),
            File("/Library/Fonts"),
            File(System.getProperty("user.home") + "/Library/Fonts"),
        )
        else -> listOf(
            File("/usr/share/fonts/truetype"),
            File("/usr/local/share/fonts"),
        )
    }
    for (dir in dirs) {
        if (!dir.isDirectory) continue
        (dir.listFiles() ?: continue)
            .filter { it.isFile && it.extension.lowercase() in setOf("ttf", "otf") }
            .forEach { fontFile ->
                runCatching {
                    val awtFont = AwtFont.createFont(AwtFont.TRUETYPE_FONT, fontFile)
                    val family = awtFont.family
                    val fontName = awtFont.fontName.lowercase()
                    val weight = if ("bold" in fontName) 700 else 400
                    val style = if ("italic" in fontName || "oblique" in fontName) PdfFontStyle.ITALIC else PdfFontStyle.NORMAL
                    useFont(fontFile, family, weight, style, true)
                }
            }
    }
}
