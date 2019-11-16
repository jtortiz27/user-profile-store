package com.ortiz.userprofilestore.service.model;

import com.ortiz.userprofilestore.data.model.Country;
import com.ortiz.userprofilestore.data.model.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicalAddress {
    private String streetName;
    private String streetNumber;
    private State state;
    private Country country;
    private String zipCode;
}
