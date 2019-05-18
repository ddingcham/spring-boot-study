package product.domain.support;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductOrigin {

    static final String PRODUCT_NAME_CAN_T_BE_NULL = "PRODUCT_NAME_CAN_T_BE_NULL";

    @NonNull
    private final String origin;

    public static ProductOrigin of(String origin) {
        try {
            return new ProductOrigin(origin);
        } catch (NullPointerException ignore) {
            throw new IllegalArgumentException(PRODUCT_NAME_CAN_T_BE_NULL);
        }
    }
}
