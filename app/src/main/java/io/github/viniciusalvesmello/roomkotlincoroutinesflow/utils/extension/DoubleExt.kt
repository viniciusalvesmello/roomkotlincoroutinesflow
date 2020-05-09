package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension

import java.text.NumberFormat
import java.util.Locale

fun Double?.toCurrencyFormatCurrency(
    language: String = "pt",
    country: String = "BR",
    minimumFractionDigits: Int = 2,
    maximumFractionDigits: Int = 2
): String {
    val format = NumberFormat.getCurrencyInstance(Locale(language, country))
    format.minimumFractionDigits = minimumFractionDigits
    format.maximumFractionDigits = maximumFractionDigits

    return format.format(this ?: 0.0)
}