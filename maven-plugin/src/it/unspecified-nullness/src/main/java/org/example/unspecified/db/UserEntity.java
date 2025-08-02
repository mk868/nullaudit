package org.example.unspecified.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// jakarta.persistence.Entity excluded from NullAudit analysis
@Entity
public class UserEntity {

  @Id
  private String id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String surname;

}
