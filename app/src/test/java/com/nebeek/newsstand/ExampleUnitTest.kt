package com.nebeek.newsstand

import com.nebeek.newsstand.test.AppMath
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun additionIsCorrect() {
        val appMath = AppMath()

        val list: MutableList<Int> = mutableListOf()
        list.add(2)
        assertEquals(appMath.getPrimeFactors(2), list)

        list.clear()
        list.add(3)
        assertEquals(appMath.getPrimeFactors(3), list)

        list.clear()
        list.add(2)
        assertEquals(appMath.getPrimeFactors(4), list)

        list.clear()
        list.add(2)
        list.add(5)
        assertEquals(appMath.getPrimeFactors(10), list)

        list.clear()
        list.add(2)
        list.add(3)
        list.add(5)
        assertEquals(appMath.getPrimeFactors(30), list)
    }

    @Test
    fun productIsCorrect() {
        assertEquals(5, 1 * 5)
    }

}
