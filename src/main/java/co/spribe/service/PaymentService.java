package co.spribe.service;

import co.spribe.model.Payment;
import java.math.BigDecimal;

public interface PaymentService {

  Payment create(Long aLong, Long aLong1, BigDecimal amount);
}
