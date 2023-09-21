package com.eregister.controller;


import com.eregister.model.SearchForm;
import com.eregister.model.Student;
import com.eregister.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/eregistrar")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping(value = {"/home"})
    public String displayHomePage(){

        return "home";
    }

    @GetMapping("students")
    public String listStudent(Model model){
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "student/list";
    }
    @GetMapping("students/add")
    public String showAddStudentForm(Model model){
        model.addAttribute("student", new Student());
        return "student/add";
    }
    @PostMapping("students/add")
    public String addStudent(@ModelAttribute("student") Student student){
        studentRepository.save(student);
        return "redirect:/eregistrar/students";
    }
    @GetMapping("students/edit/{id}")
    public String showEditStudentForm(@PathVariable("id") Integer id, Model model){
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null){
            return "redirect:/eregistrar/students";
        }
        model.addAttribute("student", student);
        return "student/edit";
    }
    @PostMapping("students/edit/{id}")
    public String editStudent(@PathVariable("id") Integer id, @ModelAttribute("student") Student updatedStudent){
        Student student = studentRepository.findById(id).orElse(null);
        if(student != null){
            student.setStudentNumber(updatedStudent.getStudentNumber());
            student.setFirstNname(updatedStudent.getFirstNname());
            student.setMiddleName(updatedStudent.getMiddleName());
            student.setLastName(updatedStudent.getLastName());
            student.setCgpa(updatedStudent.getCgpa());
            student.setEnrollmentDate(updatedStudent.getEnrollmentDate());
            student.setIsInternational(updatedStudent.getIsInternational());

            studentRepository.save(student);
        }
        return "redirect:/eregistrar/students";
    }
    @GetMapping("/students/delete/{id}")
    public String showDeleteConfirmation(@PathVariable Integer id, Model model) {
        // Retrieve student by ID from the database
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            model.addAttribute("student", student);
            return "student/delete"; // Assuming "deleteConfirmation" is the name of your confirmation page template
        } else {
            // Handle the case where the student with the given ID is not found
            return "error"; // You should have an error page to handle such cases
        }
    }

    @PostMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Integer id){
        studentRepository.deleteById(id);
        return "redirect:/eregistrar/students";
    }
    @PostMapping("/students/search")
    public String searchStudents(@ModelAttribute("searchForm") SearchForm searchForm, Model model) {
        String searchTerm = searchForm.getSearchTerm();
        Student searchResults = studentRepository.findStudentByStudentNumber(searchTerm);
        model.addAttribute("students", searchResults);
        return "student/list"; // Assuming you have a template for displaying the search results
    }

}
