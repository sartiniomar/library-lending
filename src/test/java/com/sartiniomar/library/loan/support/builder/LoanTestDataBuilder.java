package com.sartiniomar.library.loan.support.builder;

import com.sartiniomar.library.loan.domain.loan.Loan;
import java.util.UUID;

public class LoanTestDataBuilder {
  public Loan buildDefaultReserve() {
    return Loan.createReserve(
        UUID.fromString("00000000-1111-2222-3333-444444444444"),
        UUID.fromString("55555555-6666-7777-8888-999999999999"),
        java.time.Clock.systemDefaultZone());
  }
}
