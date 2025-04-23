package co.spribe.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.spribe.model.Unit;
import co.spribe.model.User;
import co.spribe.model.enums.AccommodationType;
import co.spribe.repository.UnitRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

class DefaultUnitServiceTest {

  @Mock
  private UnitRepository unitRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private DefaultUnitService unitService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void findAll() {
    long id = 1L;
    int unitFlor = 1;
    User user = new User();
    boolean booked = true;
    LocalDateTime max = LocalDateTime.MAX;
    LocalDateTime min = LocalDateTime.MIN;
    AccommodationType home = AccommodationType.HOME;
    Unit unit = Unit.builder()
        .id(id)
        .unitFlor(unitFlor)
        .user(user)
        .booked(booked)
        .availableStartDate(max)
        .availableEndDate(min)
        .accommodationType(home)
        .build();
    PageImpl<Unit> page = new PageImpl<>(List.of(unit));
    when(unitRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

    Page<Unit> units = unitService.findAll(PageRequest.of(0, 1), Specification.where(null));

    Unit actualUnit = units.get().findFirst().get();
    assertEquals(id, actualUnit.getId());
    assertEquals(unitFlor, actualUnit.getUnitFlor());
    assertEquals(user, actualUnit.getUser());
    assertEquals(booked, actualUnit.getBooked());
    assertEquals(max, actualUnit.getAvailableStartDate());
    assertEquals(min, actualUnit.getAvailableEndDate());
    assertEquals(home, actualUnit.getAccommodationType());
  }

  @Test
  void bookUnit() {
    Long userId = 1L;
    Long unitId = 2L;
    Unit unit1 = Unit.builder().build();
    User user = User.builder().build();
    when(userService.findById(userId)).thenReturn(
        Optional.of(user));
    when(unitRepository.findByIdLocked(unitId)).thenReturn(
        Optional.of(unit1));
    when(unitRepository.save(unit1)).thenReturn(unit1);

    unitService.bookUnit(userId, unitId);

    verify(unitRepository).save(argThat(unit -> {
      assertNull(unit.getId());
      assertNotNull(unit.getBookingDate());
      assertTrue(unit.getBooked());
      assertEquals(user, unit.getUser());
      return true;
    }));
  }

  @Test
  void cancelIfNotPayed() {
    int minutes = 15;
    when(unitRepository.findNotPayedLocked(minutes)).thenReturn(
        List.of(Unit.builder().build()));

    unitService.cancelIfNotPayed();

    verify(unitRepository).saveAll(argThat(ul -> {
      Unit unit = ul.iterator().next();
      assertNull(unit.getId());
      assertNull(unit.getBookingDate());
      assertFalse(unit.getBooked());
      assertNull(unit.getUser());
      return true;
    }));
  }
}