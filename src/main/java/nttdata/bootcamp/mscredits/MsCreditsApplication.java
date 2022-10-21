package nttdata.bootcamp.mscredits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MsCreditsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCreditsApplication.class, args);
	}

}
