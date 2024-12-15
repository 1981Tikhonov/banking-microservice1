package com.bank.project.controller;

import com.bank.project.entity.Agreement;
import com.bank.project.service.AgreementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agreements")
@Tag(name = "Agreement Management", description = "Endpoints for managing bank agreements")
public class AgreementController {

    private static final Logger logger = LoggerFactory.getLogger(AgreementController.class);
    private final AgreementService agreementService;

    @Autowired
    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @Operation(summary = "Create a new agreement", description = "Creates a new agreement for a specific account.")
    @PostMapping
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) {
        logger.info("Creating new agreement for account ID: {}", agreement.getAccountId()); // передаем аргумент
        Agreement createdAgreement = agreementService.createAgreement(agreement);
        logger.info("Agreement created with ID: {}", createdAgreement.getId()); // передаем аргумент
        return ResponseEntity.ok(createdAgreement);
    }

    @Operation(summary = "Get agreement by ID", description = "Retrieves an agreement by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Agreement> getAgreementById(@Parameter(description = "ID of the agreement to retrieve") @PathVariable Long id) {
        logger.info("Fetching agreement with ID: {}", id); // передаем аргумент
        Agreement agreement = agreementService.getAgreementById(id);
        if (agreement != null) {
            logger.info("Agreement found with ID: {}", id); // передаем аргумент
            return ResponseEntity.ok(agreement);
        } else {
            logger.warn("Agreement with ID: {} not found", id); // передаем аргумент
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all agreements", description = "Fetches all agreements.")
    @GetMapping
    public ResponseEntity<List<Agreement>> getAllAgreements() {
        logger.info("Fetching all agreements.");
        List<Agreement> agreements = agreementService.getAllAgreements();
        return ResponseEntity.ok(agreements);
    }

    @Operation(summary = "Get agreements by status", description = "Fetches agreements based on their status.")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Agreement>> getAgreementsByStatus(@Parameter(description = "Status of the agreements to retrieve") @PathVariable String status) {
        logger.info("Fetching agreements with status: {}", status); // передаем аргумент
        List<Agreement> agreements = agreementService.getAgreementsByStatus(status);
        return ResponseEntity.ok(agreements);
    }

    @Operation(summary = "Get agreements by account ID", description = "Fetches agreements based on the associated account ID.")
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Agreement>> getAgreementsByAccountId(@Parameter(description = "Account ID associated with the agreements") @PathVariable Long accountId) {
        logger.info("Fetching agreements for account ID: {}", accountId); // передаем аргумент
        List<Agreement> agreements = agreementService.getAgreementsByAccountId(accountId);
        return ResponseEntity.ok(agreements);
    }

    @Operation(summary = "Get agreements by product ID", description = "Fetches agreements based on the associated product ID.")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Agreement>> getAgreementsByProductId(@Parameter(description = "Product ID associated with the agreements") @PathVariable Long productId) {
        logger.info("Fetching agreements for product ID: {}", productId); // передаем аргумент
        List<Agreement> agreements = agreementService.getAgreementsByProductId(productId);
        return ResponseEntity.ok(agreements);
    }

    @Operation(summary = "Get agreements by interest rate", description = "Fetches agreements based on the specified interest rate.")
    @GetMapping("/interest-rate/{interestRate}")
    public ResponseEntity<List<Agreement>> getAgreementsByInterestRate(@Parameter(description = "Interest rate of the agreements") @PathVariable Double interestRate) {
        logger.info("Fetching agreements with interest rate: {}", interestRate); // передаем аргумент
        List<Agreement> agreements = agreementService.getAgreementsByInterestRate(interestRate);
        return ResponseEntity.ok(agreements);
    }

    @Operation(summary = "Get agreements by sum", description = "Fetches agreements based on the specified sum.")
    @GetMapping("/sum/{sum}")
    public ResponseEntity<List<Agreement>> getAgreementsBySum(@Parameter(description = "Sum associated with the agreements") @PathVariable Double sum) {
        logger.info("Fetching agreements with sum: {}", sum); // передаем аргумент
        List<Agreement> agreements = agreementService.getAgreementsBySum(sum);
        return ResponseEntity.ok(agreements);
    }

    @Operation(summary = "Get agreements created within a date range", description = "Fetches agreements created between the specified start and end dates.")
    @GetMapping("/created-between")
    public ResponseEntity<List<Agreement>> getAgreementsByCreatedAtBetween(
            @Parameter(description = "Start date of the range") @RequestParam LocalDateTime startDate,
            @Parameter(description = "End date of the range") @RequestParam LocalDateTime endDate) {
        logger.info("Fetching agreements created between {} and {}", startDate, endDate); // передаем аргументы
        List<Agreement> agreements = agreementService.getAgreementsByCreatedAtBetween(startDate, endDate);
        return ResponseEntity.ok(agreements);
    }

    @Operation(summary = "Get agreements updated after a specific date", description = "Fetches agreements updated after the specified date.")
    @GetMapping("/updated-after")
    public ResponseEntity<List<Agreement>> getAgreementsByUpdatedAtAfter(
            @Parameter(description = "Date to filter agreements updated after") @RequestParam LocalDateTime updatedAt) {
        logger.info("Fetching agreements updated after: {}", updatedAt); // передаем аргумент
        List<Agreement> agreements = agreementService.getAgreementsByUpdatedAtAfter(updatedAt);
        return ResponseEntity.ok(agreements);
    }

    @Operation(summary = "Update an agreement", description = "Updates an existing agreement by its ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Agreement> updateAgreement(@Parameter(description = "ID of the agreement to update") @PathVariable Long id, @RequestBody Agreement agreement) {
        logger.info("Updating agreement with ID: {}", id); // передаем аргумент
        Agreement updatedAgreement = agreementService.updateAgreement(id, agreement);
        if (updatedAgreement != null) {
            logger.info("Agreement with ID: {} updated successfully", id); // передаем аргумент
            return ResponseEntity.ok(updatedAgreement);
        } else {
            logger.warn("Agreement with ID: {} not found for update", id); // передаем аргумент
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete an agreement", description = "Deletes an agreement by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgreement(@Parameter(description = "ID of the agreement to delete") @PathVariable Long id) {
        logger.info("Attempting to delete agreement with ID: {}", id); // передаем аргумент
        agreementService.deleteAgreement(id);
        logger.info("Agreement with ID: {} deleted successfully", id); // передаем аргумент
        return ResponseEntity.noContent().build();
    }
}
