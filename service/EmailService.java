package banksys.sys.service;

import banksys.sys.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);

}
