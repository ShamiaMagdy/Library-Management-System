package com.library.library;

import com.library.library.entity.Patron;
import com.library.library.repository.PatronRepository;
import com.library.library.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    private Patron patron;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setPhoneNumber("12345678901");
        patron.setContactInformation("john.doe@example.com");
    }

    @Test
    void testGetAllPatrons() {
        patronService.getAllPatrons();
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void testGetPatronById_Success() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        Optional<Patron> foundPatron = patronService.getPatronById(1L);
        assertTrue(foundPatron.isPresent(), "Expected an Optional containing a Patron");
        assertEquals("John Doe", foundPatron.get().getName(), "Patron name should match");
    }

    @Test
    void testGetPatronById_NotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Patron> foundPatron = patronService.getPatronById(1L);
        assertFalse(!foundPatron.isPresent(), "Expected Optional to be empty");
    }

    @Test
    void testAddPatron_Success() {
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);
        when(patronRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(patronRepository.findByContactInformation(any(String.class))).thenReturn(Optional.empty());
        when(patronRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.empty());

        Patron savedPatron = patronService.addPatron(patron);
        assertNotNull(savedPatron, "Saved patron should not be null");
        assertEquals("John Doe", savedPatron.getName(), "Patron name should match");
    }

    @Test
    void testAddPatron_DuplicateName() {
        when(patronRepository.findByName(any(String.class))).thenReturn(Optional.of(patron));
        when(patronRepository.findByContactInformation(any(String.class))).thenReturn(Optional.empty());
        when(patronRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            patronService.addPatron(patron);
        });
        assertEquals("Patron Attributes Must Be Unique.", thrown.getMessage());
    }

    @Test
    void testUpdatePatron_Success() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);
        when(patronRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(patronRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.empty());
        when(patronRepository.findByContactInformation(any(String.class))).thenReturn(Optional.empty());

        Patron updatedPatron = new Patron();
        updatedPatron.setName("Jane Doe");
        updatedPatron.setPhoneNumber("98765432101");
        updatedPatron.setContactInformation("jane.doe@example.com");

        Patron result = patronService.updatePatron(1L, updatedPatron);

        assertEquals("Jane Doe", result.getName(), "Updated patron name should match");
        assertEquals("98765432101", result.getPhoneNumber(), "Updated phone number should match");
        assertEquals("jane.doe@example.com", result.getContactInformation(), "Updated contact information should match");
    }

    @Test
    void testUpdatePatron_NotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        Patron updatedPatron = new Patron();
        updatedPatron.setName("Jane Doe");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            patronService.updatePatron(1L, updatedPatron);
        });

        assertEquals("Patron Id 1 Not Found.", thrown.getMessage(), "Exception message should match");
    }

    @Test
    void testDeletePatron_Success() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        doNothing().when(patronRepository).deleteById(1L);

        boolean isDeleted = patronService.deletePatron(1L);
        assertTrue(isDeleted, "Patron is deleted successfully");
        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatron_NotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        boolean isDeleted = patronService.deletePatron(1L);
        assertFalse(isDeleted, "Fail if patron is not found");
    }
}