package com.sartiniomar.library.loan.infrastructure.web.error;

import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotAvailableException;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.commons.infrastructure.web.error.ErrorResponse;
import com.sartiniomar.library.commons.infrastructure.web.error.Error;
import com.sartiniomar.library.loan.domain.loan.LoanLimitExceededException;
import com.sartiniomar.library.loan.domain.loan.OnlyResearcherCanLoanRestrictedBooksException;
import com.sartiniomar.library.loan.domain.loan.TransitionStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class
LendingExceptionHandler {

  @ExceptionHandler(BookInstanceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBookInstanceNotFoundExceptionHandler(BookInstanceNotFoundException ex) {
    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(404).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), errors));
  }

  @ExceptionHandler(BookInstanceNotAvailableException.class)
  public ResponseEntity<ErrorResponse> handleBookAlreadyOnLoanExceptionHandler(BookInstanceNotAvailableException ex) {

    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailureExceptionHandler(ObjectOptimisticLockingFailureException ex) {

    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }

  @ExceptionHandler(LoanLimitExceededException.class)
  public ResponseEntity<ErrorResponse> handleLoanLimitExceededExceptionHandler(LoanLimitExceededException ex) {

    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }

  @ExceptionHandler(OnlyResearcherCanLoanRestrictedBooksException.class)
  public ResponseEntity<ErrorResponse> onlyResearcherCanLoanRestrictedBooksExceptionHandler(
      OnlyResearcherCanLoanRestrictedBooksException ex) {

    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }

  @ExceptionHandler(TransitionStatusException.class)
  public ResponseEntity<ErrorResponse> transitionStatusExceptionHandler(
      TransitionStatusException ex) {

    List<Error> errors = List.of(new Error(ex.getMessage()));

    return ResponseEntity.status(409).body(new ErrorResponse(HttpStatus.CONFLICT.toString(), errors));
  }
}