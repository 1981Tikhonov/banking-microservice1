package com.bank.project.controller;

import com.bank.project.entity.TaxCode;
import com.bank.project.service.TaxCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaxCodeController.class)
public class TaxCodeControllerTest {

    @Mock
    private TaxCodeService taxCodeService;

    @InjectMocks
    private TaxCodeController taxCodeController;

    private MockMvc mockMvc;

    private TaxCode taxCode;

    @BeforeEach
    public void setup() {
        taxCode = new TaxCode();
        taxCode.setId(1L);
        taxCode.setCode("TAX123");
        taxCode.setName("Sample Tax Code");

        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(taxCodeController).build();
    }

    @Test
    public void testGetTaxCodeById_found() throws Exception {
        when(taxCodeService.findTaxCodeById(1L)).thenReturn(java.util.Optional.of(taxCode));

        mockMvc.perform(get("/api/tax-codes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("TAX123"))
                .andExpect(jsonPath("$.name").value("Sample Tax Code"));

        verify(taxCodeService).findTaxCodeById(1L);
    }

    @Test
    public void testGetTaxCodeById_notFound() throws Exception {
        when(taxCodeService.findTaxCodeById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/tax-codes/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(taxCodeService).findTaxCodeById(1L);
    }

    @Test
    public void testGetTaxCodeByCode_found() throws Exception {
        when(taxCodeService.findTaxCodeByCode("TAX123")).thenReturn(java.util.Optional.of(taxCode));

        mockMvc.perform(get("/api/tax-codes/code/{code}", "TAX123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("TAX123"))
                .andExpect(jsonPath("$.name").value("Sample Tax Code"));

        verify(taxCodeService).findTaxCodeByCode("TAX123");
    }

    @Test
    public void testGetTaxCodeByCode_notFound() throws Exception {
        when(taxCodeService.findTaxCodeByCode("TAX123")).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/tax-codes/code/{code}", "TAX123"))
                .andExpect(status().isNotFound());

        verify(taxCodeService).findTaxCodeByCode("TAX123");
    }

    @Test
    public void testGetTaxCodeByName_found() throws Exception {
        when(taxCodeService.findTaxCodeByName("Sample Tax Code")).thenReturn(java.util.Optional.of(taxCode));

        mockMvc.perform(get("/api/tax-codes/name/{name}", "Sample Tax Code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("TAX123"))
                .andExpect(jsonPath("$.name").value("Sample Tax Code"));

        verify(taxCodeService).findTaxCodeByName("Sample Tax Code");
    }

    @Test
    public void testGetTaxCodeByName_notFound() throws Exception {
        when(taxCodeService.findTaxCodeByName("Sample Tax Code")).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/tax-codes/name/{name}", "Sample Tax Code"))
                .andExpect(status().isNotFound());

        verify(taxCodeService).findTaxCodeByName("Sample Tax Code");
    }
}
