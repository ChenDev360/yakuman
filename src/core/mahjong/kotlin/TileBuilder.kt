class TileBuilder {
    fun buildFromPrintable(value: Char, symbol: Char) : Tile{
        when(symbol){
            BAMBOO_SYMBOL -> return SuitTile(value.toString().toInt(), Pattern.BAMBOO)
            CHARACTER_SYMBOL -> return SuitTile(value.toString().toInt(), Pattern.CHARACTER)
            DOT_SYMBOL -> return SuitTile(value.toString().toInt(), Pattern.DOT)
            HONOR_SYMBOL -> when(value.toString().toInt()){
                1 -> return WindTile(Wind.EAST)
                2 -> return WindTile(Wind.SOUTH)
                3 -> return WindTile(Wind.WEST)
                4 -> return WindTile(Wind.NORTH)
                5 -> return DragonTile(Dragon.WHITE)
                6 -> return DragonTile(Dragon.GREEN)
                7 -> return DragonTile(Dragon.RED)
            }
        }
        throw Exception("Unrecognized tile value or symbol")
    }

    fun buildFromShortPrintable(hand: String) : List<Tile>{
        val handTiles = mutableListOf<Tile>()

        var startIndex = 0
        for (i in hand.indices){
            if(hand[i].isLetter()){
                val endIndex = i - 1
                val symbol = hand[i]
                for(j in endIndex downTo startIndex){
                    val tile = buildFromPrintable(hand[j], symbol)
                    handTiles.add(tile)
                }
                startIndex = i + 1
            }
        }

        return handTiles
    }
}