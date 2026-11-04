package com.brunnofdev.meubolso.util

import androidx.room.TypeConverter
import com.brunnofdev.meubolso.model.TipoMovimentacao

class Converters {
    @TypeConverter
    fun fromTipoMovimentacao(value: TipoMovimentacao) = value.name

    @TypeConverter
    fun toTipoMovimentacao(value: String) = enumValueOf<TipoMovimentacao>(value)
}