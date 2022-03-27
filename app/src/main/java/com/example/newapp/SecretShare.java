package com.example.newapp;

import java.math.BigInteger;

public class SecretShare
{
    public SecretShare(final int number, final BigInteger share)
    {
        this.number = number;
        this.share = share;
    }

    public int getNumber()
    {
        return number;
    }

    public BigInteger getShare()
    {
        return share;
    }

    //SecretShare[] arr = {};

    @Override
    public String toString()
    {

        return  number + "," + share ;
    }

    public final int number;
    public final BigInteger share;
}