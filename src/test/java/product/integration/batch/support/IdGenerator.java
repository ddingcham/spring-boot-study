package product.integration.batch.support;

public interface IdGenerator {
    long next();
    void reset();
}
