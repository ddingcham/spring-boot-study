package product.domain;

import org.junit.Test;

public class FoodTest {

    @Test(expected = IllegalArgumentException.class)
    public void price_lower_zero_then_exception() {
        Food.of("", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void name_null_then_exception() {
        Food.of(null, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void name_empty_then_exception() {
        Food.of("", -1);
    }
}
