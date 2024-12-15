package com.bank.project.service;

import com.bank.project.entity.TaxCode;
import com.bank.project.repository.TaxCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaxCodeService {

    private static final Logger logger = LoggerFactory.getLogger(TaxCodeService.class);

    private final TaxCodeRepository taxCodeRepository;

    @Autowired
    public TaxCodeService(TaxCodeRepository taxCodeRepository) {
        this.taxCodeRepository = taxCodeRepository;
    }

    public Optional<TaxCode> findTaxCodeById(Long id) {
        logger.info("Searching for tax code with ID: {}", id);
        Optional<TaxCode> taxCode = taxCodeRepository.findById(id);
        if (taxCode.isPresent()) {
            logger.info("Tax code found with ID: {}", id);
        } else {
            logger.warn("Tax code not found with ID: {}", id);
        }
        return taxCode;
    }

    public Optional<TaxCode> findTaxCodeByCode(String code) {
        logger.info("Searching for tax code with code: {}", code);
        Optional<TaxCode> taxCode = taxCodeRepository.findByCode(code);
        if (taxCode.isPresent()) {
            logger.info("Tax code found with code: {}", code);
        } else {
            logger.warn("Tax code not found with code: {}", code);
        }
        return taxCode;
    }

    public Optional<TaxCode> findTaxCodeByName(String name) {
        logger.info("Searching for tax code with name: {}", name);
        Optional<TaxCode> taxCode = taxCodeRepository.findByName(name);
        if (taxCode.isPresent()) {
            logger.info("Tax code found with name: {}", name);
        } else {
            logger.warn("Tax code not found with name: {}", name);
        }
        return taxCode;
    }
}
