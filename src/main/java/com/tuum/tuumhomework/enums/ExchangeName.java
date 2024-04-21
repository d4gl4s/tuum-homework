package com.tuum.tuumhomework.enums;

public enum ExchangeName {
    TRANSACTION("transaction-exchange"),
    ACCOUNT("account-exchange");

    private final String name;

    ExchangeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    }
