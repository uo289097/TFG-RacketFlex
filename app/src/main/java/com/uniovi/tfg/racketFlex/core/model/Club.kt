package com.uniovi.tfg.racketFlex.core.model

data class Club(
    val name: String = "",
    val modules: List<ClubModule> = emptyList(),
)
