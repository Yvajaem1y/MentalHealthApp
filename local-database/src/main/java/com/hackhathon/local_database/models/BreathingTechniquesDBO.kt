package com.hackhathon.local_database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorite_technique_table")
data class BreathingTechniquesDBO(
    @PrimaryKey val id : Int
)
