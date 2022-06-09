package mahjong.kotlin

import Hand
import SUIT_SYMBOL_ORDER
import SYMBOL_ORDER
import Tile
import kotlin.math.min

class HandEvaluator {

    private fun getSet(hand: Hand, getSetFunction: (input: List<Tile>) -> List<List<Tile>>): List<List<Tile>> {
        val set = mutableListOf<List<Tile>>()

        SYMBOL_ORDER
            .map { hand.getBySymbol(it) }
            .forEach { set.addAll(getSetFunction(it)) }

        return set
    }

    private fun getSet(
        tiles: List<Tile>,
        getSetFunction: (input: List<Tile>) -> List<List<Tile>>,
    ): List<List<Tile>> {
        val set = mutableListOf<List<Tile>>()

        SYMBOL_ORDER
            .map { Extensions.getBySymbol(tiles.toMutableList(), it) }
            .forEach { set.addAll(getSetFunction(it)) }

        return set
    }

    fun getSequences(hand: Hand): List<List<Tile>> = getSet(hand, ::getSequences)

    private fun getSequences(tiles: List<Tile>): List<List<Tile>> {
        val sequences = mutableListOf<List<Tile>>()

        for (i in tiles.indices) {
            var currentIndex = i
            val firstTile = tiles[currentIndex]
            while (currentIndex + 1 < tiles.size && tiles[currentIndex + 1].getValue() == firstTile.getValue()) {
                currentIndex++
            }
            if (currentIndex + 1 < tiles.size && tiles[currentIndex + 1].getValue() == firstTile.getValue() + 1) {
                val secondTile = tiles[currentIndex + 1]
                while (currentIndex + 1 <= tiles.size && tiles[currentIndex + 1].getValue() == firstTile.getValue()) {
                    currentIndex++
                }
                if (currentIndex + 2 < tiles.size && tiles[currentIndex + 2].getValue() == firstTile.getValue() + 2) {
                    val thirdTile = tiles[currentIndex + 2]
                    val sequence = listOf(firstTile, secondTile, thirdTile)
                    sequences.add(sequence)
                }
            }
        }

        return sequences
    }

    fun getTriplets(hand: Hand): List<List<Tile>> = getSet(hand, ::getTriplets)

    fun getTriplets(tiles: List<Tile>): List<List<Tile>> {
        val triplets = mutableListOf<List<Tile>>()

        for (i in tiles.indices) {
            var currentIndex = i
            val firstTile = tiles[currentIndex]
            if (currentIndex + 1 < tiles.size && tiles[currentIndex + 1].getValue() == firstTile.getValue() && !triplets.any(
                    { it.contains(firstTile) })
            ) {
                val secondTile = tiles[currentIndex + 1]
                if (currentIndex + 2 < tiles.size && tiles[currentIndex + 2].getValue() == firstTile.getValue()) {
                    val thirdTile = tiles[currentIndex + 2]
                    val triplet = listOf(firstTile, secondTile, thirdTile)
                    triplets.add(triplet)
                }
            }
        }

        return triplets
    }

    fun getMentsu(hand: Hand): List<List<Tile>> = getSet(hand, ::getSequences).plus(getTriplets(hand))

    fun getMentsu(tiles: List<Tile>): List<List<Tile>> = getSet(tiles, ::getSequences).plus(getTriplets(tiles))

    fun getTaatsu(tiles: List<Tile>): List<List<Tile>> {
        var taatsus = mutableListOf<List<Tile>>()

        SUIT_SYMBOL_ORDER
            .map { Extensions.getBySymbol(tiles.toMutableList(), it) }
            .forEach { taatsus.addAll(getAlmostSequences(it)) }

        SUIT_SYMBOL_ORDER
            .map { Extensions.getBySymbol(tiles.toMutableList(), it) }
            .forEach { taatsus.addAll(getPairs(it)) }

        return taatsus
    }

