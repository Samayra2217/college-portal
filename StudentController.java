
package com.example.portal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/api/student")
    public Optional<Student> getStudent(
            @RequestParam(required = false) String rollNo,
            @RequestParam(required = false) String registrationNo) {

        if (rollNo != null && !rollNo.isBlank()) {
            return studentRepository.findByRollNo(rollNo);
        }

        if (registrationNo != null && !registrationNo.isBlank()) {
            return studentRepository.findByRegistrationNo(registrationNo);
        }

        return Optional.empty();
    }
}
