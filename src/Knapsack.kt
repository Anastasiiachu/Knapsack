import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.*

public class Knapsack(val capacity: Int, val items: Array<Int>, val weight: Array<Int>, var mutationCount: Int) {
    val sumCost: Int = items.sum()
    val sumWeight: Int = weight.sum()

    init {
        if (items.size != weight.size) throw IllegalArgumentException()
    }


    fun findBest(): Array<Boolean> {
        var generation = firstGeneration()
        var bestSingle = generation.maxBy { estimateFunc(it) } ?: generation[0]
        var bestEst = estimateFunc(bestSingle)

        while (bestEst != 1.0 && mutationCount > 0) {
            val generationAvg = averageFunc(generation)
            generation = nextGeneration(generation)

            val newAvg = averageFunc(generation)
            if (newAvg <= generationAvg) {
                val random = Random()
                val index = random.nextInt(items.size - 1)
                generation[index] = mutation(generation[index])
            }

            val bestThisGeneration = generation.maxBy { estimateFunc(it) } ?: generation[0]
            bestSingle = if (estimateFunc(bestThisGeneration) > bestEst) bestThisGeneration else bestSingle
            bestEst = estimateFunc(bestSingle)
        }

        println("------------------------")
        bestSingle.forEach { print(if (it) 1 else 0) }
        print(" /  $${bestSingle.mapIndexed{i, b -> if (b) items[i] else 0 }.sum()}, ${bestSingle.mapIndexed { index, b -> if (b) this.weight[index] else 0 }.sum()}")
        return bestSingle
    }
    fun estimateFunc(single: Array<Boolean>): Double {
        val weight = single.mapIndexed { index, b -> if (b) this.weight[index] else 0 }.sum()
        val cost = single.mapIndexed { index, b -> if (b) this.items[index] else 0 }.sum()
        return (1.0 - if (weight > capacity) 1.0 else (capacity - weight).toDouble() / capacity) * (cost.toDouble() / sumCost)
    }
    fun averageFunc(generation: Array<Array<Boolean>>): Double = generation.sumByDouble { estimateFunc(it) } / items.size
    fun firstGeneration(): Array<Array<Boolean>> {
        val generation = Array(items.size, { _ -> Array(items.size, {false})})
        println("Creating random first generation:")
        for (i in 0..items.size - 1) {
            for (j in 0..items.size -1) {
                val r = Random()
                generation[i][j] = r.nextBoolean()
                print(if (generation[i][j]) 1 else 0)
            }
            println()
        }
        return generation
    }
    fun nextGeneration(prev: Array<Array<Boolean>>): Array<Array<Boolean>> {
        val needed = factorialCount(prev.size)
        val generation = Array(items.size, { _ -> Array(items.size, {false})})
        val survivors = prev.sortedBy { estimateFunc(it) }.take(needed)
        val random = Random()
        for (i in 0..prev.size - 1) {
            val fatherIndex = random.nextInt(needed - 1)
            var motherIndex = random.nextInt(needed - 1)
            while (fatherIndex == motherIndex) {
                motherIndex = random.nextInt(needed - 1)
            }
            val shift = 1 + random.nextInt(prev.size - 2)
            generation[i] = arrayOf(
                    *(survivors[fatherIndex].take(shift).toTypedArray()),
                    *(survivors[motherIndex].takeLast(items.size - shift).toTypedArray()))
        }
        return generation
    }
    fun mutation(single: Array<Boolean>): Array<Boolean> {
        val mutant = single.clone()
        val random = Random()
        val pos = random.nextInt(single.size - 1)
        mutant[pos] = !mutant[pos]
        mutationCount--
        return mutant
    }

    private fun factorialCount(more: Int): Int {
        var prev = 0
        var value = 1
        while (value < more) {
            prev++
            value *= prev
        }
        return prev
    }
}
