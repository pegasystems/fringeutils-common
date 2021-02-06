/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/

package com.pega.gcs.fringecommon.utilities.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ProgressMonitor;

public class MyersDifferenceAlgorithm {

    /**
     * Longest Common Subsequence ( LCS ) Greedy Algorithm checks for ObjectA.equals(ObjectB).
     *
     * @param                 <T> - entities type used in ListA and ListB
     * @param progressMonitor - for monitor cancel action
     * @param listA           - left Side list of entries
     * @param listB           - right side list of entries
     * @param matcher         - equals provider for comparing entries.
     * @return - list of edit command (insert, delete, snake)
     * @throws Exception  - error
     */
    public static <T> List<EditCommand> diffGreedyLCS(ProgressMonitor progressMonitor, List<T> listA, List<T> listB,
            Matcher<T> matcher) throws Exception {

        int listAsize = listA.size();
        int listBsize = listB.size();

        // long counter = 0;

        List<EditCommand> editScript = new ArrayList<EditCommand>();

        if ((listAsize > 0) || (listBsize > 0)) {

            int totalListSize = listAsize + listBsize;
            int size = (2 * totalListSize) + 1;
            int offset = size / 2;

            Path[] pathArray = new Path[size];

            int[] indexArray = new int[size];

            indexArray[1] = 0;

            for (int d = 0; d <= totalListSize; d++) {

                for (int k = -d; ((progressMonitor != null) ? (!progressMonitor.isCanceled()) : true)
                        && (k <= d); k += 2) {

                    int xindex;
                    int koffset = offset + k;
                    int kplus = koffset + 1;
                    int kminus = koffset - 1;

                    EditCommand editCommand = null;
                    Path prev = null;

                    if ((k == -d) || ((k != d) && (indexArray[kminus] < indexArray[kplus]))) {
                        xindex = indexArray[kplus];
                        prev = pathArray[kplus];
                        editCommand = EditCommand.INSERT;
                    } else {
                        xindex = indexArray[kminus] + 1;
                        prev = pathArray[kminus];
                        editCommand = EditCommand.DELETE;
                    }

                    int yindex = xindex - k;

                    if (kminus > -1) {
                        // LOG.info("Setting kMinus: " + kMinus
                        // + " x:" + x + " y:" + y);
                        pathArray[kminus] = null;
                    }

                    // counter++;
                    //
                    // if (counter % 10000000L == 0) {
                    // LOG.info("Counter: " + counter);
                    // }

                    Path path = new Path(editCommand, prev);

                    while ((xindex < listAsize) && (yindex < listBsize)
                            && (matcher.match(listA.get(xindex), listB.get(yindex)))) {

                        xindex++;
                        yindex++;

                        path = new Path(EditCommand.SNAKE, path);
                    }

                    indexArray[koffset] = xindex;
                    pathArray[koffset] = path;

                    if ((xindex >= listAsize) && (yindex >= listBsize)) {

                        // LOG.info("DONE!!!");
                        // path.print("");

                        path.getEditScript(editScript);

                        for (int i = 0; i < size; i++) {

                            // if (pathArray[i] != null) {
                            // pathArray[i].print("");
                            // } else {
                            // LOG.info("NULL: " + i);
                            // }
                            pathArray[i] = null;
                        }

                        // for (EditCommand ec : editScript) {
                        // LOG.info(ec);
                        // }

                        // edit command list includes D + snakes
                        // LOG.info("Size of shortest edit script: "
                        // + D + " length of editCommandList: "
                        // + editScript.size());

                        return editScript;
                    }

                }

                // LOG.info("Setting (offset + D - 1): "
                // + (offset + D - 1) + " offset:" + offset + " D:"
                // + D);

                pathArray[offset + d - 1] = null;
            }

            throw new Exception("not able to find the LCS/SES");

        }

        return editScript;

    }

    public static void main(String[] args) throws Exception {

        Matcher<String> matcher = new Matcher<String>() {

            @Override
            public boolean match(String o1, String o2) {
                return o1.equals(o2);
            }
        };

        // String[] A = { "A", "B", "C", "A", "B", "B", "A" };
        // String[] B = { "C", "B", "A", "B", "A", "C" };

        String[] alist = { "C", "D", "E", "F", "G", "H", "I" };
        String[] blist = { "A", "B", "C", "D", "E", "F", "G" };

        // String[] B = { "A", "B", "E", "F", "G", "H", "I" };
        // String[] A = { "A", "B", "C", "D", "G", "H", "I" };

        // String[] A = { "A", "B", "C", "D" };
        // String[] B = { "E", "F", "G", "H" };

        // String[] A = { "A", "B", "C", "D" };
        // String[] B = { "E", "F", "G", "H", "I" };

        // String[] A = { "A", "B", "C", "D", "X", };
        // String[] B = { "E", "F", "G", "H" };

        // String[] A = { "A", "B", "C", "D", "X", };
        // String[] B = {};

        diffGreedyLCS(null, Arrays.asList(alist), Arrays.asList(blist), matcher);
    }
}
