class Hand(
    var handTiles: MutableList<Tile>,
    val discardedTiles: MutableList<Tile> = mutableListOf()
) {

    //All operations on hand assumes that it is sorted
    fun getBySymbol(symbol: Char): List<Tile> {
        sort()
        return handTiles.filter { it.getSymbol() == symbol }.toList()
    }

    fun getCharacters(): List<Tile> {
        sort()
        return handTiles.filter { it.getSymbol() == CHARACTER_SYMBOL }.toList()
    }

    fun getBamboo(): List<Tile> {
        sort()
        return handTiles.filter { it.getSymbol() == BAMBOO_SYMBOL }.toList()
    }

    fun getDots(): List<Tile> {
        sort()
        return handTiles.filter { it.getSymbol() == DOT_SYMBOL }.toList()
    }

    fun sort() {
        val suitTiles = handTiles.filterIsInstance<SuitTile>().toMutableList()
        suitTiles.sortWith(compareBy({ it.pattern }, { it.number }))

        val winds = handTiles.filterIsInstance<WindTile>().toMutableList()
        winds.sortWith(compareBy { it.wind })

        val honors = handTiles.filterIsInstance<DragonTile>().toMutableList()
        honors.sortWith(compareBy { it.dragon })

        handTiles = suitTiles.plus(winds).plus(honors).toMutableList()
    }

    fun toPrintableShort(): String {
        sort()

        val resultBuilder = StringBuilder()

        val symbolToPrintIndices = mutableSetOf<Int>()

        SYMBOL_ORDER
            .forEach { symbol ->
                {
                    val last = handTiles.findLast { it.getSymbol() == symbol }
                    val lastIndex = handTiles.indexOfLast { it == last }
                    symbolToPrintIndices.add(lastIndex)
                }
            }

        handTiles
            .map { resultBuilder.append(it.getValue()) }
            .forEachIndexed { index, _ -> if (symbolToPrintIndices.contains(index)) resultBuilder.append(handTiles[index].getSymbol()) }

        return resultBuilder.toString();
    }

    fun toPrintable(): String {
        sort()

        val resultBuilder = StringBuilder()

        handTiles.map { resultBuilder.append(it.toPrintable()) }

        return resultBuilder.toString()
    }

    fun copy(): Hand {
/*        val suitTiles = handTiles.filterIsInstance<SuitTile>().toMutableList()
        val winds = handTiles.filterIsInstance<WindTile>().toMutableList()
        val honors = handTiles.filterIsInstance<DragonTile>().toMutableList()
        val tilesCopy: MutableList<Tile> = mutableListOf()
        for(tile in suitTiles){
            tilesCopy.add(tile.copy())
        }
        for(tile in winds){
            tilesCopy.add(tile.copy())
        }
        for(tile in honors){
            tilesCopy.add(tile.copy())
        }*/
        //TODO copy discards
        return Hand(handTiles)
    }
}