package com.nanumi.board_give;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}

/*
 Spring Boot의 주요 특징 중 하나는 컴포넌트 스캔과 자동 구성을 지원하여, 애플리케이션의 다양한 부분에서 클래스를 찾고 사용할 수 있도록 하는 것입니다. 따라서 BoardApplication 클래스가 어떤 패키지 위치에 있어도 상관없습니다.

Spring Boot는 클래스 패스 내에 있는 모든 클래스를 검색하고 처리합니다. @SpringBootApplication 어노테이션이 부여된 클래스를 기준으로 시작하여, 이 클래스가 위치한 패키지와 하위 패키지들에서 컴포넌트를 스캔하여 빈(bean)으로 등록하고 구성합니다.

일반적으로 BoardApplication 클래스는 최상위 패키지에 위치하는 것이 일반적입니다. 하지만 Spring Boot는 클래스의 패키지 위치에 구애받지 않고 동작하기 때문에, 필요에 따라 다른 패키지에 위치시켜도 잘 작동할 것입니다.

예를 들어, 다음과 같은 패키지 구조를 가지고 있다고 가정해보겠습니다:


 */
