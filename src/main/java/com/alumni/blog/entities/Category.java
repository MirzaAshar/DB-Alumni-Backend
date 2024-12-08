package com.alumni.blog.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="categories")
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    @Column(name="title", length=100, nullable=false)
    private String categoryTitle;
    private String categoryDescription;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)

    private List<Post> posts=new ArrayList<>();



}