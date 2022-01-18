package project.mapper;

import java.util.ArrayList;
import java.util.List;
import project.dto.UserDTO;
import project.persistence.entity.UserEntity;

public class UserMapper {


    public static UserDTO mapEntityToDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserName(userEntity.getUserName());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setName(userEntity.getName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setRole(userEntity.getRole());
        userDTO.setActive(userEntity.isActive());
        userDTO.setId(userEntity.getId());

        if ("EMPLOYEE".equals(userEntity.getRole())) {
            userDTO.setEmployeeType(userEntity.getEmployeeType());
        } else if ("CUSTOMER".equals(userEntity.getRole())) {
            userDTO.setAddress(userEntity.getAddress());
            userDTO.setCardType(userEntity.getCardType());
            userDTO.setCardNumber(userEntity.getCardNumber());
            userDTO.setExpirationDate(userEntity.getExpirationDate());
            userDTO.setCvc(userEntity.getCvc());
        }

        return userDTO;
    }

    public static UserEntity mapDTOToEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();

        userEntity.setUserName(userDTO.getUserName());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setName(userDTO.getName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setRole(userDTO.getRole());
        userEntity.setActive(userEntity.isActive());
        userEntity.setId(userEntity.getId());
        if ("EMPLOYEE".equals(userDTO.getRole())) {
            userEntity.setEmployeeType(userDTO.getEmployeeType());

        } else if ("CUSTOMER".equals(userDTO.getRole())) {
            userEntity.setAddress(userDTO.getAddress());
            userEntity.setCardType(userDTO.getCardType());
            userEntity.setCardNumber(userDTO.getCardNumber());
            userEntity.setExpirationDate(userDTO.getExpirationDate());
            userEntity.setCvc(userDTO.getCvc());
        }

        return userEntity;
    }

    public static List<UserDTO> mapEntityListToDTOList(List<UserEntity> userEntities) {
        List<UserDTO> userDTOS = new ArrayList<>();
        userEntities.forEach(userEntity -> {
            userDTOS.add(mapEntityToDTO(userEntity));
        });
        return userDTOS;
    }
}
