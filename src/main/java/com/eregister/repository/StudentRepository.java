package com.eregister.repository;

import com.eregister.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findStudentByStudentNumber(String studentNumber);
}
