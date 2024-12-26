package com.itsdecker.androidai.database.typeconverter

import androidx.room.TypeConverter
import com.itsdecker.androidai.data.SupportedProvider

class SupportedProviderTypeConverter {

    @TypeConverter
    public fun fromSupportedProvider(model: SupportedProvider): String = model.name

    @TypeConverter
    public fun toSupportedProvider(model: String): SupportedProvider =
        try {
            SupportedProvider.valueOf(model)
        } catch (e: IllegalArgumentException) {
            SupportedProvider.UNKNOWN
        }
}