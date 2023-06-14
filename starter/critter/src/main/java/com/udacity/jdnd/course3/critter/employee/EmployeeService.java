package com.udacity.jdnd.course3.critter.employee;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    public EmployeeDTO convertToEmployeeDTO(EmployeeEntity employeeEntity) {
        EmployeeDTO employeeDTO = new EmployeeDTO(employeeEntity.getId(), employeeEntity.getName(), 
                                                    employeeEntity.getSkills(), employeeEntity.getDaysAvailable());
        return employeeDTO;
    }

    public EmployeeEntity convertToEmployeeEntity(EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = new EmployeeEntity(employeeDTO.getId(), employeeDTO.getName(), 
                                                            employeeDTO.getSkills(), employeeDTO.getDaysAvailable());
        return employeeEntity;
    }

    public EmployeeDTO saveEmployeeDTO(EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = convertToEmployeeEntity(employeeDTO);
        employeeEntity = employeeRepository.save(employeeEntity);
        return convertToEmployeeDTO(employeeEntity);
    }

    public EmployeeDTO getEmployeeDTO(long id) {
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
        if(employeeEntity.isPresent()) {
            return convertToEmployeeDTO(employeeEntity.get());
        } else {
            throw new RuntimeException(String.format("Unable to find EmployeeEntity with id: %d", id));
        }
    }

    public List<EmployeeDTO> getEmployeeDTOs() {
        List<EmployeeEntity> employeeList = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employeeEntity -> convertToEmployeeDTO(employeeEntity))
                                                        .collect(Collectors.toList());
        return employeeDTOList;
    }

    public void setEmployeeAvailibility(Set<DayOfWeek> daysAvailable, long employeeId) {
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);
        if(employeeEntity.isPresent()) {
            employeeEntity.get().setDaysAvailable(daysAvailable);
            employeeRepository.save(employeeEntity.get());
        } else {
            throw new RuntimeException(String.format("Unable to find EmployeeEntity with id: %d", employeeId));
        }
    }

    public List<EmployeeDTO> findEmployeeDTOsForService(EmployeeRequestDTO employeeRequestDTO) {
        List<EmployeeEntity> employeeList = employeeRepository.findAll();
        List<EmployeeDTO> availableEmployees = employeeList.stream()
            .filter(employeeEntity -> employeeEntity.getDaysAvailable().contains(employeeRequestDTO.getDate().getDayOfWeek()))
            .filter(employeeEntity -> employeeEntity.getSkills().containsAll(employeeRequestDTO.getSkills()))
            .map(employeeEntity -> convertToEmployeeDTO(employeeEntity))
            .collect(Collectors.toList());
        return availableEmployees;
    }
}
