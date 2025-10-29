package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointTest {

    @DisplayName("포인트를 생성할 때 초기 금액은 0이다.")
    @Test
    void createsPointWithZeroAmount() {
        // act
        Point point = Point.create("testuser01");

        // assert
        assertThat(point.getUserId()).isEqualTo("testuser01");
        assertThat(point.getAmount()).isEqualTo(0L);
    }

    @DisplayName("초기 금액을 지정하여 포인트를 생성할 수 있다.")
    @Test
    void createsPointWithInitialAmount() {
        // act
        Point point = Point.create("testuser01", 1000L);

        // assert
        assertThat(point.getUserId()).isEqualTo("testuser01");
        assertThat(point.getAmount()).isEqualTo(1000L);
    }

    @DisplayName("사용자 ID가 null이면 예외가 발생한다.")
    @Test
    void throwsException_whenUserIdIsNull() {
        // act & assert
        assertThatThrownBy(() -> Point.create(null))
                .isInstanceOf(CoreException.class)
                .hasMessage("사용자 ID는 비어있을 수 없습니다.");
    }

    @DisplayName("사용자 ID가 빈 문자열이면 예외가 발생한다.")
    @Test
    void throwsException_whenUserIdIsEmpty() {
        // act & assert
        assertThatThrownBy(() -> Point.create(""))
                .isInstanceOf(CoreException.class)
                .hasMessage("사용자 ID는 비어있을 수 없습니다.");
    }

    @DisplayName("포인트 금액이 null이면 예외가 발생한다.")
    @Test
    void throwsException_whenAmountIsNull() {
        // act & assert
        assertThatThrownBy(() -> Point.create("testuser01", null))
                .isInstanceOf(CoreException.class)
                .hasMessage("포인트 금액은 필수입니다.");
    }

    @DisplayName("포인트 금액이 음수이면 예외가 발생한다.")
    @Test
    void throwsException_whenAmountIsNegative() {
        // act & assert
        assertThatThrownBy(() -> Point.create("testuser01", -100L))
                .isInstanceOf(CoreException.class)
                .hasMessage("포인트 금액은 0 이상이어야 합니다.");
    }

    @DisplayName("포인트를 충전할 수 있다.")
    @Test
    void canChargePoint() {
        // arrange
        Point point = Point.create("testuser01");

        // act
        point.charge(1000L);

        // assert
        assertThat(point.getAmount()).isEqualTo(1000L);
    }

    @DisplayName("여러 번 충전할 수 있다.")
    @Test
    void canChargeMultipleTimes() {
        // arrange
        Point point = Point.create("testuser01");

        // act
        point.charge(1000L);
        point.charge(500L);
        point.charge(300L);

        // assert
        assertThat(point.getAmount()).isEqualTo(1800L);
    }

    @DisplayName("충전 금액이 0 이하이면 예외가 발생한다.")
    @Test
    void throwsException_whenChargeAmountIsZeroOrNegative() {
        // arrange
        Point point = Point.create("testuser01");

        // act & assert
        assertThatThrownBy(() -> point.charge(0L))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining("충전 금액은 0보다 커야 합니다.");

        assertThatThrownBy(() -> point.charge(-100L))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining("충전 금액은 0보다 커야 합니다.");
    }

    @DisplayName("충전 금액이 null이면 예외가 발생한다.")
    @Test
    void throwsException_whenChargeAmountIsNull() {
        // arrange
        Point point = Point.create("testuser01");

        // act & assert
        assertThatThrownBy(() -> point.charge(null))
                .isInstanceOf(CoreException.class)
                .hasMessage("포인트 금액은 필수입니다.");
    }

    @DisplayName("포인트를 사용할 수 있다.")
    @Test
    void canUsePoint() {
        // arrange
        Point point = Point.create("testuser01", 1000L);

        // act
        point.use(300L);

        // assert
        assertThat(point.getAmount()).isEqualTo(700L);
    }

    @DisplayName("여러 번 사용할 수 있다.")
    @Test
    void canUseMultipleTimes() {
        // arrange
        Point point = Point.create("testuser01", 1000L);

        // act
        point.use(300L);
        point.use(200L);
        point.use(100L);

        // assert
        assertThat(point.getAmount()).isEqualTo(400L);
    }

    @DisplayName("사용 금액이 0 이하이면 예외가 발생한다.")
    @Test
    void throwsException_whenUseAmountIsZeroOrNegative() {
        // arrange
        Point point = Point.create("testuser01", 1000L);

        // act & assert
        assertThatThrownBy(() -> point.use(0L))
                .isInstanceOf(CoreException.class)
                .hasMessage("사용 금액은 0보다 커야 합니다.");

        assertThatThrownBy(() -> point.use(-100L))
                .isInstanceOf(CoreException.class)
                .hasMessage("사용 금액은 0보다 커야 합니다.");
    }

    @DisplayName("사용 금액이 null이면 예외가 발생한다.")
    @Test
    void throwsException_whenUseAmountIsNull() {
        // arrange
        Point point = Point.create("testuser01", 1000L);

        // act & assert
        assertThatThrownBy(() -> point.use(null))
                .isInstanceOf(CoreException.class)
                .hasMessage("포인트 금액은 필수입니다.");
    }

    @DisplayName("잔액이 부족하면 예외가 발생한다.")
    @Test
    void throwsException_whenInsufficientBalance() {
        // arrange
        Point point = Point.create("testuser01", 500L);

        // act & assert
        assertThatThrownBy(() -> point.use(600L))
                .isInstanceOf(CoreException.class)
                .hasMessage("포인트 잔액이 부족합니다.");
    }

    @DisplayName("충전과 사용을 복합적으로 수행할 수 있다.")
    @Test
    void canChargeAndUsePoint() {
        // arrange
        Point point = Point.create("testuser01");

        // act
        point.charge(1000L);
        point.use(300L);
        point.charge(500L);
        point.use(200L);

        // assert
        assertThat(point.getAmount()).isEqualTo(1000L);
    }
}
