package com.alumni.blog.entities;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int postId;
    private String postProfile;
    private String postDesc;
    private Integer reqExperience;
    @ElementCollection
    private List<String> postTechStack;
    private String userName;
    private String companyName;
    @ElementCollection
    private List<Integer> salaryRange = new ArrayList<>(2); // List of Integer to store min and max salary
    private String jobUrl;
    private String city;
    private String country;
}