package org.example.fitseek.gender;

import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.model.Gender;
import org.example.fitseek.repository.GenderRepository;
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
public class GenderRepositoryTest {
    @Autowired
    private GenderRepository genderRepository;

    private Gender expectedGender;

    @BeforeEach
    public void setUp() {
        genderRepository.deleteAll();
        expectedGender = new Gender();
        expectedGender.setName("Male");
        genderRepository.save(expectedGender);
    }

    @Test
    public void testGetByGenderNameSuccess() {
        Gender actualGender = genderRepository.findByName(expectedGender.getName());
        Assertions.assertEquals(actualGender, expectedGender);
    }

    @Test
    public void testGetByGenderNameFailWrongGenderName() {
        GenderRequest genderRequest = new GenderRequest();
        genderRequest.setName("Wrong");
        Gender actualGender = genderRepository.findByName(genderRequest.getName());

        Assertions.assertNull(actualGender);
    }
}
