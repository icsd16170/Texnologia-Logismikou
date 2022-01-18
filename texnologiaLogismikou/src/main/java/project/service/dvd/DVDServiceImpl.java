package project.service.dvd;



import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.DVDDTO;
import project.errorhandling.exception.DVDNotFoundException;
import project.mapper.DVDMapper;
import project.persistence.entity.DVDEntity;
import project.persistence.repository.DVDRepository;

@Service
public class DVDServiceImpl implements DVDService {
    private final DVDRepository dvdRepository;


    @Autowired
    public DVDServiceImpl(DVDRepository dvdRepository) {
        this.dvdRepository = dvdRepository;
    }


    @Override
    public DVDDTO create(DVDDTO dvdDTO) {
        DVDEntity dvdEntity = DVDMapper.mapDTOToEntity(dvdDTO);
        return DVDMapper.mapEntityToDTO(dvdRepository.save(dvdEntity));
    }

    @Override
    public DVDDTO findById(Long id) {
        Optional<DVDEntity> dvdEntity = dvdRepository.findById(id);
        if (dvdEntity.isPresent()) {
            return DVDMapper.mapEntityToDTO(dvdEntity.get());
        }
        throw new DVDNotFoundException(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (dvdRepository.existsById(id)) {
            dvdRepository.deleteById(id);
            return;
        }
        throw new DVDNotFoundException(id);
    }

    @Override
    public DVDDTO update(Long id, DVDDTO dvddto) {
        findById(id);
        DVDEntity dvdEntity = DVDMapper.mapDTOToEntity(dvddto);
        dvdEntity.setId(id);
        DVDEntity updateEntity = dvdRepository.save(dvdEntity);
        return DVDMapper.mapEntityToDTO(updateEntity);
    }

    @Override
    public List<DVDDTO> findByTitleLike(String title) {

        List<DVDEntity> dvdEntityList = dvdRepository.findByTitleLike("%" + title + "%");

        return DVDMapper.mapEntityListToDTOList(dvdEntityList);

    }

    @Override
    public List<DVDDTO> findAll() {
        return DVDMapper.mapEntityListToDTOList(dvdRepository.findAll());
    }
}
