package product.domain;

import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class Restaurant {

    // 가게명
    private String name;
    // 어떤 종류 음식점 인지
    private String type;
    private String location;
    private Set<Food> menus;
}
