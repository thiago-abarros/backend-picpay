package com.projeto.spring.backendpicpay.services;

import com.projeto.spring.backendpicpay.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class AuthorizationService {
    @Autowired
    private RestTemplate restTemplate;

    public boolean authorizeTransaction(User sender, BigDecimal value){
        // Mocky não está funcionando, como medida provisória foi comentado o código e colocado o retorno sempre igual a verdadeiro

//        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);

//        if (authorizationResponse.getStatusCode() == HttpStatus.OK){
//            String message = (String) authorizationResponse.getBody().get("message");
//            return "Autorizado".equalsIgnoreCase(message);
//        } else return false;
        return true;
    }
}
