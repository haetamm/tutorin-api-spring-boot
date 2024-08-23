package tutorin.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tutorin.com.constant.ApiUrl;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.notification.NotificationJobResponse;
import tutorin.com.exception.NotFoundException;
import tutorin.com.helper.Utilities;
import tutorin.com.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.API_NOTIFICATION)
@RequiredArgsConstructor
@Tag(name = "Notification", description = "Notification API")
public class NotificationController {
    private final Utilities utilities;
    private final NotificationService notificationService;

    @Operation(summary = "Student get job notification")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TUTOR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<List<NotificationJobResponse>>> getNotificationJob() {
        return utilities.handleRequest(notificationService::getNotificationJob, HttpStatus.OK, StatusMessages.SUCCESS_RETRIEVE_LIST);
    }

    @Operation(summary = "Student get job notification by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TUTOR')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<NotificationJobResponse>> getNotificationJobById(@PathVariable String id) {
        return utilities.handleRequest(()-> {
            try {
                return notificationService.getNotificationJobById(id);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.OK, StatusMessages.SUCCESS_RETRIEVE);
    }
}
