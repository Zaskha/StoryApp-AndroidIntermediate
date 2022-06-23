

/*
*
*
* dan saya pun stuck di pagging juga kak
* saya sudah pelajari ulang materinya beberapa kali namun
* saya bingung untuk mengimplementasikan nya disini kak
*
* */





//package com.zaskha.storyapepe.data
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.zaskha.storyapepe.data.DetailStoryItem
//import com.zaskha.storyapepe.data.ApiService
//
//
//
///**
// * Paging3
// *
// * Handle retrieve data from server until last page
// * Data will automatically changing if move to next page
// * */
//class StoryPagingSource(private val apiService: ApiService, private val token: String) :
//    PagingSource<Int, DetailStoryItem>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DetailStoryItem> {
//        return try {
//            val page = params.key ?: INITIAL_PAGE_INDEX
//            val response = apiService.allStories(
//                token = token,
//                page = page,
//                size = params.loadSize
//            )
//            val data = response.listStory
//
//            LoadResult.Page(
//                data = data,
//                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
//                nextKey = if (data.isNullOrEmpty()) null else page + 1
//            )
//        } catch (exception: Exception) {
//            LoadResult.Error(exception)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, DetailStoryItem>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 1
//    }
//}