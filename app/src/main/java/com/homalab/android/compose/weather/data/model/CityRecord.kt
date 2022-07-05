package com.homalab.android.compose.weather.data.model

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.homalab.android.compose.weather.domain.entity.Coord
import kotlinx.serialization.Serializable

@Serializable
data class CityRecord(
    val id: Int,
    val name: String,
    val state: String,
    val country: String,
    val coord: Coord,
    override val objectID: ObjectID
) : Indexable

