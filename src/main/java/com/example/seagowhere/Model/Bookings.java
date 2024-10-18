package com.example.seagowhere.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @OneToOne
    //@NotNull(message = "Package id cannot be blank.")
    @JoinColumn(nullable = false, name = "package_id")
    private Packages packages;    // (OneToOne) FK to package table

    @Column
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String nationality;

    @NotBlank
    @Column(nullable = false)
    private String gender;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dob;

    @NotBlank
    @Column(nullable = false)
    private String passportNo;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date passportExp;

}
