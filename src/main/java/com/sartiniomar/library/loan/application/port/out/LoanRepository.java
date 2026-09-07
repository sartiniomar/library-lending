package com.sartiniomar.library.loan.application.port.out;

import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanRepository {
  int countActiveLoansByPatronId(UUID patronId, List<LoanStatus> statuses);
  Loan save(Loan hold);
  Optional<Loan> findById(UUID id);
  List<Loan> findAllByPatronId(UUID patronId);
}
