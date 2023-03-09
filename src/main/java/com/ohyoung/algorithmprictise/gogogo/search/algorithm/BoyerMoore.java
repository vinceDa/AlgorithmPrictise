package com.ohyoung.algorithmprictise.gogogo.search.algorithm;

/**
 * @author ohyoung
 * @date 2023/2/9 20:00
 */
public class BoyerMoore {

    private static final int SIZE = 25600000;

    public static int match(String origin, String pattern) {
        BoyerMoore boyerMoore = new BoyerMoore();
        return boyerMoore.bm(origin.toCharArray(), origin.toCharArray().length,
                pattern.toCharArray(), pattern.toCharArray().length);
    }

    public static void main(String[] args) {
        System.out.println(match("abce", "ce"));;
    }

    /**
     *
     * @param a 主串
     * @param n 主串长度
     * @param b 模式串
     * @param m 模式串长度
     * @return 第一次匹配上的位置
     */
    public int bm(char[] a, int n, char[] b, int m) {
        // 初始化模式串
        int[] bc = new int[SIZE];
        generateBC(b, m, bc);
        int[] suffix = new int[m];
        boolean[] prefix = new boolean[m];
        generateGS(b, m, prefix, suffix);
        // 模式串与主串匹配的下标
        int i = 0;
        while (i <= n - m) {
            int j;
            for (j =  m - 1; j >= 0; j--) {
                if (a[i + j] != b[j]) {
                    break;
                }
            }
            // 匹配成功(返回主串与模式串第一个匹配的字符的位置)
            if (j < 0) {
                return i;
            }
            // 移动si - xi的位置(这里等同于将模式串往后滑动j-bc[(int)a[i+j]]位)
            int x = j - bc[a[i + j]];
            int y = 0;
            // 如果有好后缀
            if (j < m - 1) {
                y = moveByGS(j, m, suffix, prefix);
            }
            System.out.println("x: " + x + "   y:" + y);
            i = i + Math.max(x, y);
        }
        return -1;
    }

    /**
     * 记录模式串的每个字符在模式串中所处的位置
     * @param b 模式串
     * @param m 模式串的长度
     * @param bc 散列表
     */
    private void generateBC(char[] b, int m, int[] bc) {
        // 初始化bc数组
        for (int i = 0; i < SIZE; i++) {
            bc[i] = -1;
        }
        // 以每个字符的ascii码为key, 在模式串中的位置为值存储
        for (int i = 0; i < m; i++) {
            int ascii = b[i];
            // 如果存在相同字符, 总是用靠后的字符覆盖之前的, 保证移动位数不会过多, 避免错过可匹配的情况
            bc[ascii] = i;
        }
    }

    /**
     * 记录suffix和prefix，供后续比对使用
     * @param b 模式串
     * @param m 模式串的长度
     * @param prefix 记录每个好后缀是否同时是模式串的前缀
     * @param suffix 记录每个好后缀在模式串中所匹配到的字符串的起始下标，如果存在多个则记录靠后的下标避免过度移动，如果不存在则记为-1
     */
    private void generateGS(char[] b, int m, boolean[] prefix, int[] suffix) {
        // 初始化bc数组
        for (int i = 0; i < m; i++) {
            prefix[i] = false;
            suffix[i] = -1;
        }

        for (int i = 0; i < m - 1; i++) {
            int j = i;
            // 好后缀的长度
            int k = 0;
            // 在匹配上第一个相同字符后， j--表示向前做进一步匹配, 尝试获取最大长度的好后缀
            // 例如acbecb中, 末尾的b匹配成功后向前一位判断c是否匹配, 如此循环下去
            while (j >= 0 && b[j] == b[m - 1 - k]) {
                j--;
                k++;
                // j表示坏字符的位置, j+1表示好后缀的起始位置,
                suffix[k] = j + 1;
            }
            // 说明j从好后缀的末端一直到模式串的起始位置都是匹配成功的,
            // 也就是说好后缀同时也是模式串的前缀子串
            if (j == -1) {
                prefix[k] = true;
            }
        }
    }

    /**
     * 根据不同情况计算移动位数
     * 1、存在匹配的子串
     * 2、不存在匹配子串但是好后缀为模式串前缀子串
     * @param j 坏字符对应的模式串中的字符下标
     * @param m 模式串长度
     */
    private int moveByGS(int j, int m, int[] suffix, boolean[] prefix) {
        // 好后缀长度(好后缀起始位置的前一位一定是坏字符)
        int k = m - 1 - j;
        // 1、如果存在多个, 这个好后缀肯定是靠后的位置的下标(查看generateGS代码得出)
        if (suffix[k] != -1) {
            return j - suffix[k] + 1;
        }
        // 2、不满足条件1则判断所有的好后缀中是否存在为模式串的前缀子串的情况
        // j+1是好后缀，j+2是好后缀的后缀子串的起始(避免出现过渡移动导致错失匹配的情况)
        for (int r = j+2; r <= m - 1 ; r++) {
            if (prefix[m - r]) {
                return r;
            }
        }
        // 3、不满足上述条件说明所有的好后缀中不存在模式串的前缀子串, 所以直接移动m位
        return m;
    }

}
