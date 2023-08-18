package com.example.newscreenactivity.ui.theme.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SelectedMenuDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(selectedMenu: SelectedMenu)

    @Query("SELECT * FROM selected_menu WHERE id = 0")
    suspend fun getSelectedMenu(): SelectedMenu?
}