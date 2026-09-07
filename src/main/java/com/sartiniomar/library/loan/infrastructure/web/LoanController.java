package com.sartiniomar.library.loan.infrastructure.web;

import com.sartiniomar.library.loan.application.port.in.CancelUseCase;
import com.sartiniomar.library.loan.application.port.in.CheckoutReserveUseCase;
import com.sartiniomar.library.loan.application.port.in.CheckoutUseCase;
import com.sartiniomar.library.loan.application.port.in.LoanCommand;
import com.sartiniomar.library.loan.application.port.in.LoanIdCommand;
import com.sartiniomar.library.loan.application.port.in.ReserveUseCase;
import com.sartiniomar.library.loan.infrastructure.mapper.LoanMapper;
import com.sartiniomar.library.loan.infrastructure.web.dto.LoanResponse;
import com.sartiniomar.library.loan.infrastructure.web.dto.CreateLoanRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/loans")
public class LoanController {

  private final ReserveUseCase reserveUseCase;
  private final CancelUseCase cancelUseCase;
  private final CheckoutUseCase checkoutUseCase;
  private final CheckoutReserveUseCase checkoutReserveUseCase;
  private final LoanMapper loanMapper;

  @PostMapping("/reserves")
  public ResponseEntity<LoanResponse> reserve(@Valid @RequestBody CreateLoanRequest request) {
    LoanCommand command = loanMapper.createLoanRequestToLoanCommand(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(loanMapper.loanToLoanResponse(reserveUseCase.execute(command)));
  }

  @PostMapping("/{loanId}/cancel")
  public ResponseEntity<LoanResponse> cancel(@PathVariable UUID loanId) {
    return ResponseEntity.ok(loanMapper.loanToLoanResponse(cancelUseCase.execute(new LoanIdCommand(loanId))));
  }

  @PostMapping("/checkouts")
  public ResponseEntity<LoanResponse> checkout(@Valid @RequestBody CreateLoanRequest request) {
    LoanCommand command = loanMapper.createLoanRequestToLoanCommand(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(loanMapper.loanToLoanResponse(checkoutUseCase.execute(command)));
  }

  @PostMapping("/{loanId}/checkouts")
  public ResponseEntity<LoanResponse> checkoutReserve(@PathVariable UUID loanId) {
    return ResponseEntity.ok(loanMapper.loanToLoanResponse(checkoutReserveUseCase.execute(new LoanIdCommand(loanId))));
  }
}
