package co.spribe.controller;

import co.spribe.controller.dto.PageDto;
import co.spribe.controller.dto.UnitDto;
import co.spribe.controller.dto.UnitSearchParamsDto;
import co.spribe.controller.dto.CreatetUnitDto;
import co.spribe.mapper.PageMapper;
import co.spribe.mapper.UnitMapper;
import co.spribe.model.Unit;
import co.spribe.service.UnitService;
import co.spribe.util.UnitParamSpecificationEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/units")
public class UnitController {

  private final UnitService unitService;
  private final PageMapper pageMapper;
  private final UnitMapper unitMapper;

  @GetMapping
  public PageDto<UnitDto> getUnitsPaginated(@RequestBody UnitSearchParamsDto searchParamsDto,
      @PageableDefault(sort = "costWithFee", direction = Sort.Direction.DESC) Pageable pageable) {

    Specification<Unit> specification = UnitParamSpecificationEnum.getSpecification(
        searchParamsDto);
    Page<UnitDto> unitDtoPage = unitService.findAll(pageable, specification)
        .map(unitMapper::toDto);
    return pageMapper.toDto(unitDtoPage);
  }

  @GetMapping("/available")
  public PageDto<UnitDto> getAvailable(
      @PageableDefault(sort = "costWithFee", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<UnitDto> unitDtoPage = unitService.findAvailable(pageable)
        .map(unitMapper::toDto);
    return pageMapper.toDto(unitDtoPage);
  }

  @PostMapping("/user/{userId}/book/{unitId}")
  public UnitDto bookUnit(Long userId, Long unitId) {
    return unitMapper.toDto(unitService.bookUnit(userId, unitId));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UnitDto createSampleBox(@RequestBody CreatetUnitDto createtUnitDto) {
    return unitMapper.toDto(unitService.create(unitMapper.toEntity(createtUnitDto)));
  }

  @PostMapping("/{unitId}/cancel")
  public void cancelBooking(Long unitId) {
    unitService.cancelBooking(unitId);
  }
}
