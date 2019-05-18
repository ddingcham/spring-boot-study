package product.domain.support;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
    <Test Cases>
    products (file) : raw data
 */
public class ProductPropertiesTest {

    @Test
    public void square_bracket() {
        ProductProperties properties = ProductProperties
                .builder(ProductOrigin.of("[가방] 무지[검정]에코백 [만든 곳]"))
                .build();

        assertTrue(properties.contains("가방"));
        assertTrue(properties.contains("검정"));
        assertTrue(properties.contains("만든 곳"));
    }

    @Test
    public void parentheses_1() {
        ProductProperties properties = ProductProperties
                .builder(ProductOrigin.of("[위드삭스] 06_여성 스포츠파일 단목^43P(화이트)"))
                .build();

        assertTrue(properties.contains("화이트"));
    }

    @Test
    public void parentheses_2() {
        ProductProperties properties = ProductProperties
                .builder(ProductOrigin.of("[메이튼] 02_기아^오피러스프리미엄(09년5월~11년)^블랙"))
                .build();

        assertTrue(properties.contains("09년5월~11년"));
    }

    @Test
    public void parentheses_3() {
        ProductProperties properties = ProductProperties
                .builder(ProductOrigin.of("79_여)삼색페이크삭스^핑크 (12켤레)"))
                .build();

        assertFalse(properties.contains("79_여"));
    }

    @Test
    public void caret_1() {
        ProductProperties properties = ProductProperties
                .builder(ProductOrigin.of("[카플스] 146_모더니^브라운"))
                .build();

        assertTrue(properties.contains("브라운"));
    }

    @Test
    public void caret_2() {
        ProductProperties properties = ProductProperties
                .builder(ProductOrigin.of("[유니홀릭] 02_소피아 원피스^보라^5호"))
                .build();

        assertTrue(properties.contains("보라"));
        assertTrue(properties.contains("5호"));
    }

    /*
        enum PropertyRegex
     */
    @Test
    public void not_default_case() {
        ProductProperties properties = ProductProperties
                .builder(ProductOrigin.of("[유니홀릭] 02_소피아 원피스^보라^5호 {special}"))
                .with("\\{(.+?)\\}")
                .build();

        assertTrue(properties.contains("special"));

    }

}
