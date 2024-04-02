package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

  @Autowired
  private TimerService timerService;

  @MessageMapping("/startGame")
  public void startGame() {
    timerService.startTimer();
  }
}
