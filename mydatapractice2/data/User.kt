package com.example.mydatapractice2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true ) val uid: Int =0,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("mail") val mail: String? = null,
    @ColumnInfo("phone") val phone: String? = null,
)