package org.asue24.financetrackerbackend.repositories;

import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.services.caching.CachingService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findUserById(Long id);
}
