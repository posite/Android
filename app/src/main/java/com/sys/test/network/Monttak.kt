package com.sys.test.network

data class Monttak(
    val currentPage: Int,
    val items: List<Item>,
    val pageCount: Int,
    val pageSize: Int,
    val result: String,
    val resultCount: Int,
    val resultMessage: String,
    val totalCount: Int
)