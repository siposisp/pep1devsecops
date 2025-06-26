package G1TBD.LABTBD;
import G1TBD.LABTBD.entities.RankingEntity;
import G1TBD.LABTBD.entities.TaskEntity;
import G1TBD.LABTBD.entities.UserEntity;
import G1TBD.LABTBD.repositories.RankingRepository;
import G1TBD.LABTBD.services.RankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

    @InjectMocks
    private RankingService rankingService;

    private RankingEntity ranking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ranking = new RankingEntity();
        ranking.setRanking_id(1L);
        ranking.setValue(5);
        ranking.setUser(new UserEntity());
        ranking.setTask(new TaskEntity());
    }

    @Test
    void createRanking() {
        rankingService.create(ranking);
        verify(rankingRepository, times(1)).create(ranking.getUser().getRut(), ranking.getTask().getTask_id(), ranking.getValue());
    }

    @Test
    void updateRanking() {
        rankingService.update(ranking);
        verify(rankingRepository, times(1)).update(ranking.getRanking_id(), ranking.getUser().getRut(), ranking.getTask().getTask_id(), ranking.getValue());
    }

    @Test
    void getAllRankings() {
        when(rankingRepository.getAll()).thenReturn(Arrays.asList(ranking));
        List<RankingEntity> rankings = rankingService.getAll();
        assertNotNull(rankings);
        assertEquals(1, rankings.size());
        assertEquals(5, rankings.get(0).getValue());
    }

    @Test
    void getRankingById() {
        when(rankingRepository.getById(1L)).thenReturn(ranking);
        RankingEntity result = rankingService.getById(1L);
        assertNotNull(result);
        assertEquals(5, result.getValue());
    }

    @Test
    void getRankingsByTaskId() {
        when(rankingRepository.getByTaskId(1L)).thenReturn(Arrays.asList(ranking));
        List<RankingEntity> result = rankingService.getByTaskId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getValue());
    }

    @Test
    void deleteRanking() {
        rankingService.delete(1L);
        verify(rankingRepository, times(1)).delete(1L);
    }
}
