package br.com.Qolli.service.export;

import br.com.Qolli.dto.UserProfile;
import br.com.Qolli.service.fetcher.UserProfileFetcher;
import br.com.Qolli.service.pdf.ProfilePdfBuilder;
import org.springframework.stereotype.Service;


@Service
public class ProfileExportService {
    private final UserProfileFetcher fetcher;
    private final ProfilePdfBuilder pdfBuilder;

    public ProfileExportService(UserProfileFetcher fetcher, ProfilePdfBuilder pdfBuilder) {
        this.fetcher = fetcher;
        this.pdfBuilder = pdfBuilder;
    }

    public byte[] generateProfilePdf(String userId) {
        UserProfile profile = fetcher.fetch(userId);
        return pdfBuilder.build(profile);
    }
}