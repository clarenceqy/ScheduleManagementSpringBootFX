package com.synir.models.Repo;

import com.synir.models.POJO.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    UserSession findUsersByUsernameAndPassword(String username, String password);
    UserSession findUsersByUsername(String username);
}