package ch.uzh.ifi.hase.soprafs24.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
public class TimerService {

  private int seconds;
  private ScheduledFuture<?> future;

  private final TaskScheduler scheduler = new ConcurrentTaskScheduler();

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  public TimerService() {
    this.seconds = 10;
  }

  public void startTimer() {
    if (future != null && !future.isCancelled()) {
      future.cancel(true);
    }
    this.seconds = 10;
    future = scheduler.scheduleAtFixedRate(() -> {
      seconds--;
      messagingTemplate.convertAndSend("/topic/timer", seconds);
      if (seconds == 0) {
        future.cancel(false);
        messagingTemplate.convertAndSend("/topic/timerNotification", "Timer has expired!");
      }
    }, 1000);
  }
}
