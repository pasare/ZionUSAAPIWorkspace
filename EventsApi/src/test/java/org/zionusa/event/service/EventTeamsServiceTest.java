package org.zionusa.event.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.event.domain.eventTeam.EventTeamsDao;
import org.zionusa.event.domain.eventTeam.EventTeam;
import org.zionusa.event.domain.eventTeam.EventTeamsService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class EventTeamsServiceTest {

    @InjectMocks
    EventTeamsService service;

    private List<EventTeam> mockEventTeams;

    @Mock
    private EventTeamsDao dao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockEventTeamsPath = "src/test/resources/event-teams.json";

        byte[] eventTeamsData = Files.readAllBytes(Paths.get(mockEventTeamsPath));
        mockEventTeams = mapper.readValue(eventTeamsData, new TypeReference<List<EventTeam>>() {
        });

        when(dao.findAll()).thenReturn(mockEventTeams);
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockEventTeams.get(0)));
    }

    @Test
    public void getEventTeamsByBranch() {
        // Arrange
        EventTeam eventTeam = mockEventTeams.get(0);
        when(dao.findByBranchId(anyInt())).thenReturn(eventTeam);
        // Act
//        EventTeam actual = service.getEventTeamByBranch(eventTeam.getId());
        // Assert
//        assertThat(actual).isNotNull();
//        assertThat(actual).isEqualTo(eventTeam);
    }

//    @Test
//    public void getEventTeamsByBranchNotFound() throws NotFoundException {
//        try {
//            // Arrange
//            when(dao.findByBranchId(anyInt())).thenReturn(null);
//            // Act
//            service.getEventTeamByBranch(1);
//            // Assert
//            fail("Should throw NotFoundException");
//        } catch (NotFoundException ignored) {
//        }
//    }
}
