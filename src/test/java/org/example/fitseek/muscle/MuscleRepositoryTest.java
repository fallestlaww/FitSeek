package org.example.fitseek.muscle;

import org.example.fitseek.dto.request.MuscleRequest;
import org.example.fitseek.model.Muscle;
import org.example.fitseek.repository.MuscleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.test.database.replace=NONE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.sql.init.mode=never"
})
public class MuscleRepositoryTest {
    @Autowired
    private MuscleRepository muscleRepository;

    private Muscle expectedMuscle;

    @BeforeEach
    public void setUp() {
        muscleRepository.deleteAll();
        expectedMuscle = new Muscle();
        expectedMuscle.setName("Biceps");
        muscleRepository.save(expectedMuscle);
    }

    @Test
    public void testGetMuscleSuccessful() {
        MuscleRequest muscleRequest = new MuscleRequest();
        muscleRequest.setName("Biceps");

        Muscle actualMuscle = muscleRepository.findByName(muscleRequest.getName());

        Assertions.assertNotNull(actualMuscle);
        Assertions.assertEquals(actualMuscle.getName(), expectedMuscle.getName());
    }

    @Test
    public void testGetMuscleFailed() {
        MuscleRequest muscleRequest = new MuscleRequest();
        muscleRequest.setName("Triceps");

        Muscle muscle = muscleRepository.findByName(muscleRequest.getName());

        Assertions.assertNull(muscle);
    }
}
