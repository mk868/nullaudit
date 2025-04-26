package org.example.multimodule.service1.entity;

public class RoleEntity {

  private int id;
  private String name;

  public RoleEntity() {
  }

  public RoleEntity(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
