package com.puc.moeda.repository;

import com.puc.moeda.model.Advantage;
import com.puc.moeda.model.Company;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvantageRepository extends JpaRepository<Advantage, Long> {
    List<Advantage> findByCompany(Company company);
}
