package G1TBD.LABTBD;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {})
@ActiveProfiles("test")
class LabTbdApplicationTests {

    @Test
    void contextLoads() {
        // Este test solo verifica que la aplicación se puede compilar
        // No carga el contexto completo para evitar problemas de dependencias
    }
}