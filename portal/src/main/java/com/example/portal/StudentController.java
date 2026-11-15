
package com.example.portal;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentRepository studentRepository;
    private final FeeRepository feeRepository;
    private final MarkRepository markRepository;
    private final AssignmentRepository assignmentRepository;

    public StudentController(
            StudentRepository studentRepository,
            FeeRepository feeRepository,
            MarkRepository markRepository,
            AssignmentRepository assignmentRepository) {

        this.studentRepository = studentRepository;
        this.feeRepository = feeRepository;
        this.markRepository = markRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @GetMapping("/student")
    public Object getStudent(
            @RequestParam(required = false) String rollNo,
            @RequestParam(required = false) String registrationNo,
            @RequestParam String dob) {

        Optional<Student> student;

        if (rollNo != null && !rollNo.isBlank()) {
            student = studentRepository.findByRollNo(rollNo);
        } else if (registrationNo != null && !registrationNo.isBlank()) {
            student = studentRepository.findByRegistrationNo(registrationNo);
        } else {
            return Map.of("error", "Enter a roll number or registration number.");
        }

        if (student.isEmpty()) {
            return Map.of("error", "Student not found.");
        }

        Student s = student.get();

        if (s.getDob() == null || !s.getDob().equals(LocalDate.parse(dob))) {
            return Map.of("error", "Date of birth does not match.");
        }

        Long studentId = s.getId();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("student", s);
        response.put("fees", feeRepository.findByStudentId(studentId).orElse(null));
        response.put("marks", markRepository.findByStudentId(studentId));
        response.put("assignments", assignmentRepository.findByStudentId(studentId));

        return response;
    }
}
