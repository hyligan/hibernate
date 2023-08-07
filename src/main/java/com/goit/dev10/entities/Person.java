package com.goit.dev10.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@ToString
public class Person {

  public Person(String name) {
    this.name = name;
  }

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "first_name", length = 500)
  private String name;
}