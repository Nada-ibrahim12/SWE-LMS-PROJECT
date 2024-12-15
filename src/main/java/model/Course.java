package model;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    private User instructor;

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();

    // Constructors
    public Course() {}
    public Course(String title, String description, User instructor) {
        this.title = title;
        this.description = description;
        this.instructor = instructor;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public User getInstructor() { return instructor; }
    public void setInstructor(User instructor) { this.instructor = instructor; }
}