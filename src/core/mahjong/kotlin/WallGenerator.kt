class WallGenerator {
    fun generateWall(): List<Tile> {
        val wall = mutableListOf<Tile>()
        val patterns = Pattern.values()
        for (i in 0..2) {
            val currentPattern = patterns[i]
            for (j in 0..8) {
                for (k in 0..3) {
                    val tile = SuitTile(j + 1, currentPattern)
                    wall.add(tile)
                }
            }
        }

        val winds = Wind.values()
        for (i in 0..3) {
            val currentWind = winds[i]
            for (j in 0..3) {
                val tile = WindTile(currentWind)
                wall.add(tile)
            }
        }

        val dragons = Dragon.values()
        for (i in 0..2) {
            val currentDragon = dragons[i]
            for (j in 0..3) {
                val tile = DragonTile(currentDragon)
                wall.add(tile)
            }
        }

        wall.shuffle()
        return wall
    }

    fun generateDeadWall(wall: MutableList<Tile>): List<Tile> {
        val deadWall = mutableListOf<Tile>()

        for (i in 0..13) {
            val randomIndex = (0..wall.size - i).random()
            val tile = wall[randomIndex]
            wall.removeAt(randomIndex)
            deadWall.add(tile)
        }

        return deadWall
    }
}