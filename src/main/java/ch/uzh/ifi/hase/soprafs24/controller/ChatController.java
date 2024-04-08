package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.websocket.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

  private final SimpMessagingTemplate template;

  public ChatController(SimpMessagingTemplate template) {
    this.template = template;
  }


  @MessageMapping("/chat/{lobbyId}/sendMessage")
  public void broadcastMessage(@DestinationVariable Long lobbyId, @Payload ChatMessage message) {
    String destination = "/topic/" + lobbyId + "/chat";
    template.convertAndSend(destination, message);
  }

}
