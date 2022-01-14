package example.springdata.jdbc.howto.selectiveupdate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
class SelectiveUpdateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SelectiveUpdateApplication.class, args);
	}

}
