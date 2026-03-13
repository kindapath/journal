package com.kindaboii.journal.features.entries.impl.export

import kotlinx.browser.window
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

actual fun printHtml(html: String, fileName: String) {
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    val options = js("({type: 'text/html; charset=utf-8'})") as BlobPropertyBag
    val titled = html.replace(Regex("<title>[^<]*</title>"), "<title>$fileName</title>")
    val blob = Blob(arrayOf(titled), options)
    val url = URL.createObjectURL(blob)
    val win = window.open(url, "_blank")
    win?.onload = {
        URL.revokeObjectURL(url)
        win.print()
    }
}
