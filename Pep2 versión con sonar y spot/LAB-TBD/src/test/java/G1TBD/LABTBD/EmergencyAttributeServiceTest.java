package G1TBD.LABTBD;

import G1TBD.LABTBD.entities.EmergencyAttributeEntity;
import G1TBD.LABTBD.repositories.EmergencyAttributeRepository;
import G1TBD.LABTBD.services.EmergencyAttributeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmergencyAttributeServiceTest {

    @Mock
    private EmergencyAttributeRepository emergencyAttributeRepository;

    @InjectMocks
    private EmergencyAttributeService emergencyAttributeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        emergencyAttributeService.create(1L, 2L, true);
        verify(emergencyAttributeRepository, times(1)).create(1L, 2L, true);
    }

    @Test
    void testCreateVarious() {
        EmergencyAttributeEntity ea1 = new EmergencyAttributeEntity();
        ea1.setEmergency_attribute_id(1L);
        ea1.setEmergency(10L);
        ea1.setAttribute(20L);
        ea1.setCompatibility(true);

        EmergencyAttributeEntity ea2 = new EmergencyAttributeEntity();
        ea2.setEmergency_attribute_id(2L);
        ea2.setEmergency(11L);
        ea2.setAttribute(21L);
        ea2.setCompatibility(false);

        List<EmergencyAttributeEntity> inputList = Arrays.asList(ea1, ea2);

        List<EmergencyAttributeEntity> result = emergencyAttributeService.createVarious(inputList);

        verify(emergencyAttributeRepository).create(10L, 20L, true);
        verify(emergencyAttributeRepository).create(11L, 21L, false);

        assertEquals(2, result.size());
        assertEquals(ea1, result.get(0));
    }

    @Test
    void testUpdate() {
        EmergencyAttributeEntity entity = new EmergencyAttributeEntity();
        entity.setEmergency_attribute_id(1L);
        entity.setEmergency(10L);
        entity.setAttribute(20L);
        entity.setCompatibility(true);

        emergencyAttributeService.update(entity);

        verify(emergencyAttributeRepository, times(1))
                .update(1L, 10L, 20L, true);
    }

    @Test
    void testGetAll() {
        EmergencyAttributeEntity ea1 = new EmergencyAttributeEntity();
        ea1.setEmergency_attribute_id(1L);
        ea1.setEmergency(10L);
        ea1.setAttribute(20L);
        ea1.setCompatibility(true);

        EmergencyAttributeEntity ea2 = new EmergencyAttributeEntity();
        ea2.setEmergency_attribute_id(2L);
        ea2.setEmergency(11L);
        ea2.setAttribute(21L);
        ea2.setCompatibility(false);

        List<EmergencyAttributeEntity> expected = Arrays.asList(ea1, ea2);

        when(emergencyAttributeRepository.getAll()).thenReturn(expected);

        List<EmergencyAttributeEntity> result = emergencyAttributeService.getAll();

        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).getEmergency());
    }

    @Test
    void testGetById() {
        EmergencyAttributeEntity entity = new EmergencyAttributeEntity();
        entity.setEmergency_attribute_id(1L);
        entity.setEmergency(10L);
        entity.setAttribute(20L);
        entity.setCompatibility(true);

        when(emergencyAttributeRepository.getById(1L)).thenReturn(entity);

        EmergencyAttributeEntity result = emergencyAttributeService.getById(1L);

        assertEquals(1L, result.getEmergency_attribute_id());
        assertEquals(10L, result.getEmergency());
        assertTrue(result.isCompatibility());
    }

    @Test
    void testDelete() {
        emergencyAttributeService.delete(1L);
        verify(emergencyAttributeRepository, times(1)).delete(1L);
    }
}

