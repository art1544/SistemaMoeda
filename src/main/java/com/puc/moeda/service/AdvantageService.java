package com.puc.moeda.service;

import com.puc.moeda.dto.AdvantageCreationDTO;
import com.puc.moeda.model.Advantage;
import com.puc.moeda.model.Company;
import com.puc.moeda.repository.AdvantageRepository;
import com.puc.moeda.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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

    public List<Advantage> getAdvantagesByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found")); // TODO: Custom exception
        return advantageRepository.findByCompany(company);
    }

    @Transactional
    public Advantage updateAdvantage(Long id, AdvantageCreationDTO updateDTO) {
        Advantage existingAdvantage = advantageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advantage not found")); // TODO: Custom exception

        // TODO: Add check to ensure the company updating the advantage is the owner

        existingAdvantage.setName(updateDTO.getName());
        existingAdvantage.setDescription(updateDTO.getDescription());
        existingAdvantage.setImageUrl(updateDTO.getImageUrl());
        existingAdvantage.setCostInCoins(updateDTO.getCostInCoins());
        // Note: Company cannot be changed after creation in this model

        return advantageRepository.save(existingAdvantage);
    }

    @Transactional
    public void deleteAdvantage(Long id) {
        Advantage advantageToDelete = advantageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advantage not found")); // TODO: Custom exception

         // TODO: Add check to ensure the company deleting the advantage is the owner

        advantageRepository.delete(advantageToDelete);
    }
}
