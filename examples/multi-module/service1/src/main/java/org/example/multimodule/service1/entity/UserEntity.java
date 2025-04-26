package org.example.multimodule.service1.entity;

public class UserEntity {

  private String username;
  private RoleEntity role;

  public UserEntity() {
  }

  public UserEntity(String username, RoleEntity role) {
    this.username = username;
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public RoleEntity getRole() {
    return role;
  }

  public void setRole(RoleEntity role) {
    this.role = role;
  }
}
