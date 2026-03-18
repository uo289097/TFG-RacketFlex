package com.uniovi.tfg.racketFlex.features.home

import com.uniovi.tfg.racketFlex.core.model.ClubModule

data class HomeState(
    val modules: List<ClubModule> = emptyList(),
    val loading: Boolean = true
)
