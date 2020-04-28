# SpringBoot Web 구조 및 정리
```
'스프링 부트와 AWS로 혼자 구현하는 웹 서비스' 를 읽으며 정리한 글
```
## 1. WEB project 구조
- 최상위 패키지 com.000.000.springboot
   - Application.java와 같이 @SpringBootApplication 으로 프로젝트의 메인 클래스를 생성함
   - 스프링 부트의 자동 설정, Bean 읽기나 생성등을 모두 자동으로 함
   - 내장WAS를 실행시켜준다
   - SpringBoot는 해당 위치부터 설정을 읽어가므로 항상 최상위에 위치

- web : (API) 요청을 받을 Controller와 관련된 클래스들

- web/dto : Request 데이터를 받을 Dto 관련 클래스들(계층 간에 데이터 교환을 위한 객체)

- domain : 비즈니스 로직 등 문제 해결을 위한 개발 대상 구역. DAO +@. 실제 DB의 테이블과 매칭될 Entity클래스, Entity와 전용으로 매칭될 JPA Interface 등.
  - JPA에서는 Repository가 DAO. 인터페이스 생성후 extends JpaRepository<Entity클래스, PK타입> 으로 상속하면 기본 CRUD가 자동 생성됨.
  - Entity 클래스와 기본 Entity Repository는 함께 위치해야 함.

- service : 트랜잭션, 도메인 기능 간의 순서를 보장한다.

0. Layer : Web, Service, Repository, Dto, Domain
1. Controller : Request(+param)을 받음. Dto에 Param을 매핑하여 return Service.save();
2. Service : Controller로부터 DTO 객체를 인자로 받고 return Repository.save(DTO.toEntity().getSomething()); toEntity를 이용해 Build 하여 Entity에 일치시킨다.
3. 
<!-- - Entity 클래스 :  -->

## 2. @Annotations / Test

- @SpringBootApplication : SpringBoot 프로젝트의 메인 클래스가 되어 자동 설정, 스프링 Bean 읽기/생성을 자동으로 설정한다. 내장WAS를 실행시키며, 해당 위치부터 설정을 읽어가므로 항상 프로젝트 최상단 패키지에 위치해야 한다.

- @RestController : 해당 클래스를 JSON을 반환하는 컨트롤러로 만들어 준다. (예전 @ResponseBody를 각 메소드마다 선언했던 것을 한 번에)

- @GetMapping("/hello") : HTTP Method인 (/hello) Get의 요청을 받을 수 있는 API를 만들어 준다. (예전 @RequestMapping(method = RequestMethod.GET)임.)

- @Autowired : 스프링이 관리하는 Bean을(Getter/Setter) 주입 받는다.

- @Getter : 선언된 모든 필드의 get 메소드를 생성해 준다.

- @RequiredArgsConstructor : 초기화 되지 않은 **final** 필드와 @NonNull 어노테이션이 붙은 필드에 대한 '생성자'를 생성해 준다.

- @AllArgsConstructor : 모든 필드에 대한 '생성자'를 생성해 준다.

- @NoArgsConstructor : 기본 생성자 == public Posts(){}

- @RequestParam("name") String name : 메서드의 인자로서, 외부에서 API로 넘긴 파라미터를 가져온다. == url뒤에 붙는 Parameter.  
e.g. http://127.0.0.1/posts?index=1&name=kim 여기서 posts는 @GetMapping("posts") index나 name은 @RequestParam

- @RequestBody : 파라미터에 사용하면 HTTP Request의 본문 body 부분이 그대로 자바 객체로 전달됨.

- @PathVariable : @GetMapping("/api/v1/posts/{id}") 같은 REST API 호출의 경우에서 {id}를 인자로 가져올 때 사용

- @Entity : 테이블과 연결할 클래스.   
e.g. SalesManger.java -> sales_manager table 식으로 매칭한다.

- @Id : 해당 테이블의 PK 필드를 의미한다.

- @GeneratedValue(strategy = GenerationType.IDENTITY) : PK의 생성 규칙을 나타내며 부트2.0에서는 (strategy = GenerationType.IDENTITY)를 추가해야 auto_increament가 됨.

- @Column : 클래스의 필드는 모두 테이블의 칼럼이 되지만 명시하여 임의로 필요한 옵션을 사용할 수 있다.   
e.g. (length =500, nullable = false, columnDefinition = "TEXT")

- @Builder : 생성자에서 받는 인자를 명확하게 명시한다. 해당 클래스의 빌더 패턴 클래스를 생성. 생성자 상단에 선언시에는 생성자에 포함된 필드만 빌더에 포함. 

- postsRepository.save() : 테이블 posts에 id값이 있다면 update, 없다면 insert 쿼리를 실행

- postsRepository.findAll() : 테이블 posts에 있는 모든 데이터 조회해 옴





### Test용

- @Test : Test를 할 메서드 명시

- @SpringBootTest : @SpringBootApplication을 찾아 테스트를 위한 빈을 생성함. @RunWith와 같이 써야 함. H2 DB를 자동으로 실행하여 테스트.

- @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) : 실제 가용한 포트로 내장톰캣을 띄우고 응답을 받아서 테스트를 수행한다.

- @RunWith(SpringRunner.class) : 테스트 진행시 스프링부트 테스트와 JUnit사이 연결자 역할을 하여 JUnit 내장 실행자가 아닌 Spring 실행자를 사용.

- @WebMvcTest(controllers = HelloController.class) : Web(Spring MVC)에 집중하게 하여 @Controller, @ControllerAdvice를 사용할 수 있다. 외부 연동 관련 부분만 활성화 하고 JPA 기능은 작동하지 않아 테스트가 불가능.

- private MockMvc mvc : 웹 API 테스트시 사용하며 스프링 MVC 테스트의 시작점이다.

- mvc.perform(get("/hello")) : MockMvc를 통해 /hello 주소로 HTTP GET 요청을 함. 체이닝이 지원되어 이어서 선언한다.

- .andExpect(status().isOK()) : perform의 결과를 검증한다. HTTP Header의 Status가 200, 404, 500 등인지. OK == 200

- .andExpect(content().string(hello)) : 응답 본문의 내용중 hello를 포함하여 return 되는지 검증한다.

- .param("name", name) : API 테스트시 사용될 요청 파라미터를 설정한다. String만 허용되어 String.valueOf(someNumber) 식으로 변경해야 함

- org.assertj.core.api.Assertions.assertThat : 검증하고 싶은 대상을 메소드 인자로 받고 메소드 체이닝으로 검증한다.

- isEqualTo : 동등 비교하여 검증한다.

- .andExpect(jsonPath("$.name", is(name))) : JSON 응답값을 필드별로 검증한다. $를 기준으로 명시하므로 name을 검증하려면 $.name

- @After : JUnit에서 단위테스트가 끝날 때마다 수행되는 메소드를 지정함. 테스트간 데이터 침범을 막기 위해 사용  
e.g. ```public void cleanup(){ postsRepository.deleteAll(); }```

- TestRestTemplate : Rest방식 API 테스트용

```
 ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
 ```
-  - ResponseEntity : StatusCode + Body를 반환한다.
   - postForEntity : POST 요청을 보내고 결과로 ResponseEntity를 반환받는다 (String url, Object request, Class<T> responseType) 

```
= restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
```
- - restTemplate.exchange : HTTP 메소드로 요청받은 내용 requestEntity을 수행(업데이트)

