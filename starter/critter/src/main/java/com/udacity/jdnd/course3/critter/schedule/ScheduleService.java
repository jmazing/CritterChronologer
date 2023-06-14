package com.udacity.jdnd.course3.critter.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.customer.CustomerEntity;
import com.udacity.jdnd.course3.critter.customer.CustomerRepository;
import com.udacity.jdnd.course3.critter.employee.EmployeeEntity;
import com.udacity.jdnd.course3.critter.employee.EmployeeRepository;
import com.udacity.jdnd.course3.critter.pet.PetEntity;
import com.udacity.jdnd.course3.critter.pet.PetRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ScheduleService {
    
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    public ScheduleDTO convertToScheduleDTO(ScheduleEntity scheduleEntity) {
        ScheduleDTO scheduleDTO = new ScheduleDTO(scheduleEntity.getId(), 
                                                    scheduleEntity.getDate(), 
                                                    scheduleEntity.getSkillsNeeded());
        
        List<Long> employeeIds = null;
        if(scheduleEntity.getEmployees() != null) {
            employeeIds = scheduleEntity.getEmployees().stream()
                            .map(employeeEntity -> employeeEntity.getId()).collect(Collectors.toList());
        }

        List<Long> petIds = null;
        if(scheduleEntity.getPets() != null) {
            petIds = scheduleEntity.getPets().stream()
                        .map(petEntity -> petEntity.getId()).collect(Collectors.toList());
        }

        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);
        return scheduleDTO;
    }

    public ScheduleEntity convertToScheduleEntity(ScheduleDTO scheduleDTO) {
        List<EmployeeEntity> employees = null;
        if(scheduleDTO.getEmployeeIds() != null) {
            employees = scheduleDTO.getEmployeeIds().stream().map(id -> {
                Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
                if(employeeEntity.isPresent()) {
                    return employeeEntity.get();
                } else {
                    throw new RuntimeException(String.format("Unable to get EmployeeEntity with id: %d", id));
                }
            }).collect(Collectors.toList());
        }

        List<PetEntity> pets = null;
        if(scheduleDTO.getPetIds() != null) {
            pets = scheduleDTO.getPetIds().stream().map(id -> {
                Optional<PetEntity> petEntity = petRepository.findById(id);
                if(petEntity.isPresent()) {
                    return petEntity.get();
                } else {
                    throw new RuntimeException(String.format("Unable to get PetEntity with id: %d", id));
                }
            }).collect(Collectors.toList());
        }
        
        ScheduleEntity scheduleEntity = new ScheduleEntity(scheduleDTO.getId(), 
                                                            scheduleDTO.getDate(), 
                                                            scheduleDTO.getActivities());
        scheduleEntity.setEmployees(employees);
        scheduleEntity.setPets(pets);
        return scheduleEntity;
    } 

    public ScheduleDTO getScheduleDTO(long id) {
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(id);
        if(scheduleEntity.isPresent()) {
            return convertToScheduleDTO(scheduleEntity.get());
        } else {
            throw new RuntimeException(String.format("Unable to find scheduleEntity with id: %d", id));
        }
    }

    public List<ScheduleDTO> getScheduleDTOs() {
        List<ScheduleEntity> scheduleList = scheduleRepository.findAll();
        List<ScheduleDTO> schduleDTOList = scheduleList.stream().map(scheduleEntity -> convertToScheduleDTO(scheduleEntity))
                                                        .collect(Collectors.toList());
        return schduleDTOList;
    }


    public ScheduleDTO saveScheduleDTO(ScheduleDTO scheduleDTO) {
        ScheduleEntity scheduleEntity = convertToScheduleEntity(scheduleDTO);
        scheduleEntity = scheduleRepository.save(scheduleEntity);
        return convertToScheduleDTO(scheduleEntity);
    }

    public List<ScheduleDTO> getScheduleDTOsForPet(long petId) {
        List<ScheduleDTO> petScheduleDTOs = null;
        Optional<PetEntity> petEntity = petRepository.findById(petId);
        if(petEntity.isPresent()) {
            List<ScheduleEntity> petScheduleEntities = petEntity.get().getPetSchedule();
            petScheduleDTOs = petScheduleEntities.stream().map(scheduleEntity -> convertToScheduleDTO(scheduleEntity))
                                                    .collect(Collectors.toList());
        }
        return petScheduleDTOs;
    }

    public List<ScheduleDTO> getScheduleDTOsForEmployee(long employeeId) {
        List<ScheduleDTO> employeeScheduleDTOs = null;
        Optional<EmployeeEntity> empolyeeEntity = employeeRepository.findById(employeeId);
        if(empolyeeEntity.isPresent()) {
            List<ScheduleEntity> employeeScheduleEntities = empolyeeEntity.get().getEmployeeSchedule();
            employeeScheduleDTOs = employeeScheduleEntities.stream().map(scheduleEntity -> convertToScheduleDTO(scheduleEntity))
                                                            .collect(Collectors.toList());
        }
        return employeeScheduleDTOs;
    }

    public List<ScheduleDTO> getScheduleDTOsForCustomer(long customerId) {
        List<ScheduleDTO> customerScheduleDTOs = new ArrayList<ScheduleDTO>();
        Optional<CustomerEntity> customerEntity = customerRepository.findById(customerId);
        if(customerEntity.isPresent()) {
            List<PetEntity> petEntities = customerEntity.get().getPets();
            for(PetEntity petEntity: petEntities) {
                customerScheduleDTOs.addAll(getScheduleDTOsForPet(petEntity.getId()));
            }
        }
        return customerScheduleDTOs;
    }

}
