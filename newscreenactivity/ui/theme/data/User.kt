package com.example.newscreenactivity.ui.theme.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_menu")
data class SelectedMenu(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "menu") val menu: String
)