package com.naverfinance.internproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaAuditing
//JPA Auditing 어노테이션을 모두 활성화할 수 있게 Application클래스에 활성화 어노테이션을 추가
@SpringBootApplication
@RestController // 이 코드가 웹에서 돌아가야 하는 엔드포인트임을 스프링에 알려준다.
public class InternprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternprojectApplication.class, args);
    }

    @GetMapping("/hello")
    //localhost:8080/hello주소로 전송되는 요청에 응답하기 위해 hello()메서드를 사용하게 만듦.
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) { //요청값을 name으로, 없으면 World로
        return String.format("Hello %s!", name);
    }
}

