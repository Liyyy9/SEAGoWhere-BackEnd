package com.example.seagowhere.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private Users users;


    @JoinColumn(name = "package_id", nullable = false)
    private Package aPackage;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "dob", nullable = false)
    private Date dob;

    @Column(name = "passport_no", nullable = false)
    private String passportNo;

    @Column(name = "passport_exp", nullable = false)
    private Date passportExp;

}
