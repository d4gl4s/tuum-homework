package com.tuum.tuumhomework.mapper;

import com.tuum.tuumhomework.model.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TransactionMapper {

    @Insert("INSERT INTO transaction (account_id, amount, currency, direction, description, balance_after_transaction) " +
            "VALUES (#{transaction.accountId}, #{transaction.amount}, #{transaction.currency}, #{transaction.direction}, " +
            "#{transaction.description}, #{transaction.balanceAfterTransaction})")
    void insertTransaction(@Param("transaction") Transaction transaction);

    @Select("SELECT * FROM transaction WHERE account_id = #{accountId}")
    List<Transaction> getTransactionsByAccountId(Long accountId);
}

