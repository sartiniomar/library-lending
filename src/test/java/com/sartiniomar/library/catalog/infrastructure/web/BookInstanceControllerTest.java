package com.sartiniomar.library.catalog.infrastructure.web;

import com.sartiniomar.library.LibraryApplicationTests;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateCirculatingBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.CreateRestrictedBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.DeleteBookInstanceUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetAllBookInstancesByBookIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.GetBookInstanceByIdUseCase;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceCommand;
import com.sartiniomar.library.catalog.application.port.in.bookInstance.UpdateBookInstanceUseCase;
import com.sartiniomar.library.catalog.domain.book.Book;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstance;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceNotFoundException;
import com.sartiniomar.library.catalog.domain.bookInstance.BookInstanceStatus;
import com.sartiniomar.library.catalog.domain.bookInstance.BookType;
import com.sartiniomar.library.catalog.support.builder.BookInstanceTestDataBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookInstanceControllerTest extends LibraryApplicationTests  {

  @MockBean
  private CreateCirculatingBookInstanceUseCase createCirculatingBookInstanceUseCase;

  @MockBean
  private CreateRestrictedBookInstanceUseCase createRestrictedBookInstanceUseCase;

  @MockBean
  private UpdateBookInstanceUseCase updateBookInstanceUseCase;

  @MockBean
  private DeleteBookInstanceUseCase deleteBookInstanceUseCase;

  @MockBean
  private GetAllBookInstancesByBookIdUseCase getAllBookInstancesByBookIdUseCase;

  @MockBean
  private GetBookInstanceByIdUseCase getBookInstanceByIdUseCase;

  @Test
  @SneakyThrows
  void shouldCreateCirculatingBookInstanceResponse() {
    ArgumentCaptor<CreateBookInstanceCommand> createBookCommandArgumentCaptor = ArgumentCaptor.forClass(CreateBookInstanceCommand.class);

    BookInstance bookInstance = new BookInstanceTestDataBuilder().buildCirculatingDefault();

    when(createCirculatingBookInstanceUseCase.execute(createBookCommandArgumentCaptor.capture())).thenReturn(bookInstance);

    mockMvc.perform(post("/books/{bookId}/instances/circulating", bookInstance.getBookId().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.bookId").value(bookInstance.getBookId().toString()))
        .andExpect(jsonPath("$.type").value(BookType.CIRCULATING.toString()))
        .andExpect(jsonPath("$.status").value(BookInstanceStatus.AVAILABLE.toString()));

    assertEquals(bookInstance.getBookId(), createBookCommandArgumentCaptor.getValue().bookId());

    verify(createCirculatingBookInstanceUseCase, times(1)).execute(createBookCommandArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundWhenBookIdNotFoundInCirculatingBookInstanceCreation() {
    UUID id = UUID.randomUUID();
    ArgumentCaptor<CreateBookInstanceCommand> createBookCommandArgumentCaptor = ArgumentCaptor.forClass(CreateBookInstanceCommand.class);

    when(createCirculatingBookInstanceUseCase.execute(createBookCommandArgumentCaptor.capture()))
        .thenThrow(new BookInstanceNotFoundException("Book not found with id: " + id));

    mockMvc.perform(post("/books/{bookId}/instances/circulating", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book not found with id: " + id));

    assertEquals(id, createBookCommandArgumentCaptor.getValue().bookId());

    verify(createCirculatingBookInstanceUseCase, times(1)).execute(createBookCommandArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestWhenCirculatingBookIdIsNotValidUuid() {
    mockMvc.perform(
            post("/books/{bookId}/instances/circulating", "invalid-uuid"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description").value("Parameter 'bookId' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(createCirculatingBookInstanceUseCase, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldCreateRestrictedBookInstanceResponse() {
    ArgumentCaptor<CreateBookInstanceCommand> createBookCommandArgumentCaptor = ArgumentCaptor.forClass(CreateBookInstanceCommand.class);

    BookInstance bookInstance = new BookInstanceTestDataBuilder().buildRestrictedDefault();

    when(createRestrictedBookInstanceUseCase.execute(createBookCommandArgumentCaptor.capture())).thenReturn(bookInstance);

    mockMvc.perform(post("/books/{bookId}/instances/restricted", bookInstance.getBookId().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.bookId").value(bookInstance.getBookId().toString()))
        .andExpect(jsonPath("$.type").value(BookType.RESTRICTED.toString()))
        .andExpect(jsonPath("$.status").value(BookInstanceStatus.AVAILABLE.toString()));

    assertEquals(bookInstance.getBookId(), createBookCommandArgumentCaptor.getValue().bookId());

    verify(createRestrictedBookInstanceUseCase, times(1)).execute(createBookCommandArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundWhenBookIdNotFoundInRestrictedBookCreation() {
    UUID id = UUID.randomUUID();
    ArgumentCaptor<CreateBookInstanceCommand> createBookCommandArgumentCaptor =
        ArgumentCaptor.forClass(CreateBookInstanceCommand.class);

    when(createRestrictedBookInstanceUseCase
        .execute(createBookCommandArgumentCaptor.capture()))
        .thenThrow(new BookInstanceNotFoundException("Book not found with id: " + id));

    mockMvc.perform(post("/books/{bookId}/instances/restricted", id)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book not found with id: " + id));

    assertEquals(id, createBookCommandArgumentCaptor.getValue().bookId());

    verify(createRestrictedBookInstanceUseCase, times(1))
        .execute(createBookCommandArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestWhenRestrictedBookIdIsNotValidUuid() {
    mockMvc.perform(
            post("/books/{bookId}/instances/restricted", "invalid-uuid"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Parameter 'bookId' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(createRestrictedBookInstanceUseCase, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnUpdateBookInstanceResponse() {
    ArgumentCaptor<UpdateBookInstanceCommand> updateBookCommandArgumentCaptor =
        ArgumentCaptor.forClass(UpdateBookInstanceCommand.class);

    UUID  bookId = UUID.randomUUID();
    BookInstance bookInstance = new BookInstanceTestDataBuilder().build(bookId, BookType.RESTRICTED, BookInstanceStatus.RESERVED);

    when(updateBookInstanceUseCase.execute(updateBookCommandArgumentCaptor.capture())).thenReturn(bookInstance);

    String bodyRequest = getContentFromFile("catalog/bookInstance/updateBookInstanceRequest.json");

    mockMvc.perform(put("/books/{bookId}/instances/{id}", bookInstance.getBookId(), bookInstance.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookInstance.getId().toString()))
        .andExpect(jsonPath("$.bookId").value(bookInstance.getBookId().toString()))
        .andExpect(jsonPath("$.type").value(bookInstance.getType().toString()))
        .andExpect(jsonPath("$.status").value(BookInstanceStatus.RESERVED.toString()));

    assertEquals(bookInstance.getId(), updateBookCommandArgumentCaptor.getValue().id());
    assertEquals(BookType.RESTRICTED, updateBookCommandArgumentCaptor.getValue().type());

    verify(updateBookInstanceUseCase, times(1)).execute(updateBookCommandArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnUpdate() {
    String bodyRequest = getContentFromFile("catalog/bookInstance/updateBookInstanceRequest.json");
    Book book = new Book(UUID.randomUUID(), "Test Book", "Test Author", "1234567890");

    mockMvc.perform(put("/books/{bookId}/instances/{id}", book.getId(), "invalid-uuid")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Parameter 'id' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(updateBookInstanceUseCase, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingBookInstanceOnUpdate() {
    UUID id = UUID.randomUUID();
    UUID bookId = UUID.randomUUID();

    when(updateBookInstanceUseCase.execute(any())).thenThrow(new BookInstanceNotFoundException("Book Instance not found with id: " + bookId));

    String bodyRequest = getContentFromFile("catalog/bookInstance/updateBookInstanceRequest.json");

    mockMvc.perform(put("/books/{bookId}/instances/{id}", bookId, id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(bodyRequest))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book Instance not found with id: " + bookId));

    verify(updateBookInstanceUseCase, times(1)).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldGetById() {
    ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = new BookInstanceTestDataBuilder().buildCirculatingDefault();

    when(getBookInstanceByIdUseCase.execute(uuidArgumentCaptor.capture())).thenReturn(bookInstance);

    mockMvc.perform(get("/books/{bookId}/instances/{id}", bookId, bookInstance.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(bookInstance.getId().toString()))
        .andExpect(jsonPath("$.bookId").value(bookInstance.getBookId().toString()))
        .andExpect(jsonPath("$.type").value(BookType.CIRCULATING.toString()))
        .andExpect(jsonPath("$.status").value(BookInstanceStatus.AVAILABLE.toString()));

    assertEquals(bookInstance.getId(), uuidArgumentCaptor.getValue());

    verify(getBookInstanceByIdUseCase, times(1)).execute(uuidArgumentCaptor.getValue());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingBookInstanceOnGetById() {
    UUID inexistentId = UUID.randomUUID();

    when(getBookInstanceByIdUseCase.execute(inexistentId))
        .thenThrow(new BookInstanceNotFoundException("Book Instance not found with id: " + inexistentId));

    mockMvc.perform(get("/books/{bookId}/instances/{id}", UUID.randomUUID(), inexistentId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book Instance not found with id: " +  inexistentId));

    verify(getBookInstanceByIdUseCase, times(1)).execute(inexistentId);
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnGetById() {
    String invalidId = "invalid-uuid";

    mockMvc.perform(get("/books/{bookId}/instances/{id}", UUID.randomUUID(), invalidId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Parameter 'id' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(getBookInstanceByIdUseCase, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldGetAllBookInstancesByBookId() {
    BookInstance bookInstance1 = new BookInstanceTestDataBuilder().buildCirculatingDefault();
    BookInstance bookInstance2 = new BookInstanceTestDataBuilder().buildCirculatingDefault();

    when(getAllBookInstancesByBookIdUseCase.execute(bookInstance1.getBookId()))
        .thenReturn(java.util.List.of(bookInstance1, bookInstance2));

    mockMvc.perform(get("/books/" + bookInstance1.getBookId() + "/instances"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(bookInstance1.getId().toString()))
        .andExpect(jsonPath("$[0].bookId").value(bookInstance1.getBookId().toString()))
        .andExpect(jsonPath("$[0].type").value(bookInstance1.getType().toString()))
        .andExpect(jsonPath("$[0].status").value(BookInstanceStatus.AVAILABLE.toString()))
        .andExpect(jsonPath("$[1].id").value(bookInstance2.getId().toString()))
        .andExpect(jsonPath("$[1].bookId").value(bookInstance2.getBookId().toString()))
        .andExpect(jsonPath("$[1].type").value(bookInstance2.getType().toString()))
        .andExpect(jsonPath("$[1].status").value(BookInstanceStatus.AVAILABLE.toString()));

    verify(getAllBookInstancesByBookIdUseCase, times(1)).execute(bookInstance1.getBookId());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingBookOnGetAllById() {
    UUID bookId = UUID.randomUUID();

    when(getAllBookInstancesByBookIdUseCase.execute(bookId))
        .thenThrow(new BookInstanceNotFoundException("Book not found with id: " + bookId));

    mockMvc.perform(get("/books/" + bookId + "/instances"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book not found with id: " +  bookId));

    verify(getAllBookInstancesByBookIdUseCase, times(1)).execute(bookId);
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnGetByBookId() {
    String invalidId = "invalid-uuid";

    mockMvc.perform(MockMvcRequestBuilders.get("/books/" + invalidId + "/instances"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Parameter 'bookId' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(getAllBookInstancesByBookIdUseCase, never()).execute(any());
  }

  @Test
  @SneakyThrows
  void shouldDeleteBookInstance() {
    UUID bookId = UUID.randomUUID();
    BookInstance bookInstance = new BookInstanceTestDataBuilder().buildCirculatingDefault();

    mockMvc.perform(delete("/books/" + bookId + "/instances/" + bookInstance.getId()))
        .andExpect(status().isNoContent());

    verify(deleteBookInstanceUseCase, times(1)).execute(bookInstance.getId());
  }

  @Test
  @SneakyThrows
  void shouldReturnNotFoundForNonExistingBookInstanceOnDelete() {
    UUID bookId = UUID.randomUUID();
    UUID bookInstanceId = UUID.randomUUID();

    doThrow(new BookInstanceNotFoundException("Book Instance not found with id: " + bookInstanceId))
        .when(deleteBookInstanceUseCase).execute(any());

    mockMvc.perform(delete("/books/{bookId}/instances/{id}", bookId, bookInstanceId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
        .andExpect(jsonPath("$.errors[0].description").value("Book Instance not found with id: " +  bookInstanceId));

    verify(deleteBookInstanceUseCase, times(1)).execute(bookInstanceId);
  }

  @Test
  @SneakyThrows
  void shouldReturnBadRequestForInvalidUuidOnDelete() {
    String invalidId = "invalid-uuid";

    mockMvc.perform(delete("/books/{id}/instances/{instanceId}", UUID.randomUUID(), invalidId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
        .andExpect(jsonPath("$.errors[0].description")
            .value("Parameter 'id' with value 'invalid-uuid' could not be converted to type UUID"));

    verify(deleteBookInstanceUseCase, never()).execute(any());
  }
}
