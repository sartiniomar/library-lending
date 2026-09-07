package com.sartiniomar.library.loan.application.usecase;

import com.sartiniomar.library.loan.application.port.out.LoanRepository;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.domain.patron.Patron;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GetAllLoansByPatronIdUseCaseTest {

  @Mock
  private LoanRepository loanRepository;

  @InjectMocks
  private GetAllLoansByPatronIdUseCaseImpl useCase;

  @Test
  void shouldExecuteSuccessfully() {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);
    UUID patronId = patron.getId();

    Loan existing0 = Loan.createReserve(patronId, UUID.randomUUID(), Clock.systemDefaultZone());
    Loan existing1 = Loan.createLent(patronId, UUID.randomUUID(), Clock.systemDefaultZone(), 7);

    when(loanRepository.findAllByPatronId(patronId)).thenReturn(List.of(existing0, existing1));

    List<Loan> result = useCase.execute(patronId);

    assertEquals(2, result.size());
    assertTrue(result.contains(existing0));
    assertTrue(result.contains(existing1));
    assertEquals(patronId, result.get(0).getPatronId());
    assertEquals(patronId, result.get(1).getPatronId());
    assertEquals(existing0.getBookInstanceId(), result.get(0).getBookInstanceId());
    assertEquals(existing1.getBookInstanceId(), result.get(1).getBookInstanceId());
    assertEquals(LoanStatus.RESERVED, result.get(0).getStatus());
    assertEquals(LoanStatus.LENT, result.get(1).getStatus());
    assertEquals(existing0.getReservedAt(), result.get(0).getReservedAt());
    assertEquals(existing1.getReservedAt(), result.get(1).getReservedAt());
    assertEquals(existing0.getLentAt(), result.get(0).getLentAt());
    assertEquals(existing1.getLentAt(), result.get(1).getLentAt());
    assertEquals(existing0.getDueAt(), result.get(0).getDueAt());
    assertEquals(existing1.getDueAt(), result.get(1).getDueAt());
    assertEquals(existing0.getReturnedAt(), result.get(0).getReturnedAt());
    assertEquals(existing1.getReturnedAt(), result.get(1).getReturnedAt());

    verify(loanRepository, times(1)).findAllByPatronId(patronId);
  }
}
