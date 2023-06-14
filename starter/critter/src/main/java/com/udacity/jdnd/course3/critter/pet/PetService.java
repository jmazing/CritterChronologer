package com.udacity.jdnd.course3.critter.pet;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.customer.CustomerEntity;
import com.udacity.jdnd.course3.critter.customer.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PetService {
 
    @Autowired
    private PetRepository petRepository;

    @Autowired 
    private CustomerRepository customerRepository;
    
    public PetDTO convertToPetDTO(PetEntity petEntity) {
        PetDTO petDTO = new PetDTO(petEntity.getId(), petEntity.getPetType(), petEntity.getName(),
                                    petEntity.getBirthDate(), petEntity.getNotes());
        
        if(petEntity.getOwner() != null) {
            petDTO.setOwnerId(petEntity.getOwner().getId());
        }
        return petDTO;
    }

    public PetEntity convertToPetEntity(PetDTO petDTO) {
        Optional<CustomerEntity> owner = customerRepository.findById(petDTO.getOwnerId());
        PetEntity petEntity = new PetEntity(petDTO.getId(), petDTO.getType(), petDTO.getName(), 
                                            petDTO.getBirthDate(), petDTO.getNotes());
        
        // I also have to get the owner and make to save 
        if(owner.isPresent()) {
            petEntity.setOwner(owner.get());
        }
        return petEntity;
    }

    public PetDTO savePetDTO(PetDTO PetDTO) {
        PetEntity petEntity = convertToPetEntity(PetDTO);
        petEntity = petRepository.save(petEntity);
        System.out.println("Pet Entity owner is: " + petEntity.getOwner());
        return convertToPetDTO(petEntity);
    }

    public PetDTO getPetDTO(long id) {
        Optional<PetEntity> petEntity = petRepository.findById(id);
        if(petEntity.isPresent()) {
            return convertToPetDTO(petEntity.get());
        } else {
            throw new RuntimeException(String.format("Unable to find PetEntity with id: %d", id));
        }
    }

    public List<PetDTO> getPetDTOs() {
        List<PetEntity> petList = petRepository.findAll();
        List<PetDTO> petDTOList = petList.stream().map(petEntity -> convertToPetDTO(petEntity))
                                                    .collect(Collectors.toList());
        return petDTOList;
    }

}
