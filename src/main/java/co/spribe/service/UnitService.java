package co.spribe.service;

import co.spribe.model.Unit;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UnitService {

  Page<Unit> findAll(Pageable pageable, Specification<Unit> specification);

  Optional<Unit> findById(Long id);

  Unit bookUnit(Long userId, Long unitId);

  Unit cancelBooking(Long unitId);

  Unit create(Unit unit);

  Page<Unit> findAvailable(Pageable pageable);

  void cancelIfNotPayed();
}
