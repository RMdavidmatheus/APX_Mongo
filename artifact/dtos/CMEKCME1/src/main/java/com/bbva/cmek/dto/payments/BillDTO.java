package com.bbva.cmek.dto.payments;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class BillDTO implements Serializable {

    private static final long serialVersionUID = 2931699728946643245L;

    private String number;
    private long value;
    private String state;
    private String supplier;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BillDTO billDTO = (BillDTO) o;

        return new EqualsBuilder()
                .append(number, billDTO.number)
                .append(value, billDTO.value)
                .append(state, billDTO.state)
                .append(supplier, billDTO.supplier)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(number)
                .append(value)
                .append(state)
                .append(supplier)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "BillDTO{" +
                "number=" + number +
                ", value=" + value +
                ", state='" + state + '\'' +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
