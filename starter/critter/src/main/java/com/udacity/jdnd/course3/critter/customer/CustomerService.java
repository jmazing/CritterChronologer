package com.udacity.jdnd.course3.critter.customer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.PetEntity;
import com.udacity.jdnd.course3.critter.pet.PetRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    public CustomerDTO convertToCustomerDTO(CustomerEntity customerEntity) {
            List<Long> petIds = null;
            if(customerEntity.getPets() != null) {
                petIds = customerEntity.getPets().stream().map(petEntity -> petEntity.getId()).collect(Collectors.toList());
            }
            CustomerDTO customerDTO = new CustomerDTO(customerEntity.getId(), customerEntity.getName(), 
                                            customerEntity.getPhoneNumber(), customerEntity.getNotes());
            customerDTO.setPetIds(petIds);
            return customerDTO;
    }

    public CustomerEntity convertToCustomerEntity(CustomerDTO customerDTO) {
        List<PetEntity> pets = null;
        if(customerDTO.getPetIds() != null) {
            pets = customerDTO.getPetIds().stream().map(id -> {
                Optional<PetEntity> petEntity = petRepository.findById(id);
                if(petEntity.isPresent()) {
                    return petEntity.get();
                } else {
                    throw new RuntimeException(String.format("Unable to get PetEntity with id: %d", id));
                }
            }).collect(Collectors.toList());
        }
        CustomerEntity customer = new CustomerEntity(customerDTO.getId(), customerDTO.getName(), 
                                    customerDTO.getPhoneNumber(), customerDTO.getNotes());
        customer.setPets(pets);
        return customer;
    }

    public CustomerDTO saveCustomerDTO(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = convertToCustomerEntity(customerDTO);
        customerEntity = customerRepository.save(customerEntity);
        return convertToCustomerDTO(customerEntity);
    }

    public CustomerDTO getCustomerDTO(long id) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(id);
        if(customerEntity.isPresent()) {
            return convertToCustomerDTO(customerEntity.get());
        } else {
            throw new RuntimeException(String.format("Unable to find CustomerEntity with id: %d", id));
        }
    }

    public List<CustomerDTO> getCustomerDTOs() {
        List<CustomerEntity> customerEntityList = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = customerEntityList.stream().map(customerEntity -> convertToCustomerDTO(customerEntity))
                                                .collect(Collectors.toList());
        return customerDTOList;
    }

    public CustomerDTO getCustomerDTOByPet(long id) {
        Optional<PetEntity> petEntity = petRepository.findById(id);
        if(petEntity.isPresent()) {
            CustomerEntity customerEntity = petEntity.get().getOwner();
            return convertToCustomerDTO(customerEntity);
        } else {
            throw new RuntimeException(String.format("Unable to find PetEntity with id: %d", id));
        }
    }

}
