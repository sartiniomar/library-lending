package com.sartiniomar.library.loan.infrastructure.persistence.jpa.repository;

import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.infrastructure.persistence.model.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, UUID>  {
  @Query("""
        SELECT COUNT(l)
        FROM LoanEntity l
        WHERE l.patronId = :patronId
          AND l.status IN :statuses
    """)
  Integer countActiveLoansByPatronId(
      @Param("patronId") UUID patronId,
      @Param("statuses") List<LoanStatus> statuses
  );

  List<LoanEntity> findAllByPatronId(UUID patronId);
}
