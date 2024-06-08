package com.novandi.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.novandi.core.consts.Rooms

@Entity(tableName = Rooms.AssistantTable)
data class AssistantEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "chat")
    val chat: String,

    @ColumnInfo(name = "is_from_me")
    val isFromMe: Boolean,

    @ColumnInfo(name = "user_chat")
    val userChat: String,

    @ColumnInfo(name = "is_error")
    val isError: Boolean = false,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
