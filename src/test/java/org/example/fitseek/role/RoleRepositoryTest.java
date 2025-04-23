package org.example.fitseek.role;

import org.example.fitseek.model.Role;
import org.example.fitseek.repository.RoleRepository;
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
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    private Role expectedRole;

    @BeforeEach
    public void setUp() {
        roleRepository.deleteAll();

        expectedRole = new Role();
        expectedRole.setName("ADMIN");

        roleRepository.save(expectedRole);
    }

    @Test
    public void testGetExpectedRoleSuccessful() {
        Role actualRole = roleRepository.findByName(expectedRole.getName());

        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(expectedRole.getName(), actualRole.getName());
    }

    @Test
    public void testGetExpectedRoleFailed() {
        Role actualRole = roleRepository.findByName("USER");

        Assertions.assertNull(actualRole);
    }
}
