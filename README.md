#Spring boot REST API
1. meaven 명령어
   * compile : java 클래스 파일 생성
   * package : compile -> jar 파일 생성
   * install : 배포

2. SpringBoot 동작 원리
   * DispatcherServlet : 사용자 요청을 처리하는 게이트웨이
     * 클라이언트 요청을 제일 앞에서 받아서 처리합니다. 요청에 맞는 Handler로 전달하여 실행 결과를 HttpResponse형태로 만들어서 반환
   ~~~
   DispatcherServlet -> HanlderMapping -> Controller -> Http HttpResponse
   ~~~
   
3. static 블럭 인스턴스 블럭
   * 실행순서 :  클래스 로딩 -> 클래스 변수 메모리 생성 -> static 순서 실행
   * 주로 클래스 초기화시 사용 인스턴스도 동일한 기능 블럭이 제일 먼저 호출
      ~~~
       static {
           users.add(new User(1,"gunho",new Date()));
           users.add(new User(2,"minho",new Date()));
           users.add(new User(3,"jinho",new Date()));
       }
      ~~~

4. ServletUriComponentsBuilder 클래스
   1. Rest Api 를 구현할때 ServletUriComponentsBuilder클래스를 통해 원하는 값을 클라이언트에게 전달할수있다.
   ~~~
   URI location =  ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).build();
   ~~~
   * ServletUriComponentsBuilder.fromCurrentRequest() 통해 사용자 요청 URI 를 가져온후 path를 통해 원하는 정보(변수) buildAndExpand(변수값) 을 넣어 ResponseEntity를 통해
   클라이언트에게 Location에 응답값을 통해 알려줄수있다. ResponseEntity를 created라는 메서드로 반환하였으므로 응답코드도 201이 호출되는것을 볼수있다.
   같은 메서드 사용하지 말고 메서드에 맞는 반환코드를 클라이언트에게 알려주자

5. 예외클래스 위에 @ResponseStatus 어노테이션을 통해 오류 코드를 바꾸어 보여줄수 있다.
~~~
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
~~~

6. ResponseEntityExceptionHandler
   * @ControllerAdvice   // 모든 컨트롤러가 실행시 해당 어노테이션을 가지고 있는 빈이 실행됩니다.
   * 클래스를 해당 ResponseEntityExceptionHandler 상속받으므 에러가 발생되면 해당 클래스 빈에 에러메시지가 생성이됩니다.
   ~~~
    //exceptionhandler 사용 여부와 어떤 에러가 발생하면 사용할지 선택
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
   ~~~
   
7. 다국어 지원 API 
   * 다국어를 사용할 properties 문서를 정의한다. (messages 라는 properties에 정의)
   ~~~
     spring:
       message:
       basename: messages
   ~~~
   * Bean을 지정해 다국어 default값설정
* ~~~
  	@Bean
	public LocaleResolver localeResolver () {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.KOREA);
		return localeResolver;
	}
  ~~~
  * 다국어 사용할 api메서드 설정
      * MessageSource 클래스를 사용하여 다국어 설정을 한다.
      * header 에 Accept-Language를 통해 나라별 호출값을 받아 맞게 호출한다.
    ~~~
    greeting.message -> properties에 설정한 응답값
    
    @GetMapping(path = "/hello-world-internationalized")
    public String hellWorldInternationalized(@RequestHeader(name = "Accept-Language",required=false) Locale locale) {
        log.info("country {} ",locale.getCountry());
        return source.getMessage("greeting.message",null,locale);
    }
    ~~~
  
8. API filtering
   * 외부에 노출시키고 싶지 않은 값이 있다면 방법
     * Jackson lib 이용
       1. 필드에 두는 방법
       2. 클래스에 두는 방법
     ~~~
     @JsonIgnore
     private String password;
     
     @JsonIgnoreProperties(value = {"password","ssn"})
     public class User {
     ~~~
   * 관리자에겐 사용자에 정보를 모두 보여주고 사용자에겐 부분적으로 보여주고 싶다면
   ~~~
   DTO 에 jsonFilter를 사용할껄 명시하고 컨트롤러에서 filter를 이용하여 조ㄱ
   
   @JsonFilter("UserInfo")
   public class User {
   
    ctr에 응답값도 MappingJacksonValue 변형
   @GetMapping
   public MappingJacksonValue retrieveUser (@PathVariable int id) {
   
   SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id","name","joinDate","ssn"); //전달할 응답값 id값
   //위에 만든 필터를 적용할떄 어떤 빈에 적용할지 id 값을 넣어야 한다.
    FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        //Object json 응답값으로 변형
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
    return mapping;
   ~~~
* BeanUtils -> Bean관련 util 라이브러리

9. version 관리
   1. 파라미터와 pathvariable 로 가능하다.
   ~~~
   @GetMapping("/v1/users/{id}")
   @GetMapping(value = "/users/{id}/", params = "version=1")
   ~~~
   2. header로 관리
   ~~~
   @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1")
   ~~~
   3. produces minetype이용 방법
   ~~~
   @GetMapping(value = "/v1/users/{id}" , produces = "/application/vnd.company/appv2+json")
   ~~~

10. HATEOAS (Hypermedia As the Engine Of Applcation Services)
    * 현재 호출된 자원의 상태 정보 제공
    스프링 버전에 따른 클래스
    * 2.18 : Resource
             ControllerLinkBuilder
    ~~~
    //HATEOAS
        Resource<User> resource = new Resource<>(user);
        ControllerLinkBuilder linkTo = ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));
    ~~~
    * 2.2 : Resource -> EntityModel
            ControllerLinkBuilder ->  WebMvcLinkBuilder
    ~~~
    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel<User>> retrieveUser (@PathVariable int id) {
    //HATEOAS
        EntityModel entityModel = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(linkTo.withRel("all-users"));
        return ResponseEntity.ok(entityModel);
    ~~~
    
11. swagger
    * Docket 클래스로 swagger Config파일을 만들 수 있다.
      * http://localhost:8088/v2/api-docs / http://localhost:8088/swagger-ui/index.html
      * 만들어진 swagger 에 json 형식이나 swagger document 확인할 수 있다.
      * api 문서는 Docket class 에서 정의가 가능하다.
      * 도메인에 대한 api 정의는 도메인 객체 클래스에서 @ApiMode description 추가가 가능하다

12. API 모니터링
    ~~~
    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    ~~~
    * dependency 추가로만으로도 어떤 API가 구동되는지 알 수 있다.
    ~~~
    추가로 더 많은 정보를 확인 할 수 있다.
    management:
    endpoint:
    web:
    exporsure:
    include: "*"
    ~~~
    
13. HAL(Hypertext Application Language)
    * hypertext 로 api 부가적 기능 제공

14. security
    * 패스워드 자동 생성 됨 : fae0a308-073c-4651-9daa-7beac9185e2e
    * security 설정후 api 메서드 이용시 에러가 남 헤더값에 인증값 입력
    * Authorization Basic Author name password 입력
    * 자동생성이 아닌 아이디와 패스워드 설정후 입력 하는법
    * yml 이용
    ~~~
    spring:
    message:
    basename: messages
    security:
    user:
    name: admin
    password: admin
    ~~~
    * yml 설정 파일 이용하는건 너무 번거롭고 효율적이지 못한다. 설-정 파일을 통해 인메모리 방식 데이터베이스 방식 로그인으로 사용 할 수 있다.
    ~~~
    @Configuration
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
        //AuthenticationManagerBuilder jdbc 관련 인증 또는 메모리를 통한 인증방식 사용 가능
        //.password("{12345") <-pliain Text 에러 발생 Encoding 없이 사용한다고 명시해야 에러가 안납니다.
        auth.inMemoryAuthentication()
                .withUser("gunho")
                .password("{noop}12345")
                .roles("USERS"); //login시 사용 권한
    }
    ~~~
    * security 를 사용시 디비 접근 등이 어렵다 -> 권한을 풀어야함
    1. JPA
       * 자바 ORM 기술 API 명세 (인터페이스)
       * Hibernate : JPA 구현체 인터페이스 직접 구현한 라이브러리
       * Spring Data JPA : JPA를 추상한  Repository Module
       ISSUE
         * ~.sql 파일을 실행하려 하는데 에러가 발생 **Spring boot 2.5x 이후부턴 추가 필요**
           -> application.yml(또는 properties)에 spring.jpa.defer-datasource-initialization 옵션 값을 true로 추가
         ~~~
         jpa:
             defer-datasource-initialization: true
         ~~~
    2. JPA 통한 hateoas
    ~~~
    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel> retrieveUser(@PathVariable int id){
    Optional<User> user = userRepository.findById(id);
    user.orElseThrow(() -> new UserNotFoundException(String.format("ID{%s} not found",id)));
    EntityModel<User> resource = EntityModel.of(user.get());
    WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).findAll());
    resource.add(linkTo.withRel("all-User"));
    return ResponseEntity.ok(resource);
    }
    ~~~