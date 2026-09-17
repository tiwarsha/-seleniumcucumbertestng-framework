package com.example.automation.model;

public record BillingDetails(
        String firstName,
        String lastName,
        String countryCode,
        String addressLine1,
        String city,
        String stateCode,
        String postcode,
        String phone,
        String email) {
}
