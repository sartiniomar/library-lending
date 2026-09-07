package com.sartiniomar.library.loan.infrastructure.config;

import com.sartiniomar.library.loan.application.port.in.CancelUseCase;
import com.sartiniomar.library.loan.application.port.in.CheckoutReserveUseCase;
import com.sartiniomar.library.loan.application.port.in.CheckoutUseCase;
import com.sartiniomar.library.loan.application.port.in.GetAllLoansByPatronIdUseCase;
import com.sartiniomar.library.loan.application.port.in.GetLoanByIdUseCase;
import com.sartiniomar.library.loan.application.port.in.ReserveUseCase;
import com.sartiniomar.library.loan.application.port.in.ReturnUseCase;
import com.sartiniomar.library.loan.application.port.out.BookInstanceLoanRepository;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.application.port.out.PatronLoanRepository;
import com.sartiniomar.library.loan.application.usecase.CancelUseCaseImpl;
import com.sartiniomar.library.loan.application.usecase.CheckoutReserveUseCaseImpl;
import com.sartiniomar.library.loan.application.usecase.CheckoutUseCaseImpl;
import com.sartiniomar.library.loan.application.usecase.GetAllLoansByPatronIdUseCaseImpl;
import com.sartiniomar.library.loan.application.usecase.GetLoanByIdUseCaseImpl;
import com.sartiniomar.library.loan.application.usecase.ReserveUseCaseImpl;
import com.sartiniomar.library.loan.application.service.LoanLimitChecker;
import com.sartiniomar.library.loan.application.usecase.ReturnUseCaseImpl;
import com.sartiniomar.library.loan.domain.loan.service.CancelServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.CheckoutReserveServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.CheckoutServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.ReserveServiceDomain;
import com.sartiniomar.library.loan.domain.loan.service.ReturnServiceDomain;
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

  @Bean
  CheckoutReserveUseCase checkoutReserveUseCase(
      LoanRepository loanRepository,
      PatronLoanRepository patronLoanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      CheckoutReserveServiceDomain service
  ) {
    return new CheckoutReserveUseCaseImpl(
        loanRepository,
        patronLoanRepository,
        bookInstanceLoanRepository,
        service
    );
  }

  @Bean
  ReturnUseCase returnUseCase(
      LoanRepository loanRepository,
      BookInstanceLoanRepository bookInstanceLoanRepository,
      ReturnServiceDomain service
  ) {
    return new ReturnUseCaseImpl(
        loanRepository,
        bookInstanceLoanRepository,
        service
    );
  }
  @Bean
  GetLoanByIdUseCase getLoanByIdUseCase(LoanRepository loanRepository) {
    return new GetLoanByIdUseCaseImpl(loanRepository);
  }

  @Bean
  GetAllLoansByPatronIdUseCase getAllLoansByPatronIdUseCase(LoanRepository loanRepository) {
    return new GetAllLoansByPatronIdUseCaseImpl(loanRepository);
  }
}