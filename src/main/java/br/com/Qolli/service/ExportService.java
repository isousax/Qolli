package br.com.Qolli.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class ExportService {
    public byte[] generateMessagePdf(Long userId) {
        return new byte[0];
    }

    public byte[] generateProfileJson(Long userId) {
        return new byte[0];
    }

    public byte[] generateMessageJson(Long userId) {
        return new byte[0];
    }
}
