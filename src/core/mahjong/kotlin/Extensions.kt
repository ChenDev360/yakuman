package mahjong.kotlin

import DragonTile
import SuitTile
import Tile
import WindTile

class Extensions {
    companion object {
        fun getBySymbol(tiles: List<Tile>, symbol: Char): List<Tile> {
            val sortedTiles = sort(tiles)
            return sortedTiles.filter { it.getSymbol() == symbol }
        }

        fun sort(tiles: List<Tile>): List<Tile> {
            val suitTiles = tiles
                .filterIsInstance<SuitTile>().toMutableList()
                .sortedWith(compareBy({ it.pattern }, { it.number }))

            val winds = tiles.filterIsInstance<WindTile>()
                .sortedWith(compareBy { it.wind })

            val honors = tiles.filterIsInstance<DragonTile>()
                .sortedWith(compareBy { it.dragon })

//            val suitTiles = tiles
//                .filterIsInstance<SuitTile>().toMutableList()
//                .sortedWith(compareByDescending<SuitTile> { it.pattern }
//                    .thenByDescending { it.number })
//
//            val winds = tiles.filterIsInstance<WindTile>()
//                .sortedWith(compareByDescending { it.wind })
//
//            val honors = tiles.filterIsInstance<DragonTile>()
//                .sortedWith(compareByDescending { it.dragon })

            return suitTiles.plus(winds).plus(honors)
        }
    }
}