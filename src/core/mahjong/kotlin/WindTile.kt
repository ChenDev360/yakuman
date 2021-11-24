data class WindTile (val wind: Wind) : Honor {

    var number = 0
    init {
        number = when(wind){
            Wind.EAST -> 1
            Wind.SOUTH -> 2
            Wind.WEST -> 3
            Wind.NORTH -> 4
        }
    }

    override fun toPrintable(): String {
        return number.toString() + WIND_SYMBOL
    }

    override fun getValue(): Int {
        return number
    }

    override fun getSymbol(): Char {
        return WIND_SYMBOL
    }
}