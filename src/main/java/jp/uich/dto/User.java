package jp.uich.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User implements Serializable {

  private String name;
  private LocalDate birthdate;
  private BloodType bloodType;

  public enum BloodType {
    A,
    B,
    O,
    AB;
  }
}
