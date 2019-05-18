package product.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import product.domain.Product;
import product.domain.ProductRepository;
import product.domain.support.ProductNo;
import product.domain.support.ProductOrigin;
import product.domain.support.ProductProperties;

import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataMongoTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void save() {
        ProductOrigin origin = ProductOrigin.of("[메이튼] 02_기아^오피러스프리미엄(09년5월~11년)^블랙");
        Product beforeSave = buildTestProduct(origin, 0);

        log.info("beforeSave : {}", beforeSave);

        Product afterSave = productRepository.save(beforeSave);

        log.info("afterSave : {}", afterSave);

        Optional<Product> product = productRepository.findById(afterSave.getId());

        assertThat(product
                .orElseThrow(RuntimeException::new)
                .getId())
                .isEqualTo(afterSave.getId());
    }

    @Test
    public void update() {
        // given
        int originScore = 0;
        int updateScore = 1000;
        ProductOrigin origin = ProductOrigin.of("[메이튼] 02_기아^오피러스프리미엄(09년5월~11년)^블랙");
        Product product = buildTestProduct(origin, originScore);

        product = productRepository.save(product);
        BigInteger duplicatedId = product.getId();

        Product updatedProduct = buildTestProduct(origin, updateScore);
        updatedProduct.setId(duplicatedId);

        // when
        productRepository.save(updatedProduct);

        // then
        Optional<Product> foundedProduct = productRepository.findById(duplicatedId);

        assertThat(foundedProduct
                .orElseThrow(RuntimeException::new).getRecommendScore())
                .isEqualTo(updateScore)
                .isNotEqualTo(originScore);
    }

    private Product buildTestProduct(ProductOrigin origin, int score) {
        return Product.builder()
                .productNo(new ProductNo(1L))
                .origin(origin)
                .productProperties(ProductProperties
                        .builder(origin)
                        .build())
                .recommendScore(score)
                .build();
    }
}
