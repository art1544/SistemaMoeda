package com.puc.moeda.service;

import com.puc.moeda.model.Company;
import com.puc.moeda.model.Professor;
import com.puc.moeda.model.Student;
import com.puc.moeda.repository.CompanyRepository;
import com.puc.moeda.repository.ProfessorRepository;
import com.puc.moeda.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> studentOptional = studentRepository.findByEmail(email);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    student.getEmail(),
                    student.getPassword(), // TODO: Use encoded password
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT"))
            );
        }

        Optional<Professor> professorOptional = professorRepository.findByEmail(email);
        if (professorOptional.isPresent()) {
            Professor professor = professorOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    professor.getEmail(),
                    professor.getPassword(), // TODO: Use encoded password
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_PROFESSOR"))
            );
        }

        Optional<Company> companyOptional = companyRepository.findByEmail(email);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    company.getEmail(),
                    company.getPassword(), // TODO: Use encoded password
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_COMPANY"))
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
