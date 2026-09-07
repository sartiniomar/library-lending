package com.sartiniomar.library.loan.infrastructure.web;

import com.sartiniomar.library.loan.application.port.in.LoanCommand;
import com.sartiniomar.library.loan.application.port.in.ReserveUseCase;
import com.sartiniomar.library.loan.infrastructure.mapper.LoanMapper;
import com.sartiniomar.library.loan.infrastructure.web.dto.LoanResponse;
import com.sartiniomar.library.loan.infrastructure.web.dto.CreateLoanRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/loans")
public class LoanController {

  private final ReserveUseCase useCase;
  private final LoanMapper loanMapper;

  @PostMapping("/reserves")
  public ResponseEntity<LoanResponse> reserve(@Valid @RequestBody CreateLoanRequest request) {
    LoanCommand command = loanMapper.createLoanRequestToLoanCommand(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(loanMapper.loanToLoanResponse(useCase.execute(command)));
  }
}
