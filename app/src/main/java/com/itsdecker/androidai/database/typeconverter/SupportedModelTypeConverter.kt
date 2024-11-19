package com.itsdecker.androidai.database.typeconverter

import androidx.room.TypeConverter
import com.itsdecker.androidai.data.SupportedModel

class SupportedModelTypeConverter {

    @TypeConverter
    public fun fromSupportedModel(model: SupportedModel): String = model.name

    @TypeConverter
    public fun toSupportedModel(model: String): SupportedModel =
        try {
            SupportedModel.valueOf(model)
        } catch (e: IllegalStateException) {
            SupportedModel.UNKNOWN
        }
}