package co.spribe.util;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<E, D> {

  Specification<E> buildSpecification(D searchParams);
}
