/*
 * Copyright Txus Ballesteros 2018 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
package com.txusballesteros.codelabs.billboard.api.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.txusballesteros.codelabs.billboard.api.instrumentation.AuthInterceptor
import com.txusballesteros.codelabs.billboard.api.instrumentation.ResponseInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiInfrastructureModule = module {
    val BASE_URL = "https://api.themoviedb.org/"

    val moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    fun buildRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    single {
        val authInterceptor: AuthInterceptor = get()
        val responseInterceptor: ResponseInterceptor = get()
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(responseInterceptor)
            .build()
    }

    single { AuthInterceptor() }
    single { ResponseInterceptor() }
    single {
        val httpClient: OkHttpClient = get()
            buildRetrofit(httpClient)
    }
}
