data class DragonTile (val dragon: Dragon) : Honor {
    var number = 0
    init {
        number = when(dragon){
            Dragon.WHITE -> 5
            Dragon.GREEN -> 6
            Dragon.RED -> 7
        }
    }

    override fun toPrintable(): String {
        return number.toString() + DRAGON_SYMBOL
    }

    override fun getValue(): Int {
        return number
    }

    override fun getSymbol(): Char {
        return DRAGON_SYMBOL
    }
}

// haku, hatsu, chun
enum class Dragon {
    WHITE, GREEN, RED
}