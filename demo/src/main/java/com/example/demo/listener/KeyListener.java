package com.example.demo.listener;

import com.example.demo.service.DemoRedisService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@AllArgsConstructor
public class KeyListener implements MessageListener {

    private final DemoRedisService demoRedisService;
    private final ScheduledExecutorService scheduler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
//        demoRedisService.deleteFromSet(expiredKey);
        log.info("expired ket {}", expiredKey);

        // Schedule deletion from the set after 2 minutes
        scheduler.schedule(() -> demoRedisService.deleteFromSet(expiredKey),
                2, TimeUnit.MINUTES);
    }
}