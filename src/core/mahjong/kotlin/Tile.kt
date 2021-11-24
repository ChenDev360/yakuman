interface Tile {
    fun toPrintable(): String
    fun getValue(): Int
    fun getSymbol(): Char
}

interface Honor : Tile