package lotto.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import lotto.constant.Rank;
import lotto.dto.LottoResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class WinningLottoTest {
    @Nested
    @DisplayName("당첨 로또를 생성할때")
    class winningLottoInitErrorTest {
        private final Lotto ticket = Lotto.of(List.of(1, 2, 3, 4, 5, 6));

        @ParameterizedTest
        @CsvSource(value = {"0", "46"})
        @DisplayName("보너스 번호가 1보다 작거나 45보다 크면 예외가 발생한다")
        void winningLottoInitErrorTest(String outOfRangeNumber) {
            assertThatThrownBy(() -> new WinningLotto(ticket, Integer.parseInt(outOfRangeNumber)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 보너스 번호는 1와 45사이 숫자여야합니다.");
        }

        @Test
        @DisplayName("보너스 번호가 당첨로또 번호와 중복이 있다면 예외가 발생한다")
        void winningLottoDuplicateErrorTest() {
            assertThatThrownBy(() -> new WinningLotto(ticket, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 보너스 번호는 로또 번호와 중복될 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("당첨 로또 체크 테스트")
    class WinningLottoCheckLottoTest {
        private final Lotto ticket = Lotto.of(List.of(1, 2, 3, 4, 5, 6));

        private static Stream<Arguments> provideLottoRankTestCases() {
            return Stream.of(
                    // NONE 랭크
                    Arguments.of("로또 번호 0개 맞고 보너스도 맞지 않을때 None 랭크를 반환한다"
                            , List.of(7, 8, 9, 10, 11, 12), 13, 0, Rank.NONE),
                    Arguments.of("로또 번호 1개 맞고 보너스도 맞지 않을때 None 랭크를 반환한다"
                            , List.of(6, 7, 8, 9, 10, 11), 12, 1, Rank.NONE),
                    Arguments.of("로또 번호 1개 맞고 보너스 번호 맞을때 None 랭크를 반환한다"
                            , List.of(6, 7, 8, 9, 10, 11), 7, 1, Rank.NONE),
                    Arguments.of("로또 번호 2개 맞고 보너스도 맞지 않을때 None 랭크를 반환한다"
                            , List.of(5, 6, 7, 8, 9, 10), 11, 2, Rank.NONE),
                    Arguments.of("로또 번호 2개 맞고 보너스 번호 맞을때 None 랭크를 반환한다"
                            , List.of(5, 6, 7, 8, 9, 10), 7, 2, Rank.NONE),
                    // FIFTH 랭크
                    Arguments.of("로또 번호 3개 맞고 보너스도 맞지 않을때 FIFTH 랭크를 반환한다"
                            , List.of(4, 5, 6, 7, 8, 9), 10, 3, Rank.FIFTH),
                    Arguments.of("로또 번호 3개 맞고 보너스 번호 맞을때 FIFTH 랭크를 반환한다"
                            , List.of(4, 5, 6, 7, 8, 9), 7, 3, Rank.FIFTH),
                    // FOURTH 랭크
                    Arguments.of("로또 번호 4개 맞고 보너스도 맞지 않을때 FOURTH 랭크를 반환한다"
                            , List.of(3, 4, 5, 6, 7, 8), 9, 4, Rank.FOURTH),
                    Arguments.of("로또 번호 4개 맞고 보너스 번호 맞을때 FOURTH 랭크를 반환한다"
                            , List.of(3, 4, 5, 6, 7, 8), 8, 4, Rank.FOURTH),
                    // THIRD 랭크
                    Arguments.of("로또 번호 5개 맞고 보너스도 맞지 않을때 THIRD 랭크를 반환한다"
                            , List.of(2, 3, 4, 5, 6, 7), 8, 5, Rank.THIRD),
                    // SECOND 랭크
                    Arguments.of("로또 번호 5개 맞고 보너스 번호 맞을때 SECOND 랭크를 반환한다"
                            , List.of(2, 3, 4, 5, 6, 7), 7, 5, Rank.SECOND),
                    // FIRST 랭크
                    Arguments.of("로또 번호 6개 맞을때 FIRST 랭크를 반환한다"
                            , List.of(1, 2, 3, 4, 5, 6), 7, 6, Rank.FIRST)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("provideLottoRankTestCases")
        @DisplayName("로또 결과를 검증시")
        void rankingTest(String description, List<Integer> numbers, int bonusNumber, int expectedMatchCount,
                         Rank expectedRank) {
            // given
            WinningLotto winningLotto = new WinningLotto(ticket, bonusNumber);
            Lotto newTicket = Lotto.of(numbers);

            // when
            LottoResult result = winningLotto.checkLotto(newTicket);

            // then
            assertThat(result.getMatchCount()).isEqualTo(expectedMatchCount);
            assertThat(result.getRank()).isEqualTo(expectedRank);
        }
    }

}