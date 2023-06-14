package com.udacity.jdnd.course3.critter.employee;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to find available employees by skills. Does not map
 * to the database directly.
 */
@Data
@NoArgsConstructor
public class EmployeeRequestDTO {
    private Set<EmployeeSkill> skills;
    private LocalDate date;
}
