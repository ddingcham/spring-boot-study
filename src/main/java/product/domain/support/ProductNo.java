package product.domain.support;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ProductNo {

    static final String PRODUCT_NO_CAN_T_BE_NEGATIVE = "productNo can't be negative";
    private final long number;

    public ProductNo(long number) {
        if(number < 0L) {
            throw new IllegalArgumentException(PRODUCT_NO_CAN_T_BE_NEGATIVE);
        }
        this.number = number;
    }
}
