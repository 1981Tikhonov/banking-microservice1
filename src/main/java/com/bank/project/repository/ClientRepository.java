package com.bank.project.repository;

import com.bank.project.entity.Client;
import com.bank.project.entity.enums.ClientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    
    boolean existsByEmail(String email);
    
    Optional<Client> findByEmail(String email);
    
    @Query("SELECT c FROM Client c WHERE c.status = :status")
    Page<Client> findByStatus(@Param("status") ClientStatus status, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE c.taxCode = :taxCode")
    List<Client> findByTaxCode(@Param("taxCode") String taxCode);

    @Query("SELECT c FROM Client c WHERE LOWER(c.address) LIKE LOWER(concat('%', :address,'%'))")
    Page<Client> findByAddressContainingIgnoreCase(@Param("address") String address, Pageable pageable);

    @Query("SELECT c FROM Client c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Client> findByCreatedAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    Object findByStatus(String status);

    boolean existsByTaxCode(String number);

    Object findByAddressContainingIgnoreCase(String mainAddress);

    int updateClientStatus(Long id, String suspended);
}
