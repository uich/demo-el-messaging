package jp.uich.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Item implements Serializable {

  private Long id;
  private String sku;
  private String name;
  private Brand brand;
  private Category category1;
  private Category category2;
  private Integer price;

  @Value
  @Builder
  public static class Brand implements Serializable {
    private Integer id;
    private String name;
  }

  @Value
  @Builder
  public static class Category implements Serializable {
    private Integer id;
    private String name;
  }

}
