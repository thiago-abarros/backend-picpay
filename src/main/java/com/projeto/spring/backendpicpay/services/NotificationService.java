package com.projeto.spring.backendpicpay.services;

import com.projeto.spring.backendpicpay.domain.user.User;
import com.projeto.spring.backendpicpay.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

  @Autowired
  private RestTemplate restTemplate;

  public void sendNotification(User user, String message) throws Exception {
    String email = user.getEmail();
    NotificationDTO notificationRequest = new NotificationDTO(email, message);

    // Como o mocky não funciona mais, deixei isso como provisório para focar nas outras implementações.
    // ResponseEntity<String> notificationResponse = restTemplate.postForEntity("http://o4d9z.mocklab.io/notify", notificationRequest, String.class);
    HttpStatus notificationResponse = HttpStatus.OK;

    if (!(notificationResponse == HttpStatus.OK)) {
      System.out.println("Erro ao enviar notificação");
      throw new Exception("Serviço de notificação está fora do ar");
    }

    System.out.println("Notificação enviada para o usuário");
  }
}
