package com.kanban.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;


@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {
    @Id
    private String username;
    private Map<String, Boolean> notificationMessage;
    private JSONObject jsonObject;
}
