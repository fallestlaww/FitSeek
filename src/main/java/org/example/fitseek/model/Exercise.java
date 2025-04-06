package org.example.fitseek.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name="muscles_id", referencedColumnName = "id")
    private Muscle muscle;
    @ManyToOne
    @JoinColumn(name="day_id", referencedColumnName = "id")
    private Day day;
    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Gender gender;
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> recommendationList = new ArrayList<>();
}
