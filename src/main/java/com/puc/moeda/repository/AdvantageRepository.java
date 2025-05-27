package com.puc.moeda.repository;

import com.puc.moeda.model.Advantage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvantageRepository extends JpaRepository<Advantage, Long> {
}
