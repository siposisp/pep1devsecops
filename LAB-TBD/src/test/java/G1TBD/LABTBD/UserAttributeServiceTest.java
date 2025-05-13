package G1TBD.LABTBD;

import G1TBD.LABTBD.entities.AttributeEntity;
import G1TBD.LABTBD.entities.UserAttributeEntity;
import G1TBD.LABTBD.entities.UserEntity;
import G1TBD.LABTBD.repositories.UserAttributeRepository;
import G1TBD.LABTBD.services.UserAttributeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

public class UserAttributeServiceTest {

    @Mock
    private UserAttributeRepository userAttributeRepository;

    @InjectMocks
    private UserAttributeService userAttributeService;

    private UserAttributeEntity userAttribute;
    private List<UserAttributeEntity> userAttributeList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        UserEntity user = new UserEntity();
        user.setRut("12345678-9");

        AttributeEntity attribute = new AttributeEntity();
        attribute.setAttribute_id(1L);

        userAttribute = new UserAttributeEntity();
        userAttribute.setUser_attribute_id(1L);
        userAttribute.setUser(user);
        userAttribute.setAttribute(attribute);

        userAttributeList = new ArrayList<>();
        userAttributeList.add(userAttribute);
    }

    @Test
    void testCreate() {
        userAttributeService.create(userAttribute);
        verify(userAttributeRepository).create("12345678-9", 1L);
    }

    @Test
    void testUpdate() {
        userAttributeService.update(userAttribute);
        verify(userAttributeRepository).update(1L, "12345678-9", 1L);
    }

    @Test
    public void testGetAll() {
        Mockito.when(userAttributeRepository.getAll()).thenReturn(userAttributeList);
        List<UserAttributeEntity> userAttributes = userAttributeService.getAll();
        verify(userAttributeRepository).getAll();
        assert userAttributes.size() == 1;
    }

    @Test
    public void testGetById() {
        Mockito.when(userAttributeRepository.getById(1L)).thenReturn(userAttribute);
        UserAttributeEntity retrievedUserAttribute = userAttributeService.getById(1L);
        verify(userAttributeRepository).getById(1L);
        assert retrievedUserAttribute.getUser_attribute_id() == 1L;
    }

    @Test
    public void testGetByRut() {
        Mockito.when(userAttributeRepository.getByRut("12345678-9")).thenReturn(userAttributeList);
        List<UserAttributeEntity> userAttributes = userAttributeService.getByRut("12345678-9");
        verify(userAttributeRepository).getByRut("12345678-9");
        assert userAttributes.size() == 1;
    }

    @Test
    public void testGetByAttributeId() {
        Mockito.when(userAttributeRepository.getByAttributeId(1L)).thenReturn(userAttributeList);
        List<UserAttributeEntity> userAttributes = userAttributeService.getByAttributeId(1L);
        verify(userAttributeRepository).getByAttributeId(1L);
        assert userAttributes.size() == 1;
    }

    @Test
    public void testDelete() {
        userAttributeService.delete(1L);
        verify(userAttributeRepository).delete(1L);
    }
}
