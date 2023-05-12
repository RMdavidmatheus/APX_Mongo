package com.bbva.cmek.dto.accounts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class AccountDTO implements Serializable {
    private static final long serialVersionUID = 2931699728946643245L;

    private String id;

    /**
     * The account state
     */
    private String state;

    /**
     * The balance that the account has
     */
    private long balance;

    /**
     * The account type
     */
    private String accountType;

    /**
     * The owner name or id
     */
    private String owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AccountDTO that = (AccountDTO) o;

        return new EqualsBuilder()
                .append(balance, that.balance)
                .append(id, that.id)
                .append(state, that.state)
                .append(accountType, that.accountType)
                .append(owner, that.owner)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(state)
                .append(balance)
                .append(accountType)
                .append(owner)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
