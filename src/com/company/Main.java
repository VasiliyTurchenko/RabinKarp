package com.company;

import java.lang.*;
import java.util.*;


public class Main {

    private static byte[] pattern = {9,8,7,6,5,4,3,2,1,0};
    private static  int bufSize = 100000000;
    private static final long MAGIC_P = Integer.MAX_VALUE;
    private static final long X = 666 /*Integer.MAX_VALUE*/;
    private static final int FIT_POSITION = 10000;

    private static long[] powers;

    private static long collisions;


    public static long mod(long a, long modulo) {
        return ((a % modulo) + modulo) % modulo;
    }

    public static long hashIt(byte[] str, int start, int len) {
        long hash = 0;
        int ii = 0;
        for (int i = start; i < start + len; i++) {
            long hd;
            hd = mod((long)str[i]*powers[ii], MAGIC_P);
            hash = mod(hash + hd, MAGIC_P);
            ii++;
        }
        return hash;
    }

    public static boolean isEqual(byte[] pattern, byte[] text, int startPosition) {
        boolean result = true;
        for(int j = 0; j < pattern.length; j++) {
            if ((startPosition + j) == text.length) {
                collisions++;
                result = false;
                return result;
            }
            if (pattern[j] != text[j + startPosition]) {
                collisions++;
                result = false;
                return result;
            }
        }
        return result;
    }

    public static  ArrayList<Integer> searchRK(byte[] pattern, byte[] text) {
        ArrayList<Integer> ans = new ArrayList<Integer>();
        int lp = pattern.length;
        int lt = text.length;
        long lastPow = powers[lp - 1];
        long hp = hashIt(pattern, 0, lp);
        int k = lt - lp;
        long ht = hashIt(text, k, lp);
        if (hp == ht) {
            if (isEqual(pattern, text, k) == true) {
                ans.add(k);
            }
        }

        for (int i = k - 1; i >= 0; i--) {
            long a = mod(lastPow * (long) text[i + lp], MAGIC_P);
            long hm = mod((ht - a), MAGIC_P);
            hm = mod(hm * X, MAGIC_P);
            ht = mod(hm + (long) text[i], MAGIC_P);
            if (hp == ht) {
                if (isEqual(pattern, text, i) == true) {
                    ans.add(i);
                }
            }

        }
        return ans;
    }

    public static void main(String[] args) {

        long tStart = System.currentTimeMillis();
        byte[] text = new byte[bufSize];
        for (int i = 0; i < bufSize; i++) {
            text[i] = (byte)i;
        }
        for (int i = 0; i < pattern.length; i++) {
            text[i + FIT_POSITION] = pattern[i];
        }


        collisions = 0;
        powers = new long[pattern.length];
        powers[0] = 1L;

        for (int i = 1; i < pattern.length; i++) {
            powers[i] = mod(X*powers[i-1], MAGIC_P);
        }

        ArrayList<Integer> ans = searchRK(pattern, text);
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
