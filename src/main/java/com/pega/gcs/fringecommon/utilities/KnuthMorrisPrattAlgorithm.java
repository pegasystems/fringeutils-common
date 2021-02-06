/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities;

/**
 * Knuth-Morris-Pratt Algorithm for Pattern Matching.
 */
public class KnuthMorrisPrattAlgorithm {

    /**
     * Computes the failure function using a bootstrap process, where the pattern is matched against itself.
     */
    private static int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];

        int jcounter = 0;

        for (int i = 1; i < pattern.length; i++) {
            while (jcounter > 0 && pattern[jcounter] != pattern[i]) {
                jcounter = failure[jcounter - 1];
            }
            if (pattern[jcounter] == pattern[i]) {
                jcounter++;
            }
            failure[i] = jcounter;
        }

        return failure;
    }

    /**
     * Finds the first occurrence of the pattern in the text.
     *
     * @param data    - data to be searched
     * @param pattern - search pattern
     * @return - index of first occurrence
     */
    public static int indexOf(byte[] data, byte[] pattern) {
        return indexOf(data, pattern, 0);
    }

    /**
     * Finds the first occurrence of the pattern in the text from the offset index.
     *
     * @param data     - data to be searched
     * @param pattern  - search pattern
     * @param startidx - index location on data to start search
     * @return - index of first occurrence from startidx
     */
    public static int indexOf(byte[] data, byte[] pattern, int startidx) {
        int[] failure = computeFailure(pattern);

        int jcounter = 0;

        if (data.length == 0) {
            return -1;
        }

        for (int i = startidx; i < data.length; i++) {

            while (jcounter > 0 && pattern[jcounter] != data[i]) {
                jcounter = failure[jcounter - 1];
            }

            if (pattern[jcounter] == data[i]) {
                jcounter++;
            }

            if (jcounter == pattern.length) {
                return i - pattern.length + 1;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of the pattern in the text. index returned includes the pattern length.
     *
     * @param data     - data to be searched
     * @param pattern  - search pattern
     * @param startidx - index location on data to start search
     * @return - index of first occurrence from startidx. includes search pattern size.
     */
    public static int indexOfWithPatternLength(byte[] data, byte[] pattern, int startidx) {

        int index = indexOf(data, pattern, startidx);

        if (index != -1) {
            index = index + pattern.length;
        }

        return index;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
}
