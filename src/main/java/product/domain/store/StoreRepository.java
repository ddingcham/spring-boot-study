package product.domain.store;

import org.springframework.data.repository.CrudRepository;
import product.common.config.database.ReadOnlyRepository;

@ReadOnlyRepository
public interface StoreRepository extends CrudRepository<Store, Long> {
}
