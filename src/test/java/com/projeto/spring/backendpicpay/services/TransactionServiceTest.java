package com.projeto.spring.backendpicpay.services;

import com.projeto.spring.backendpicpay.domain.user.User;
import com.projeto.spring.backendpicpay.domain.user.UserType;
import com.projeto.spring.backendpicpay.dtos.TransactionDTO;
import com.projeto.spring.backendpicpay.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    // Basicamente o mock está criando uma dependência falsa para que os testes possam ser executados sem
    // que haja dependência real dessas classes. Ele cria uma classe igualzinha a cada uma dessas mas seus métodos
    // estarão vazios.

    // Quando alguma dessas classes for chamada, nós que devemos delegar o retorno que o mockito substituirá para que haja
    // o funcionamento do teste.
    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AuthorizationService authService;

    @Mock
    private NotificationService notificationService;

    // Devemos especificar para o Mockito que, quando formos usar essa classe para os testes, ela utilize os Mocks criados
    // no teste e não os da própria classe, por isso que colocamos a annotation @InjectMocks, dessa forma injetando os mocks na classe.
    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    // Antes dos testes unitários, isso sempre irá executar.
    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create Transaction successfully when everything is ok")
    void createTransactionCase1() throws Exception {
        User sender =  new User(1L, "Maria", "Souza", "99999999901", "Maria@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "Joao", "Silva", "99999999902", "Joao@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1L, 2L);
        transactionService.createTransaction(request);

        // Verifica se a transação foi salva no banco de dados
        verify(repository, times(1)).save(any());

        // Verifica se os usuários foram atualizados no banco de dados
        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);

        // Verifica se as notificações foram enviadas
        verify(notificationService, times(1)).sendNotification(sender, "Transação realizada com sucesso");
        verify(notificationService, times(1)).sendNotification(receiver, "Transação recebida com sucesso");
    }

    @Test
    @DisplayName("Should throw Exception when Transaction is not allowed")
    void createTransactionCase2() throws Exception {
        User sender =  new User(1L, "Maria", "Souza", "99999999901", "Maria@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "Joao", "Silva", "99999999902", "Joao@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authService.authorizeTransaction(any(), any())).thenReturn(false);

        // Verificando se a exceção foi lançada
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1L, 2L);
            transactionService.createTransaction(request);
        });

        // Verificando o corpo da mensagem
        Assertions.assertEquals("Transação não autorizada.", thrown.getMessage());
    }
}