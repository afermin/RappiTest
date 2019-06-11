/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.rappitest.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import android.util.Log
import com.example.rappitest.AppExecutors
import com.example.rappitest.api.ApiEmptyResponse
import com.example.rappitest.api.ApiErrorResponse
import com.example.rappitest.api.ApiResponse
import com.example.rappitest.api.ApiSuccessResponse
import com.example.rappitest.util.AbsentLiveData
import com.example.rappitest.vo.Resource

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    protected val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("loadFromDbLeakingThis")

        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            result.addSource(dbSource) { newData ->
                if (shouldFetch(data)) {
                    result.removeSource(dbSource)
                    fetchFromNetwork(dbSource)
                } else {
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        val resulat = saveCallResult(processResponse(response))
                        appExecutors.mainThread().execute {
                            setValue(Resource.success(resulat))
                        }
                        appExecutors.mainThread().execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            Log.d("ApiSuccessResponse", "")
                            result.addSource(loadFromDb()) { newData ->
                                Log.d("ApiSuccessResponse", "" + newData)
                                setValue(Resource.success(newData))
                            }
                        }

                        /*appExecutors.mainThread().execute {
                            setValue(Resource.success(result))
                        }*/
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        // reload from disk whatever we had
                        result.addSource(loadFromDb()) { newData ->
                            Log.d("ApiEmptyResponse", "" + newData)
                            setValue(Resource.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean = true

    @MainThread
    protected open fun loadFromDb(): LiveData<ResultType> = AbsentLiveData.create()

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType): ResultType

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}
