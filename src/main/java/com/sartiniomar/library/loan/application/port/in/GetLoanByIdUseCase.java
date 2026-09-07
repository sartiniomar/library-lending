package com.sartiniomar.library.loan.application.port.in;

import com.sartiniomar.library.loan.domain.loan.Loan;

public interface GetLoanByIdUseCase {
  Loan execute(LoanIdCommand command);
}
