package com.projeto.spring.backendpicpay.repositories;

import com.projeto.spring.backendpicpay.domain.user.User;
import com.projeto.spring.backendpicpay.domain.user.UserType;
import com.projeto.spring.backendpicpay.dtos.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
// Para o spring boot saber que é para usar o arquivo application-test ao invés do normal.
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User successfully from database")
    void findUserByDocumentCase1() {
        String document = "99999999901";
        UserDTO data = new UserDTO("firstName", "test", document, new BigDecimal(10), "test@example.com", "441241", UserType.COMMON);
        this.createUser(data);

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("Should not get User from database when user does not exists")
    void findUserByDocumentCase2() {
        String document = "99999999901";

        Optional<User> result = this.userRepository.findUserByDocument(document);

        assertThat(result).isNotPresent();
    }

    private void createUser(UserDTO data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
    }
}