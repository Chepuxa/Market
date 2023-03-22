package com.demo.market.repository;

import com.demo.market.entity.User;
import com.demo.market.enums.ActiveStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends CrudRepository<User, String> {

    @Query(value = "select * from user_t where email=:email or username=:username", nativeQuery = true)
    Set<User> findAllByEmailOrUsername(@Param("email") String email, @Param("username") String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByIdAndStatus(String userId, ActiveStatus status);
}
