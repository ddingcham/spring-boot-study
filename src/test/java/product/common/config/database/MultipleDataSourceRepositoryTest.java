package product.common.config.database;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import product.domain.book.Book;
import product.domain.book.BookRepository;
import product.domain.store.StoreRepository;


@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {EditableDatabaseConfig.class, ReadOnlyDatabaseConfig.class})
@TestPropertySource("/application-datasource.yml")
@DataJpaTest
public class MultipleDataSourceRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void contextLoads() {
    }
}


//    TODO - Study - Test 환경에서 실행
//    1. Configuration 패키지/네이밍 정리
//    2. readable한 store를 가저와서 에약(book)을 만드(editable)는 케이스
//    3. editable한 book 을 가지고 트랜잭션 나오는 케이스

//  TODO - next Week
//  1. db 환경 설정하기 - docker
//  2. 일단은 간단하게 계정 베이스로 readonly / editable 나눠서 테스트
//  3. 간단한 api 만들기
//  4. unit test
//  5. end-to-end test