package com.codepatissier.keki.user.repository;

import com.codepatissier.keki.user.entity.Provider;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);
    Optional<User> findByUserIdxAndStatusEquals(Long userIdx, String status);
    User findByEmailAndProviderAndStatusEquals(String email, Provider provider, String activeStatus);

    User findByEmailAndProvider(String email, Provider provider);
}
