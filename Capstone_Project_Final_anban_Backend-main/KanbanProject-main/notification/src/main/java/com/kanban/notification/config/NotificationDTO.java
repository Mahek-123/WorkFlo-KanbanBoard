package com.kanban.notification.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private JSONObject jsonObject;
}
