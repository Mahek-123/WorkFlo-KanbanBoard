package com.kanban.kanban.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8090")
public interface NotificationProxy {
    @PostMapping("/api/v1/notifications/email/{email}")
    String sendRegistrationEmail(@PathVariable String email, @RequestBody String message);
}
