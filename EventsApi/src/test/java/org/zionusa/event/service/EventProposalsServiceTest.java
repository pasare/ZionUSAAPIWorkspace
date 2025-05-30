package org.zionusa.event.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

public class EventProposalsServiceTest {
/*
    @InjectMocks
    EventProposalsService service;

    private List<EventProposal> mockEventProposals;

    @Mock
    private EventProposalsDao dao;

    @Mock
    private EventStatusService eventStatusService;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockEventProposalsPath = "src/test/resources/event-proposals.json";
        String mockDisplayEventProposalsPath = "src/test/resources/event-proposals-display.json";

        byte[] eventProposalsData = Files.readAllBytes(Paths.get(mockEventProposalsPath));
        mockEventProposals = mapper.readValue(eventProposalsData, new TypeReference<List<EventProposal>>() {});

        when(dao.findAll()).thenReturn(mockEventProposals);
        when(dao.findById(anyInt())).thenReturn(Optional.of(mockEventProposals.get(0)));
    }

    @Test
    public void archiveEventProposal() {
        // Arrange
        EventProposal eventProposal = mockEventProposals.get(0);
        when(dao.save(any(EventProposal.class))).thenReturn(eventProposal);
        EventStatus eventStatus = new EventStatus();
        eventStatus.setEventProposalId(eventProposal.getId());
        when(eventStatusService.getByEventProposalId(anyInt())).thenReturn(eventStatus);
        // Act
        service.archive(1);
        // Assert
        verify(dao, times(1)).findById(anyInt());
        verify(dao, times(1)).save(any(EventProposal.class));
    }

    @Test
    public void archiveEventProposalNotFound() throws NotFoundException {
        try {
            // Arrange
            when(dao.findById(anyInt())).thenReturn(Optional.empty());
            // Act
            service.archive(1);
            // Assert
            fail("Should fail with NotFoundException");
        } catch (NotFoundException e) {
            // Assert
            verify(dao, times(1)).findById(anyInt());
            verify(dao, times(0)).save(any(EventProposal.class));
        }
    }

    @Test
    public void getAllEventProposals() {
        // Act
        List<EventProposal> eventProposals = service.getAll();
        // Assert
        verify(dao, times(1)).findAll();
        assertThat(eventProposals).isNotNull();
        assertThat(eventProposals.size()).isEqualTo(11);
        assertThat(mockEventProposals).containsAll(eventProposals);
    }

    @Test
    public void getAllDisplayEventProposals() {
        // Act
        List<EventProposalDisplay> eventProposals = service.getAllDisplay();
        // Assert
        verify(dao, times(1)).findAll();
        assertThat(eventProposals).isNotNull();
        assertThat(eventProposals.size()).isEqualTo(11);
    }

    @Test
    public void getAllUpcomingEventProposals() {
        // Arrange
        when(dao.getAllByIsPublishedAndIsPrivateAndProposedEndDateAfter(anyBoolean(), anyBoolean(), anyString())).thenReturn(mockEventProposals);
        // Act
        List<EventProposalDisplay> actual = service.getAllUpcoming();
        // Assert
        verify(dao, times(1)).getAllByIsPublishedAndIsPrivateAndProposedEndDateAfter(anyBoolean(), anyBoolean(), anyString());
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(11);
    }

    @Test
    public void getAllUpcomingEventProposalsHidesPrivate() {
        // Arrange
        EventProposal eventProposal = mockEventProposals.get(0);
        eventProposal.setWorkflowStatus(WorkflowStatus.ADMIN_APPROVED.status());
        eventProposal.setPrivate(true);
        List<EventProposal> eventProposals = new ArrayList<>();
        eventProposals.add(eventProposal);
        when(dao.getAllByIsPublishedAndIsPrivateAndProposedEndDateAfter(anyBoolean(), anyBoolean(), anyString())).thenReturn(eventProposals);
        // Act
        List<EventProposalDisplay> actual = service.getAllUpcoming();
        // Assert
        verify(dao, times(1)).getAllByIsPublishedAndIsPrivateAndProposedEndDateAfter(anyBoolean(), anyBoolean(), anyString());
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    public void saveEventProposalWithEventStatus() {
        // Arrange
        EventProposal eventProposal = mockEventProposals.get(0);
        EventStatus eventStatus = new EventStatus();
        eventStatus.setEventProposalId(eventProposal.getId());
        when(dao.save(any(EventProposal.class))).thenReturn(eventProposal);
        when(eventStatusService.getByEventProposalId(anyInt())).thenReturn(eventStatus);
        // Act
        EventProposal actual = service.save(eventProposal);
        // Assert
        verify(dao, times(1)).save(any(EventProposal.class));
        verify(eventStatusService, times(1)).getByEventProposalId(anyInt());
        verify(eventStatusService, times(0)).createEventStatus(any(EventProposal.class));
        assertThat(actual).isEqualTo(eventProposal);
    }

    @Test
    public void saveEventProposalWithoutEventStatus() {
        // Arrange
        EventProposal eventProposal = mockEventProposals.get(0);
        EventStatus eventStatus = new EventStatus();
        eventStatus.setEventProposalId(eventProposal.getId());
        when(dao.save(any(EventProposal.class))).thenReturn(eventProposal);
        when(eventStatusService.getByEventProposalId(anyInt())).thenThrow(new NotFoundException());
        when(eventStatusService.createEventStatus(any(EventProposal.class))).thenReturn(eventStatus);
        // Act
        EventProposal actual = service.save(eventProposal);
        // Assert
        verify(dao, times(1)).save(any(EventProposal.class));
        verify(eventStatusService, times(1)).getByEventProposalId(anyInt());
        verify(eventStatusService, times(1)).createEventStatus(any(EventProposal.class));
        assertThat(actual).isEqualTo(eventProposal);
    }*/
}
