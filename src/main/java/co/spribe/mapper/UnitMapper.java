package co.spribe.mapper;

import co.spribe.controller.dto.CreatetUnitDto;
import co.spribe.controller.dto.UnitDto;
import co.spribe.model.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = Config.class)
public interface UnitMapper {

  UnitDto toDto(Unit unit);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "bookingDate", ignore = true)
  Unit toEntity(CreatetUnitDto dto);

}
