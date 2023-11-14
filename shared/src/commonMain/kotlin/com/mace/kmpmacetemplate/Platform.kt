package com.mace.kmpmacetemplate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform