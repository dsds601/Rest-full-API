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