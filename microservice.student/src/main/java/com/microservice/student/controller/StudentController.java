package com.microservice.student.controller;

import com.microservice.student.entities.Student;
import com.microservice.student.services.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:8080")
public class StudentController {

    private Long loggedInStudentId; // Variable para almacenar el ID del estudiante logueado

    @Autowired
    private IStudentService studentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStudent(@RequestBody Student student){
        studentService.save(student);
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllStudent(){
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return ResponseEntity.ok(studentService.findById(id));
    }

    @GetMapping("/search-by-course/{idCourse}")
    public ResponseEntity<?> findByIdCourse(@PathVariable Long idCourse){
        return ResponseEntity.ok(studentService.findByIdCourse(idCourse));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Student loginCredentials) {
        Student authenticatedStudent = studentService.authenticateStudent(loginCredentials.getEmail(), loginCredentials.getPassword());

        if (authenticatedStudent != null) {
            loggedInStudentId = authenticatedStudent.getId(); // Almacena el ID del estudiante logueado
            return ResponseEntity.ok(authenticatedStudent);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/search/loggedin")
    public ResponseEntity<?> getLoggedInStudent() {
        // Utiliza el ID del estudiante logueado
        if (loggedInStudentId != null) {
            Student student = studentService.findById(loggedInStudentId);

            if (student != null) {
                return ResponseEntity.ok(student);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            // Manejo si no hay un estudiante logueado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
