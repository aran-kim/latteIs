package site.LatteIs.latteIs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //JPA Auditing 활성화
@SpringBootApplication //@SpringBootApplication으로 인해 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정
//특히 @SpringBootApplication이 있는 위치부터 설정을 읽기 때문에 이 클래스는 항상 프로젝트의 최상단에 위치해야 함
public class LatteIsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LatteIsApplication.class, args); //내장 WAS(Web Application Server)를 실행
		//내장 WAS: 별도로 외부에 WAS를 두지 않고 애플리케이션을 실행할 때 내부에서 WAS를 실행하는 것
		//항상 서버에 톰캣을 설치할 필요가 없고 스프링 부트로 만들어진 Jar파일로 실행 가능
	}

}
