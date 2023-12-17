package com.projeto.spring.backendpicpay.services;

import com.projeto.spring.backendpicpay.domain.transaction.Transaction;
import com.projeto.spring.backendpicpay.domain.user.User;
import com.projeto.spring.backendpicpay.dtos.TransactionDTO;
import com.projeto.spring.backendpicpay.repositories.TransactionRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  @Autowired
  private UserService userService;

  @Autowired
  private TransactionRepository repository;

  @Autowired
  private AuthorizationService authService;

  @Autowired
  private NotificationService notificationService;

  public TransactionService() {
  }

  public Transaction createTransaction(TransactionDTO transaction) throws Exception {
    User sender = this.userService.findUserById(transaction.senderId());
    User receiver = this.userService.findUserById(transaction.receiverId());

    userService.validateTransaction(sender, transaction.value());
    boolean isAuthorized = this.authService.authorizeTransaction(sender, transaction.value());
    if (!isAuthorized) {
      throw new Exception("Transação não autorizada.");
    }

    Transaction newtransaction = new Transaction();
    newtransaction.setAmount(transaction.value());
    newtransaction.setSender(sender);
    newtransaction.setReceiver(receiver);
    newtransaction.setTimestamp(LocalDateTime.now());

    sender.setBalance(sender.getBalance().subtract(transaction.value()));
    receiver.setBalance(receiver.getBalance().add(transaction.value()));

    this.repository.save(newtransaction);
    this.userService.saveUser(sender);
    this.userService.saveUser(receiver);

    this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
    this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");

    return newtransaction;
  }
}
