package com.bank.project.repository;

import com.bank.project.entity.TaxCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxCodeRepository extends JpaRepository<TaxCode, Long> {

    @NotNull Optional<TaxCode> findById(@NotNull Long id);

    Optional<TaxCode> findByCode(String code);

    Optional<TaxCode> findByName(String name);
}
