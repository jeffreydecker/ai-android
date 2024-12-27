package com.itsdecker.androidai.utils

import android.content.Context
import com.itsdecker.androidai.R

object TimeAgoFormatter {
    private const val MINUTE_MS = 60_000L
    private const val HOUR_MS = MINUTE_MS * 60L
    private const val DAY_MS = HOUR_MS * 24L
    private const val WEEK_MS = DAY_MS * 7L
    private const val MONTH_MS = DAY_MS * 30L
    private const val YEAR = DAY_MS * 365L

    fun format(epochMillis: Long, context: Context): String {
        val now = System.currentTimeMillis()
        val diffMillis = now - epochMillis

        return when {
            diffMillis < MINUTE_MS -> context.getString(R.string.time_just_now)
            else -> when {
                diffMillis < HOUR_MS -> {
                    val minutes = (diffMillis / MINUTE_MS).toInt()
                    context.resources.getQuantityString(
                        R.plurals.time_minutes,
                        minutes,
                        minutes,
                    )
                }

                diffMillis < DAY_MS -> {
                    val hours = (diffMillis / HOUR_MS).toInt()
                    context.resources.getQuantityString(
                        R.plurals.time_hours,
                        hours,
                        hours,
                    )
                }

                diffMillis < WEEK_MS -> {
                    val days = (diffMillis / DAY_MS).toInt()
                    context.resources.getQuantityString(
                        R.plurals.time_days,
                        days,
                        days,
                    )
                }

                diffMillis < MONTH_MS -> {
                    val weeks = (diffMillis / WEEK_MS).toInt()
                    context.resources.getQuantityString(
                        R.plurals.time_weeks,
                        weeks,
                        weeks,
                    )
                }

                diffMillis < YEAR -> {
                    val months = (diffMillis / MONTH_MS).toInt()
                    context.resources.getQuantityString(
                        R.plurals.time_months,
                        months,
                        months,
                    )
                }

                else -> {
                    val years = (diffMillis / YEAR).toInt()
                    context.resources.getQuantityString(
                        R.plurals.time_years,
                        years,
                        years,
                    )
                }
            }
        }
    }
}