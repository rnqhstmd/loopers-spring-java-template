package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderTotalAmount {

    @Column(name = "total_amount", nullable = false)
    private Long value;

    private OrderTotalAmount(Long value) {
        validate(value);
        this.value = value;
    }

    public static OrderTotalAmount of(Long value) {
        return new OrderTotalAmount(value);
    }

    public static OrderTotalAmount zero() {
        return new OrderTotalAmount(0L);
    }

    private void validate(Long value) {
        if (value == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 총액은 필수입니다.");
        }
    }

    public OrderTotalAmount add(OrderTotalAmount other) {
        return new OrderTotalAmount(this.value + other.value);
    }
}