    fun getAlmostSequences(tiles: List<Tile>): List<List<Tile>> {
        val almostSequences = mutableListOf<List<Tile>>()

        for (i in tiles.indices) {
            var currentIndex = i
            val firstTile = tiles[currentIndex]
            while (currentIndex + 1 < tiles.size && tiles[currentIndex + 1].getValue() == firstTile.getValue()) {
                currentIndex++
            }
            if (currentIndex + 1 < tiles.size && tiles[currentIndex + 1].getValue() == firstTile.getValue() + 1) {
                val secondTile = tiles[currentIndex + 1]
                val almostSequence = listOf(firstTile, secondTile)
                almostSequences.add(almostSequence)
            }
        }

        return almostSequences
    }


    fun getPairs(hand: Hand): List<List<Tile>> = SYMBOL_ORDER.flatMap { getPairs(hand.getBySymbol(it)) }

    fun getPairs(tiles: List<Tile>): List<List<Tile>> {
        val visitedIndices = mutableListOf<Int>()
        val pairs = mutableListOf<List<Tile>>()
        for (i in tiles.indices) {
            var currentIndex = i
            val firstTile = tiles[currentIndex]
            if (currentIndex + 1 < tiles.size && tiles[currentIndex + 1].getValue() == firstTile.getValue()
                && !visitedIndices.contains(currentIndex) && !visitedIndices.contains(currentIndex + 1)
            ) {
                val secondTile = tiles[currentIndex + 1]
                visitedIndices.add(currentIndex)
                visitedIndices.add(currentIndex + 1)
                val pair = mutableListOf(firstTile, secondTile)
                pairs.add(pair)
            }
        }
        return pairs
    }

    fun getGroups(hand: Hand): MutableList<Tile> {
        return mutableListOf()
    }

    fun calculateShanten(hand: Hand): Int {
        val tiles = hand.handTiles.toMutableList()
        val pairs = getPairs(hand)
        var maxShanten = Int.MAX_VALUE

        val bestShanten = pairs
            .map { tiles.minus(it) }
            .map { recursiveMentsuSearch(it, Int.MAX_VALUE) }
            .fold(maxShanten) { _, el -> min(el, maxShanten) }

        // handle when no pair
        // handle kokushi
        // handle seven pairs

        if (bestShanten == -1) return 0

        return bestShanten
    }

    fun recursiveMentsuSearch(tiles: List<Tile>, bestShanten: Int): Int {
        return insideRecursiveMentsuSearch(tiles, mutableListOf(), mutableListOf(), Int.MAX_VALUE)
    }

    fun insideRecursiveMentsuSearch(
        tiles: List<Tile>,
        mentsuTiles: MutableList<List<Tile>>,
        taatsuTiles: MutableList<List<Tile>>,
        bestShanten: Int
    ): Int {
        //TODO probably need to sort mentsus and taatsus (?)
        var bestShantenSoFar = Int.MAX_VALUE
        val allMentsu = getMentsu(tiles.toMutableList())
        bestShantenSoFar =
            calculateBestShanten(allMentsu, tiles, mentsuTiles, taatsuTiles, bestShanten, bestShantenSoFar)

        val allTaatsu = getTaatsu(tiles.toMutableList())
        bestShantenSoFar =
            calculateBestShanten(allTaatsu, tiles, mentsuTiles, taatsuTiles, bestShanten, bestShantenSoFar)

        //val currentShanten = tiles.size + taatsuTiles.size
        // +2 because we assume that we have a pair

        val currentShanten = 14 - (mentsuTiles.size + taatsuTiles.size + 2)
        return min(currentShanten, bestShantenSoFar);
    }

    private fun calculateBestShanten(
        mentsuOrTaatsu: List<List<Tile>>,
        tiles: List<Tile>,
        mentsuTiles: MutableList<List<Tile>>,
        taatsuTiles: MutableList<List<Tile>>,
        bestShanten: Int,
        bestShantenSoFar: Int
    ): Int {
        return mentsuOrTaatsu.fold(bestShantenSoFar) { _, element ->
            min(
                insideRecursiveMentsuSearch(
                    tiles.minus(element).toMutableList(),
                    (mentsuTiles + element) as MutableList<List<Tile>>,
                    taatsuTiles.toMutableList(),
                    bestShanten
                ), bestShantenSoFar
            )
        }
    }
}