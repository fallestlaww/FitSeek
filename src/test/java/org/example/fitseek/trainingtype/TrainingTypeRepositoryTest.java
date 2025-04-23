package org.example.fitseek.trainingtype;

import org.example.fitseek.dto.request.TrainingTypeRequest;
import org.example.fitseek.model.TrainingType;
import org.example.fitseek.repository.TrainingTypeRepository;
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
public class TrainingTypeRepositoryTest {
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    private TrainingTypeRequest trainingTypeRequest;
    private TrainingType trainingType;

    @BeforeEach
    public void setUp() {
        trainingTypeRepository.deleteAll();
        trainingTypeRequest = new TrainingTypeRequest();
        trainingTypeRequest.setName("Test Training Type");

        trainingType = new TrainingType();
        trainingType.setName("Test Training Type");

        trainingTypeRepository.save(trainingType);
    }

    @Test
    public void testTrainingTypeRepositorySuccess() {
        TrainingType actualTrainingType = trainingTypeRepository.findByName(trainingTypeRequest.getName());

        Assertions.assertNotNull(actualTrainingType);
        Assertions.assertEquals(actualTrainingType.getName(), trainingType.getName());
    }

    @Test
    public void testTrainingTypeRepositoryFailureWrongName() {
        trainingTypeRequest.setName("wrong");
        TrainingType actualTrainingType = trainingTypeRepository.findByName(trainingTypeRequest.getName());

        Assertions.assertNull(actualTrainingType);
    }
}
