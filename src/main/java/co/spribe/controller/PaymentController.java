package co.spribe.controller;

import co.spribe.controller.dto.CreatePaymentDto;
import co.spribe.controller.dto.PaymentDto;
import co.spribe.mapper.PaymentMapper;
import co.spribe.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/payments")
public class PaymentController {

  private final PaymentService paymentService;
  private final PaymentMapper paymentMapper;

  @PostMapping
  public PaymentDto create(@RequestBody CreatePaymentDto paymentDto) {
    return paymentMapper.toDto(paymentService.create(
        paymentDto.userId(),
        paymentDto.unitId(),
        paymentDto.amount()));
  }

}
