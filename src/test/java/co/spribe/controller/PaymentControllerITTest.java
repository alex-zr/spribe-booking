package co.spribe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.spribe.BookingApplication;
import co.spribe.controller.dto.CreatePaymentDto;
import co.spribe.controller.dto.PaymentDto;
import co.spribe.repository.PaymentRepository;
import co.spribe.common.ITBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseSetups;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DatabaseTearDowns;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(
    classes = {BookingApplication.class}
)
@AutoConfigureMockMvc
@DatabaseSetups({
    @DatabaseSetup(value = "/dbunit/user.xml", type = DatabaseOperation.INSERT),
    @DatabaseSetup(value = "/dbunit/unit.xml", type = DatabaseOperation.INSERT),
    @DatabaseSetup(value = "/dbunit/payment.xml", type = DatabaseOperation.INSERT)
})
@DatabaseTearDowns({
    @DatabaseTearDown(value = "/dbunit/payment.xml", type = DatabaseOperation.DELETE),
    @DatabaseTearDown(value = "/dbunit/unit.xml", type = DatabaseOperation.DELETE),
    @DatabaseTearDown(value = "/dbunit/user.xml", type = DatabaseOperation.DELETE),
})
@TestExecutionListeners({
    TransactionDbUnitTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    MockitoTestExecutionListener.class
})
public class PaymentControllerITTest extends ITBase {

  private static ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PaymentController paymentController;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private TransactionTemplate txTemplate;

  @BeforeAll
  public static void startUp() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
  }

  @Test
  public void shouldResponseAllWhenParamsEmpty() throws Exception {

    BigDecimal amount = new BigDecimal("100");
    CreatePaymentDto requestSearchParamsDto = CreatePaymentDto.builder()
        .userId(100L)
        .unitId(300L)
        .amount(amount)
        .build();

    ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(requestSearchParamsDto);

    MvcResult result = this.mockMvc.perform(post("/payments")
            .contentType(APPLICATION_JSON_VALUE)
            .content(requestJson))
        .andDo(print()).andExpect(status().isOk())
        .andReturn();

    String json = result.getResponse().getContentAsString();
    PaymentDto paymentDto = objectMapper.readValue(json, new TypeReference<>() {
    });
    assertNotNull(paymentDto);
    assertEquals(amount, paymentDto.amount());
    paymentRepository.deleteAll();
  }

}

