package G1TBD.LABTBD;

import G1TBD.LABTBD.entities.AttributeEntity;
import G1TBD.LABTBD.repositories.AttributeRepository;
import G1TBD.LABTBD.services.AttributeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttributeServiceTest {

    @Mock
    private AttributeRepository attributeRepository;

    @InjectMocks
    private AttributeService attributeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        AttributeEntity attribute = new AttributeEntity();
        attribute.setAttribute("Test Attribute");

        attributeService.create(attribute);

        verify(attributeRepository, times(1)).create("Test Attribute");
    }

    @Test
    void testUpdate() {
        AttributeEntity attribute = new AttributeEntity();
        attribute.setAttribute_id(1L);
        attribute.setAttribute("Updated Attribute");

        attributeService.update(attribute);

        verify(attributeRepository, times(1)).update(1L, "Updated Attribute");
    }

    @Test
    void testGetAll() {
        List<AttributeEntity> expected = Arrays.asList(
                new AttributeEntity(1L, "Attr1"),
                new AttributeEntity(2L, "Attr2")
        );

        when(attributeRepository.getAll()).thenReturn(expected);

        List<AttributeEntity> result = attributeService.getAll();

        assertEquals(2, result.size());
        assertEquals("Attr1", result.get(0).getAttribute());
    }

    @Test
    void testGetById() {
        AttributeEntity expected = new AttributeEntity(1L, "Attr1");

        when(attributeRepository.getById(1L)).thenReturn(expected);

        AttributeEntity result = attributeService.getById(1L);

        assertEquals("Attr1", result.getAttribute());
        assertEquals(1L, result.getAttribute_id());
    }

    @Test
    void testDelete() {
        attributeService.delete(1L);
        verify(attributeRepository, times(1)).delete(1L);
    }
}
