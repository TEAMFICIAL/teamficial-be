package teamficial.teamficial_be;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthTestController {

    @GetMapping("/health")
    public String apiHealthTest() {
        return "hello world";
    }

}
