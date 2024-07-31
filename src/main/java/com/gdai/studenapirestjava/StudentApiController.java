package com.gdai.studenapirestjava;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
public class StudentApiController {

    private static final List<Student> listStudent = new ArrayList<Student>();
    private static Integer studentId =1;

    @GetMapping
    public ResponseEntity<List<Student>> getStudents(){
        if(listStudent.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(listStudent);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student){
        student.setId(studentId++);
        listStudent.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }


    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getOneStudent(@PathVariable Integer studentId) {
        Optional<Student> studentOptional = findStudentById(studentId);
        return studentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PatchMapping("/{studentId}")
    public ResponseEntity<Void> updatedStudent (@PathVariable Integer studentId, @RequestBody Student student) {

        Optional<Student> studentOptional = findStudentById(studentId);
        if (studentOptional.isEmpty())  return ResponseEntity.notFound().build();

        List<Student> updatedList = listStudent.stream().map(studentData -> {
            if(studentData.getId().equals(studentId)) {
                studentData.setName(student.getName());
                return studentData;
            }
            return studentData;
        }).toList();

        listStudent.clear();
        listStudent.addAll(updatedList);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer studentId ){

        Optional<Student> studentFound = this.findStudentById(studentId);

        if (studentFound.isEmpty())  return ResponseEntity.notFound().build();

        listStudent.remove(studentFound.get());
        return ResponseEntity.noContent().build();
    }

    private Optional<Student> findStudentById(Integer studentId) {
        return listStudent.stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst();
    }

}
