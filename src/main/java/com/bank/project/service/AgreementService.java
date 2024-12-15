package com.bank.project.service;

import com.bank.project.entity.Agreement;
import com.bank.project.repository.AgreementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgreementService {

    private static final Logger logger = LoggerFactory.getLogger(AgreementService.class);

    private final AgreementRepository agreementRepository;

    @Autowired
    public AgreementService(AgreementRepository agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    // Creates a new agreement
    public Agreement createAgreement(Agreement agreement) {
        logger.info("Creating a new agreement: {}", agreement);
        agreement.setCreatedAt(LocalDateTime.now());
        Agreement savedAgreement = agreementRepository.save(agreement);
        logger.info("Agreement successfully created: {}", savedAgreement);
        return savedAgreement;
    }

    // Retrieves an agreement by ID
    public Agreement getAgreementById(Long id) {
        logger.info("Retrieving agreement by ID: {}", id);
        Optional<Agreement> agreement = agreementRepository.findById(id);
        return agreement.orElseThrow(() -> {
            logger.error("Agreement not found with ID: {}", id);
            return new RuntimeException("Agreement not found with ID: " + id);
        });
    }

    // Retrieves all agreements
    public List<Agreement> getAllAgreements() {
        logger.info("Retrieving all agreements");
        List<Agreement> agreements = agreementRepository.findAll();
        logger.info("Found {} agreements", agreements.size());
        return agreements;
    }

    // Updates an existing agreement
    public Agreement updateAgreement(Long id, Agreement agreementDetails) {
        logger.info("Updating agreement with ID: {} using details: {}", id, agreementDetails);
        Agreement existingAgreement = getAgreementById(id);
        existingAgreement.setStatus(agreementDetails.getStatus());
        existingAgreement.setSum(agreementDetails.getSum());
        existingAgreement.setInterestRate(agreementDetails.getInterestRate());
        existingAgreement.setUpdatedAt(LocalDateTime.now());
        Agreement updatedAgreement = agreementRepository.save(existingAgreement);
        logger.info("Agreement successfully updated: {}", updatedAgreement);
        return updatedAgreement;
    }

    // Deletes an agreement by ID
    public void deleteAgreement(Long id) {
        logger.info("Deleting agreement with ID: {}", id);
        Agreement agreement = getAgreementById(id);
        agreementRepository.delete(agreement);
        logger.info("Agreement with ID: {} successfully deleted", id);
    }

    // Retrieves agreements by status
    public List<Agreement> getAgreementsByStatus(String status) {
        logger.info("Retrieving agreements by status: {}", status);
        List<Agreement> agreements = agreementRepository.findByStatus(status);
        logger.info("Found {} agreements with status {}", agreements.size(), status);
        return agreements;
    }

    // Retrieves agreements by account ID
    public List<Agreement> getAgreementsByAccountId(Long accountId) {
        logger.info("Retrieving agreements by account ID: {}", accountId);
        return agreementRepository.findByAccountId(accountId);
    }

    // Retrieves agreements by product ID
    public List<Agreement> getAgreementsByProductId(Long productId) {
        logger.info("Retrieving agreements by product ID: {}", productId);
        return agreementRepository.findByProductId(productId);
    }

    // Retrieves agreements with interest rate greater than the specified value
    public List<Agreement> getAgreementsByInterestRate(Double interestRate) {
        logger.info("Retrieving agreements with interest rate greater than: {}", interestRate);
        return agreementRepository.findByInterestRateGreaterThan(interestRate);
    }

    // Retrieves agreements with sum less than the specified value
    public List<Agreement> getAgreementsBySum(Double sum) {
        logger.info("Retrieving agreements with sum less than: {}", sum);
        return agreementRepository.findBySumLessThan(sum);
    }

    // Retrieves agreements created between two dates
    public List<Agreement> getAgreementsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Retrieving agreements created between {} and {}", startDate, endDate);
        return agreementRepository.findByCreatedAtBetween(startDate, endDate);
    }

    // Retrieves agreements updated after the specified date
    public List<Agreement> getAgreementsByUpdatedAtAfter(LocalDateTime updatedAt) {
        logger.info("Retrieving agreements updated after: {}", updatedAt);
        return agreementRepository.findByUpdatedAtAfter(updatedAt);
    }
}
