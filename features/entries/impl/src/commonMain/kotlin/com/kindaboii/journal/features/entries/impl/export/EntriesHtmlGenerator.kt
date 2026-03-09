package com.kindaboii.journal.features.entries.impl.export

import com.kindaboii.journal.features.entries.api.models.Entry
import com.kindaboii.journal.features.entries.api.models.Mood
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val CSS = """
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      font-family: system-ui, -apple-system, 'Segoe UI', sans-serif;
      max-width: 720px; margin: 44px auto; padding: 0 32px;
      color: #1a1a1a; background: #fff; font-size: 14px; line-height: 1.5;
    }
    .doc-header { margin-bottom: 48px; padding-bottom: 20px; border-bottom: 2px solid #1a1a1a; }
    .doc-title { font-size: 26px; font-weight: 600; letter-spacing: -0.3px; margin-bottom: 6px; }
    .doc-meta { font-size: 12px; color: #999; }
    .entry { padding: 36px 0; border-bottom: 1px solid #ebebeb; }
    .entry:last-child { border-bottom: none; }
    .entry-date { font-size: 11px; color: #aaa; text-transform: uppercase; letter-spacing: 0.9px; margin-bottom: 14px; }
    .entry-title { font-size: 20px; font-weight: 600; margin-bottom: 10px; line-height: 1.3; }
    .entry-body { font-size: 15px; line-height: 1.85; color: #222; white-space: pre-wrap; }
    .mood-section { margin-bottom: 20px; padding-bottom: 16px; border-bottom: 1px solid #f0f0f0; }
    .section-label { font-size: 10px; color: #bbb; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 10px; }
    .mood-bar-wrap {
      position: relative; height: 8px; border-radius: 4px; margin-bottom: 8px;
      background: linear-gradient(to right, #6B5BCF 0%, #6FA9FF 16%, #9DD6FF 33%, #CFEAF7 50%, #9EDB8B 66%, #F0C56B 83%, #F5A354 100%);
      print-color-adjust: exact; -webkit-print-color-adjust: exact;
    }
    .mood-marker {
      position: absolute; top: 50%; transform: translate(-50%, -50%);
      width: 14px; height: 14px; border-radius: 50%;
      background: #fff; border: 2.5px solid rgba(0,0,0,0.3); box-shadow: 0 1px 4px rgba(0,0,0,0.18);
    }
    .mood-moment { font-size: 12px; color: #777; margin-bottom: 12px; }
    .tags-group { margin-top: 10px; }
    .tags-group-label { font-size: 10px; color: #bbb; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 5px; }
    .tags-row { display: flex; flex-wrap: wrap; gap: 5px; }
    .tag { display: inline-block; padding: 3px 10px; border-radius: 12px; font-size: 11px; }
    .tag-emotion { background: #eef2ff; color: #4055b0; border: 1px solid #d5dcf5; print-color-adjust: exact; -webkit-print-color-adjust: exact; }
    .tag-influence { background: #f5f5f5; color: #666; border: 1px solid #e0e0e0; print-color-adjust: exact; -webkit-print-color-adjust: exact; }
    @media print {
      body { margin: 0; padding: 24px 32px; max-width: 100%; }
      .entry { page-break-after: always; }
      .entry:last-child { page-break-after: auto; }
    }
""".trimIndent()

fun generateEntryHtml(entry: Entry): String {
    val tz = TimeZone.currentSystemDefault()
    val dt = entry.createdAt.toLocalDateTime(tz)
    val meta = "${dt.date.dayOfMonth} ${monthName(dt.date.monthNumber)} ${dt.date.year}"
    return htmlDocument("Запись из дневника", meta, entryHtml(entry, tz))
}

fun generateAllEntriesHtml(entries: List<Entry>): String {
    val tz = TimeZone.currentSystemDefault()
    val dates = entries.map { it.createdAt.toLocalDateTime(tz).date }
    val min = dates.minOrNull()
    val max = dates.maxOrNull()
    val meta = when {
        min != null && max != null && min != max ->
            "с ${min.dayOfMonth} ${monthName(min.monthNumber)} ${min.year} " +
            "по ${max.dayOfMonth} ${monthName(max.monthNumber)} ${max.year}"
        min != null -> "${min.dayOfMonth} ${monthName(min.monthNumber)} ${min.year}"
        else -> ""
    }
    val title = "Дневник — ${entries.size} ${pluralEntries(entries.size)}"
    return htmlDocument(title, meta, entries.joinToString("") { entryHtml(it, tz) })
}

