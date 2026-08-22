package com.pascal.noctra.data.audio

import kotlin.math.sin
import kotlin.random.Random

class SoundGeneratorState {
    var pinkB0 = 0f; var pinkB1 = 0f; var pinkB2 = 0f
    var pinkB3 = 0f; var pinkB4 = 0f; var pinkB5 = 0f; var pinkB6 = 0f
    var brownV = 0f
}

object SoundGenerator {

    fun generate(
        soundType: String,
        buffer: ShortArray,
        sampleRate: Int,
        phaseOffset: Double,
        random: Random,
        state: SoundGeneratorState = SoundGeneratorState()
    ): Double {
        var phase = phaseOffset
        for (i in buffer.indices) {
            val sample = when (soundType) {
                "white_noise" -> whiteNoise(random) * 0.5f
                "pink_noise" -> pinkNoise(random, state) * 0.5f
                "brown_noise", "brown_noise_deep" -> brownNoise(random, state) * 0.4f
                "rain", "light_rain" -> rainNoise(random, state) * 0.5f
                "heavy_rain", "storm" -> heavyRainNoise(random, state) * 0.6f
                "thunder" -> thunderNoise(random, state) * 0.5f
                "wind" -> windNoise(random, phase, state) * 0.5f
                "ocean", "whale" -> oceanNoise(random, phase, state) * 0.5f
                "fireplace", "campfire" -> fireNoise(random, state) * 0.4f
                "birds" -> birdNoise(random, phase, state) * 0.3f
                "crickets" -> cricketNoise(random, phase, state) * 0.3f
                "creek" -> creekNoise(random, state) * 0.5f
                "cafe" -> cafeNoise(random, state) * 0.4f
                "train" -> trainNoise(random, phase, state) * 0.3f
                "fan" -> fanNoise(random, phase, state) * 0.4f
                "ac", "vacuum" -> acNoise(random, phase, state) * 0.4f
                "river" -> riverNoise(random, phase, state) * 0.5f
                "forest" -> forestNoise(random, phase, state) * 0.4f
                "rain_window", "rain_leaves" -> rainNoise(random, state) * 0.45f
                "water_drops" -> waterDropsNoise(random, phase, state) * 0.3f
                else -> brownNoise(random, state) * 0.4f
            }
            buffer[i] = (sample.coerceIn(-1f, 1f) * Short.MAX_VALUE).toInt().toShort()
            phase += 1.0 / sampleRate
        }
        return phase
    }

    private fun whiteNoise(random: Random) = (random.nextFloat() * 2f - 1f).coerceIn(-1f, 1f)

    private fun pinkNoise(random: Random, s: SoundGeneratorState): Float {
        val w = random.nextFloat() * 2f - 1f
        s.pinkB0 = 0.99886f * s.pinkB0 + w * 0.0555179f
        s.pinkB1 = 0.99332f * s.pinkB1 + w * 0.0750759f
        s.pinkB2 = 0.96900f * s.pinkB2 + w * 0.1538520f
        s.pinkB3 = 0.86650f * s.pinkB3 + w * 0.3104856f
        s.pinkB4 = 0.55000f * s.pinkB4 + w * 0.5329522f
        s.pinkB5 = -0.7616f * s.pinkB5 - w * 0.0168980f
        val pink = (s.pinkB0 + s.pinkB1 + s.pinkB2 + s.pinkB3 + s.pinkB4 + s.pinkB5 + s.pinkB6 + w * 0.5362f) * 0.11f
        s.pinkB6 = w * 0.115926f
        return pink.coerceIn(-1f, 1f)
    }

    private fun brownNoise(random: Random, s: SoundGeneratorState): Float {
        val w = random.nextFloat() * 2f - 1f
        s.brownV = (s.brownV + 0.02f * w).coerceIn(-1f, 1f)
        return s.brownV
    }

