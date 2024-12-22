package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Assignment;

@Repository
public class AssignmentRepository {

    public List<Assignment> assignments = new ArrayList<>();

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
    
   @SuppressWarnings("unlikely-arg-type")
public List<Assignment> findByCourseIdAndStudentId(Long courseId, Long studentId) {
    return assignments.stream()
            .filter(a -> courseId != null && courseId.equals(a.getCourseId()) && studentId.equals(a.getStudentId()))
            .collect(Collectors.toList());
}

public boolean existsById(Long assessmentId) {
    return assignments.stream().anyMatch(a -> a.getId().equals(assessmentId));
}

    
    

}
