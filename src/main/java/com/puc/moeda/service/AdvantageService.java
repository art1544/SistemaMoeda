package com.puc.moeda.service;

import com.puc.moeda.dto.AdvantageCreationDTO;
import com.puc.moeda.model.Advantage;
import com.puc.moeda.model.Company;
import com.puc.moeda.repository.AdvantageRepository;
import com.puc.moeda.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvantageService {

    @Autowired
    private AdvantageRepository advantageRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<Advantage> getAllAdvantages() {
        return advantageRepository.findAll();
    }

    public Advantage createAdvantage(AdvantageCreationDTO creationDTO) {
        Company company = companyRepository.findById(creationDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found")); // TODO: Custom exception

        Advantage advantage = new Advantage();
        advantage.setName(creationDTO.getName());
        advantage.setDescription(creationDTO.getDescription());
        advantage.setImageUrl(creationDTO.getImageUrl());
        advantage.setCostInCoins(creationDTO.getCostInCoins());
        advantage.setCompany(company);

        return advantageRepository.save(advantage);
    }

    // TODO: Add method to get advantages by company
}
