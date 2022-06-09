import mahjong.kotlin.HandEvaluator

fun main(args: Array<String>) {
/*    val wallGenerator = WallGenerator()
    val handGenerator = HandGenerator()
    val wall = wallGenerator.generateWall()
    wall.forEach{print(it.toPrintable())}
    wallGenerator.generateDeadWall(wall)
    val hand = handGenerator.generateFromWall(wall)
    hand.sort()
    println()
    println(hand.toPrintableShort())
    println(hand.toPrintable())*/

    val handGenerator = HandGenerator()
    val hand = handGenerator.generateFromShortPrintable("12333s4m679p44456z")
    println(hand.toPrintable())

    val handEvaluator = HandEvaluator()
    val pairs = handEvaluator.getPairs(hand)
    pairs.map {
        {
            it.map { it.getValue() }
            println()
        }
    }
//    for (pair in pairs) {
//        for (tile in pair) {
//            print(tile.getValue())
//        }
//        //print(sequence[0].getSymbol())
//        println()
//    }

/*    val shanten = handEvaluator.calculateShanten(hand)
    print(shanten)*/
/*
    val handGenerator = HandGenerator()
    val hand = handGenerator.generateFromShortPrintable("12333s4m679p44456z")

    val handEvaluator = mahjong.kotlin.HandEvaluator()
    val sequences = handEvaluator.getSequences(hand)

    for (sequence in sequences){
        for(tile in sequence){
            print(tile.getValue())
        }
        print(sequence[0].getSymbol())
        println()
    }

    val triplets = handEvaluator.getTriplets(hand)
    for (triplet in triplets){
        for(tile in triplet){
            print(tile.getValue())
        }
        print(triplet[0].getSymbol())
        println()
    }

    val pairs = handEvaluator.getPairs(hand)
    for (pair in pairs){
        for(tile in pair){
            print(tile.getValue())
        }
        print(pair[0].getSymbol())
        println()
    }*/


}
