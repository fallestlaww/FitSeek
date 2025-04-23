package org.example.fitseek.day;

import org.example.fitseek.dto.request.DayRequest;
import org.example.fitseek.model.Day;
import org.example.fitseek.repository.DayRepository;
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
public class DayRepositoryTest {
    @Autowired
    private DayRepository dayRepository;

    private Day expectedDay;

    @BeforeEach
    public void setUp() {
        dayRepository.deleteAll();

        expectedDay = new Day();
        expectedDay.setName("Monday");

        dayRepository.save(expectedDay);
    }

    @Test
    public void testGetExpectedDaySuccessful() {
        DayRequest dayRequest = new DayRequest();
        dayRequest.setName("Monday");

        Day actualDay = dayRepository.findByName(dayRequest.getName());

        Assertions.assertNotNull(actualDay);
        Assertions.assertEquals(expectedDay.getName(), actualDay.getName());
    }

    @Test
    public void testGetExpectedDayFailed() {
        DayRequest dayRequest = new DayRequest();
        dayRequest.setName("Wrong");

        Day actualDay = dayRepository.findByName(dayRequest.getName());
        Assertions.assertNull(actualDay);
    }
}