private fun htmlDocument(title: String, meta: String, body: String) = """
<!DOCTYPE html>
<html lang="ru">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${title.escapeHtml()}</title>
<style>
$CSS
</style>
</head>
<body>
<div class="doc-header">
  <div class="doc-title">${title.escapeHtml()}</div>
  ${if (meta.isNotEmpty()) """<div class="doc-meta">$meta</div>""" else ""}
</div>
$body
</body>
</html>
""".trimIndent()

private fun entryHtml(entry: Entry, tz: TimeZone): String {
    val dt = entry.createdAt.toLocalDateTime(tz)
    val time = "${dt.hour.toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}"
    val date = "${dt.date.dayOfMonth} ${monthName(dt.date.monthNumber)} ${dt.date.year} · $time"
    val titleHtml = entry.title?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?.let { """<div class="entry-title">${it.escapeHtml()}</div>""" }
        ?: ""
    val bodyHtml = entry.body?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?.let { """<div class="entry-body">${it.escapeHtml()}</div>""" }
        ?: ""
    val moodSection = entry.mood?.let { moodHtml(it) } ?: ""
    return """
<div class="entry">
  <div class="entry-date">$date</div>
  $moodSection
  $titleHtml
  $bodyHtml
</div>"""
}

private fun moodHtml(mood: Mood): String {
    val pct = mood.value.coerceIn(0, 100)
    return """
<div class="mood-section">
  <div class="section-label">Настроение</div>
  <div class="mood-bar-wrap"><div class="mood-marker" style="left:${pct}%"></div></div>
  <div class="mood-moment">${moodMomentLabel(mood.value)}</div>
  ${if (mood.emotions.isNotEmpty()) tagsHtml(mood.emotions, "tag-emotion", "Эмоции") else ""}
  ${if (mood.influences.isNotEmpty()) tagsHtml(mood.influences, "tag-influence", "Влияния") else ""}
</div>"""
}

private fun tagsHtml(tags: List<String>, cssClass: String, label: String): String {
    val pills = tags.joinToString("") { """<span class="tag $cssClass">${it.escapeHtml()}</span>""" }
    return """
<div class="tags-group">
  <div class="tags-group-label">$label</div>
  <div class="tags-row">$pills</div>
</div>"""
}

// Gradient stops: [pct, r, g, b] — mirrors the UI mood bar colours.
private val MOOD_STOPS = arrayOf(
    intArrayOf(0, 0x6B, 0x5B, 0xCF), intArrayOf(16, 0x6F, 0xA9, 0xFF),
    intArrayOf(33, 0x9D, 0xD6, 0xFF), intArrayOf(50, 0xCF, 0xEA, 0xF7),
    intArrayOf(66, 0x9E, 0xDB, 0x8B), intArrayOf(83, 0xF0, 0xC5, 0x6B),
    intArrayOf(100, 0xF5, 0xA3, 0x54),
)

internal fun moodColor(pct: Int): String {
    val lo = MOOD_STOPS.last { it[0] <= pct }
    val hi = MOOD_STOPS.first { it[0] >= pct }
    return if (lo === hi) {
        hex3(lo[1], lo[2], lo[3])
    } else {
        val t = (pct - lo[0]).toFloat() / (hi[0] - lo[0])
        hex3(
            (lo[1] + t * (hi[1] - lo[1])).toInt(),
            (lo[2] + t * (hi[2] - lo[2])).toInt(),
            (lo[3] + t * (hi[3] - lo[3])).toInt(),
        )
    }
}

private fun hex3(r: Int, g: Int, b: Int): String {
    fun Int.hex2() = toString(16).padStart(2, '0')
    return "#${r.hex2()}${g.hex2()}${b.hex2()}"
}

private fun moodMomentLabel(value: Long): String = when {
    value <= 14 -> "Очень неприятный момент"
    value <= 28 -> "Неприятный момент"
    value <= 42 -> "Слегка неприятный момент"
    value <= 57 -> "Нейтральный момент"
    value <= 71 -> "Слегка приятный момент"
    value <= 85 -> "Приятный момент"
    else -> "Очень приятный момент"
}

private fun String.escapeHtml(): String = this
    .replace("&", "&amp;")
    .replace("<", "&lt;")
    .replace(">", "&gt;")
    .replace("\"", "&quot;")

private fun monthName(month: Int): String = when (month) {
    1 -> "января"; 2 -> "февраля"; 3 -> "марта"; 4 -> "апреля"
    5 -> "мая"; 6 -> "июня"; 7 -> "июля"; 8 -> "августа"
    9 -> "сентября"; 10 -> "октября"; 11 -> "ноября"; else -> "декабря"
}

private fun pluralEntries(n: Int): String = when {
    n % 100 in 11..19 -> "записей"
    n % 10 == 1 -> "запись"
    n % 10 in 2..4 -> "записи"
    else -> "записей"
}
