package com.alumni.blog.payloads;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private int id;
    @NotEmpty
    @Size(min = 3, message="Name must be of minimum length 3")
    private String name;
    @Email(message = "Email is not valid")
    private String email;
    @NotEmpty
    @Size(min=8, message = "Password must be of minimum 8 characters")
//    @JsonIgnore
    private String password;
    @Size(min=8, message = "Password must be of minimum 8 characters")
//    @JsonIgnore
    private String confirmpassword;
    @NotEmpty
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

}
