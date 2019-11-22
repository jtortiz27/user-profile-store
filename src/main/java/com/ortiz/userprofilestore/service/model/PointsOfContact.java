package com.ortiz.userprofilestore.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointsOfContact {
    private Set<EmailAddress> emailAddresses = new HashSet<>();
    private Set<PhysicalAddress> physicalAddresses = new HashSet<>();
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();
}
