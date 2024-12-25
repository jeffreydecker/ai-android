package com.itsdecker.androidai.database.typeconverter

import androidx.room.TypeConverter
import com.itsdecker.androidai.data.SupportedProvider

class SupportedModelTypeConverter {

    @TypeConverter
    public fun fromSupportedModel(model: SupportedProvider): String = model.name

    @TypeConverter
    public fun toSupportedModel(model: String): SupportedProvider =
        try {
            SupportedProvider.valueOf(model)
        } catch (e: IllegalArgumentException) {
            SupportedProvider.UNKNOWN
        }
}