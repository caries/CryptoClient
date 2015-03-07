package com.crypto.helpers;

/**
 * User metadata
 */
public class Metadata {
    public String address;
    public String request;
    public String filteringExpression;

    @Override
    public String toString() {
        return "Metadata{" +
                "address='" + address + '\'' +
                ", request='" + request + '\'' +
                ", filteringExpression='" + filteringExpression + '\'' +
                '}';
    }
}