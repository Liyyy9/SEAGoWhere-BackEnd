package com.example.seagowhere.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.After;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "package")
public class Packages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "package_id")
    private Long id;

    @NotNull(message = "Category Id cannot be blank.")
    @ManyToOne(cascade =  CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    @Column(nullable = false, name = "package_name")
    @NotBlank(message = "Package name cannot be blank.")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Country cannot be blank.")
    private String country;

    @Column(nullable = false, name = "package_blurb")
    @NotBlank(message = "Blurb name cannot be blank.")
    private String blurb;

    @Column(nullable = false, columnDefinition = "LONGTEXT", name = "package_desc")
    @NotBlank(message = "Package description cannot be blank.")
    private String desc;

    @Column(nullable = false)
    @NotNull(message = "Price cannot be blank.")
    private BigDecimal price;

    @Column(nullable = false)
    @NotNull(message = "Start date cannot be blank.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate start_date;

    @Column(nullable = false)
    @NotNull(message = "End date cannot be blank.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate end_date;

    @Column(nullable = false)
    @NotNull(message = "Number of days cannot be blank.")
    private int no_of_days;

    @Column(nullable = false)
    @NotNull(message = "Number of nights cannot be blank.")
    private int no_of_nights;

    @Column
    @NotBlank(message = "Image URL cannot be blank")
    @Size(max = 255, message = "Image URL must be less than 255 characters.")
    @Pattern(
            regexp = "([^\s]+(\\.(?i)(jpg|jpeg|png))$)",
            message = "Please use a valid image (.jpg, .jpeg, .png)."
    )
    private String image_url;

}
