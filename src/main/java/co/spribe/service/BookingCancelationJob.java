package co.spribe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingCancelationJob {

  private final UnitService unitService;

  @Scheduled(cron = "${scheduling.cron.bookingCanselation:-}")
  public void executeProcessBookingCancelation() {
    unitService.cancelIfNotPayed();
  }
}
