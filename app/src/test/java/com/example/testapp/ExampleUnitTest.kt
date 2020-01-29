package com.example.testapp

import com.example.testapp.util.RollUtil
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun rollTest() {
        val rollvalue = RollUtil().roll3D6()
        assertTrue(rollvalue in 3..18)
    }

    @Test
    fun rollTest2() {
        val rollvalue = RollUtil().roll3D6(+1)
        assertTrue(rollvalue in 4..19)
    }
}
