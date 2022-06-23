import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HandTest {

    @ParameterizedTest
    @MethodSource
    fun shouldReturnSortedDots(handAsPrintable: String, expectedDots: List<Tile>) {
        val hand = generateFromShortPrintable(handAsPrintable)
        Assertions.assertEquals(hand.getDots(), expectedDots)
    }

    fun shouldReturnSortedDots(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                "13334s44m677p44456z", buildFromShortPrintable("677p")
            ),
            Arguments.of(
                "13334s44m915p44456z", buildFromShortPrintable("159p")
            ),
            Arguments.of(
                "13334s44m44456z", buildFromShortPrintable("")
            )
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnSortedBamboo(handAsPrintable: String, expectedBamboo: List<Tile>) {
        val hand = generateFromShortPrintable(handAsPrintable)
        Assertions.assertEquals(expectedBamboo, hand.getBamboo())
    }

    fun shouldReturnSortedBamboo(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                "13334s44m677p44456z", buildFromShortPrintable("13334s")
            ),
            Arguments.of(
                "481s44m915p44456z", buildFromShortPrintable("148s")
            ),
            Arguments.of(
                "44m44456z", buildFromShortPrintable("")
            )
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnSortedCharacters(handAsPrintable: String, expectedCharacters: List<Tile>) {
        val hand = generateFromShortPrintable(handAsPrintable)
        Assertions.assertEquals(expectedCharacters, hand.getCharacters())
    }

    fun shouldReturnSortedCharacters(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                "13334s445m67p44456z", buildFromShortPrintable("445m")
            ),
            Arguments.of(
                "1333s749m915p44456z", buildFromShortPrintable("479m")
            ),
            Arguments.of(
                "44456z", buildFromShortPrintable("")
            )
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnSortedPrintable(handAsPrintable: String, expectedShortPrintable: String) {
        val hand = generateFromShortPrintable(handAsPrintable)
        Assertions.assertEquals(expectedShortPrintable, hand.toPrintable())
    }

    fun shouldReturnSortedPrintable(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("13334s445m67p44456z", "1s3s3s3s4s4m4m5m6p7p4z4z4z5z6z"),
            Arguments.of("65444z76p544m43331s", "1s3s3s3s4s4m4m5m6p7p4z4z4z5z6z"),
            Arguments.of("31s56m91p", "1s3s5m6m1p9p")
        )
    }

    @ParameterizedTest
    @MethodSource
    fun shouldReturnSortedShortPrintable(handAsPrintable: String, expectedShortPrintable: String) {
        val hand = generateFromShortPrintable(handAsPrintable)
        Assertions.assertEquals(expectedShortPrintable, hand.toPrintableShort())
    }

    fun shouldReturnSortedShortPrintable(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("13334s445m67p44456z", "13334s445m67p44456z"),
            Arguments.of("65444z76p544m43331s", "13334s445m67p44456z"),
            Arguments.of("31s56m91p", "13s56m19p")
        )
    }
}