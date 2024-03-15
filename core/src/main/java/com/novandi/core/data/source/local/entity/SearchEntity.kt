package com.novandi.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.novandi.core.consts.Rooms

@Entity(tableName = Rooms.SearchTable)
data class SearchEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "keyword")
    val keyword: String
)
