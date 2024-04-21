package com.tuum.tuumhomework.enums;

public enum RoutingKey {
    TRANSACTION_CREATED("transaction.created"),
    TRANSACTION_UPDATED("transaction.updated"),
    ACCOUNT_CREATED("account.created"),
    ACCOUNT_UPDATED("account.updated");

    private final String key;

    RoutingKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
