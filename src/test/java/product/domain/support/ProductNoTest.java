package product.domain.support;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static product.domain.support.ProductNo.PRODUCT_NO_CAN_T_BE_NEGATIVE;

public class ProductNoTest {

    @Test
    public void productNo_is_not_negative() {
        assertThatThrownBy(() -> new ProductNo(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_NO_CAN_T_BE_NEGATIVE);
    }

    @Test
    public void equals() {
        ProductNo expected = new ProductNo(1L);
        assertThat(new ProductNo(1L)).isEqualTo(expected);
    }
}
