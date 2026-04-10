package com.uniovi.tfg.racketFlex.features.home.presentation

import com.uniovi.tfg.racketFlex.core.model.ClubModule

data class HomeState(
    val modules: List<ClubModule> = emptyList(),
    val loading: Boolean = true
)
