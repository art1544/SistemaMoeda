package com.puc.moeda.service;

import com.puc.moeda.dto.CompanyRegistrationDTO;
import com.puc.moeda.dto.ProfessorRegistrationDTO;
import com.puc.moeda.dto.StudentRegistrationDTO;
import com.puc.moeda.model.Company;
import com.puc.moeda.model.Institution;
import com.puc.moeda.model.Professor;
import com.puc.moeda.model.Student;
import com.puc.moeda.repository.CompanyRepository;
import com.puc.moeda.repository.InstitutionRepository;
import com.puc.moeda.repository.ProfessorRepository;
import com.puc.moeda.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Student registerStudent(StudentRegistrationDTO registrationDTO) {
        if (studentRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado para um aluno"); // TODO: Custom exception
        }
        if (studentRepository.findByCpf(registrationDTO.getCpf()).isPresent()) {
             throw new RuntimeException("CPF já cadastrado para um aluno"); // TODO: Custom exception
        }

        Institution institution = institutionRepository.findById(registrationDTO.getInstitutionId())
                .orElseThrow(() -> new RuntimeException("Institution not found")); // TODO: Custom exception

        Student student = new Student();
        student.setName(registrationDTO.getName());
        student.setEmail(registrationDTO.getEmail());
        student.setCpf(registrationDTO.getCpf());
        student.setRg(registrationDTO.getRg());
        student.setAddress(registrationDTO.getAddress());
        student.setInstitution(institution);
        student.setCourse(registrationDTO.getCourse());
        student.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        return studentRepository.save(student);
    }

    public Professor registerProfessor(ProfessorRegistrationDTO registrationDTO) {
         if (professorRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado para um professor"); // TODO: Custom exception
        }
        if (professorRepository.findByCpf(registrationDTO.getCpf()).isPresent()) {
             throw new RuntimeException("CPF já cadastrado para um professor"); // TODO: Custom exception
        }

        Institution institution = institutionRepository.findById(registrationDTO.getInstitutionId())
                .orElseThrow(() -> new RuntimeException("Institution not found")); // TODO: Custom exception

        Professor professor = new Professor();
        professor.setName(registrationDTO.getName());
        professor.setCpf(registrationDTO.getCpf());
        professor.setDepartment(registrationDTO.getDepartment());
        professor.setInstitution(institution);
        professor.setEmail(registrationDTO.getEmail());
        professor.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        return professorRepository.save(professor);
    }

    public Company registerCompany(CompanyRegistrationDTO registrationDTO) {
         if (companyRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado para uma empresa"); // TODO: Custom exception
        }

        Company company = new Company();
        company.setName(registrationDTO.getName());
        company.setEmail(registrationDTO.getEmail());
        company.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        return companyRepository.save(company);
    }
}
