package co.spribe.service;

import co.spribe.exception.NotFoundException;
import co.spribe.model.Payment;
import co.spribe.model.Unit;
import co.spribe.model.User;
import co.spribe.repository.PaymentRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultPaymentService implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final UserService userService;
  private final UnitService unitService;

  @Override
  @Transactional
  public Payment create(Long userId, Long unitId, BigDecimal amount) {
    User user = userService.findById(userId)
        .orElseThrow(() -> new NotFoundException("User not found"));
    Unit unit = unitService.findById(unitId)
        .orElseThrow(() -> new NotFoundException("Unit not found"));
    return paymentRepository.save(Payment.builder()
        .unit(unit)
        .user(user)
        .amount(amount)
        .build());

  }
}
