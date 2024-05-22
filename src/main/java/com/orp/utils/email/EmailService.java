package com.orp.utils.email;

import com.orp.dto.ClaimEmailDTO;

import java.util.List;

public interface EmailService {
    String  EMAIL_TEMP = "/layout/email/emailTemplate";

    void sendHtmlEmail(ClaimEmailDTO claimEmailDTO, List<String> toList, String receiver, String mailTemplate, String url, String content);

}
