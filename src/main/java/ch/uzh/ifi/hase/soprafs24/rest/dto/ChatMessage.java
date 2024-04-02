package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class ChatMessage {
  private String content;
  private Long userId;
  private String username;
  private Long gameId;


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getGameId() {
    return gameId;
  }

  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }
}
