package com.shoppi.kotlin.repository.home

import com.shoppi.kotlin.model.HomeData

interface HomeDataSource {
    fun getHomeData(): HomeData?
}
