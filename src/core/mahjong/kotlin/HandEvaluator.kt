class HandEvaluator {

    fun getSequences(hand: Hand): List<List<Tile>> {
        var sequences = mutableListOf<List<Tile>>()

        SUIT_SYMBOL_ORDER
            .map { hand.getBySymbol(it) }
            .forEach { sequences.addAll(getSequences(it)) }

        return sequences
    }

    fun getSequences(tiles: List<Tile>): List<List<Tile>> {
        val sequences = mutableListOf<List<Tile>>()

        for (i in tiles.indices) {
            var currentIndex = i
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

    fun getTriplets(hand: Hand): List<List<Tile>> {
        var triplets = mutableListOf<List<Tile>>()

        SYMBOL_ORDER
            .map { hand.getBySymbol(it) }
            .forEach { triplets.addAll(getTriplets(it)) }

        return triplets
    }

    fun getTriplets(tiles: List<Tile>): List<List<Tile>> {
        val triplets = mutableListOf<List<Tile>>()

        for (i in tiles.indices) {
            var currentIndex = i
            val firstTile = tiles[currentIndex]
            if (currentIndex + 1 < tiles.size && tiles[currentIndex + 1].getValue() == firstTile.getValue() && !triplets.any({ it.contains(firstTile)})) {
                val secondTile = tiles[currentIndex + 1]
                if (currentIndex + 2 < tiles.size && tiles[currentIndex + 2].getValue() == firstTile.getValue()) {
                    val thirdTile = tiles[currentIndex + 2]
                    val triplet = listOf(firstTile, secondTile, thirdTile)
                    triplets.add(triplet)
                }
            }
        }

        return triplets
    }

    fun getMentsu(hand: Hand): List<List<Tile>> {
        return getSequences(hand).plus(getTriplets(hand))
    }

    fun getMentsu(tiles: List<Tile>): List<List<Tile>> {
        return getSequences(tiles).plus(getTriplets(tiles))
    }


    fun getPairs(hand: Hand): List<List<Tile>> {
        return SYMBOL_ORDER.flatMap{ getPairs(hand.getBySymbol(it)) }
    }

    fun getPairs(tiles: List<Tile>): List<List<Tile>> {
        val visitedIndices = mutableListOf<Int>()
        val pairs = mutableListOf<List<Tile>>()
        for (i in tiles.indices) {
            var currentIndex = i
            val firstTile = tiles[currentIndex]
            if (currentIndex + 1 < tiles.size && tiles[currentIndex + 1].getValue() == firstTile.getValue()
                && !visitedIndices.contains(currentIndex) && !visitedIndices.contains(currentIndex + 1)
            ) {
                val secondTile = tiles[currentIndex + 1]
                visitedIndices.add(currentIndex)
                visitedIndices.add(currentIndex + 1)
                val pair = mutableListOf(firstTile, secondTile)
                pairs.add(pair)
            }
        }
        return pairs
    }

    fun getGroups(hand: Hand): MutableList<Tile> {
        return mutableListOf()
    }

    fun calculateShanten(hand: Hand): Int {
        val tiles = hand.handTiles.toMutableList()
        val pairs = getPairs(hand)
        var bestShanten = Int.MAX_VALUE

        for (pair in pairs) {
            val tilesWithoutPair = tiles.minus(pair)
            val currentShanten = recursiveMentsuSearch(tilesWithoutPair, Int.MAX_VALUE)
            if (currentShanten < bestShanten) {
                bestShanten = currentShanten
            }
        }
        // handle when no pair
        // handle kokushi
        // handle seven pairs

        return bestShanten
    }

    fun recursiveMentsuSearch(tiles: List<Tile>, bestShanten: Int): Int {
        val allMentsu = getMentsu(tiles.toMutableList())
        if (allMentsu.isEmpty()) {
            val currentShanten = tiles.size
            if (currentShanten < bestShanten) {
                return currentShanten
            }
            return bestShanten
        }
        for (mentsu in allMentsu) {
            recursiveMentsuSearch(tiles.minus(mentsu), tiles.size - mentsu.size)
        }
        //TODO
        return -1
    }
}