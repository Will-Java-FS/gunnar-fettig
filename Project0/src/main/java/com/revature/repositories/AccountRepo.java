package com.revature.repositories;

import com.revature.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
    @Query("From Account WHERE name = :nameVar")
    public Account searchAccountByName(@Param("nameVar") String name);
}
