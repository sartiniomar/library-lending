package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.GetLoanByIdUseCase;
import com.sartiniomar.library.loan.application.port.in.LoanIdCommand;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanNotFoundException;

public class GetLoanByIdUseCaseImpl implements GetLoanByIdUseCase {

  private final LoanRepository loanRepository;

  public GetLoanByIdUseCaseImpl(LoanRepository loanRepository) {
    this.loanRepository = loanRepository;
  }

  @Override
  public Loan execute(LoanIdCommand command) {
    return loanRepository.findById(command.loanId())
        .orElseThrow(() -> new LoanNotFoundException(command.loanId().toString()));
  }
}
