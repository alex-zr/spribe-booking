package co.spribe.util;

import co.spribe.controller.dto.UnitSearchParamsDto;
import co.spribe.model.Unit;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@AllArgsConstructor
public enum UnitParamSpecificationEnum
    implements GenericSearchSpecification<Unit, UnitSearchParamsDto> {

  AVAILABLE_START_DATE("availableStartDate") {
    @Override
    public Specification<Unit> buildSpecification(UnitSearchParamsDto searchParams) {
      return buildIfNotNullEqual(this, searchParams.availableStartDate());
    }
  },
  AVAILABLE_END_DATE("availableEndDate") {
    @Override
    public Specification<Unit> buildSpecification(UnitSearchParamsDto searchParams) {
      return buildIfNotNullEqual(this, searchParams.availableEndDate());
    }
  },
  NUMBER_OF_ROOMS("numberOfRooms") {
    @Override
    public Specification<Unit> buildSpecification(UnitSearchParamsDto searchParams) {
      return buildIfNotNullEqual(this, searchParams.numberOfRooms());
    }
  },
  ACCOMMODATION_TYPE("accommodationType") {
    @Override
    public Specification<Unit> buildSpecification(UnitSearchParamsDto searchParams) {
      return buildIfNotNullEqual(this, searchParams.accommodationType());
    }
  },
  COST_WITH_FEE("costWithFee") {
    @Override
    public Specification<Unit> buildSpecification(UnitSearchParamsDto searchParams) {
      return buildIfNotNullEqual(this, searchParams.costWithFee());
    }
  },
  COST("cost") {
    @Override
    public Specification<Unit> buildSpecification(UnitSearchParamsDto searchParams) {
      return buildIfNotNullEqual(this, searchParams.cost());
    }
  },
  UNIT_FLOR("unitFlor") {
    @Override
    public Specification<Unit> buildSpecification(UnitSearchParamsDto searchParams) {
      return buildIfNotNullEqual(this, searchParams.unitFlor());
    }
  },
  DESCRIPTION("description") {
    @Override
    public Specification<Unit> buildSpecification(UnitSearchParamsDto searchParams) {
      return buildIfNotNullLike(this, searchParams.description());
    }
  };

  private final String entityField;

  public static Specification<Unit> getSpecification(UnitSearchParamsDto searchParams) {
    if (isSearchParamsEmpty(searchParams)) {
      return Specification.where(null);
    }

    return Arrays.stream(values())
        .map(param -> param.buildSpecification(searchParams))
        .filter(Objects::nonNull)
        .reduce(Specification::and)
        .orElse(Specification.where(null));
  }

  private static boolean isSearchParamsEmpty(UnitSearchParamsDto searchParams) {
    return Objects.isNull(searchParams) || Stream.of(
            searchParams.availableStartDate(),
            searchParams.availableEndDate(),
            searchParams.numberOfRooms(),
            searchParams.accommodationType(),
            searchParams.costWithFee(),
            searchParams.cost(),
            searchParams.unitFlor(),
            searchParams.description()
        )
        .allMatch(Objects::isNull);
  }
}
