
package com.example.portal;
import org.springframework.security.web.csrf.CsrfToken;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final StudentRepository studentRepository;
    private final FeeRepository feeRepository;
    private final MarkRepository markRepository;
    private final AssignmentRepository assignmentRepository;

    public AdminController(
    StudentRepository studentRepository,
    FeeRepository feeRepository,
    MarkRepository markRepository,
    AssignmentRepository assignmentRepository) {

    this.studentRepository = studentRepository;
    this.feeRepository = feeRepository;
    this.markRepository = markRepository;
    this.assignmentRepository = assignmentRepository;
}

    // Display all students
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Add a new student
    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    // Update existing student marks
@PutMapping("/marks/{id}")
public ResponseEntity<Mark> updateMark(
        @PathVariable Long id,
        @RequestBody Mark updatedMark) {

    return markRepository.findById(id)
            .map(mark -> {

                if (updatedMark.getMarksObtained() == null ||
                    updatedMark.getMaxMarks() == null ||
                    updatedMark.getMarksObtained() < 0 ||
                    updatedMark.getMaxMarks() <= 0 ||
                    updatedMark.getMarksObtained() > updatedMark.getMaxMarks()) {

                    return ResponseEntity.badRequest().<Mark>build();
                }

                mark.setSemester(updatedMark.getSemester());
                mark.setSubject(updatedMark.getSubject());
                mark.setMarksObtained(updatedMark.getMarksObtained());
                mark.setMaxMarks(updatedMark.getMaxMarks());

                Mark savedMark = markRepository.save(mark);

                return ResponseEntity.ok(savedMark);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
}
// Delete student marks
@DeleteMapping("/marks/{id}")
public ResponseEntity<Void> deleteMark(@PathVariable Long id) {

    if (!markRepository.existsById(id)) {
        return ResponseEntity.notFound().build();
    }

    markRepository.deleteById(id);

    return ResponseEntity.noContent().build();
}
    // Display all student marks
@GetMapping("/marks")
public List<Mark> getAllMarks() {
    return markRepository.findAll();
}

// Add marks for a student
@PostMapping("/marks")
public ResponseEntity<Mark> addMark(@RequestBody Mark mark) {

    if (!studentRepository.existsById(mark.getStudentId())) {
        return ResponseEntity.notFound().build();
    }

    if (mark.getMarksObtained() == null ||
        mark.getMaxMarks() == null ||
        mark.getMarksObtained() < 0 ||
        mark.getMaxMarks() <= 0 ||
        mark.getMarksObtained() > mark.getMaxMarks()) {

        return ResponseEntity.badRequest().build();
    }

    mark.setId(null);

    Mark savedMark = markRepository.save(mark);

    return ResponseEntity.ok(savedMark);
}
    // Display all student fee records
    @GetMapping("/fees")
    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    // Add fee details for a student
    @PostMapping("/fees")
    public ResponseEntity<Fee> addFee(@RequestBody Fee fee) {

        if (!studentRepository.existsById(fee.getStudentId())) {
            return ResponseEntity.notFound().build();
        }

        if (feeRepository.findByStudentId(fee.getStudentId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        fee.setId(null);

        Fee savedFee = feeRepository.save(fee);

        return ResponseEntity.ok(savedFee);
    }

    // Update student fee payment
    @PutMapping("/fees/{id}")
    public ResponseEntity<Fee> updateFee(
            @PathVariable Long id,
            @RequestBody Fee updatedFee) {

        return feeRepository.findById(id)
                .map(fee -> {

                    if (updatedFee.getPaid() == null ||
                        updatedFee.getPaid().compareTo(BigDecimal.ZERO) < 0 ||
                        updatedFee.getPaid().compareTo(fee.getTotalFee()) > 0) {
                        return ResponseEntity.badRequest().<Fee>build();
                    }

                    fee.setPaid(updatedFee.getPaid());
                    fee.setLastPaymentDate(updatedFee.getLastPaymentDate());

                    Fee savedFee = feeRepository.save(fee);

                    return ResponseEntity.ok(savedFee);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/csrf")
public Map<String, String> getCsrfToken(CsrfToken csrfToken) {
    return Map.of(
        "token", csrfToken.getToken(),
        "headerName", csrfToken.getHeaderName()
    );
     }     // Display all student assignments
// Display all student assignments
@GetMapping("/assignments")
public List<Assignment> getAllAssignments() {
    return assignmentRepository.findAll();
}

// Add a new assignment
@PostMapping("/assignments")
public ResponseEntity<Assignment> addAssignment(
        @RequestBody Assignment assignment) {

    if (assignment.getStudentId() == null ||
        !studentRepository.existsById(assignment.getStudentId())) {
        return ResponseEntity.notFound().build();
    }

    if (assignment.getSubject() == null ||
        assignment.getSubject().isBlank() ||
        assignment.getTitle() == null ||
        assignment.getTitle().isBlank() ||
        assignment.getDueDate() == null ||
        assignment.getStatus() == null ||
        assignment.getStatus().isBlank()) {
        return ResponseEntity.badRequest().build();
    }

    assignment.setId(null);

    Assignment savedAssignment =
            assignmentRepository.save(assignment);

    return ResponseEntity.ok(savedAssignment);
}

// Update an existing assignment
@PutMapping("/assignments/{id}")
public ResponseEntity<Assignment> updateAssignment(
        @PathVariable Long id,
        @RequestBody Assignment updatedAssignment) {

    return assignmentRepository.findById(id)
            .map(assignment -> {

                if (updatedAssignment.getSubject() == null ||
                    updatedAssignment.getSubject().isBlank() ||
                    updatedAssignment.getTitle() == null ||
                    updatedAssignment.getTitle().isBlank() ||
                    updatedAssignment.getDueDate() == null ||
                    updatedAssignment.getStatus() == null ||
                    updatedAssignment.getStatus().isBlank()) {

                    return ResponseEntity.badRequest()
                            .<Assignment>build();
                }

                assignment.setSubject(updatedAssignment.getSubject());
                assignment.setTitle(updatedAssignment.getTitle());
                assignment.setDueDate(updatedAssignment.getDueDate());
                assignment.setStatus(updatedAssignment.getStatus());

                Assignment savedAssignment =
                        assignmentRepository.save(assignment);

                return ResponseEntity.ok(savedAssignment);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
}

// Delete an assignment
@DeleteMapping("/assignments/{id}")
public ResponseEntity<Void> deleteAssignment(
        @PathVariable Long id) {

    if (!assignmentRepository.existsById(id)) {
        return ResponseEntity.notFound().build();
    }

    assignmentRepository.deleteById(id);

    return ResponseEntity.noContent().build();
}

// Delete an assignment


    // Update an existing student
    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student updatedStudent) {

        return studentRepository.findById(id)
                .map(student -> {

                    student.setName(updatedStudent.getName());
                    student.setRollNo(updatedStudent.getRollNo());
                    student.setRegistrationNo(updatedStudent.getRegistrationNo());
                    student.setCourse(updatedStudent.getCourse());
                    student.setSemester(updatedStudent.getSemester());
                    student.setDob(updatedStudent.getDob());

                    Student savedStudent =
                            studentRepository.save(student);

                    return ResponseEntity.ok(savedStudent);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Delete a student
    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {

        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
