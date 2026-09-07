package com.sartiniomar.library.loan.infrastructure.web;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.loan.application.port.in.CancelUseCase;
import com.sartiniomar.library.loan.application.port.in.CheckoutReserveUseCase;
import com.sartiniomar.library.loan.application.port.in.CheckoutUseCase;
import com.sartiniomar.library.loan.application.port.in.LoanCommand;
import com.sartiniomar.library.loan.application.port.in.LoanIdCommand;
import com.sartiniomar.library.loan.application.port.in.ReserveUseCase;
import com.sartiniomar.library.loan.domain.loan.Loan;
import com.sartiniomar.library.loan.domain.loan.LoanStatus;
import com.sartiniomar.library.loan.support.builder.LoanTestDataBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanControllerTest extends LibraryApplicationTests {

  @MockBean
  ReserveUseCase reserveUseCase;

  @MockBean
  CancelUseCase cancelUseCase;

  @MockBean
  CheckoutUseCase checkoutUseCase;

  @MockBean
  CheckoutReserveUseCase checkoutReserveUseCase;

  private final UUID DEFAULT_PATRON_ID = UUID.fromString("00000000-1111-2222-3333-444444444444");
  private final UUID DEFAULT_BOOK_INSTANCE_ID = UUID.fromString("55555555-6666-7777-8888-999999999999");

  @Test
  @SneakyThrows
  void shouldCreateLoanReserveResponse() {
    ArgumentCaptor<LoanCommand> createLoanRequestArgumentCaptor = ArgumentCaptor.forClass(LoanCommand.class);

    Loan loan = new LoanTestDataBuilder().buildDefaultReserve();
    when(reserveUseCase.execute(createLoanRequestArgumentCaptor.capture())).thenReturn(loan);

    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(loan.getId().toString()))
        .andExpect(jsonPath("$.patronId").value(loan.getPatronId().toString()))
        .andExpect(jsonPath("$.bookInstanceId").value(loan.getBookInstanceId().toString()))
        .andExpect(jsonPath("$.status").value(LoanStatus.RESERVED.name()))
        .andExpect(jsonPath("$.reservedAt").value(loan.getReservedAt().toString()));

    assertEquals(DEFAULT_PATRON_ID, createLoanRequestArgumentCaptor.getValue().patronId());
    assertEquals(DEFAULT_BOOK_INSTANCE_ID, createLoanRequestArgumentCaptor.getValue().bookInstanceId());

    verify(reserveUseCase).execute(any(LoanCommand.class));
  }

  @Test
  @SneakyThrows
  void shouldCreateLoanCancelResponse() {
    ArgumentCaptor<LoanIdCommand> loanIdCommandArgumentCaptor = ArgumentCaptor.forClass(LoanIdCommand.class);

    Loan loan = new LoanTestDataBuilder().buildDefaultReserve();
    loan.cancelled();
    when(cancelUseCase.execute(loanIdCommandArgumentCaptor.capture())).thenReturn(loan);

    mockMvc.perform(post("/loans/{id}/cancel", loan.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(loan.getId().toString()))
        .andExpect(jsonPath("$.patronId").value(loan.getPatronId().toString()))
        .andExpect(jsonPath("$.bookInstanceId").value(loan.getBookInstanceId().toString()))
        .andExpect(jsonPath("$.status").value(LoanStatus.CANCELLED.name()))
        .andExpect(jsonPath("$.reservedAt").value(loan.getReservedAt().toString()));

    assertEquals(loan.getId(), loanIdCommandArgumentCaptor.getValue().loanId());

    verify(cancelUseCase).execute(any(LoanIdCommand.class));
  }

  @Test
  @SneakyThrows
  void shouldCreateLoanCheckoutResponse() {
    ArgumentCaptor<LoanCommand> createLoanRequestArgumentCaptor = ArgumentCaptor.forClass(LoanCommand.class);

    Loan loan = new LoanTestDataBuilder().buildDefaultCheckout();
    when(checkoutUseCase.execute(createLoanRequestArgumentCaptor.capture())).thenReturn(loan);

    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    mockMvc.perform(post("/loans/checkouts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(loan.getId().toString()))
        .andExpect(jsonPath("$.patronId").value(loan.getPatronId().toString()))
        .andExpect(jsonPath("$.bookInstanceId").value(loan.getBookInstanceId().toString()))
        .andExpect(jsonPath("$.status").value(LoanStatus.LENT.name()))
        .andExpect(jsonPath("$.lentAt").value(loan.getLentAt().toString()));

    assertEquals(DEFAULT_PATRON_ID, createLoanRequestArgumentCaptor.getValue().patronId());
    assertEquals(DEFAULT_BOOK_INSTANCE_ID, createLoanRequestArgumentCaptor.getValue().bookInstanceId());

    verify(checkoutUseCase).execute(any(LoanCommand.class));
  }

  @Test
  @SneakyThrows
  void shouldCreateLoanCheckoutReserveResponse() {
    ArgumentCaptor<LoanIdCommand> loanIdCommandArgumentCaptor = ArgumentCaptor.forClass(LoanIdCommand.class);

    Loan loan = new LoanTestDataBuilder().buildDefaultCheckout();
    when(checkoutReserveUseCase.execute(loanIdCommandArgumentCaptor.capture())).thenReturn(loan);

    mockMvc.perform(post("/loans/{id}/checkouts", loan.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(loan.getId().toString()))
        .andExpect(jsonPath("$.patronId").value(loan.getPatronId().toString()))
        .andExpect(jsonPath("$.bookInstanceId").value(loan.getBookInstanceId().toString()))
        .andExpect(jsonPath("$.status").value(LoanStatus.LENT.name()))
        .andExpect(jsonPath("$.lentAt").value(loan.getLentAt().toString()));

    assertEquals(loan.getId(), loanIdCommandArgumentCaptor.getValue().loanId());

    verify(checkoutReserveUseCase).execute(any(LoanIdCommand.class));
  }

  // Estos solo tienen sentido en los test de integración, ya que en los test unitarios no se hace la validación de los ids, sino que se mockea el use case y se lanza la excepción directamente.
  /*@Test
  @SneakyThrows
  void shouldReturnNotFoundWhenPatronIdNotFoundInLoanReserveCreation() {
    when(reserveUseCase.execute(any(LoanCommand.class)))
        .thenThrow(new BookInstanceNotFoundException("Patron not found with id: " + DEFAULT_PATRON_ID));

    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Patron not found with id: " + DEFAULT_PATRON_ID));

    verify(reserveUseCase).execute(any(LoanCommand.class));
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundWhenBookInstanceIdNotFoundInLoanReserveCreation() {
    when(reserveUseCase.execute(any(LoanCommand.class)))
        .thenThrow(new BookInstanceNotFoundException("Book instance not found with id: " + DEFAULT_BOOK_INSTANCE_ID));

    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Book instance not found with id: " + DEFAULT_BOOK_INSTANCE_ID));

    verify(reserveUseCase).execute(any(LoanCommand.class));
  }

  @Test
  @SneakyThrows
  void shouldReturnConflictWhenLoanLimitExceededInLoanReserveCreation() {
    when(reserveUseCase.execute(any(LoanCommand.class)))
        .thenThrow(new LoanLimitExceededException("Loan Limit Exceeded."));

    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("409 CONFLICT"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Loan Limit Exceeded."));

    verify(reserveUseCase).execute(any(LoanCommand.class));
  }

  @Test
  @SneakyThrows
  void shouldReturnConflictWhenOnlyResearcherCanLoanRestrictedBooksExceptionIsThrown() {
    when(reserveUseCase.execute(any(LoanCommand.class)))
        .thenThrow(new OnlyResearcherCanLoanRestrictedBooksException("Only researcher can loan restricted books."));

    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("409 CONFLICT"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Only researcher can loan restricted books."));

    verify(reserveUseCase).execute(any(LoanCommand.class));
  }

  @Test
  @SneakyThrows
  void shouldReturnConflictWhenBookInstanceNotAvailableExceptionIsThrown() {
    when(reserveUseCase.execute(any(LoanCommand.class)))
        .thenThrow(new BookInstanceNotAvailableException("Book instance not available."));

    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("409 CONFLICT"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Book instance not available."));

    verify(reserveUseCase).execute(any(LoanCommand.class));
  }

  @Test
  @SneakyThrows
  void shouldReturnConflictWhenTransitionStatusExceptionIsThrown() {
    when(reserveUseCase.execute(any(LoanCommand.class)))
        .thenThrow(new TransitionStatusException("Transition status exception."));

    String bodyRequest = getContentFromFile("loan/createLoanRequest.json");

    mockMvc.perform(post("/loans/reserves")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("409 CONFLICT"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Transition status exception."));

    verify(reserveUseCase).execute(any(LoanCommand.class));
  }*/
}
