package com.bbva.cmek.dto.payments;

import com.bbva.cmek.dto.accounts.AccountDTO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

public class PaymentDTO implements Serializable {
    private static final long serialVersionUID = 2931699728946643245L;

    private String id;

    /**
     * The Origin account that performs the payment
     */
    private AccountDTO originAccount;

    /**
     * The bill to be paid
     */
    private BillDTO bill;

    /**
     * The date of the payment
     */
    private String operationDateTime;

    public String getOperationDateTime() {
        return operationDateTime;
    }

    public void setOperationDateTime(String operationDateTime) {
        this.operationDateTime = operationDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountDTO getOriginAccount() {
        return originAccount;
    }

    public void setOriginAccount(AccountDTO originAccount) {
        this.originAccount = originAccount;
    }

    public BillDTO getBill() {
        return bill;
    }

    public void setBill(BillDTO bill) {
        this.bill = bill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PaymentDTO that = (PaymentDTO) o;

        return new
                EqualsBuilder()
                .append(id, that.id)
                .append(originAccount, that.originAccount)
                .append(bill, that.bill)
                .append(operationDateTime, that.operationDateTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(originAccount)
                .append(bill)
                .append(operationDateTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "id='" + id + '\'' +
                ", originAccount=" + originAccount +
                ", bill=" + bill +
                ", operationDateTime=" + operationDateTime +
                '}';
    }
}
