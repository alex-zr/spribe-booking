package co.spribe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.spribe.BookingApplication;
import co.spribe.common.ITBase;
import co.spribe.controller.dto.PageDto;
import co.spribe.controller.dto.UnitDto;
import co.spribe.controller.dto.UnitSearchParamsDto;
import co.spribe.model.enums.AccommodationType;
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
import java.time.LocalDateTime;
import java.time.Month;
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

@SpringBootTest(
    classes = {BookingApplication.class}
)
@AutoConfigureMockMvc
@DatabaseSetups({
    @DatabaseSetup(value = "/dbunit/units.xml", type = DatabaseOperation.INSERT)
})
@DatabaseTearDowns({
    @DatabaseTearDown(value = "/dbunit/units.xml", type = DatabaseOperation.DELETE),
})
@TestExecutionListeners({
    TransactionDbUnitTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    MockitoTestExecutionListener.class
})
public class UnitControllerITTest extends ITBase {

    private static ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void startUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    @Test
    public void shouldResponseAllWhenParamsEmpty() throws Exception {

        UnitSearchParamsDto requestSearchParamsDto = UnitSearchParamsDto.builder()
            .build();

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestSearchParamsDto);

        MvcResult result = this.mockMvc.perform(get("/units")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andDo(print()).andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();
        PageDto<UnitDto> unitDtoList = objectMapper.readValue(json, new TypeReference<>(){});
        assertEquals(10, unitDtoList.items().size());
    }

    @Test
    public void shouldResponseTwoWhenParamsAll() throws Exception {

        UnitSearchParamsDto requestSearchParamsDto = UnitSearchParamsDto.builder()
            .accommodationType(AccommodationType.HOME)
            .availableStartDate(LocalDateTime.of(2020, Month.JANUARY, 18, 2, 0))
            .availableEndDate(LocalDateTime.of(2021, Month.JANUARY, 18, 2, 0))
            .numberOfRooms(2)
            .unitFlor(0)
            .cost(BigDecimal.valueOf(100))
            .costWithFee(BigDecimal.valueOf(115))
            .description("desc")
            .build();

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestSearchParamsDto);

        MvcResult result = this.mockMvc.perform(get("/units")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andDo(print()).andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();
        PageDto<UnitDto> unitDtoList = objectMapper.readValue(json, new TypeReference<>(){});
        assertEquals(2, unitDtoList.items().size());
    }

    @Test
    public void shouldResponseEmptyWhenParamsDoesntMatch() throws Exception {

        UnitSearchParamsDto requestSearchParamsDto = UnitSearchParamsDto.builder()
            .accommodationType(AccommodationType.APARTMENTS)
            .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestSearchParamsDto);

        MvcResult result = this.mockMvc.perform(get("/units")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andDo(print()).andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();
        PageDto<UnitDto> unitDtoList = objectMapper.readValue(json, new TypeReference<>(){});
        assertEquals(0, unitDtoList.items().size());
    }

    @Test
    public void shouldResponseOneWhenFlorOne() throws Exception {

        UnitSearchParamsDto requestSearchParamsDto = UnitSearchParamsDto.builder()
            .unitFlor(3)
            .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestSearchParamsDto);

        MvcResult result = this.mockMvc.perform(get("/units")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andDo(print()).andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();
        PageDto<UnitDto> unitDtoList = objectMapper.readValue(json, new TypeReference<>(){});
        assertEquals(1, unitDtoList.items().size());
    }

    @Test
    public void shouldResponseOneWhenNumberOfRooms() throws Exception {

        UnitSearchParamsDto requestSearchParamsDto = UnitSearchParamsDto.builder()
            .numberOfRooms(5)
            .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestSearchParamsDto);

        MvcResult result = this.mockMvc.perform(get("/units")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andDo(print()).andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();
        PageDto<UnitDto> unitDtoList = objectMapper.readValue(json, new TypeReference<>(){});
        assertEquals(1, unitDtoList.items().size());
    }

    @Test
    public void shouldResponseOneWhenDesc() throws Exception {

        UnitSearchParamsDto requestSearchParamsDto = UnitSearchParamsDto.builder()
            .description("besc1")
            .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestSearchParamsDto);

        MvcResult result = this.mockMvc.perform(get("/units")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andDo(print()).andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();
        PageDto<UnitDto> unitDtoList = objectMapper.readValue(json, new TypeReference<>(){});
        assertEquals(1, unitDtoList.items().size());
    }

    @Test
    public void shouldResponseOneWhenCost() throws Exception {

        UnitSearchParamsDto requestSearchParamsDto = UnitSearchParamsDto.builder()
            .cost(BigDecimal.valueOf(200))
            .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestSearchParamsDto);

        MvcResult result = this.mockMvc.perform(get("/units")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andDo(print()).andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();
        PageDto<UnitDto> unitDtoList = objectMapper.readValue(json, new TypeReference<>(){});
        assertEquals(1, unitDtoList.items().size());
    }

    @Test
    public void shouldResponseOneWhenCostWithFee() throws Exception {

        UnitSearchParamsDto requestSearchParamsDto = UnitSearchParamsDto.builder()
            .costWithFee(BigDecimal.valueOf(225))
            .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(requestSearchParamsDto);

        MvcResult result = this.mockMvc.perform(get("/units")
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andDo(print()).andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();
        PageDto<UnitDto> unitDtoList = objectMapper.readValue(json, new TypeReference<>(){});
        assertEquals(1, unitDtoList.items().size());
    }

    @Test
    public void shouldResponseOneWhenGetAvailable() throws Exception {


        MvcResult result = this.mockMvc.perform(get("/units/available")
                .contentType(APPLICATION_JSON_VALUE))
            .andDo(print()).andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();
        PageDto<UnitDto> unitDtoList = objectMapper.readValue(json, new TypeReference<>(){});
        assertEquals(3, unitDtoList.items().size());
    }
}
