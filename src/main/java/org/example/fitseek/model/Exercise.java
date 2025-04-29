package org.example.fitseek.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <h4>Info:</h4>
 * Entity class that represents exercise in the training system.
 *
 * Mapped to the {@code exercises} table in database
 *  <li>{@link #id} – unique identifier of the exercise (primary key).</li>
 *  <li>{@link #name} – name of the exercise (e.g. "Leg press", "Lateral raises").</li>
 *  <li>{@link #muscle} - object with name of the muscle (e.g. "Chest", "Back").</li>
 *  <li>{@link #day} - object with name of the day (e.g. "Monday", "Tuesday").</li>
 *  <li>{@link #gender} - object with name of the gender (e.g. "Male", "Female"</li>
 *  <li>{@link #recommendationList} - list of the recommendations for exercise</li>
 *
 * @see Muscle
 * @see Day
 * @see Gender
 * @see Recommendation
 */
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
    @JsonManagedReference
    private List<Recommendation> recommendationList = new ArrayList<>();
}
