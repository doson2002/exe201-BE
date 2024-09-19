package com.exe201.exe201be.services.sendmails;
import com.exe201.exe201be.dtos.DataMailDTO;
import jakarta.mail.MessagingException;



public interface IMailService {
    void sendHtmlMail(DataMailDTO dataMail, String templateName) throws MessagingException;
}