    private fun rainNoise(r: Random, s: SoundGeneratorState) = (pinkNoise(r, s) * 0.6f + drip(r, 0.995f, 0.4f)).coerceIn(-1f, 1f)
    private fun heavyRainNoise(r: Random, s: SoundGeneratorState) = (pinkNoise(r, s) * 0.7f + drip(r, 0.99f, 0.5f)).coerceIn(-1f, 1f)
    private fun thunderNoise(r: Random, s: SoundGeneratorState) = (pinkNoise(r, s) * 0.3f + drip(r, 0.998f, 0.8f) - 0.4f * drip(r, 0.998f, 1f)).coerceIn(-1f, 1f)
    private fun windNoise(r: Random, p: Double, s: SoundGeneratorState) = (pinkNoise(r, s) * (sin(p * 0.5) * 0.3f + 0.5f).toFloat()).coerceIn(-1f, 1f)
    private fun oceanNoise(r: Random, p: Double, s: SoundGeneratorState) = (pinkNoise(r, s) * (sin(p * 0.8) * 0.5f + 0.5f).toFloat()).coerceIn(-1f, 1f)
    private fun fireNoise(r: Random, s: SoundGeneratorState) = (pinkNoise(r, s) * 0.4f + drip(r, 0.99f, 0.6f)).coerceIn(-1f, 1f)
    private fun birdNoise(r: Random, p: Double, s: SoundGeneratorState): Float { val b = pinkNoise(r, s) * 0.2f; val c = if (r.nextFloat() > 0.997f) (sin(p * (2000 + r.nextFloat() * 2000)) * 0.3f).toFloat() else 0f; return (b + c).coerceIn(-1f, 1f) }
    private fun cricketNoise(r: Random, p: Double, s: SoundGeneratorState): Float { val b = pinkNoise(r, s) * 0.15f; val c = if (r.nextFloat() > 0.995f) (sin(p * (4000 + r.nextFloat() * 2000)) * 0.25f).toFloat() else 0f; return (b + c).coerceIn(-1f, 1f) }
    private fun creekNoise(r: Random, s: SoundGeneratorState) = (pinkNoise(r, s) * 0.5f + drip(r, 0.998f, 0.3f)).coerceIn(-1f, 1f)
    private fun cafeNoise(r: Random, s: SoundGeneratorState) = (pinkNoise(r, s) * 0.4f + drip(r, 0.99f, 0.2f)).coerceIn(-1f, 1f)
    private fun trainNoise(r: Random, p: Double, s: SoundGeneratorState) = (brownNoise(r, s) * 0.3f + if ((p * 2.0).toLong() % 2L == 0L) 0.15f else 0f).coerceIn(-1f, 1f)
    private fun fanNoise(r: Random, p: Double, s: SoundGeneratorState) = (whiteNoise(r) * 0.3f + (sin(p * 60.0) * 0.1f).toFloat()).coerceIn(-1f, 1f)
    private fun acNoise(r: Random, p: Double, s: SoundGeneratorState) = (brownNoise(r, s) * 0.3f + (sin(p * 50.0) * 0.1f).toFloat()).coerceIn(-1f, 1f)
    private fun riverNoise(r: Random, p: Double, s: SoundGeneratorState) = (pinkNoise(r, s) * 0.5f + (sin(p * 1.5) * 0.2f).toFloat()).coerceIn(-1f, 1f)
    private fun forestNoise(r: Random, p: Double, s: SoundGeneratorState): Float { val b = pinkNoise(r, s) * 0.3f; val c = if (r.nextFloat() > 0.999f) (sin(p * 3000.0) * 0.2f).toFloat() else 0f; return (b + c).coerceIn(-1f, 1f) }
    private fun waterDropsNoise(r: Random, p: Double, s: SoundGeneratorState): Float { val b = pinkNoise(r, s) * 0.15f; val c = if (r.nextFloat() > 0.993f) (sin(p * (3000 + r.nextFloat() * 2000)) * 0.4f).toFloat() else 0f; return (b + c).coerceIn(-1f, 1f) }

    private fun drip(r: Random, threshold: Float, max: Float) = if (r.nextFloat() > threshold) r.nextFloat() * max else 0f
}
