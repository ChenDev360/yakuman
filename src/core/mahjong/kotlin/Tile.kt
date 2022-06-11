interface Tile {
    fun toPrintable(): String
    fun getValue(): Int
    fun getSymbol(): Char
    fun isTerminalOrHonor(): Boolean {
        return when (this) {
            is SuitTile -> this.isTerminal()
            else -> true
        }
    }

    fun getHash(): String {
        return getValue().toString().plus(getSymbol())
    }
}

interface Honor : Tile