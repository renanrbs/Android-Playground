package com.playground.microbenchmark

import android.util.Log
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Benchmark, which will execute on an Android device.
 *
 * The body of [BenchmarkRule.measureRepeated] is measured in a loop, and Studio will
 * output the result. Modify your code to see how it affects performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun foreachTest() {
        val list = (1..10_000_000)
        benchmarkRule.measureRepeated {
            list.forEach {
                it + 1
            }
        }
    }

    @Test
    fun forTest() {
        val list = (1..10_000_000)
        benchmarkRule.measureRepeated {
            for (i in list) {
                i + 1
            }
        }
    }
}