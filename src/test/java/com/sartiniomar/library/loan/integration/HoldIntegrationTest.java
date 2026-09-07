package com.sartiniomar.library.loan.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sartiniomar.library.loan.domain.patron.PatronType;
import com.sartiniomar.library.loan.infrastructure.web.dto.CreateLoanRequest;
import com.sartiniomar.library.loan.domain.patron.Patron;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HoldIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldReturn404_whenBookDoesNotExist() throws Exception {
    Patron patron = new Patron(UUID.randomUUID(), PatronType.REGULAR);

    CreateLoanRequest request =
        new CreateLoanRequest(UUID.randomUUID(), patron.getId());

    mockMvc.perform(post("/hold")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isNotFound());
  }

}
