package com.alumni.blog.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name="users")
@NoArgsConstructor
@Getter
@Setter

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="user_name", nullable=false, length=100)
    private String name;
    private String email;
    private String password;
    private String confirmpassword;
    private String about;
    private String universityID;
    private String cnic;
    private String currentOrganization;
    private String currentDesignation;
    private String currentCountry;
    private String currentCity;
    private String campusLocation;
    private String graduationYear;
    private String degreeProgram;
    private String major;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts=new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "\"user\"", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "\"role\"", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "attendees")
    private Set<Event> events = new HashSet<>();


    //    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//      List<SimpleGrantedAuthority> authorities=  this.roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//        return authorities;
//    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        System.out.println("User Authorities: " + authorities);
        return authorities;
    }
//@ManyToMany(fetch = FetchType.EAGER)
//@JoinTable(
//    name = "user_role",
//    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
//)
//private Set<Role> roles = new HashSet<>();


    @Override
    public String getUsername() {
        return this.email;
    }

    // @Override
    public String getUserName() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
