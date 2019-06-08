package product.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class RestaurantTest {
    
    @Test
    public void generateRestaurant() {
        Restaurant restaurant = new Restaurant("식당", "중식", "서울시마포구", new HashSet<>(Arrays.asList(defaultFood(), defaultFood(), defaultFood())));
    }

    @Test
    public void name() {
    }

    private static Food defaultFood() {
        return new Food("음식명", 1000);
    }

}
