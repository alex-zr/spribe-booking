package co.spribe.util;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;

public interface GenericSearchSpecification<E, D> extends SpecificationBuilder<E, D> {

  String getEntityField();

  default Specification<E> buildIfNotNullEqual(GenericSearchSpecification<E, D> param,
      Object value) {
    return Optional.ofNullable(value)
        .map(val -> (Specification<E>) (root, query, builder) ->
            builder.equal(getPath(root, param.getEntityField()), value))
        .orElse(null);
  }

  default Specification<E> buildIfNotNullLike(GenericSearchSpecification<E, D> param,
      String value) {
    return Optional.ofNullable(value)
        .filter(s -> !s.isBlank())
        .map(val -> (Specification<E>) (root, query, builder) ->
            builder.like(getPath(root, param.getEntityField()), "%" + value + "%"))
        .orElse(null);
  }

  private <I, X> Path<X> getPath(final Root<I> root, final String fieldName) {
    if (fieldName.contains(".")) {
      var innerFields = fieldName.split("\\.");
      Path<Object> path = root.get(innerFields[0]);
      for (int i = 1; i < innerFields.length; i++) {
        if (i == innerFields.length - 1) {
          return path.get(innerFields[i]);
        }
        path = path.get(innerFields[i]);
      }
    }

    return root.get(fieldName);
  }
}
