package lotto.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParserTest {

    private final Parser parser = new Parser();

    @Test
    @DisplayName("숫자로만 구성된 문자열이 들어오면 정수형으로 변환한다")
    void parseStringToIntegerTest() {
        // given
        String value = "8000";
        // when
        int result = parser.parseStringToInteger(value);
        // then
        assertThat(result)
                .isInstanceOf(Integer.class)
                .isEqualTo(8000);
    }

    @Nested
    @DisplayName("콤마로 구분된 숫자로 구성된 문자열이 들어올 때")
    class ParseStringToIntegerListTest {
        @Test
        @DisplayName("정수형 리스트로 변환한다")
        void parseStringToIntegerListTest() {
            // given
            String numbers = "1,2,3,4,5";
            // when
            List<Integer> result = parser.parseWinningLottoNumbers(numbers);
            // then
            assertThat(result).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        @DisplayName("스페이스가 포함되어있을때 정수형 리스트로 변환한다")
        void parseStringContainSpaceToIntegerListTest() {
            // given
            String numbers = "1, 2, 3, 4, 5";
            // when
            List<Integer> result = parser.parseWinningLottoNumbers(numbers);
            // then
            assertThat(result).containsExactly(1, 2, 3, 4, 5);
        }
    }

    @Nested
    @DisplayName("숫자로 구성되지 않은 문자열이 들어올때")
    class containNotNumberStringTest {

        @Test
        void 문자열을_숫자로_변환할때_예외가_발생한다() {
            // given
            String value = "8000a";
            // when// then
            assertThatThrownBy(() -> parser.parseStringToInteger(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 잘못된 입력입니다. 숫자만 입력할 수 있습니다.");
        }

        @Test
        void 콤마를_기준으로_자르고_숫자로_변환할때_예외가_발생한다() {
            // given
            String numbers = "1,2 3,4,a";
            // when// then
            assertThatThrownBy(() -> parser.parseWinningLottoNumbers(numbers))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 잘못된 입력입니다. 숫자만 입력할 수 있습니다.");
        }

    }

}