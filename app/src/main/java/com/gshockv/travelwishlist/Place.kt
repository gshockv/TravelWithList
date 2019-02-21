package com.gshockv.travelwishlist

import android.content.Context

data class Place (
    val name: String,
    val imageName: String,
    val isFav: Boolean = false
)

fun Place.imageResourceId(context: Context) = context.resources.getIdentifier(
    this.imageName, "drawable", context.packageName)
