package project.service.dvd;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.dto.DVDDTO;
import project.errorhandling.exception.DVDNotFoundException;
import project.persistence.entity.DVDEntity;
import project.persistence.repository.DVDRepository;

@ExtendWith(MockitoExtension.class)
class DVDServiceImplTest {

    public static final LocalDate DATE = LocalDate.of(2021, 1, 1);

    @Mock
    private DVDRepository dvdRepository;

    @InjectMocks
    private DVDServiceImpl testee;

    @Test
    void create_returnsDTO() {
        DVDEntity dvdEntity = getDvdEntity();

        Mockito.when(dvdRepository.save(Mockito.any())).thenReturn(dvdEntity);
        DVDDTO dvdDto = getDVDDto();

        DVDDTO actual = testee.create(dvdDto);

        Assertions.assertEquals(actual.getActors(), dvdDto.getActors());
        Assertions.assertEquals(actual.getCost(), dvdDto.getCost());
        Assertions.assertEquals(actual.getDuration(), dvdDto.getDuration());
        Assertions.assertEquals(actual.getId(), dvdDto.getId());
        Assertions.assertEquals(actual.getProductionDate(), dvdDto.getProductionDate());
        Assertions.assertEquals(actual.getQuantityAvailable(), dvdDto.getQuantityAvailable());
        Assertions.assertEquals(actual.getStageDirector(), dvdDto.getStageDirector());
        Assertions.assertEquals(actual.getTitle(), dvdDto.getTitle());
        Assertions.assertEquals(actual.getType(), dvdDto.getType());
        Assertions.assertEquals(actual.getSupportedSpokenLanguages(), dvdDto.getSupportedSpokenLanguages());
        Assertions.assertEquals(actual.getSupportedSubtitleLanguages(), dvdDto.getSupportedSubtitleLanguages());

    }

    @Test
    void findById_returnsDTO() {
        DVDEntity dvdEntity = getDvdEntity();

        Mockito.when(dvdRepository.findById(Mockito.any())).thenReturn(Optional.of(dvdEntity));
        DVDDTO dvdDto = getDVDDto();

        DVDDTO actual = testee.findById(1L);

        Assertions.assertEquals(actual.getActors(), dvdDto.getActors());
        Assertions.assertEquals(actual.getCost(), dvdDto.getCost());
        Assertions.assertEquals(actual.getDuration(), dvdDto.getDuration());
        Assertions.assertEquals(actual.getId(), dvdDto.getId());
        Assertions.assertEquals(actual.getProductionDate(), dvdDto.getProductionDate());
        Assertions.assertEquals(actual.getQuantityAvailable(), dvdDto.getQuantityAvailable());
        Assertions.assertEquals(actual.getStageDirector(), dvdDto.getStageDirector());
        Assertions.assertEquals(actual.getTitle(), dvdDto.getTitle());
        Assertions.assertEquals(actual.getType(), dvdDto.getType());
        Assertions.assertEquals(actual.getSupportedSpokenLanguages(), dvdDto.getSupportedSpokenLanguages());
        Assertions.assertEquals(actual.getSupportedSubtitleLanguages(), dvdDto.getSupportedSubtitleLanguages());

    }

    @Test
    void findById_entityNotExist_throwsDVDNotFoundException() {
        Mockito.when(dvdRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(DVDNotFoundException.class, () -> testee.findById(1L));
    }

    @Test
    void delete_entityNotExist_throwsDVDNotFoundException() {
        Mockito.when(dvdRepository.existsById(Mockito.any())).thenReturn(false);

        Assertions.assertThrows(DVDNotFoundException.class, () -> testee.delete(1L));
    }

    @Test
    void delete_entityExists_deletesDVD() {
        Mockito.when(dvdRepository.existsById(Mockito.any())).thenReturn(true);
        testee.delete(1L);
        Mockito.verify(dvdRepository).deleteById(1L);
    }


    private DVDDTO getDVDDto() {
        DVDDTO dvddto = new DVDDTO();
        dvddto.setActors(Arrays.asList("actor1", "actor2"));
        dvddto.setCost(10);
        dvddto.setDuration(100);
        dvddto.setId(1L);
        dvddto.setProductionDate(DATE);
        dvddto.setQuantityAvailable(100);
        dvddto.setStageDirector("stageDirector");
        dvddto.setTitle("title");
        dvddto.setType("type");
        dvddto.setSupportedSpokenLanguages(Arrays.asList("en", "gr"));
        dvddto.setSupportedSubtitleLanguages(Arrays.asList("en", "gr"));
        return dvddto;
    }

    private DVDEntity getDvdEntity() {
        DVDEntity dvdEntity = new DVDEntity();
        dvdEntity.setActors(Arrays.asList("actor1", "actor2"));
        dvdEntity.setCost(10);
        dvdEntity.setDuration(100);
        dvdEntity.setId(1L);
        dvdEntity.setProductionDate(DATE);
        dvdEntity.setQuantityAvailable(100);
        dvdEntity.setStageDirector("stageDirector");
        dvdEntity.setTitle("title");
        dvdEntity.setType("type");
        dvdEntity.setSupportedSpokenLanguages(Arrays.asList("en", "gr"));
        dvdEntity.setSupportedSubtitleLanguages(Arrays.asList("en", "gr"));
        return dvdEntity;
    }
}
