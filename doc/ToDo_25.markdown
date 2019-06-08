* DataSource 분리  (Annotation 기반)  
  > 레퍼런스는 아직 못 찾아서, 삽질 예정 ...  
  
  * ~.~.~.common.config 에 넣어놓고, ~.~.~.product.* 이랑 ~.~.~.book.* 에서 의존하는 형태로 쓸 예정  
    * book 에서는  

  * 이 걸 가지고, elasticsearch / MySQL - Master / MySQL -Slave 등의 역할을 갖는 레파지토리별로 설정 충돌 안나게 관리 하고 싶음  
    > elasticsearch 는 spring.data.elasticsearch 베이스로  
    > 그치만 인터페이스는 java.persistence 계열로 맞추면서 ...  

  * repository 역할별로 Data Access 설정 분리 (Master - CUD / Slave - ReadOnly)
    > DataSource 나 DataSourceTransactionManager 이런 애들

  * Configuration  
  
    * JPA Repository 관련 스캔 설정용 어노테이션 찾기(ex] org.mybatis.spring.annotation.MapperScan 같은 역할)  
      > target 어노테이션 지정 / 스캔할 패키지나 클래스 syntax  
    * @Configuration SampleConfiguration implements EnvironmentAware 이런 식이었으면 좋겠음  
      > @ConfigurationProperties + 각각의 DB 접근 관련  

    * Sample Annotation  
        ~~~java  
        @Documented  
        @Inherited  
        @Retention(RUNTIME ... )  
        @Target({ TYPE, METHOD, FIELD, PARAMETER .... })  
        public @interface SampleAnnotation {  
        }  
        ~~~  
        
        @ADBRepository
        ARepository
        
        @BDBRepository
        BRepository



