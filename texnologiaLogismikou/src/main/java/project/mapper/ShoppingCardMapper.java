package project.mapper;

import java.util.ArrayList;
import java.util.List;
import project.dto.ShoppingCardDTO;
import project.persistence.entity.ShoppingCardEntity;

public class ShoppingCardMapper {


    public static ShoppingCardDTO mapEntityToDTO(ShoppingCardEntity shoppingCardEntity) {
        ShoppingCardDTO shoppingCardDTO = new ShoppingCardDTO();

        shoppingCardDTO.setId(shoppingCardEntity.getId());
        shoppingCardDTO.setCustomerUserName(shoppingCardEntity.getCustomerUserName());
        shoppingCardDTO.setDvd(shoppingCardEntity.getDvd());
        shoppingCardDTO.setCreatedDate(shoppingCardEntity.getCreatedDate());
        shoppingCardDTO.setTotalCost(shoppingCardEntity.getTotalCost());
        shoppingCardDTO.setStatus(shoppingCardEntity.getStatus());

        return shoppingCardDTO;
    }

    public static ShoppingCardEntity mapDTOToEntity(ShoppingCardDTO shoppingCardDTO) {
        ShoppingCardEntity shoppingCardEntity = new ShoppingCardEntity();

        shoppingCardEntity.setId(shoppingCardDTO.getId());
        shoppingCardEntity.setCustomerUserName(shoppingCardDTO.getCustomerUserName());
        shoppingCardEntity.setDvd(shoppingCardDTO.getDvd());
        shoppingCardEntity.setCreatedDate(shoppingCardDTO.getCreatedDate());
        shoppingCardEntity.setTotalCost(shoppingCardDTO.getTotalCost());
        shoppingCardEntity.setStatus(shoppingCardDTO.getStatus());

        return shoppingCardEntity;
    }

    public static List<ShoppingCardDTO> mapEntityListToDTOList(List<ShoppingCardEntity> shoppingCardEntityList) {
        List<ShoppingCardDTO> shoppingCardDTOs = new ArrayList<>();
        for (ShoppingCardEntity cardEntity : shoppingCardEntityList) {
            shoppingCardDTOs.add(mapEntityToDTO(cardEntity));
        }
        return shoppingCardDTOs;
    }

    public static List<ShoppingCardEntity> mapDTOListToEntityList(List<ShoppingCardDTO> shoppingCardDTOList) {
        List<ShoppingCardEntity> shoppingCardDTOs = new ArrayList<>();
        for (ShoppingCardDTO cardEntity : shoppingCardDTOList) {
            shoppingCardDTOs.add(mapDTOToEntity(cardEntity));
        }
        return shoppingCardDTOs;
    }
}
