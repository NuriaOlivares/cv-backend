package com.nuria.cvplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "profiles", indexes = @Index(name = "idx_profile_email", columnList = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;
    private String title;

    @Column(length = 2000)
    private String summary;

    private String phone;
    private String secondaryPhone;
    private String email;
    private String linkedin;
    private String github;

    @JsonIgnore
    @OneToMany(mappedBy = "profile",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Experience> experiences;

    @JsonIgnore
    @OneToMany(mappedBy = "profile",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Project> projects;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> education;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certification> certifications;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Language> languages;
}
