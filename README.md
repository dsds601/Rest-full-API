#Spring boot REST API
1. meaven 명령어
   * compile : java 클래스 파일 생성
   * package : compile -> jar 파일 생성
   * install : 배포

2. SpringBoot 동작 원리
   * DispatcherServlet : 사용자 요청을 처리하는 게이트웨이
     * 클라이언트 요청을 제일 앞에서 받아서 처리합니다. 요청에 맞는 Handler로 전달하여 실행 결과를 HttpResponse형태로 만들어서 반환 
   * 응답 -> HttpMessageAutoConverter