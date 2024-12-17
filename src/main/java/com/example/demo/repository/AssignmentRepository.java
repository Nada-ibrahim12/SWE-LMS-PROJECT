package com.example.demo.repository;

import com.example.demo.model.Assignment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AssignmentRepository {

    public List<Assignment> assignments = new ArrayList<>();

    public List<Assignment> findAll(){
        return assignments;
    }
    public Assignment findById(Long id){
        for(Assignment assignment : assignments){
            if(assignment.getId() == id){
                return assignment;
            }
        }
        return null;
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
        Assignment assignment = findById(id);
        assignments.remove(assignment);
    }

}
