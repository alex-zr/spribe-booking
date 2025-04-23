package co.spribe.model;

import co.spribe.model.enums.AccommodationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@Entity
@Table(name = "unit")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Unit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  Long id;

  @NotNull
  @Column(name = "available_start_date")
  private LocalDateTime availableStartDate;

  @NotNull
  @Column(name = "available_end_date")
  private LocalDateTime availableEndDate;

  @Column(name = "number_of_rooms")
  private Integer numberOfRooms;

  @Enumerated(EnumType.STRING)
  @Column(name = "accommodation_type")
  private AccommodationType accommodationType;

  @Column(name = "cost")
  private BigDecimal cost;

  @Column(name = "cost_with_fee")
  private BigDecimal costWithFee;

  @Column(name = "unit_flor")
  private Integer unitFlor;

  @Column(name = "description")
  private String description;

  @Column(name = "booked")
  private Boolean booked;

  @Column(name = "booking_date")
  private LocalDateTime bookingDate;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
