package com.tuum.tuumhomework.mapper;


import com.tuum.tuumhomework.DTO.AccountDatabaseDTO;
import com.tuum.tuumhomework.model.Account;
import com.tuum.tuumhomework.model.Balance;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountMapper {

    //@Insert("INSERT INTO account (customer_id, country) VALUES (#{account.customerId}, #{account.country})")
    //@SelectKey(statement="SELECT currval('account_id_seq') AS id", keyProperty="account.id", before=false, resultType=Long.class)
    //void insertAccount(@Param("account") Account account);

    @Insert("INSERT INTO account (customer_id, country) VALUES (#{account.customerId}, #{account.country})")
    @SelectKey(statement="SELECT currval('account_id_seq') AS id", keyProperty="account.id", before=false, resultType=Long.class)
    void insertAccount(@Param("account") Account account);


    @Insert("INSERT INTO balance (account_id, available_amount, currency) " +
            "VALUES (#{accountId}, #{balance.availableAmount}, #{balance.currency})")
    void insertBalance(@Param("accountId") Long accountId, @Param("balance") Balance balance);


    @Select("SELECT * FROM account WHERE id = #{id}")
    Optional<AccountDatabaseDTO> getAccountById(@Param("id") Long id);

    @Select("SELECT available_amount, currency FROM balance WHERE account_id = #{accountId}")
    List<Balance> getBalancesByAccountId(@Param("accountId") Long accountId);

    @Update("UPDATE balance SET available_amount = #{accountBalance.availableAmount} " +
            "WHERE account_id = #{id} AND currency = #{accountBalance.currency}")
    void updateAccountBalance(@Param("id") Long accountId, @Param("accountBalance") Balance accountBalance);
}

