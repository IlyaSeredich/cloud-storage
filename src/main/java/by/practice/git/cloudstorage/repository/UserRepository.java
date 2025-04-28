package by.practice.git.cloudstorage.repository;

import by.practice.git.cloudstorage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String username);

    Optional<User> findUserByUsername(String username);

    boolean existsUserByEmail(String email);
}
