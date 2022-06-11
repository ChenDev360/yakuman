package mahjong.kotlin

import Tile

class AssertHelpers {
    companion object {
        fun areCollectionsEqual(first: List<Tile>, second: List<Tile>): Boolean {
            if (first.count() != second.count()) return false
            val firstSorted = first.sortedBy { it.getValue() }
            val secondSorted = second.sortedBy { it.getValue() }
            firstSorted.forEachIndexed { index, value ->
                if (secondSorted[index] != value) {
                    return false
                }
            }
            return true
        }

        fun areNestedCollectionsEqual(first: List<List<Tile>>, second: List<List<Tile>>): Boolean {
            first.indices.forEach {
                val res = areCollectionsEqual(first[it], second[it])
                if (!res) return res
            }
            return true
        }

        fun areNotOrderedNestedCollectionsEqual(first: List<List<Tile>>, second: List<List<Tile>>): Boolean {
            first.indices.forEach {
                val res = areNotOrderedCollectionsEqual(first[it], second[it])
                if (!res) return res
            }
            return true
        }

        // TODO fix
        fun areNotOrderedCollectionsEqual(first: List<Tile>, second: List<Tile>): Boolean {
            if (first.count() != second.count()) return false
            return first.containsAll(second)
        }
    }
}