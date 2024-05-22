package com.orp.utils.email;

import com.orp.dto.ClaimEmailDTTO;

import java.util.List;

public interface EmailService {
    String  EMAIL_TEMP = "/layout/email/emailTemplate";

    void sendHtmlEmail(ClaimEmailDTTO claimEmailDTTO, List<String> toList, String receiver, String mailTemplate, String url, String content);

}
