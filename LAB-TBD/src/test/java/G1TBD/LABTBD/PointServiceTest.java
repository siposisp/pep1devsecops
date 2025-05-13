package G1TBD.LABTBD;

import G1TBD.LABTBD.data.point.PointEntity;
import G1TBD.LABTBD.data.point.PointRepository;
import G1TBD.LABTBD.data.point.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

    private PointEntity samplePoint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        samplePoint = new PointEntity();
        samplePoint.setPoint_id(1L);
        samplePoint.setLatitude(10.0);
        samplePoint.setLongitude(20.0);
    }

    @Test
    void create_shouldCallRepositoryAndLog() {
        pointService.create(samplePoint);
        verify(pointRepository, times(1)).create(10.0, 20.0);
    }

    @Test
    void findByLatitudeAndLongitude_shouldReturnCorrectId() {
        when(pointRepository.findIdByLatitudeAndLongitude(10.0, 20.0)).thenReturn(1L);
        Long result = pointService.findByLatitudeAndLongitude(10.0, 20.0);
        assertEquals(1L, result);
    }

    @Test
    void getAll_shouldReturnListOfPoints() {
        when(pointRepository.getAll()).thenReturn(List.of(samplePoint));
        List<PointEntity> result = pointService.getAll();
        assertEquals(1, result.size());
        assertEquals(samplePoint, result.get(0));
    }

    @Test
    void getById_shouldReturnPoint() {
        when(pointRepository.getById(1L)).thenReturn(samplePoint);
        PointEntity result = pointService.getById(1L);
        assertNotNull(result);
        assertEquals(samplePoint.getLatitude(), result.getLatitude());
    }

    @Test
    void update_shouldCallRepositoryAndLog() {
        pointService.update(samplePoint);
        verify(pointRepository).update(1L, 10.0, 20.0);
    }

    @Test
    void delete_shouldCallRepositoryAndLog() {
        pointService.delete(1L);
        verify(pointRepository).delete(1L);
    }

    @Test
    void getNearbyPoints_shouldReturnNearbyPoints() {
        when(pointRepository.getById(1L)).thenReturn(samplePoint);
        List<PointEntity> mockNearby = Arrays.asList(new PointEntity(), new PointEntity());
        when(pointRepository.findXNearbyPoints(10.0, 20.0, 1000, 10)).thenReturn(mockNearby);

        List<PointEntity> result = pointService.getNearbyPoints(1L, 1000, 10);

        assertEquals(2, result.size());
        verify(pointRepository).findXNearbyPoints(10.0, 20.0, 1000, 10);
    }
}
