package br.com.Qolli.controller;

import br.com.Qolli.service.MessageExportService;
import br.com.Qolli.service.ProfileExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
public class ExportController {
    private final MessageExportService messageExportService;
    private final ProfileExportService profileExportService;

    public ExportController(MessageExportService service, ProfileExportService profileExportService) {
        this.messageExportService = service;
        this.profileExportService = profileExportService;
    }

    @GetMapping("/message.json")
    public ResponseEntity<byte[]> exportMessageJson(@RequestParam String conversationKey) {
        byte[] json = messageExportService.generateMessageJson(conversationKey);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Message.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @GetMapping("/message.pdf")
    public ResponseEntity<byte[]> exportMessagePdf(@RequestParam String conversationKey) {
        byte[] pdf = messageExportService.generateMessagePdf(conversationKey);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Message.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/profile.pdf")
    public ResponseEntity<byte[]> exportProfile(@RequestParam String userId) {
        byte[] pdf = profileExportService.generateProfilePdf(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Profile.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
