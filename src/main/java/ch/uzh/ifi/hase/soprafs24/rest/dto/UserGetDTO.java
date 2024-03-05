package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import java.util.Date;

public class UserGetDTO {

  private Long id;

  private String username;
  private UserStatus status;

  private Date creation_date;

  private Date birthday;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public Date getCreationDate() { return creation_date; }

  public void setCreationDate(Date creation_date) { this.creation_date = creation_date; }

  public Date getBirthday() { return birthday; }

  public void setBirthday(Date birthday) { this.birthday = birthday; }

}
