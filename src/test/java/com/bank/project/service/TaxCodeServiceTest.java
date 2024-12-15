package com.bank.project.service;

import com.bank.project.entity.TaxCode;
import com.bank.project.repository.TaxCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

class TaxCodeServiceTest {

    @Mock
    private TaxCodeRepository taxCodeRepository;

    @InjectMocks
    private TaxCodeService taxCodeService;

    private TaxCode taxCode;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создание mock налогового кода для тестов
        taxCode = new TaxCode();
        taxCode.setId(1L);
        taxCode.setCode("TAX123");
        taxCode.setName("Standard Tax");
    }

    @Test
    void testFindTaxCodeById() {
        when(taxCodeRepository.findById(1L)).thenReturn(Optional.of(taxCode));

        Optional<TaxCode> foundTaxCode = taxCodeService.findTaxCodeById(1L);

        assertTrue(foundTaxCode.isPresent());
        assertEquals("TAX123", foundTaxCode.get().getCode());
        verify(taxCodeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindTaxCodeById_NotFound() {
        when(taxCodeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TaxCode> foundTaxCode = taxCodeService.findTaxCodeById(1L);

        assertFalse(foundTaxCode.isPresent());
        verify(taxCodeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindTaxCodeByCode() {
        when(taxCodeRepository.findByCode("TAX123")).thenReturn(Optional.of(taxCode));

        Optional<TaxCode> foundTaxCode = taxCodeService.findTaxCodeByCode("TAX123");

        assertTrue(foundTaxCode.isPresent());
        assertEquals("Standard Tax", foundTaxCode.get().getName());
        verify(taxCodeRepository, times(1)).findByCode("TAX123");
    }

    @Test
    void testFindTaxCodeByCode_NotFound() {
        when(taxCodeRepository.findByCode("NONEXISTENT")).thenReturn(Optional.empty());

        Optional<TaxCode> foundTaxCode = taxCodeService.findTaxCodeByCode("NONEXISTENT");

        assertFalse(foundTaxCode.isPresent());
        verify(taxCodeRepository, times(1)).findByCode("NONEXISTENT");
    }

    @Test
    void testFindTaxCodeByName() {
        when(taxCodeRepository.findByName("Standard Tax")).thenReturn(Optional.of(taxCode));

        Optional<TaxCode> foundTaxCode = taxCodeService.findTaxCodeByName("Standard Tax");

        assertTrue(foundTaxCode.isPresent());
        assertEquals("TAX123", foundTaxCode.get().getCode());
        verify(taxCodeRepository, times(1)).findByName("Standard Tax");
    }

    @Test
    void testFindTaxCodeByName_NotFound() {
        when(taxCodeRepository.findByName("Non-existent Tax")).thenReturn(Optional.empty());

        Optional<TaxCode> foundTaxCode = taxCodeService.findTaxCodeByName("Non-existent Tax");

        assertFalse(foundTaxCode.isPresent());
        verify(taxCodeRepository, times(1)).findByName("Non-existent Tax");
    }
}
