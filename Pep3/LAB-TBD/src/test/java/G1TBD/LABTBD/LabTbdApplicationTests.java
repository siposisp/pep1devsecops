package G1TBD.LABTBD;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestConfig.class})
class LabTbdApplicationTests {

    @Test
    void contextLoads() {
        // Este test solo verifica que el contexto se puede cargar sin errores
    }
}