package com.example.demo.repository;

import com.example.demo.model.Assignment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AssignmentRepository {

    public List<Assignment> assignments = new ArrayList<>();
    public List<Assignment> submissions = new ArrayList<>();

    public List<Assignment> findAll(){
        return assignments;
    }
    public Optional<Assignment> findById(Long id) {
        return assignments.stream()
                .filter(assignment -> assignment.getId().equals(id))
                .findFirst();
    }

    public Assignment save(Assignment assignment){
        assignments.add(assignment);
        return assignment;
    }
    public Assignment update(Assignment assignment){
        assignments.remove(assignment);
        assignments.add(assignment);
        return assignment;
    }
    public void delete(Assignment assignment){
        assignments.remove(assignment);
    }
    public void deleteById(Long id){
        Optional<Assignment> assignment = findById(id);
        assignments.remove(assignment);
    }

    public Assignment saveSubmissions(Assignment assignment){
        submissions.add(assignment);
        return assignment;
    }

}
