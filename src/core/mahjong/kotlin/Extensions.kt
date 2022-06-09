package mahjong.kotlin

import DragonTile
import SuitTile
import Tile
import WindTile

class Extensions {
    companion object {
        fun getBySymbol(tiles: MutableList<Tile>, symbol: Char): List<Tile> {
            val sortedTiles = sort(tiles)
            return sortedTiles.filter { it.getSymbol() == symbol }.toList()
        }

        private fun sort(tiles: MutableList<Tile>): MutableList<Tile> {
            val suitTiles = tiles.filterIsInstance<SuitTile>().toMutableList()
            suitTiles.sortWith(compareBy({ it.pattern }, { it.number }))

            val winds = tiles.filterIsInstance<WindTile>().toMutableList()
            winds.sortWith(compareBy { it.wind })

            val honors = tiles.filterIsInstance<DragonTile>().toMutableList()
            honors.sortWith(compareBy { it.dragon })

            return suitTiles.plus(winds).plus(honors).toMutableList()
        }
    }
}