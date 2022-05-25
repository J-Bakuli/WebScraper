package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class CompaniesExportData {
    private String headquartersCountry;
    private String branchOfficeStatus;
    private int numberOfCompanies;
}
