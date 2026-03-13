package com.kindaboii.journal.features.entries.impl.export

import android.app.Application
import android.content.Intent
import org.koin.mp.KoinPlatform
import java.io.File

actual fun printHtml(html: String, fileName: String) {
    val context = KoinPlatform.getKoin().get<Application>()
    val file = File(context.cacheDir, "journal_export.html")
    file.writeText(html, Charsets.UTF_8)
    val intent = Intent("com.kindaboii.journal.ACTION_PRINT").apply {
        putExtra("html_path", file.absolutePath)
        putExtra("file_name", fileName)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        setPackage(context.packageName)
    }
    context.startActivity(intent)
}
