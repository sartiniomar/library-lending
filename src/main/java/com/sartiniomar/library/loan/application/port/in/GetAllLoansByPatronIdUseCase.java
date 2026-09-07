package com.sartiniomar.library.loan.application.port.in;

import com.sartiniomar.library.loan.domain.loan.Loan;
import java.util.List;
import java.util.UUID;

public interface GetAllLoansByPatronIdUseCase {
  List<Loan> execute(UUID patronId);
}
