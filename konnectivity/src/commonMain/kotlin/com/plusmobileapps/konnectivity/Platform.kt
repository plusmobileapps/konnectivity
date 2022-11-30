package com.plusmobileapps.konnectivity

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform