package com.ohyoung.algorithmprictise.gogogo.search.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * 位图
 * @author ohyoung
 * @date 2023/3/9 11:58
 */
public class BitMap {

    // 1char == 16bit
    private char[] bytes;
    private int nbits;

    public BitMap(int nbits) {
        this.nbits = nbits;
        this.bytes = new char[nbits / 16 + 1];
    }

    public void set(int k) {
        // 计算k位于哪个char
        int byteIndex = k / 16;
        // 计算k位于char中的哪个bit
        int bitIndex = k % 16;
        // 将bytes中byteIndex的char中的bitIndex位置的bit置为1
        bytes[byteIndex] |= (1 << bitIndex);
    }

    public boolean get(int k) {
        if (k > nbits) {
            return false;
        }
        // 计算k位于哪个char
        int byteIndex = k / 16;
        // 计算k位于char中的哪个bit
        int bitIndex = k % 16;
        return (bytes[byteIndex] & (1 << bitIndex)) != 0;
    }

    public static void main(String[] args) {

        Map<Integer, Integer> duplicateMap = new HashMap<>();
        BitMap bitMap = new BitMap(100000000);
        for (int i = 0; i < 100000000; i++) {

        }
    }
}
