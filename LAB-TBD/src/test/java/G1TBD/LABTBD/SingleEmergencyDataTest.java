package G1TBD.LABTBD;
import G1TBD.LABTBD.data.SingleEmergencyData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleEmergencyDataTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        SingleEmergencyData data = new SingleEmergencyData();
        data.setEmergencyTitle("Incendio");
        data.setVolunteerQuantity(5);
        data.setTaskQuantity(2);

        assertEquals("Incendio", data.getEmergencyTitle());
        assertEquals(5, data.getVolunteerQuantity());
        assertEquals(2, data.getTaskQuantity());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        SingleEmergencyData data = new SingleEmergencyData("Terremoto", 10, 4);

        assertEquals("Terremoto", data.getEmergencyTitle());
        assertEquals(10, data.getVolunteerQuantity());
        assertEquals(4, data.getTaskQuantity());
    }

    @Test
    void testEqualsAndHashCode() {
        SingleEmergencyData data1 = new SingleEmergencyData("Aluvión", 3, 1);
        SingleEmergencyData data2 = new SingleEmergencyData("Aluvión", 3, 1);

        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());
    }

    @Test
    void testToString() {
        SingleEmergencyData data = new SingleEmergencyData("Incendio", 2, 1);
        String str = data.toString();
        assertTrue(str.contains("emergencyTitle=Incendio"));
    }

    @Test
    void testEqualsAndHashCodeExtraBranches() {
        SingleEmergencyData data1 = new SingleEmergencyData("Aluvión", 3, 1);
        assertEquals(data1, data1);
        assertNotEquals(data1, null);
        assertNotEquals(data1, "string");
        SingleEmergencyData dataDifferent = new SingleEmergencyData("Incendio", 3, 1);
        assertNotEquals(data1, dataDifferent);
    }

    @Test
    void testEquals() {
        SingleEmergencyData data1 = new SingleEmergencyData("Aluvión", 3, 1);
        SingleEmergencyData data2 = new SingleEmergencyData("Aluvión", 3, 1);
        SingleEmergencyData data3 = new SingleEmergencyData("Incendio", 4, 2);

        assertTrue(data1.equals(data2));  // Mismos datos, deben ser iguales
        assertFalse(data1.equals(data3)); // Diferentes datos, deben ser diferentes
    }


    @Test
    void testToString_() {
        SingleEmergencyData data = new SingleEmergencyData("Aluvión", 3, 1);
        String expectedString = "SingleEmergencyData(emergencyTitle=Aluvión, volunteerQuantity=3, taskQuantity=1)";
        assertEquals(expectedString, data.toString());
    }

    @Test
    void testConstructorWithNulls() {
        // Usamos el constructor vacío y luego configuramos los atributos
        SingleEmergencyData data = new SingleEmergencyData();
        data.setEmergencyTitle("Aluvión");
        data.setVolunteerQuantity(3);
        data.setTaskQuantity(1);

        assertNotNull(data);
    }

    @Test
    void testHashCode() {
        SingleEmergencyData data1 = new SingleEmergencyData("Aluvión", 3, 1);
        SingleEmergencyData data2 = new SingleEmergencyData("Aluvión", 3, 1);
        SingleEmergencyData data3 = new SingleEmergencyData("Incendio", 4, 2);

        assertEquals(data1.hashCode(), data2.hashCode()); // Deben ser iguales
        assertNotEquals(data1.hashCode(), data3.hashCode()); // Deben ser diferentes
    }

    @Test
    void testDifferentVolunteerQuantity() {
        SingleEmergencyData data1 = new SingleEmergencyData("Aluvión", 0, 1);
        SingleEmergencyData data2 = new SingleEmergencyData("Aluvión", 3, 1);

        assertNotEquals(data1, data2);  // Asegúrate de cubrir la diferencia
    }

    @Test
    void testEdgeValues() {
        // Crear objetos con 0 y null
        SingleEmergencyData data1 = new SingleEmergencyData("Aluvión", 0, 0);
        SingleEmergencyData data2 = new SingleEmergencyData("Incendio", 10, 5);

        assertNotNull(data1);  // No puede ser nulo
        assertNotNull(data2);  // No puede ser nulo
        assertNotEquals(data1, data2);  // Son diferentes
    }

    @Test
    void testNoArgsConstructor() {
        // Usar el constructor sin parámetros
        SingleEmergencyData data = new SingleEmergencyData();
        assertNotNull(data);  // El objeto no debe ser nulo
    }

    @Test
    void testAllArgsConstructor() {
        // Usar el constructor con todos los parámetros
        SingleEmergencyData data = new SingleEmergencyData("Aluvión", 3, 1);
        assertEquals("Aluvión", data.getEmergencyTitle());
        assertEquals(3, data.getVolunteerQuantity());
        assertEquals(1, data.getTaskQuantity());
    }

    @Test
    void testGettersAndSetters() {
        SingleEmergencyData data = new SingleEmergencyData();
        data.setEmergencyTitle("Incendio");
        data.setVolunteerQuantity(10);
        data.setTaskQuantity(5);

        assertEquals("Incendio", data.getEmergencyTitle());
        assertEquals(10, data.getVolunteerQuantity());
        assertEquals(5, data.getTaskQuantity());
    }

    @Test
    void testCombinations() {
        // Probar con valores grandes y negativos si corresponde
        SingleEmergencyData data1 = new SingleEmergencyData("Incendio", Integer.MAX_VALUE, Integer.MAX_VALUE);
        SingleEmergencyData data2 = new SingleEmergencyData("Aluvión", Integer.MIN_VALUE, Integer.MIN_VALUE);
        SingleEmergencyData data3 = new SingleEmergencyData("Tsunami", 0, 0);

        assertNotNull(data1);
        assertNotNull(data2);
        assertNotNull(data3);
        assertNotEquals(data1, data2);  // Comparar objetos con diferentes datos
    }
    
}
