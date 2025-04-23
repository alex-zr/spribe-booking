package co.spribe.repository;

import co.spribe.model.Unit;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface UnitRepository extends
    JpaRepository<Unit, Long>, JpaSpecificationExecutor<Unit> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT u FROM Unit u WHERE u.id = :id")
  Optional<Unit> findByIdLocked (Long id);

  Page<Unit> findByBooked(boolean booked, Pageable pageable);

  @Query(value = """
        select u.* 
        from unit u left join payment p on p.unit_id = u.id
        where u.booked = true
           and abs(extract(minutes from u.booking_date - current_date)) > :minutes 
           and p is null 
        for update of u       
      """, nativeQuery = true)
  List<Unit> findNotPayedLocked(int minutes);
}
