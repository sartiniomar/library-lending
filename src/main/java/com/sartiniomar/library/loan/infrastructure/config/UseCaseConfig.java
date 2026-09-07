package com.sartiniomar.library.loan.infrastructure.config;

import com.sartiniomar.library.loan.application.port.in.CancelUseCase;
import com.sartiniomar.library.loan.application.port.in.CheckoutUseCase;
import com.sartiniomar.library.loan.application.port.in.ReserveUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.application.usecase.CancelUseCaseImpl;
import com.sartiniomar.library.loan.application.usecase.CheckoutUseCaseImpl;
import com.sartiniomar.library.loan.application.usecase.ReserveUseCaseImpl;
import com.sartiniomar.library.loan.application.service.LoanLimitChecker;
import com.sartiniomar.library.loan.domain.loan.service.CancelServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.CheckoutServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  ReserveUseCase reserveUseCase(
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      LoanRepository loanRepository,
      ReserveServiceDomain service,
      LoanLimitChecker validationsUtil
  ) {
    return new ReserveUseCaseImpl(
        patronLoanRepository,
        bookInstanceLoanRepository,
        loanRepository,
        service,
        validationsUtil
    );
  }

  @Bean
  CancelUseCase cancelUseCase(
      CancelServiceDomain cancelServiceDomain,
      LoanRepository loanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository
  ) {
    return new CancelUseCaseImpl(
        cancelServiceDomain,
        loanRepository,
        bookInstanceLoanRepository
    );
  }

  @Bean
  CheckoutUseCase checkoutUseCase(
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      LoanRepository loanRepository,
      CheckoutServiceDomain service,
      LoanLimitChecker validationsUtil
  ) {
    return new CheckoutUseCaseImpl(
        patronLoanRepository,
        bookInstanceLoanRepository,
        loanRepository,
        service,
        validationsUtil
    );
  }
}