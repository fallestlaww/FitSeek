package org.example.fitseek.user;

import org.example.fitseek.model.Gender;
import org.example.fitseek.model.User;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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

public class UserRepositoryTest {
    private final UserRepository userRepository;
    private final GenderRepository genderRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository, GenderRepository genderRepository) {
        this.userRepository = userRepository;
        this.genderRepository = genderRepository;
    }

    @Test
    public void findByEmail() {
        Gender gender = new Gender();
        gender.setName("Male");
        genderRepository.save(gender);
        User expectedUser = new User();
        expectedUser.setEmail("test@example.com");
        expectedUser.setPassword("password");
        expectedUser.setWeight(80.0);
        expectedUser.setAge(19);
        expectedUser.setGender(gender);
        userRepository.save(expectedUser);

        User actualUser = userRepository.findByEmail(expectedUser.getEmail());
        Assertions.assertEquals(expectedUser, actualUser);
        Assertions.assertNotNull(actualUser);
    }

    @Test
    public void findByWrongEmail() {
        Gender gender = new Gender();
        gender.setName("Male");
        genderRepository.save(gender);
        User expectedUser = new User();
        expectedUser.setEmail("test@example.com");
        expectedUser.setPassword("password");
        expectedUser.setWeight(80.0);
        expectedUser.setAge(19);
        expectedUser.setGender(gender);
        userRepository.save(expectedUser);

        String wrongEmail = "wrong_test@example.com";
        User actualUser = userRepository.findByEmail(wrongEmail);

        Assertions.assertNull(actualUser);
    }
}
