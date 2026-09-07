package com.sartiniomar.library.loan.infrastructure.persistence.jpa.adapter;

import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.infrastructure.mapper.LoanMapperImpl;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.infrastructure.mapper.LoanMapper;
import com.sartiniomar.library.loan.infrastructure.persistence.model.LoanEntity;
import com.sartiniomar.library.loan.infrastructure.persistence.jpa.repository.LoanJpaRepository;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Import(LoanMapperImpl.class)
public class LoanAdapterRepository implements LoanRepository {

  private final LoanJpaRepository repository;

  private final LoanMapper mapper;

  public LoanAdapterRepository(LoanJpaRepository repository) {
    this.repository = repository;
    this.mapper = new LoanMapperImpl();
  }

  @Override
  public int countActiveLoansByPatronId(UUID patronId, List<LoanStatus> statuses) {
    return repository.countActiveLoansByPatronId(patronId, statuses);
  }

  @Override
  public Loan save(Loan hold) {
    return mapper.toDomain(repository.save(mapper.toEntity(hold)));
  }

  @Override
  public Optional<Loan> findById(UUID id) {
    Optional<LoanEntity> entityOpt = repository.findById(id);
    return entityOpt.map(mapper::toDomain);
  }

  @Override
  public List<Loan> findAllByPatronId(UUID patronId) {
    return repository.findAllByPatronId(patronId).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }
}