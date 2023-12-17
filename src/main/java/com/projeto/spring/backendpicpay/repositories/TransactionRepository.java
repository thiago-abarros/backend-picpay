package com.projeto.spring.backendpicpay.repositories;

import com.projeto.spring.backendpicpay.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
