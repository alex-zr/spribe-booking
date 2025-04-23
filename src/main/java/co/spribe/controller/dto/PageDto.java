package co.spribe.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PageDto<T>(
    List<T> items,
    int totalPages,
    long totalElements,
    int pageNumber
) {

}
