> 네이버통장 개발팀의 인턴으로 2개월간 근무한 것을 기록한 레포입니다. 

# 본 과제 - 예약입금 API서버, Batch서버 구축 👩🏻‍ 💻
네이버 통장의 예약입금이란?
 - 잔액 기준) 네이버 통장의 잔액이 **지정 금액 이하**일 경우 외부통장으로부터 **지정 금액을 입금**
   - `"잔액이 10만원 이하일 때 30만원을 입금해줘"`
 - 지정일 기준) 매 월 **지정일에** 외부통장으로부터 **지정 금액을 입금** 
   - `"매 월 14일에 30만원을 입금해줘"`

<br/>

## 유즈 케이스
![image](https://user-images.githubusercontent.com/48276751/135598331-fe3ceb11-41b4-40b0-9fad-c14141f99435.png)


## 세부 기능
### API 서버
- [명세](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/17)

|     기능 명      |                         기능 설명                         |
| -------------- | ------------------------------------------------------- |
| 예약입금 신규추가  | 잔액 기준 예약입금 추가 <br> 지정일 기준 예약입금 추가 |
| 예약입금 수정 |             지정 잔액과 입금금액을 변경 <br> 지정일과 입금금액을 변경              |
| 예약입금 조회 |          사용자가 보유한 예약입금을 전체 조회       |
| 예약입금 중지 |             활성화 상태인 예약입금의 상태를 비활성화로 변경             |
| 예약입금 삭제 |             예약입금을 삭제             |
| 네이버 통장 인출 |             단순히 네이버통장의 금액만 차감            |
| 네이버 통장 입금 |            단순히 네이버통장의 금액만 증가           |

### 배치
- [명세](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/33)
* 입금 실행이란? 네이버통장의 잔액 증가, 외부통장의 잔액 감소
* 실제론 `외부통장의 잔액`를 건드리지 않지만, 해당 프로젝트에서는 차감시킴.

|     기능 명      |                         기능 설명                         |
| -------------- | ------------------------------------------------------- |
| 잔액 기준 예약입금 Job  |  잔액 기준 예약 입금 실행 ➡️ 입금 내역 기록 |
| 지정일 기준 예약입금 Job |              지정일 기준 예약 입금 실행 ➡️ 입금 내역 기록             |


<br/>

## DB 
- [명세](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/18)
### DB configuration
```properties
spring.datasource.url=[jdbc:mysql://ip:port/database]
spring.datasource.username=[root]
spring.datasource.password=[password]
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### DB Modeling
- 동일한 예약입금 추가를 방지
  - 하나의 네이버통장에서 지정잔액과 입금금액의 조합은 고유합니다. ` 5만원 미만일 경우 10만원 입금` 과 `5만원 미만일 경우 15만원 입금`은 동시에 존재할 수 없습니다. 
  - 하나의 네이버통장에서 지정일과 입금금액의 조합은 고유합니다. ` 15일에 10만원 입금` 과 `15일에 15만원 입금`은 동시에 존재할 수 없습니다. 

<img src = "https://user-images.githubusercontent.com/48276751/132603457-eaa9c77c-158a-40be-bc1b-10745d7081bd.jpg" width="50%">


<br />


## 배치 Job 구조도
### 공통 제한 사항
- 외부 통장의 잔액이 입금금액(deposit_amount)이상인 지(= 출금 가능한 지)
  - `30만원 있는데 50만원 출금 불가`
- 네이버통장의 최대 보유 가능 금액 미만인 지
  - ` 보유 가능 금액인 1억을 초과하는 지 `

### 1. 지정 잔액 기준
- 제한 사항
  - 네이버통장의 잔액이 지정 잔액보다 적은 지
    - `잔액이 10만원 미만일 때 예약입금을 설정했는데 잔액이 40만원으로 더 큰 경우`

- 구조도
  - <INPUT, OUTPUT> = <예약 입금, 네이버통장 & 입금 내역>
![image](https://user-images.githubusercontent.com/48276751/134812235-d8ddd26c-2cfc-4633-99d1-e7c7f3cc08ef.png)



### 2. 지정 일 기준
- 제한 사항
  - 오늘 날짜 `2021-09-20`라면 20일로 지정된 예약입금들만 확인 
 
- 구조도
  - 말일 이후로 설정된 예약 입금은 말일에 일괄 처리
    - 2월 같은 경우 28일까지밖에 없으므로, 29,30,31일의 예약 입금을` 일괄 처리
    - 4,6,9,11도 마찬가지로 30일에 30,31일의 예약 입금을 일괄 처리 

<img src = "https://user-images.githubusercontent.com/48276751/134811667-23e25316-a8ac-4cdc-b615-24a943e6c7be.png" width="60%">


<br/>

# 로깅
- 사내 로깅 시스템인 NELO로 로그 전송 

# 빌드 & 배포
- 젠킨스로 빌드 후, 사내 배포 시스템을 이용해 서버에 배포 

### 스터디
- [DI & IoC컨테이너](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/11)
- [의존 관계 주입 방식](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/12)
- [Bean](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/13)
- [예외처리](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/21)
- [JPA 영속성 컨텍스트](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/23)
- [스프링 배치](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/32)
- [스프링 배치 메타테이블](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/34)

### 문제 해결
- [CORS 이슈](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/29)
- [프로젝트를 모듈 별로 나누는 과정에서 겪은 이슈1](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/36)
- [프로젝트를 모듈 별로 나누는 과정에서 겪은 이슈2](https://github.com/una97/INTERNSHIP_NAVER_FINANCIAL/issues/37)

## 추가적으로 사용한 것들
- Swagger (API 명세 자동화)
- VisualVM (배치 프로파일링용)
  - 멀티 스레드 스텝

## 기술스택
* AdoptopenJDK1.8
* SpringBatch
* SpringBoot
* MySQL
* 사내 플랫폼
  * 사내 VM 발급, Jenkins + NDeploy 배포
  * NELO (사내 로깅 서비스)
  
## Version
* Spring Boot: 2.5.3
* Gradle: 7.1.1
