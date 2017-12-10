import org.junit.Test
import org.junit.Assert.*

class KnapsackTest {
    @Test
    fun test() {
        val knapsack = Knapsack(13, arrayOf(2, 20, 3, 11), arrayOf(15, 3, 9, 5), 10000)
        assertArrayEquals(arrayOf(false, true, false, true), knapsack.findBest())
    }
    @Test
    fun test2() {
        val knapsack = Knapsack(50, arrayOf(37, 29, 3, 31, 24, 7), arrayOf(22, 5, 35, 7, 17, 16), 10000)
        assertArrayEquals(arrayOf(true, true, false, true, false, true), knapsack.findBest())
    }
}