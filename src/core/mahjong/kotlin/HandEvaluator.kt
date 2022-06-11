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
            .map { Extensions.getBySymbol(tiles, it) }
            .forEach { set.addAll(getSetFunction(it)) }

        return set
    }

    fun getSequences(hand: Hand): List<List<Tile>> = getSet(hand, ::getSequences)

    private fun getSequences(tiles: List<Tile>): List<List<Tile>> {
        val sequences = mutableListOf<List<Tile>>()

        tiles.indices.forEachIndexed { index, _ ->
            var currentIndex = index
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

    private fun getTriplets(tiles: List<Tile>): List<List<Tile>> {
        val triplets = mutableListOf<List<Tile>>()

        tiles.indices.forEachIndexed { index, _ ->
            val firstTile = tiles[index]
            if (index + 1 < tiles.size && tiles[index + 1].getValue() == firstTile.getValue() && !triplets.any { it.contains(firstTile) }) {
                val secondTile = tiles[index + 1]
                if (index + 2 < tiles.size && tiles[index + 2].getValue() == firstTile.getValue()) {
                    val thirdTile = tiles[index + 2]
                    val triplet = listOf(firstTile, secondTile, thirdTile)
                    triplets.add(triplet)
                }
            }
        }

        return triplets
    }

    fun getMentsu(hand: Hand): List<List<Tile>> = getSet(hand, ::getSequences).plus(getTriplets(hand))

    private fun getMentsu(tiles: List<Tile>): List<List<Tile>> = getSet(tiles, ::getSequences).plus(getTriplets(tiles))

    private fun getTaatsu(tiles: List<Tile>): List<List<Tile>> {
        val taatsus = mutableListOf<List<Tile>>()

        SUIT_SYMBOL_ORDER
            .map { Extensions.getBySymbol(tiles, it) }
            .forEach { taatsus.addAll(getAlmostSequences(it)) }

        SUIT_SYMBOL_ORDER
            .map { Extensions.getBySymbol(tiles, it) }
            .forEach { taatsus.addAll(getPairs(it)) }

        return taatsus
    }

    private fun getAlmostSequences(tiles: List<Tile>): List<List<Tile>> {
        val almostSequences = mutableListOf<List<Tile>>()

        tiles.indices.forEachIndexed { index, _ ->
            var currentIndex = index
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

    private fun getPairs(tiles: List<Tile>): List<List<Tile>> {
        val visitedIndices = mutableListOf<Int>()
        val pairs = mutableListOf<List<Tile>>()

        tiles.indices.forEachIndexed { index, _ ->
            val firstTile = tiles[index]
            if (index + 1 < tiles.size && tiles[index + 1].getValue() == firstTile.getValue()
                && !visitedIndices.contains(index) && !visitedIndices.contains(index + 1)
            ) {
                val secondTile = tiles[index + 1]
                visitedIndices.add(index)
                visitedIndices.add(index + 1)
                val pair = mutableListOf(firstTile, secondTile)
                pairs.add(pair)
            }
        }

        return pairs
    }

    fun getGroups(hand: Hand): List<Tile> {
        return mutableListOf()
    }

    fun calculateShanten(hand: Hand): Int {
        val tiles = hand.handTiles
        val pairs = getPairs(hand)

        val maxShanten = min(getKokushiShanten(tiles), getChiitoiShanten(tiles))

        val bestShanten = pairs
            .map { tiles.minus(it) }
            .map { recursiveMentsuSearch(it, listOf(), listOf(), Int.MAX_VALUE) }
            .fold(maxShanten) { _, el -> min(el, maxShanten) }

        // handle when no pair
        // handle seven pairs

        if (bestShanten == -1) return 0

        return bestShanten
    }

    private fun getKokushiShanten(tiles: List<Tile>): Int {
        val terminalsAndHonors = tiles
            .filter { it.isTerminalOrHonor() }

        val notTerminalOrHonors = tiles.filter { !it.isTerminalOrHonor() }

        val distinctTerminals = terminalsAndHonors
            .distinctBy { it.getHash() }

        return 13 - distinctTerminals.size + notTerminalOrHonors.size
    }

    private fun getChiitoiShanten(tiles: List<Tile>): Int {
        return Int.MAX_VALUE
    }

    private fun recursiveMentsuSearch(
        tiles: List<Tile>,
        mentsuTiles: List<Tile>,
        taatsuTiles: List<List<Tile>>,
        bestShanten: Int
    ): Int {
        //TODO probably need to sort mentsus and taatsus (?)
        var bestShantenSoFar = Int.MAX_VALUE
        val allMentsu = getMentsu(tiles)

        bestShantenSoFar =
            calculateBestShanten(allMentsu, tiles, mentsuTiles, taatsuTiles, bestShanten, bestShantenSoFar)

        val allTaatsu = getTaatsu(tiles)
        bestShantenSoFar =
            calculateBestShanten(allTaatsu, tiles, mentsuTiles, taatsuTiles, bestShanten, bestShantenSoFar)

        //val currentShanten = tiles.size + taatsuTiles.size
        // +2 because we assume that we have a pair

        val currentShanten = 14 - (mentsuTiles.size + taatsuTiles.size + 2)
        return min(currentShanten, bestShantenSoFar)
    }

    private fun calculateBestShanten(
        mentsuOrTaatsu: List<List<Tile>>,
        tiles: List<Tile>,
        mentsuTiles: List<Tile>,
        taatsuTiles: List<List<Tile>>,
        bestShanten: Int,
        bestShantenSoFar: Int
    ): Int {
        return mentsuOrTaatsu.fold(bestShantenSoFar) { _, element ->
            min(
                recursiveMentsuSearch(
                    tiles.minus(element),
                    mentsuTiles.plus(element),
                    taatsuTiles,
                    bestShanten
                ), bestShantenSoFar
            )
        }
    }
}