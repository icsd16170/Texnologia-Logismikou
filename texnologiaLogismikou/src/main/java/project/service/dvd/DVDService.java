package project.service.dvd;

import java.util.List;
import project.dto.DVDDTO;

public interface DVDService {

    DVDDTO create(DVDDTO dvdDTO);

    DVDDTO findById(Long id);

    void delete(Long id);

    DVDDTO update(Long id, DVDDTO dvddto);

    List<DVDDTO> findByTitleLike(String title);

    List<DVDDTO> findAll();
}