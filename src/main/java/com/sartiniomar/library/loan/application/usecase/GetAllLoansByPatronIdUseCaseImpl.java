package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.GetAllLoansByPatronIdUseCase;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.Loan;
import java.util.List;
import java.util.UUID;

public class GetAllLoansByPatronIdUseCaseImpl implements GetAllLoansByPatronIdUseCase {

  private final LoanRepository loanRepository;

  public GetAllLoansByPatronIdUseCaseImpl(LoanRepository loanRepository) {
    this.loanRepository = loanRepository;
  }

  @Override
  public List<Loan> execute(UUID patronId) {
    return loanRepository.findAllByPatronId(patronId);
  }
}
