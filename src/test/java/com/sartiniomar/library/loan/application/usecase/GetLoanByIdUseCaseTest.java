package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.in.LoanIdCommand;
import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstance;
import com.sartiniomar.library.loan.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.loan.domain.bookInstance.BookType;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetLoanByIdUseCaseTest {

  @Mock
  private LoanRepository loanRepository;

  @InjectMocks
  private GetLoanByIdUseCaseImpl useCase;

  @Test
  void shouldExecuteGetLoanByIdSuccessfully() {
    Clock clock = Clock.systemDefaultZone();

    BookInstance bookInstance = new BookInstance(
        UUID.randomUUID(),
        UUID.randomUUID(),
        BookType.CIRCULATING,
        BookInstanceStatus.AVAILABLE
    );

    Loan loan = Loan.createReserve(UUID.randomUUID(), bookInstance.getId(), clock);

    when(loanRepository.findById(loan.getId()))
        .thenReturn(Optional.of(loan));

    LoanIdCommand command = new LoanIdCommand(loan.getId());

    Loan result = useCase.execute(command);

    assertNotNull(result);
    assertEquals(loan, result);

    verify(loanRepository).findById(loan.getId());
  }

  @Test
  void should_throw_exception_when_loan_not_exist() {
    UUID loanId = UUID.randomUUID();
    LoanIdCommand command = new LoanIdCommand(loanId);

    LoanNotFoundException ex =
        assertThrows(LoanNotFoundException.class,
            () -> useCase.execute(command)
        );

    assertEquals("Loan not found: " + loanId, ex.getMessage());
  }
}
