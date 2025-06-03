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

    // This is a placeholder for a semi-annual schedule. You should configure this appropriately.
    // For example, using fixedRate or a cron expression.
    // cron = "0 0 0 1 1,7 ?" // This cron expression would run at 00:00 on the 1st day of January and July
    @Scheduled(cron = "0 0 0 1 1,7 ?") // Example: Run at midnight on Jan 1st and July 1st
    public void allocateCoinsToProfessors() {
        List<Professor> professors = professorRepository.findAll();
        BigDecimal allocationAmount = new BigDecimal(1000);

        for (Professor professor : professors) {
            professor.setCoinBalance(professor.getCoinBalance().add(allocationAmount));
            professorRepository.save(professor);
        }

        // TODO: Add logging to indicate when the task runs and how many professors received coins
        // TODO: Implement a more robust mechanism to ensure allocation happens only once per semester,
        //       perhaps by storing the last allocation date in a system property or a dedicated table.
    }
}
