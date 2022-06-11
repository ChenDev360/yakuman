import mahjong.kotlin.Extensions

class Hand(
    var handTiles: MutableList<Tile>,
    val discardedTiles: MutableList<Tile> = mutableListOf()
) {

    //All operations on hand assumes that it is sorted
    fun getBySymbol(symbol: Char): List<Tile> {
        sort()
        return handTiles.filter { it.getSymbol() == symbol }
    }

    fun getCharacters(): List<Tile> {
        sort()
        return handTiles.filter { it.getSymbol() == CHARACTER_SYMBOL }
    }

    fun getBamboo(): List<Tile> {
        sort()
        return handTiles.filter { it.getSymbol() == BAMBOO_SYMBOL }
    }

    fun getDots(): List<Tile> {
        sort()
        return handTiles.filter { it.getSymbol() == DOT_SYMBOL }
    }

    fun sort() {
        handTiles = Extensions.sort(this.handTiles).toMutableList()
    }

    fun toPrintableShort(): String {
        sort()

        val resultBuilder = StringBuilder()

        val symbolToPrintIndices = mutableSetOf<Int>()

        SYMBOL_ORDER
            .forEach { symbol ->
                run {
                    val last = handTiles.findLast { it.getSymbol() == symbol }
                    val lastIndex = handTiles.indexOfLast { it == last }
                    symbolToPrintIndices.add(lastIndex)
                }
            }

        handTiles
            .map { resultBuilder.append(it.getValue()) }
            .forEachIndexed { index, _ -> if (symbolToPrintIndices.contains(index)) resultBuilder.append(handTiles[index].getSymbol()) }

        return resultBuilder.toString()
    }

    fun toPrintable(): String {
        sort()

        val resultBuilder = StringBuilder()

        handTiles.map { resultBuilder.append(it.toPrintable()) }

        return resultBuilder.toString()
    }
}