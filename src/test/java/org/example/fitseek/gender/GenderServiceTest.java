package org.example.fitseek.gender;

import jakarta.persistence.EntityNotFoundException;
import org.example.fitseek.dto.request.GenderRequest;
import org.example.fitseek.exception.exceptions.EntityNullException;
import org.example.fitseek.model.Gender;
import org.example.fitseek.repository.GenderRepository;
import org.example.fitseek.service.impl.GenderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenderServiceTest {
    @InjectMocks
    private GenderServiceImpl genderService;
    @Mock
    private GenderRepository genderRepository;
    private GenderRequest genderRequest;
    private Gender gender;

    @BeforeEach
    public void setUp() {
        genderRequest = new GenderRequest();
        genderRequest.setName("Female");

        gender = new Gender();
        gender.setName("Female");
    }

    @Test
    public void chooseGenderSuccessful() {
        when(genderRepository.findByName(genderRequest.getName())).thenReturn(gender);
        Gender foundGender = genderService.getGenderByName(genderRequest);

        Assertions.assertEquals(gender, foundGender);
    }

    @Test
    public void chooseGenderFailedRequestIsNull() {
        genderRequest.setName(null);

        Assertions.assertThrows(EntityNullException.class, () -> genderService.getGenderByName(genderRequest));
    }

    @Test
    public void chooseGenderFailedWrongGenderName() {
        when(genderRepository.findByName(genderRequest.getName())).thenReturn(null);
        Assertions.assertThrows(EntityNotFoundException.class, () -> genderService.getGenderByName(genderRequest));
    }
}
