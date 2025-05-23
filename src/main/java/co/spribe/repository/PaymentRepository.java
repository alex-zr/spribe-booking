package co.spribe.repository;

import co.spribe.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentRepository extends
    JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

}
