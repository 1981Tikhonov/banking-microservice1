package com.bank.project.service;

import com.bank.project.entity.Agreement;
import com.bank.project.repository.AgreementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AgreementServiceTest {

    @Mock
    private AgreementRepository agreementRepository;

    @InjectMocks
    private AgreementService agreementService;

    private Agreement agreement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        agreement = new Agreement();
        agreement.setId(1L);
        agreement.setAccountId(1L);
        agreement.setProductId(1L);
        agreement.setStatus("ACTIVE");
        agreement.setSum(5000.0);
        agreement.setInterestRate(5.0);
        agreement.setCreatedAt(LocalDateTime.now());
        agreement.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateAgreement() {
        when(agreementRepository.save(any(Agreement.class))).thenReturn(agreement);

        Agreement createdAgreement = agreementService.createAgreement(agreement);

        assertNotNull(createdAgreement);
        assertEquals(agreement.getId(), createdAgreement.getId());
        verify(agreementRepository, times(1)).save(any(Agreement.class));
    }

    @Test
    void testGetAgreementById() {
        when(agreementRepository.findById(anyLong())).thenReturn(Optional.of(agreement));

        Agreement fetchedAgreement = agreementService.getAgreementById(1L);

        assertNotNull(fetchedAgreement);
        assertEquals(agreement.getId(), fetchedAgreement.getId());
        verify(agreementRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAgreementByIdNotFound() {
        when(agreementRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> agreementService.getAgreementById(999L));

        assertEquals("Agreement not found with ID: 999", exception.getMessage());
        verify(agreementRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdateAgreement() {
        Agreement updatedAgreementDetails = new Agreement();
        updatedAgreementDetails.setStatus("INACTIVE");
        updatedAgreementDetails.setSum(4000.0);
        updatedAgreementDetails.setInterestRate(4.5);

        when(agreementRepository.save(any(Agreement.class))).thenReturn(updatedAgreementDetails);
        when(agreementRepository.findById(anyLong())).thenReturn(Optional.of(agreement));

        Agreement updatedAgreement = agreementService.updateAgreement(1L, updatedAgreementDetails);

        assertNotNull(updatedAgreement);
        assertEquals("INACTIVE", updatedAgreement.getStatus());
        assertEquals(4000.0, updatedAgreement.getSum());
        verify(agreementRepository, times(1)).save(any(Agreement.class));
    }

    @Test
    void testDeleteAgreement() {
        when(agreementRepository.findById(anyLong())).thenReturn(Optional.of(agreement));

        agreementService.deleteAgreement(1L);

        verify(agreementRepository, times(1)).delete(any(Agreement.class));
    }

    @Test
    void testDeleteAgreementNotFound() {
        when(agreementRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> agreementService.deleteAgreement(999L));

        assertEquals("Agreement not found with ID: 999", exception.getMessage());
        verify(agreementRepository, times(0)).delete(any(Agreement.class));
    }

    @Test
    void testGetAgreementsByStatus() {
        when(agreementRepository.findByStatus(anyString())).thenReturn(List.of(agreement));

        List<Agreement> agreements = agreementService.getAgreementsByStatus("ACTIVE");

        assertNotNull(agreements);
        assertFalse(agreements.isEmpty());
        assertEquals("ACTIVE", agreements.get(0).getStatus());
        verify(agreementRepository, times(1)).findByStatus(anyString());
    }

    @Test
    void testGetAgreementsByAccountId() {
        when(agreementRepository.findByAccountId(anyLong())).thenReturn(List.of(agreement));

        List<Agreement> agreements = agreementService.getAgreementsByAccountId(1L);

        assertNotNull(agreements);
        assertFalse(agreements.isEmpty());
        verify(agreementRepository, times(1)).findByAccountId(anyLong());
    }

    @Test
    void testGetAgreementsByProductId() {
        when(agreementRepository.findByProductId(anyLong())).thenReturn(List.of(agreement));

        List<Agreement> agreements = agreementService.getAgreementsByProductId(1L);

        assertNotNull(agreements);
        assertFalse(agreements.isEmpty());
        verify(agreementRepository, times(1)).findByProductId(anyLong());
    }

    @Test
    void testGetAgreementsByInterestRate() {
        when(agreementRepository.findByInterestRateGreaterThan(anyDouble())).thenReturn(List.of(agreement));

        List<Agreement> agreements = agreementService.getAgreementsByInterestRate(4.0);

        assertNotNull(agreements);
        assertFalse(agreements.isEmpty());
        verify(agreementRepository, times(1)).findByInterestRateGreaterThan(anyDouble());
    }

    @Test
    void testGetAgreementsBySum() {
        when(agreementRepository.findBySumLessThan(anyDouble())).thenReturn(List.of(agreement));

        List<Agreement> agreements = agreementService.getAgreementsBySum(6000.0);

        assertNotNull(agreements);
        assertFalse(agreements.isEmpty());
        verify(agreementRepository, times(1)).findBySumLessThan(anyDouble());
    }

    @Test
    void testGetAgreementsByCreatedAtBetween() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        when(agreementRepository.findByCreatedAtBetween(any(), any())).thenReturn(List.of(agreement));

        List<Agreement> agreements = agreementService.getAgreementsByCreatedAtBetween(start, end);

        assertNotNull(agreements);
        assertFalse(agreements.isEmpty());
        verify(agreementRepository, times(1)).findByCreatedAtBetween(any(), any());
    }

    @Test
    void testGetAgreementsByUpdatedAtAfter() {
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(1);
        when(agreementRepository.findByUpdatedAtAfter(any())).thenReturn(List.of(agreement));

        List<Agreement> agreements = agreementService.getAgreementsByUpdatedAtAfter(updatedAt);

        assertNotNull(agreements);
        assertFalse(agreements.isEmpty());
        verify(agreementRepository, times(1)).findByUpdatedAtAfter(any());
    }
}
