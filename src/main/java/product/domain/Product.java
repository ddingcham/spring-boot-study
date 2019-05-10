package product.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import product.domain.support.ProductNo;
import product.domain.support.ProductOrigin;
import product.domain.support.ProductProperties;

import java.math.BigInteger;

@Document
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@ToString
public class Product {

    private static final String ILLEGAL_CONSTRUCT_ARGUMENT_MESSAGE = "construct argument can't be null";

    @Id
    @Getter
    @Setter
    private BigInteger id;
    private ProductNo productNo;
    private ProductOrigin origin;
    private ProductProperties properties;
    @Getter
    private int recommendScore;

    @Builder
    public Product(ProductNo productNo, ProductOrigin origin, ProductProperties productProperties, int recommendScore) {
        try {
            initialize(productNo, origin, productProperties, recommendScore);
        } catch (NullPointerException ignore) {
            throw new IllegalArgumentException(ILLEGAL_CONSTRUCT_ARGUMENT_MESSAGE);
        }
    }

    private void initialize(@NonNull ProductNo productNo,
                            @NonNull ProductOrigin origin,
                            @NonNull ProductProperties productProperties,
                            int recommendScore) {
        this.productNo = productNo;
        this.origin = origin;
        this.properties = productProperties;
        this.recommendScore = recommendScore;
    }

}
