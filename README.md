# Today? Tomorrow!
이젠 아카데미 프로젝트 Team.재정지원  
트러블 슈팅 정리

---
손지아 트러블 슈팅

<br>

<details>
<summary>1. BkBoardService 유닛 테스트 진행 도중 CustomExceptionHandler에 문제 발생</summary>
<p>

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

---

### CustomExceptionHandler 의존성 주입 문제

<strong>문제 상황</strong> :  
CustomExceptionHandler 의존성 주입이 안 되고 있어서 그 안에 있는 메서드에 진입하지를 못한다.

<strong>시도해본 해결법</strong> :  
1. 애플리케이션 클래스에 @ComponentScan 어노테이션 적용해보기
<br /> > 권한이 필요하지 않은 경로(회원가입, 로그인 등) 요청에서 401 Unauthorized 에러 발생. 어노테이션 붙이기 전에는 없던 에러이다.  
<br /> >> 시큐리티 설정한 부분에 권한 관련한 코드(authenticationEntryPointException)에서 걸리는 것으로 추정. 그런데 어노테이션을 붙이자마자 이렇게 되는 이유가 뭐지?

<strong>결론</strong> :  
CustomExceptionHandler에는 아무런 문제가 없었다.  
예외 처리를 하기 바로 윗 부분에 repository에서 게시글 객체를 찾는 부분이 있는데, 존재하지 않는 게시글을 꺼내려고 할 때 에러가 발생하며 애초에 메서드에 진입할 여지가 없던 것이었다.  
지금은 애초에 Optional<BkBoard> 객체로 꺼내온 후, 이후에 isPresent()를 사용해 안에 값이 존재하는지 그 여부를 따지는 것으로 수정했다.  
이미 처음과 같이 작성되어 있는 곳이 많이 있기 때문에 전체적으로 수정에 들어가야겠다.

</p>
</details>
<details>
<summary>2. BkBoardService 유닛 테스트 중 페이징 처리 테스트 관련</summary>
<p>

<strong>문제 상황</strong> :    
테스트를 하며 member 객체에 List<BkBoard>값을 넣어주었음에도 불구하고 bkBoardPage 값이 null로 반환되는 상황

<br>

<strong>시도해본 해결법</strong> :  
구글링 결과 Pageable객체를 given에서 따로 설정을 해줘야 한다는 설명을 발견했다.  

![img.png](img/img_6.png)  
설명에 따라 given 영역을 다시 설정했다.  
매개변서로 쓰일 page와, member를 선언했는데 이때 member는 setter를 사용했다.  
그 이유는 List<Board>에는 member필드가 존재하고 이를 채워준 다음에 member에 게시글 리스트 객체를 담아주기 위함이었다.  
이후, Pageable과 Page<BkBoard> 역시 각각 따로 만들어주고 테스트를 진행했다.

<br>

<strong>결론</strong> :  
이 해결법이 정답이었다.  
아무래도 서비스단을 살펴본 다음, 기본적으로 준비되어야할 모든 객체를 given에서 작성해줘야 하는 듯하다.

</p>
</details>

<details>
<summary>3. MypageService 유닛 테스트 중 passwordEncoder.encode 적용 안 됨</summary>
<p>

<strong>문제 상황</strong> :  
![img.png](img/img_7.png)  
~~분명히 passwordEncoder.encode를 사용해 비밀번호를 세팅해줬음에도 불구하고 결과값이 null로 반환되는 것이 확인되었다.  
원인을 찾아보니 Member 클래스 내부, password 필드에 @JsonIgnore 처리가 되어있기 때문이었다.~~

![img.png](img/img_8.png)  
출력문을 통해 다시 확인해보니 passwordEncoder.encode 자체가 동작을 안 하는 것 같다.

<br>

<strong>시도해본 해결법</strong> :  
1. @JsonIgnoreTest 어노테이션 생성  
@JsonIgnore 어노테이션이 테스트 시에만 무시되도록 해야겠다는 생각이 들었다.  
@JsonIgnoreTest 라는 어노테이션(@JsonIgnore 어노테이션을 무시)을 새로 만들어 적용해봤으나...  
어노테이션을 사용하는 위치가 잘못된 것인지 영속성 에러가 발생하며 테스트가 진행되지 못했다.

2. @SpringBootTest, @Autowired 어노테이션을 통한 의존성 주입
@Autowired을 사용해 PasswordEncoder 의존성 주입을 하기 위해 클래스에 @SpringBootTest 어노테이션을 추가했다.  
그리고 실행해본 결과...  
![img.png](img/img_9.png)  
PasswordEncoder가 동작한다!

<br>

<strong>결론</strong> :  
PasswordEncoder는 인터페이스이기 때문에 @InjectMocks 어노테이션을 사용할 수 없다.  
그렇다면 직접적으로 의존성을 주입해주는 수밖에는 없고, 유닛 테스트에서 @Autowired를 사용하려면 클래스 자체에 @SpringBootTest 어노테이션을 추가해줘야 한다.

---

<strong>문제 상황</strong> :  
passwordEncoder.matches가 동작을 안 한다...  
정확히는 updatePassword 메서드 내부 로직인

```java
if(!passwordEncoder.matches(request.getPassword(), oldPassword)){
    log.info("비밀번호 불일치");
    return ResponseEntity.ok(HttpServletResponse.SC_BAD_REQUEST);
}
```
에서 조건문이 동작을 안 한다.  
디버깅을 돌려본 결과,
![img.png](img/img_10.png)  
위와 같이 passwordEncoder 자체가 null인 것으로 확인되었다.
돌고 돌아 다시 의존성 주입 문제...

<br>

<stron>시도해본 해결법</strong> :  
MypageServiceTest 클래스에서 MypageService 인스턴스 생성때 @InjectMocks 어노테이션을 사용하고 있었다.  
이 과정에서 PasswordEncoder에 대한 의존성 주입이 원활이 이루어지지 않았을 가능성이 보였다.  
그래서 의존성 주입에 조금 더 권장되고 있는 방법인 생성자 주입으로 의존성 주입의 방법을 변경해보았다.

<br>

<strong>결론</strong> :  
해결됐다!!!!  
편하다는 이유로 필드 주입 방법을 선택하고 있었는데, 다음부터는 뭐가 안 된다 싶으면 바로 생성자 주입 방식으로 변경해봐야겠다.

</p>

</details>