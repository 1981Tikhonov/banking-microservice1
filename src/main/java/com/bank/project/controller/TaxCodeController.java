package com.bank.project.controller;

import com.bank.project.entity.TaxCode;
import com.bank.project.service.TaxCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax-codes")
public class TaxCodeController {

    private static final Logger logger = LoggerFactory.getLogger(TaxCodeController.class);

    private final TaxCodeService taxCodeService;

    @Autowired
    public TaxCodeController(TaxCodeService taxCodeService) {
        this.taxCodeService = taxCodeService;
    }

    @Operation(summary = "Get tax code by ID", description = "Fetch a tax code by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tax code found"),
            @ApiResponse(responseCode = "404", description = "Tax code not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaxCode> getTaxCodeById(
            @Parameter(description = "ID of the tax code to be fetched") @PathVariable Long id) {
        logger.info("Request to retrieve TaxCode with ID: {}", id);
        return taxCodeService.findTaxCodeById(id)
                .map(taxCode -> {
                    logger.info("TaxCode found: {}", taxCode);
                    return ResponseEntity.ok(taxCode);
                })
                .orElseGet(() -> {
                    logger.warn("TaxCode with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Get tax code by code", description = "Fetch a tax code by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tax code found"),
            @ApiResponse(responseCode = "404", description = "Tax code not found")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<TaxCode> getTaxCodeByCode(
            @Parameter(description = "Code of the tax code to be fetched") @PathVariable String code) {
        logger.info("Request to retrieve TaxCode with code: {}", code);
        return taxCodeService.findTaxCodeByCode(code)
                .map(taxCode -> {
                    logger.info("TaxCode found: {}", taxCode);
                    return ResponseEntity.ok(taxCode);
                })
                .orElseGet(() -> {
                    logger.warn("TaxCode with code: {} not found", code);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Get tax code by name", description = "Fetch a tax code by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tax code found"),
            @ApiResponse(responseCode = "404", description = "Tax code not found")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<TaxCode> getTaxCodeByName(
            @Parameter(description = "Name of the tax code to be fetched") @PathVariable String name) {
        logger.info("Request to retrieve TaxCode with name: {}", name);
        return taxCodeService.findTaxCodeByName(name)
                .map(taxCode -> {
                    logger.info("TaxCode found: {}", taxCode);
                    return ResponseEntity.ok(taxCode);
                })
                .orElseGet(() -> {
                    logger.warn("TaxCode with name: {} not found", name);
                    return ResponseEntity.notFound().build();
                });
    }
}
