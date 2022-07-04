package com.example.newapp;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public final class Shamir
{
    public static SecretShare[] split(final BigInteger secret, int t, int n, BigInteger Zq)
    {
        //System.out.println("Prime Number: " + prime);

        final SecureRandom random = new SecureRandom();

        final BigInteger[] coeff = new BigInteger[t];
        coeff[0] = secret;
        for (int i = 1; i < t; i++)
        {
            BigInteger r;
            do {
                r = new BigInteger(Zq.bitLength(), random);
            } while (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(Zq) >= 0);
            coeff[i] = r;
        }

        final SecretShare[] shares = new SecretShare[n];
        for (int x = 1; x <= n; x++)
        {
            BigInteger accum = secret;

            for (int exp = 1; exp < t; exp++)
            {
                accum = accum.add(coeff[exp].multiply(BigInteger.valueOf(x).pow(exp).mod(Zq))).mod(Zq);
            }
            shares[x - 1] = new SecretShare(x, accum);
            //System.out.println("Share " + shares[x - 1]);
        }

        return shares;
    }

    public static BigInteger combine(final SecretShare[] shares, final BigInteger Zq)
    {
        BigInteger accum = BigInteger.ZERO;

        for(int formula = 0; formula < shares.length; formula++)
        {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for(int count = 0; count < shares.length; count++)
            {
                if(formula == count)
                    continue; // If not the same value

                int startposition = shares[formula].getNumber();
                int nextposition = shares[count].getNumber();

                numerator = numerator.multiply(BigInteger.valueOf(nextposition).negate()).mod(Zq); // (numerator * -nextposition) % prime;
                denominator = denominator.multiply(BigInteger.valueOf(startposition - nextposition)).mod(Zq); // (denominator * (startposition - nextposition)) % prime;
            }
            BigInteger value = shares[formula].getShare();
            BigInteger tmp = value.multiply(numerator) . multiply(modInverse(denominator, Zq));
            accum = Zq.add(accum).add(tmp) . mod(Zq); //  (prime + accum + (value * numerator * modInverse(denominator))) % prime;
        }

        //System.out.println("The secret is: " + accum + "\n");

        return accum;
    }

    private static BigInteger[] gcdD(BigInteger a, BigInteger b)
    {
        if (b.compareTo(BigInteger.ZERO) == 0)
            return new BigInteger[] {a, BigInteger.ONE, BigInteger.ZERO};
        else
        {
            BigInteger n = a.divide(b);
            BigInteger c = a.mod(b);
            BigInteger[] r = gcdD(b, c);
            return new BigInteger[] {r[0], r[2], r[1].subtract(r[2].multiply(n))};
        }
    }

    private static BigInteger modInverse(BigInteger k, BigInteger Zq)
    {
        k = k.mod(Zq);
        BigInteger r = (k.compareTo(BigInteger.ZERO) == -1) ? (gcdD(Zq, k.negate())[2]).negate() : gcdD(Zq,k)[2];
        return Zq.add(r).mod(Zq);
    }


}
