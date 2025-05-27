package br.com.Qolli.controller;

import br.com.Qolli.service.ExportService;
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
    private final ExportService service;

    public ExportController(ExportService service) {
        this.service = service;
    }

    @GetMapping("/message.json")
    public ResponseEntity<byte[]> exportMessageJson(@RequestParam Long userId) {
        byte[] json = service.generateMessageJson(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Message.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @GetMapping("/message.pdf")
    public ResponseEntity<byte[]> exportMessagePdf(@RequestParam Long userId) {
        byte[] pdf = service.generateMessagePdf(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Message.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/profile")
    public ResponseEntity<byte[]> exportProfile(@RequestParam Long userId) {
        byte[] json = service.generateProfileJson(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Profile.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }
}
