package com.homalab.android.compose.weather.data.datasource

import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import com.homalab.android.compose.weather.data.mapper.toCity
import com.homalab.android.compose.weather.data.model.CityRecord
import com.homalab.android.compose.weather.domain.entity.City
import javax.inject.Inject

class CityDataSourceImpl @Inject constructor(client: ClientSearch) : CityDataSource {

    companion object {
        private const val ALGOLIA_SEARCH_INDEX = "dev_homalab"
    }

    private val index = client.initIndex(indexName = IndexName(ALGOLIA_SEARCH_INDEX))

    override suspend fun search(keyword: String): List<City> {
        val response = index.run {
            search(Query(keyword))
        }
        return response.hits.deserialize(CityRecord.serializer()).map { it.toCity() }
    }
}