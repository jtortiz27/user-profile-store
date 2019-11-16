package com.ortiz.userprofilestore.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointsOfContact {
    private List<EmailAddress> emailAddresses;
    private List<PhysicalAddress> physicalAddresses;
    private List<PhoneNumber> phoneNumbers;
}
