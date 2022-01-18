package project.mapper;

import java.util.ArrayList;
import java.util.List;
import project.dto.DVDDTO;
import project.persistence.entity.DVDEntity;

public class DVDMapper {


    public static DVDDTO mapEntityToDTO(DVDEntity dvdEntity) {
        DVDDTO dvddto = new DVDDTO();

        dvddto.setId(dvdEntity.getId());
        dvddto.setTitle(dvdEntity.getTitle());
        dvddto.setActors(dvdEntity.getActors());
        dvddto.setStageDirector(dvdEntity.getStageDirector());
        dvddto.setProductionDate(dvdEntity.getProductionDate());
        dvddto.setSupportedSpokenLanguages(dvdEntity.getSupportedSpokenLanguages());
        dvddto.setSupportedSubtitleLanguages(dvdEntity.getSupportedSubtitleLanguages());
        dvddto.setDuration(dvdEntity.getDuration());
        dvddto.setType(dvdEntity.getType());
        dvddto.setQuantityAvailable(dvdEntity.getQuantityAvailable());
        dvddto.setCost(dvdEntity.getCost());

        return dvddto;
    }

    public static DVDEntity mapDTOToEntity(DVDDTO dvddto) {
        DVDEntity dvdEntity = new DVDEntity();

        dvdEntity.setId(dvddto.getId());
        dvdEntity.setTitle(dvddto.getTitle());
        dvdEntity.setActors(dvddto.getActors());
        dvdEntity.setStageDirector(dvddto.getStageDirector());
        dvdEntity.setProductionDate(dvddto.getProductionDate());
        dvdEntity.setSupportedSpokenLanguages(dvddto.getSupportedSpokenLanguages());
        dvdEntity.setSupportedSubtitleLanguages(dvddto.getSupportedSubtitleLanguages());
        dvdEntity.setDuration(dvddto.getDuration());
        dvdEntity.setType(dvddto.getType());
        dvdEntity.setQuantityAvailable(dvddto.getQuantityAvailable());
        dvdEntity.setCost(dvddto.getCost());

        return dvdEntity;
    }

    public static List<DVDDTO> mapEntityListToDTOList(List<DVDEntity> dvdEntities) {
        List<DVDDTO> dvddtos = new ArrayList<>();
        for (DVDEntity dvdEntity : dvdEntities) {
            dvddtos.add(mapEntityToDTO(dvdEntity));
        }
        return dvddtos;
    }

    public static List<DVDEntity> mapDTOListToEntityList(List<DVDDTO> dvddtos) {
        List<DVDEntity> dvdEntities = new ArrayList<>();
        for (DVDDTO dvddto : dvddtos) {
            dvdEntities.add(mapDTOToEntity(dvddto));
        }
        return dvdEntities;
    }
}
