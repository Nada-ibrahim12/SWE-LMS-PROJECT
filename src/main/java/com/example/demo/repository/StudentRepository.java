package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {
    private List<Student> studentList = new ArrayList<>();

    // Retrieve all students
    public List<Student> findAll() {
        return studentList;
    }

    // Find a student by ID
    public Student findById(String id) {
        for (Student student : studentList) {
            if (student.getUserId() != null && student.getUserId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    // Save a new student
    public Student save(Student student) {
        studentList.add(student);
        return student;
    }
}
