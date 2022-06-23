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

        val symbolToPrintIndices = SYMBOL_ORDER
            .map { symbol ->
                val last = handTiles.findLast { it.getSymbol() == symbol }
                handTiles.indexOfLast { it == last }
            }

        val resultBuilder = StringBuilder()

        handTiles
            .forEachIndexed { index, _ ->
                if (symbolToPrintIndices.contains(index)) resultBuilder.append(
                    handTiles[index].getValue(),
                    handTiles[index].getSymbol()
                ) else resultBuilder.append(handTiles[index].getValue())
            }

        return resultBuilder.toString()
    }

    fun toPrintable(): String {
        sort()

        val resultBuilder = StringBuilder()

        handTiles.map { resultBuilder.append(it.toPrintable()) }

        return resultBuilder.toString()
    }
}