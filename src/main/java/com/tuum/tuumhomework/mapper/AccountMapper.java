package com.tuum.tuumhomework.mapper;


import com.tuum.tuumhomework.model.Account;
import com.tuum.tuumhomework.model.Balance;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface AccountMapper {

    @Insert("INSERT INTO account (customer_id) VALUES (#{account.customerId})")
    void insertAccount(@Param("account") Account account);

    @Select("SELECT * FROM account WHERE id = #{id}")
    Optional<Account> getAccountById(Long id);

    @Update("UPDATE balance SET available_amount = #{accountBalance.availableAmount} " +
            "WHERE account_id = #{id} AND currency = #{accountBalance.currency}")
    void updateAccountBalance(@Param("id") Long accountId, @Param("accountBalance") Balance accountBalance);
}

