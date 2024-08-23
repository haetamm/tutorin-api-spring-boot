package tutorin.com.service;

import tutorin.com.entities.notification.NotificationJobResponse;
import tutorin.com.exception.NotFoundException;

import java.util.List;

public interface NotificationService {
    List<NotificationJobResponse> getNotificationJob();
    NotificationJobResponse getNotificationJobById(String id) throws NotFoundException;
}
