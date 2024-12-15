package com.bank.project.controller;

import com.bank.project.entity.Currency;
import com.bank.project.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CurrencyService currencyService;

    @Test
    void testCreateCurrency() throws Exception {
        Currency currency = new Currency(1L, "USD", "US Dollar", "USD", 1.0);
        when(currencyService.createCurrency(any(Currency.class))).thenReturn(currency);

        mockMvc.perform(post("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"USD\",\"name\":\"US Dollar\",\"symbol\":\"USD\",\"exchangeRate\":1.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("US Dollar"))
                .andExpect(jsonPath("$.symbol").value("USD"))
                .andExpect(jsonPath("$.exchangeRate").value(1.0));

        verify(currencyService, times(1)).createCurrency(any(Currency.class));
    }

    @Test
    void testGetCurrencyById() throws Exception {
        Currency currency = new Currency(1L, "USD", "US Dollar", "USD", 1.0);
        when(currencyService.getCurrencyById(anyLong())).thenReturn(currency);

        mockMvc.perform(get("/api/currencies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("US Dollar"))
                .andExpect(jsonPath("$.symbol").value("USD"))
                .andExpect(jsonPath("$.exchangeRate").value(1.0));

        verify(currencyService, times(1)).getCurrencyById(anyLong());
    }

    @Test
    void testGetAllCurrencies() throws Exception {
        Currency currency1 = new Currency(1L, "USD", "US Dollar", "USD", 1.0);
        Currency currency2 = new Currency(2L, "EUR", "Euro", "EUR", 0.85);
        when(currencyService.getAllCurrencies()).thenReturn(Arrays.asList(currency1, currency2));

        mockMvc.perform(get("/api/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[1].code").value("EUR"));

        verify(currencyService, times(1)).getAllCurrencies();
    }

    @Test
    void testUpdateCurrency() throws Exception {
        Currency currency = new Currency(1L, "USD", "US Dollar", "USD", 1.0);
        when(currencyService.updateCurrency(anyLong(), any(Currency.class))).thenReturn(currency);

        mockMvc.perform(put("/api/currencies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"USD\",\"name\":\"US Dollar\",\"symbol\":\"USD\",\"exchangeRate\":1.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("US Dollar"))
                .andExpect(jsonPath("$.symbol").value("USD"))
                .andExpect(jsonPath("$.exchangeRate").value(1.0));

        verify(currencyService, times(1)).updateCurrency(anyLong(), any(Currency.class));
    }

    @Test
    void testDeleteCurrency() throws Exception {
        when(currencyService.deleteCurrency(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/currencies/1"))
                .andExpect(status().isNoContent());

        verify(currencyService, times(1)).deleteCurrency(anyLong());
    }

    @Test
    void testGetCurrencyByCode() throws Exception {
        Currency currency = new Currency(1L, "USD", "US Dollar", "USD", 1.0);
        when(currencyService.getCurrencyByCode(anyString())).thenReturn(currency);

        mockMvc.perform(get("/api/currencies/code/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("USD"));

        verify(currencyService, times(1)).getCurrencyByCode(anyString());
    }

    @Test
    void testGetCurrencyByName() throws Exception {
        Currency currency = new Currency(1L, "USD", "US Dollar", "USD", 1.0);
        when(currencyService.getCurrencyByName(anyString())).thenReturn(currency);

        mockMvc.perform(get("/api/currencies/name/US Dollar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("US Dollar"));

        verify(currencyService, times(1)).getCurrencyByName(anyString());
    }

    @Test
    void testGetCurrencyBySymbol() throws Exception {
        Currency currency = new Currency(1L, "USD", "US Dollar", "USD", 1.0);
        when(currencyService.getCurrencyBySymbol(anyString())).thenReturn(currency);

        mockMvc.perform(get("/api/currencies/symbol/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.symbol").value("USD"));

        verify(currencyService, times(1)).getCurrencyBySymbol(anyString());
    }

    @Test
    void testGetCurrencyByExchangeRate() throws Exception {
        Currency currency = new Currency(1L, "USD", "US Dollar", "USD", 1.0);
        when(currencyService.getCurrencyByExchangeRate(anyDouble())).thenReturn(currency);

        mockMvc.perform(get("/api/currencies/exchange-rate/1.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.exchangeRate").value(1.0));

        verify(currencyService, times(1)).getCurrencyByExchangeRate(anyDouble());
    }
}
