package teamficial.teamficial_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TeamficialBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamficialBeApplication.class, args);
	}

}
