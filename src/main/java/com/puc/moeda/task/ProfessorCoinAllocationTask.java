package com.puc.moeda.task;

import com.puc.moeda.model.Professor;
import com.puc.moeda.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProfessorCoinAllocationTask {

    @Autowired
    private ProfessorRepository professorRepository;

    @Scheduled(cron = "0 0 0 1 1,7 ?")
    public void allocateCoinsToProfessors() {
        List<Professor> professors = professorRepository.findAll();
        BigDecimal allocationAmount = new BigDecimal(1000);

        for (Professor professor : professors) {
            professor.setCoinBalance(professor.getCoinBalance().add(allocationAmount));
            professorRepository.save(professor);
        }
    }
}
