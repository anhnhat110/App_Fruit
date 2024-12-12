package com.example.project162.Domain;

public class Price {
    private int Id;
    private String Value;

    public Price() {
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
    @Override
    public String toString() {
        if (Value.contains(".0")) {
            return Value.replace(".0", "") + " VNĐ";
        }
        return Value + " VNĐ";
    }
}
