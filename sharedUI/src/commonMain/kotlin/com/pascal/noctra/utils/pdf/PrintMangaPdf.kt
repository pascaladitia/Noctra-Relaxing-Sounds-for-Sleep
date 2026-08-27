package com.pascal.noctra.utils.pdf

expect suspend fun printMangaPdf(
    url: List<String>,
    fileName: String = "manga_${kotlin.random.Random.nextInt(1000, 9999)}.pdf"
): Result<String>
