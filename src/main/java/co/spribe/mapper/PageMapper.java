package co.spribe.mapper;

import co.spribe.controller.dto.PageDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(config = Config.class)
public interface PageMapper {

  default <T> PageDto<T> toDto(Page<T> page) {
    return new PageDto<>(page.getContent(), page.getTotalPages(), page.getTotalElements(),
        page.getNumber());
  }

}
