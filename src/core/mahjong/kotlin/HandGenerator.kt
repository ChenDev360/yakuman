class HandGenerator(){
    fun generateFromWall(wall: MutableList<Tile>) : Hand{
        val handTiles = mutableListOf<Tile>()

        for (i in 0..13){
            val randomIndex = (0..wall.size - i).random()
            val tile = wall[randomIndex]
            wall.removeAt(randomIndex)
            handTiles.add(tile)
        }

        return Hand(handTiles)
    }

    //in sorted format 123s123m123p123z
    fun generateFromShortPrintable(hand: String) : Hand{
        val handTiles = mutableListOf<Tile>()
        val tileBuilder = TileBuilder()
        return Hand(tileBuilder.buildFromShortPrintable(hand).toMutableList())
    }
}