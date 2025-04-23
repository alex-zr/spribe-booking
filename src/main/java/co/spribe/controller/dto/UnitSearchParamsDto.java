package co.spribe.controller.dto;

import co.spribe.model.enums.AccommodationType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UnitSearchParamsDto(
    LocalDateTime availableStartDate,
    LocalDateTime availableEndDate,
    Integer numberOfRooms,
    AccommodationType accommodationType,
    BigDecimal cost,
    BigDecimal costWithFee,
    Integer unitFlor,
    String description
) {

}
