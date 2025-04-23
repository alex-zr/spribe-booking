package co.spribe.service;

import co.spribe.exception.NotFoundException;
import co.spribe.model.Unit;
import co.spribe.model.User;
import co.spribe.repository.UnitRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultUnitService implements UnitService {

  private final UserService userService;
  private final UnitRepository unitRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Unit> findAll(Pageable pageable, Specification<Unit> specification) {
    return unitRepository.findAll(specification, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Unit> findById(Long id) {
    unitRepository.findAll();
    return unitRepository.findById(id);
  }

  @Override
  @Transactional
  public Unit bookUnit(Long userId, Long unitId) {
    User user = userService.findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found"));
    return setBooked(user, unitId, true, LocalDateTime.now());
  }

  @Override
  @Transactional
  public Unit cancelBooking(Long unitId) {
    return setBooked(null, unitId, false, null);
  }

  @Override
  @Transactional
  public Unit create(Unit unit) {
    return unitRepository.save(unit);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Unit> findAvailable(Pageable pageable) {
    return unitRepository.findByBooked(false, pageable);
  }

  @Override
  @Transactional
  public void cancelIfNotPayed() {
    List<Unit> units = unitRepository.findNotPayedLocked(15);
    units.forEach(u -> {
      u.setUser(null);
      u.setBooked(false);
      u.setBookingDate(null);
    });
    unitRepository.saveAll(units);
  }

  private Unit setBooked(User user, Long unitId, boolean booked, LocalDateTime bookingDate) {
    return unitRepository.findByIdLocked(unitId)
        .map(u -> {
          u.setBooked(booked);
          u.setUser(user);
          u.setBookingDate(bookingDate);
          return unitRepository.save(u);
        })
        .orElseThrow(
            () -> new NotFoundException("Unit with id=%s is not found ".formatted(unitId)));
  }
}
