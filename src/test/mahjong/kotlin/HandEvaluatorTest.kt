import mahjong.kotlin.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandEvaluatorTest {

    @ParameterizedTest
    @MethodSource
    fun shouldReturnCorrectNumberOfPairs(hand: String, expectedPairs: List<List<Tile>>) {
        val hand = generateFromShortPrintable(hand)
        val pairs = hand.getPairs()
        assertEquals(expectedPairs, pairs)
        assertEquals(expectedPairs.count(), pairs.count())
    }

    fun shouldReturnCorrectNumberOfPairs(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("13334s44m677p44456z", listOf("33s", "44m", "77p", "44z")
                .map { buildFromShortPrintable(it) }),
            Arguments.of("59999s4m679p12356z", listOf("99s", "99s")
                .map { buildFromShortPrintable(it) }),
            Arguments.of("123456s678p12456z", listOf<List<Tile>>())
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnCorrectTriplets(hand: String, expectedTriplets: List<List<Tile>>) {
        val hand = generateFromShortPrintable(hand)
        val triplets = hand.getTriplets()
        assertEquals(expectedTriplets, triplets)
        assertEquals(expectedTriplets.count(), triplets.count())
    }

    fun shouldReturnCorrectTriplets(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("111s111m111p11156z", listOf("111s", "111m", "111p", "111z")
                .map { buildFromShortPrintable(it) }),
            Arguments.of("1111s5555678p124z", listOf("111s", "555p")
                .map { buildFromShortPrintable(it) }),
            Arguments.of("223s223m223p22345z", listOf<List<Tile>>()),
            Arguments.of("222456777s444577z", listOf("222s", "777s", "444z")
                .map { buildFromShortPrintable(it) }),
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnCorrectSequences(hand: String, expectedSequences: List<List<Tile>>) {
        val hand = generateFromShortPrintable(hand)
        val sequences = hand.getSequences()
        assertTrue(AssertHelpers.areNestedCollectionsEqual(expectedSequences, sequences))
    }

    fun shouldReturnCorrectSequences(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("123456789s444577z", listOf("123s", "234s", "345s", "456s", "567s", "678s", "789s")
                .map { buildFromShortPrintable(it) }),
            Arguments.of("2345s123m679p1235z", listOf("234s", "345s", "123m")
                .map { buildFromShortPrintable(it) }),
            Arguments.of("124578s679p12477z", listOf<List<Tile>>())
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnCorrectMentsu(hand: String, expectedSequences: List<List<Tile>>) {
        val hand = generateFromShortPrintable(hand)
        val mentsus = hand.getMentsu()
        assertTrue(AssertHelpers.areNotOrderedNestedCollectionsEqual(expectedSequences, mentsus))
    }

    fun shouldReturnCorrectMentsu(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("222456777s444577z", listOf("456s", "567s", "222s", "777s", "444z")
                .map { buildFromShortPrintable(it) }),
            Arguments.of("2345s123m679p1115z", listOf("234s", "345s", "123m", "111z")
                .map { buildFromShortPrintable(it) }),
            Arguments.of("124578s679p12477z", listOf<List<Tile>>())
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnCorrectShanten(hand: String, expectedShanten: Int) {
        val hand = generateFromShortPrintable(hand)
        hand.sort()
        val shanten = hand.calculateShanten()
        assertEquals(expectedShanten, shanten)
    }

    fun shouldReturnCorrectShanten(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("123s123m123456p44z", 0),
            Arguments.of("123s123m12356p44z", 1),
            Arguments.of("123s124m12356p44z", 2),
            Arguments.of("124s124m12356p44z", 3),
            // kokushi
            Arguments.of("19s19m199p1234567z", 0),
            Arguments.of("19s19m198p1234567z", 1),
            Arguments.of("19s19m999p1234567z", 1),
            Arguments.of("19s99m999p1234567z", 2),
            // chiitoitsu
            Arguments.of("1122s1122m1122p11z", 0),
            //Arguments.of("1122s1122m1122p12z", 1),
        )
    }
}