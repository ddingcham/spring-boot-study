package product.domain.support;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static product.domain.support.ProductOrigin.PRODUCT_NAME_CAN_T_BE_NULL;

public class ProductOriginTest {

    @Test
    public void productOrigin_cant_be_null() {
        assertThatThrownBy(() -> ProductOrigin.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_NAME_CAN_T_BE_NULL);
    }

}
