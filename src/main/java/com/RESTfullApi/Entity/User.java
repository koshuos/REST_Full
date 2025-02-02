package com.RESTfullApi.Entity;

import com.RESTfullApi.Validation.ValidAge;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    private String name;

    @Column(nullable = false, unique = true)

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @Past(message = "Birthdate must be in te past")
    @ValidAge
    private LocalDate birthDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
