package e_commerce.khilat;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching 
@EnableAsync
public class KhilatApplication {

	public static void main(String[] args) {
		SpringApplication.run(KhilatApplication.class, args);
	    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

	    System.out.println("Timezone = " + TimeZone.getDefault().getID());

	    SpringApplication.run(KhilatApplication.class, args);
	}

}
