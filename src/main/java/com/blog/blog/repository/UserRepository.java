package com.blog.blog.repository;

import com.blog.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Deprecated
    User findByUsername(String username);
    @Deprecated
    User findByEmail(String email);
}
