package com.bank.project.controller;

import com.bank.project.entity.Agreement;
import com.bank.project.service.AgreementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgreementController.class)
public class AgreementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AgreementService agreementService;

    @InjectMocks
    private AgreementController agreementController;

    @Autowired
    private ObjectMapper objectMapper;

    private Agreement agreement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        agreement = new Agreement();
        agreement.setId(1L);
        agreement.setAccountId(123L);
        agreement.setProductId(456L);
        agreement.setInterestRate(5.0);
        agreement.setSum(10000.0);
    }

    @Test
    void createAgreementTest() throws Exception {
        when(agreementService.createAgreement(any(Agreement.class))).thenReturn(agreement);

        mockMvc.perform(post("/api/agreements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agreement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountId").value(123))
                .andExpect(jsonPath("$.productId").value(456))
                .andExpect(jsonPath("$.interestRate").value(5.0))
                .andExpect(jsonPath("$.sum").value(10000.0));

        verify(agreementService, times(1)).createAgreement(any(Agreement.class));
    }

    @Test
    void getAgreementByIdTest() throws Exception {
        when(agreementService.getAgreementById(1L)).thenReturn(agreement);

        mockMvc.perform(get("/api/agreements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountId").value(123))
                .andExpect(jsonPath("$.productId").value(456))
                .andExpect(jsonPath("$.interestRate").value(5.0))
                .andExpect(jsonPath("$.sum").value(10000.0));

        verify(agreementService, times(1)).getAgreementById(1L);
    }

    @Test
    void getAllAgreementsTest() throws Exception {
        when(agreementService.getAllAgreements()).thenReturn(List.of(agreement));

        mockMvc.perform(get("/api/agreements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].accountId").value(123))
                .andExpect(jsonPath("$[0].productId").value(456))
                .andExpect(jsonPath("$[0].interestRate").value(5.0))
                .andExpect(jsonPath("$[0].sum").value(10000.0));

        verify(agreementService, times(1)).getAllAgreements();
    }

    @Test
    void updateAgreementTest() throws Exception {
        Agreement updatedAgreement = new Agreement();
        updatedAgreement.setId(1L);
        updatedAgreement.setAccountId(123L);
        updatedAgreement.setProductId(456L);
        updatedAgreement.setInterestRate(6.0);
        updatedAgreement.setSum(12000.0);

        when(agreementService.updateAgreement(eq(1L), any(Agreement.class))).thenReturn(updatedAgreement);

        mockMvc.perform(put("/api/agreements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAgreement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.interestRate").value(6.0))
                .andExpect(jsonPath("$.sum").value(12000.0));

        verify(agreementService, times(1)).updateAgreement(eq(1L), any(Agreement.class));
    }

    @Test
    void deleteAgreementTest() throws Exception {
        doNothing().when(agreementService).deleteAgreement(1L);

        mockMvc.perform(delete("/api/agreements/1"))
                .andExpect(status().isNoContent());

        verify(agreementService, times(1)).deleteAgreement(1L);
    }
}
