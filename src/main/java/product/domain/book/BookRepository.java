package product.domain.book;

import org.springframework.data.repository.CrudRepository;
import product.common.config.database.EditableRepository;

@EditableRepository
public interface BookRepository extends CrudRepository<Book,Long> {
}
