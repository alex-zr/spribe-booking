package co.spribe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.spribe.model.Unit;
import co.spribe.model.User;
import co.spribe.repository.PaymentRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DefaultPaymentServiceTest {

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private UserService userService;

  @Mock
  private UnitService unitService;

  @InjectMocks
  private DefaultPaymentService paymentService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreate() {
    Long userId = 1L;
    Long unitId = 1L;
    BigDecimal amount = BigDecimal.ONE;

    when(userService.findById(anyLong())).thenReturn(
        Optional.of(User.builder().id(userId).build()));
    when(unitService.findById(anyLong())).thenReturn(
        Optional.of(Unit.builder().id(unitId).build()));

    paymentService.create(1L, 1L, amount);

    verify(paymentRepository).save(argThat(u -> {
      assertNull(u.getId());
      assertNull(u.getCreatedAt());
      assertEquals(userId, u.getUser().getId());
      assertEquals(unitId, u.getUnit().getId());
      assertEquals(amount, u.getAmount());
      return true;
    }));
  }
}