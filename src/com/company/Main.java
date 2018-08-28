package com.company;

import java.lang.*;
import java.util.*;


public class Main {

    private static byte[] Pattern = {9,8,7,6,5,4,3,2,1,0};
    private static  int bufSize = 100000000;
    private static final long magicP = Integer.MAX_VALUE;
    private static final long X = 666 /*Integer.MAX_VALUE*/;

    private static long[] powers;

    private static long collisions;


    public static long mod(long a, long MOD) {
        return ((a % MOD) + MOD) % MOD;
    }

    public static long HashIt(byte[] s, int start, int len) {
        long h = 0;
        int ii = 0;
        for (int i = start; i < start + len; i++) {
            long hd;
            hd = mod((long)s[i]*powers[ii], magicP);
            h = mod(h + hd, magicP);
            ii++;
        }
        return h;
    }

    public static boolean isEqual(byte[] pattern, byte[] text, int i) {
        boolean result = true;
        for(int j = 0; j < pattern.length; j++) {
            if ((i + j) == text.length) {
                collisions++;
                result = false;
                return result;
            }
            if (pattern[j] != text[j + i]) {
                collisions++;
                result = false;
                return result;
            }
        }
        return result;
    }

    public static  ArrayList<Integer> RKSearch(byte[] p, byte[] t) {
        ArrayList<Integer> ans = new ArrayList<Integer>();
        int lp = p.length;
        int lt = t.length;
        long lastpow = powers[lp - 1];
        long HP = HashIt(p, 0, lp);
        int k = lt - lp;
        long HT = HashIt(t, k, lp);
        if (HP == HT) {
            if (isEqual(p, t, k) == true) {
                ans.add(k);
            }
        }

        for (int i = k - 1; i >= 0; i--) {
            long A = mod(lastpow * (long) t[i + lp], magicP);
            long HM = mod((HT - A), magicP);
            HM = mod(HM * X, magicP);
            HT = mod(HM + (long) t[i], magicP);
            if (HP == HT) {
                if (isEqual(p, t, i) == true) {
                    ans.add(i);
                }
            }

        }
        return ans;
    }

    public static void main(String[] args) {

        long tStart = System.currentTimeMillis();
        byte[] T = new byte[bufSize];
        for (int i = 0; i < bufSize; i++) {
            T[i] = (byte)i;
        }
        for (int i = 0; i < Pattern.length; i++) {
            T[i + 10000] = Pattern[i];
        }


        collisions = 0;
        powers = new long[Pattern.length];
        powers[0] = 1L;

        for (int i = 1; i < Pattern.length; i++) {
            powers[i] = mod(X*powers[i-1], magicP);
        }

        ArrayList<Integer> ans = RKSearch(Pattern, T);
        for (int i= ans.size() - 1; i >= 0 ; i-- ) {
            System.out.printf("%d ", ans.get(i));
        }
        System.out.println();
        System.out.println("Collisions:" + collisions);
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta;
        System.out.println("Elapsed(ms):" + elapsedSeconds);
    }



}
