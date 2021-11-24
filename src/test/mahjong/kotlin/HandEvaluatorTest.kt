import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandEvaluatorTest {

    private val handEvaluator = HandEvaluator()
    private val handGenerator = HandGenerator()
    private val tileBuilder = TileBuilder()

    @ParameterizedTest
    @MethodSource
    fun shouldReturnCorrectNumberOfPairs(hand: String, expectedPairs: List<List<Tile>>) {
        val hand = handGenerator.generateFromShortPrintable(hand)
        val pairs = handEvaluator.getPairs(hand)
        assertEquals(expectedPairs, pairs)
        assertEquals(expectedPairs.count(), pairs.count())
    }

    fun shouldReturnCorrectNumberOfPairs(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("13334s44m677p44456z", listOf("33s", "44m", "77p", "44z").map { tileBuilder.buildFromShortPrintable(it) }),
            Arguments.of("59999s4m679p12356z", listOf("99s", "99s", ).map { tileBuilder.buildFromShortPrintable(it) }),
            Arguments.of("123456s678p12456z", listOf<List<Tile>>())
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnCorrectTriplets(hand: String, expectedTriplets: List<List<Tile>>) {
        val hand = handGenerator.generateFromShortPrintable(hand)
        val triplets = handEvaluator.getTriplets(hand)
        assertEquals(expectedTriplets, triplets)
        assertEquals(expectedTriplets.count(), triplets.count())
    }

    fun shouldReturnCorrectTriplets(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("111s111m111p11156z",  listOf("111s", "111m", "111p", "111z").map { tileBuilder.buildFromShortPrintable(it) }),
            Arguments.of("1111s5555678p124z", listOf("111s", "555p").map { tileBuilder.buildFromShortPrintable(it) }),
            Arguments.of("223s223m223p22345z", listOf<List<Tile>>())
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnCorrectSequences(hand: String, expectedSequences: List<List<Tile>>) {
        val hand = handGenerator.generateFromShortPrintable(hand)
        val sequences = handEvaluator.getSequences(hand)
        assertTrue(AreNestedCollectionsEqual(expectedSequences, sequences))
    }

    fun shouldReturnCorrectSequences(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("123456789s444577z", listOf("123s", "234s", "345s", "456s", "567s", "678s", "789s").map { tileBuilder.buildFromShortPrintable(it) }),
            Arguments.of("2345s123m679p1235z", listOf("234s", "345s", "123m").map { tileBuilder.buildFromShortPrintable(it) }),
            Arguments.of("124578s679p12477z", listOf<List<Tile>>())
        )
    }

/*    @ParameterizedTest
    @MethodSource
    fun shouldReturnCorrectShanten(hand: String, expectedShanten: Int) {
        val hand = handGenerator.generateFromShortPrintable(hand)
        val shanten = handEvaluator.calculateShanten(hand)
        assertEquals(expectedShanten, shanten)
    }*/

    fun shouldReturnCorrectShanten(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("123s123m123456p44z", 0),
            Arguments.of("123s123m12356p44z", 1),
            Arguments.of("123s124m12356p44z", 2),
            Arguments.of("124s124m12356p44z", 3),
            // kokushi
            Arguments.of("19s19m199p1234567z", 0),
            // chiitoitsu
            Arguments.of("1122s1122m1122p11z", 0),
        )
    }

    fun AreNestedCollectionsEqual(first: List<List<Tile>>, second: List<List<Tile>>) : Boolean {
        first.indices.forEach {
            val res = AreCollectionsEqual(first[it], second[it])
            if(!res) return res
        }
        return true
    }

    fun AreCollectionsEqual(first: List<Tile>, second: List<Tile>) : Boolean {
        if (first.count() != second.count()) return false
        val firstSorted = first.sortedBy { it.getValue() }
        val secondSorted = second.sortedBy { it.getValue() }
        firstSorted.forEachIndexed { index, value -> if (secondSorted[index] != value) { return false } }
        return true
    }
}