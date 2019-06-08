package product.domain;

public class Food {

    private String name;
    private int price;

    public static Food of(String name, int price) {
        return new Food(name, price);
    }

    public Food(String name, int price) {
        validatedName(name);
        this.name = name;
        validatedPrice(price);
        this.price = price;
    }

    private static void validatedPrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("");
        }
    }

    private void validatedName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("");
        }
    }
}
