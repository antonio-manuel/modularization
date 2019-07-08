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
package com.txusballesteros.codelabs.billboard.feature.nowplaying.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.txusballesteros.codelabs.billboard.core.domain.model.Movie
import com.txusballesteros.codelabs.billboard.core.view.BaseFragment
import com.txusballesteros.codelabs.billboard.feature.nowplaying.R
import com.txusballesteros.codelabs.billboard.feature.nowplaying.presentation.NowPlayingPresenter
import com.txusballesteros.codelabs.billboard.navigation.Navigator
import com.txusballesteros.codelabs.billboard.navigation.command.movieDetailNavigationCommand
import kotlinx.android.synthetic.main.fragmnet_now_playing.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.core.KoinComponent
import org.koin.core.inject

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class NowPlayingFragment : KoinComponent, BaseFragment(), NowPlayingPresenter.View {
    companion object {
        fun newget() = NowPlayingFragment()
    }

    private lateinit var adapter: NowPlayingAdapter
    private val navigate: Navigator by inject()
    private val presenter: NowPlayingPresenter by inject()
    private var sharedView: View? = null

    override fun onRequestLayoutResourceId() = R.layout.fragmnet_now_playing

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupList()
        presenter.onViewReady(this)
    }

    private fun setupList() {
        val columns = resources.getInteger(R.integer.now_playing_columns)
        val layoutManager = GridLayoutManager(context, columns, RecyclerView.VERTICAL, false)
        adapter = NowPlayingAdapter { view, movie ->
            sharedView = view
            presenter.onMovieTap(movie)
        }
        list.layoutManager = layoutManager
        list.adapter = adapter
    }

    override fun renderMovies(movies: List<Movie>) {
        adapter.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    override fun renderError() {
        Snackbar.make(root, R.string.now_playing_error, Snackbar.LENGTH_SHORT).show()
    }

    override fun navigateToMovieDetail(id: String) {
        navigate(this, movieDetailNavigationCommand(id, sharedView))
    }
}
