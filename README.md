# Today? Tomorrow!
이젠 아카데미 프로젝트 Team.재정지원  
트러블 슈팅 정리

<br>

<details>
<summary>손지아 트러블 슈팅</summary>
<p>

### BkBoardService 유닛 테스트 진행 도중 문제 발생

![img.png](img/img.png)  
게시글 업데이트 메서드에 대해 테스트 코드를 작성하고 있었다.  
성공 케이스에서는 문제가 없었는데, 실패 케이스를 작성하던 중 when 부분의 값이 null로 반환되는 것을 확인할 수 있었다.  
테스트 코드 전문은 아래와 같다.  

![img.png](img/img_1.png)  
![img.png](img/img_2.png)

디버깅을 해보니 서비스단에서 customExceptionHandler 의존성 주입이 되어 있지 않은 것이 확인되었다.  
서비스단에는 이미 @RequiredArgsConstructor 어노테이션이,  
CustomExceptionHandler 클래스에는 @Component 어노테이션이 있는 상태였다.  
실행 클래스에 @ComponentScan(basePackages = "com.ezen.jjjw.exception")를 붙여 다시 의존성을 주입해주었다.  

![img.png](img/img_3.png)  
이후 테스트 코드를 재실행해봤으나...  
여전히 when 부분 responseEntity 값이 null로 뜨는 상황.  
customExceptionHandler에 아예 진입을 못하고 있기도 하고, 생각해보니 그 내부에서 뭔가 비교를 한다던지 검증을 하지 않는 코드였기 때문에 customExceptionHandler를 거치지 않고 바로 ResponseEntity.ok()를 리턴하도록 코드를 수정했다.

![img.png](img/img_4.png)  
그 이후 테스트 결과는?

![img.png](img/img_5.png)  
테스트 통과.  
왜 customExceptionHandler에 진입하지 못했던 건지, 어떻게 하면 진입하도록 할 수 있을지에 대해서는 추후 더 알아봐야겠다.

</p>
</details>