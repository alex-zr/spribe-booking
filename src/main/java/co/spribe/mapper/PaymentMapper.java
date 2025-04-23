package co.spribe.mapper;

import co.spribe.controller.dto.PaymentDto;
import co.spribe.model.Payment;
import org.mapstruct.Mapper;

@Mapper(config = Config.class)
public interface PaymentMapper {

  PaymentDto toDto(Payment payment);

}
