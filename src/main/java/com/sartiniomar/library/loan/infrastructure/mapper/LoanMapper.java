package com.sartiniomar.library.loan.infrastructure.mapper;

import com.sartiniomar.library.loan.application.port.in.LoanCommand;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.infrastructure.persistence.model.LoanEntity;
import com.sartiniomar.library.loan.infrastructure.web.dto.CreateLoanRequest;
import com.sartiniomar.library.loan.infrastructure.web.dto.LoanResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {

  LoanCommand createLoanRequestToLoanCommand(CreateLoanRequest placeHoldRequest);

  LoanResponse loanToLoanResponse(Loan hold);

  Loan toDomain(LoanEntity entity);

  LoanEntity toEntity(Loan hold);
}
