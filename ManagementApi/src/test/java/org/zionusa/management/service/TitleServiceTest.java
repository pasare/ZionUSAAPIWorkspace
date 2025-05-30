package org.zionusa.management.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zionusa.management.domain.title.Title;
import org.zionusa.management.domain.title.TitleDao;
import org.zionusa.management.domain.title.TitleService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

public class TitleServiceTest {

    @InjectMocks
    private TitleService service;

    private List<Title> mockTitles;

    @Mock
    private TitleDao dao;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ObjectMapper mapper = new ObjectMapper();

        String mockTitlesPath = "src/test/resources/titles.json";

        byte[] titlesData = Files.readAllBytes(Paths.get(mockTitlesPath));
        mockTitles = mapper.readValue(titlesData, new TypeReference<List<Title>>() {
        });

        when(dao.findAll()).thenReturn(mockTitles);
        when(dao.findById(any(Integer.class))).thenReturn(Optional.of(mockTitles.get(0)));
    }

    @Test
    public void getAllTitles() {
        List<Title> titles = service.getAll(null);

        assertThat(titles).isNotNull();
        assertThat(titles.size()).isEqualTo(8);
    }

    @Test
    public void getTitleById() {
        Title title = service.getById(1);

        assertThat(title).isNotNull();
        assertThat(title.getName()).isEqualTo(mockTitles.get(0).getName());
        assertThat(title.getSortOrder()).isEqualTo(mockTitles.get(0).getSortOrder());
    }

    @Test
    public void saveTitle() {

        when(dao.save(any(Title.class))).thenReturn(mockTitles.get(0));

        Title returnedTitle = service.save(mockTitles.get(0));

        assertThat(returnedTitle).isNotNull();
        assertThat(returnedTitle.getId()).isEqualTo(mockTitles.get(0).getId());
        assertThat(returnedTitle.getName()).isEqualTo(mockTitles.get(0).getName());
    }

    @Test
    public void deleteTitle() {
        doNothing().when(dao).delete(any(Title.class));

        try {
            service.delete(1);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test()
    public void deleteTitleThatDoesNotExist() {
        when(dao.findById(any(Integer.class))).thenReturn(Optional.empty());

        try {
            service.delete(123);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

}
