package com.alumni.blog.payloads;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private Integer categoryId;
    @NotBlank
    @Size(min=4, message = "Minimum size for category title must be 4")
    private String categoryTitle;
    @NotBlank
    @Size(min=10, message = "Minimum size for category description must be 4")
    private String categoryDescription;

}
