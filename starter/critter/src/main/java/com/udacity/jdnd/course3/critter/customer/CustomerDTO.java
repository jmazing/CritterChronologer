package com.udacity.jdnd.course3.critter.customer;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the form that customer request and response data takes. Does not map
 * to the database directly.
 */

@Data
@NoArgsConstructor
public class CustomerDTO {
    private long id;
    private String name;
    private String phoneNumber;
    private String notes;
    private List<Long> petIds;

    public CustomerDTO(long id, String name, String phoneNumber, String notes) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }

}
