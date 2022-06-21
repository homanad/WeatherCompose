package com.homalab.android.compose.weather.ui.model

import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.search.Query
import com.homalab.android.compose.weather.domain.entity.Coord
import com.homalab.android.compose.weather.util.Constants
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

suspend fun search(query: String): List<CityRecord> {
    val client = ClientSearch(
        applicationID = ApplicationID(Constants.ALGOLIA_APP_ID),
        apiKey = APIKey(Constants.ALGOLIA_API_KEY)
    )

    val index = client.initIndex(indexName = IndexName(Constants.ALGOLIA_SEARCH_INDEX))

    val response = index.run {
        search(Query(query))
    }
    return response.hits.deserialize(CityRecord.serializer())
}
