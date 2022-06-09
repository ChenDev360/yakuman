//TODO support red five
data class SuitTile (val number: Int, val pattern: Pattern, val isRedFive: Boolean = false) : Tile {
    override fun toPrintable(): String {
        val numberInString = number.toString()
        return when(pattern){
            Pattern.BAMBOO -> numberInString + BAMBOO_SYMBOL
            Pattern.CHARACTER -> numberInString + CHARACTER_SYMBOL
            Pattern.DOT -> numberInString + DOT_SYMBOL
        }
    }

    override fun getValue(): Int {
        return number
    }

    override fun getSymbol(): Char {
        return when(pattern){
            Pattern.BAMBOO -> BAMBOO_SYMBOL
            Pattern.CHARACTER -> CHARACTER_SYMBOL
            Pattern.DOT -> DOT_SYMBOL
        }
    }

    fun isTerminal(): Boolean {
        return getValue() == 1 || getValue() == 9
    }
}

//so`, man, pin
enum class Pattern {
    BAMBOO, CHARACTER, DOT
}