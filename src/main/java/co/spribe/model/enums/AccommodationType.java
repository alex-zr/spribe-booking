package co.spribe.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccommodationType {
  HOME("Home"),
  FLAT("Flat"),
  APARTMENTS("Apartments");

  private final String displayName;
}
