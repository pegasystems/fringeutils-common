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
	 * using Longest Common Subsequence ( LCS ) Greedy Algorithm checks for
	 * ObjectA.equals(ObjectB)
	 * 
	 * @param listA
	 * @param listB
	 * @throws Exception
	 */
	public static <T> List<EditCommand> diffGreedyLCS(ProgressMonitor mProgressMonitor, List<T> listA, List<T> listB,
			Matcher<T> matcher) throws Exception {

		int listAsize = listA.size();
		int listBsize = listB.size();

		// long counter = 0;

		List<EditCommand> editScript = new ArrayList<EditCommand>();

		if ((listAsize > 0) || (listBsize > 0)) {

			int N = listA.size();
			int M = listB.size();
			int MAX = N + M;
			int size = (2 * MAX) + 1;
			int offset = size / 2;

			Path[] pathArray = new Path[size];

			int[] V = new int[size];

			V[1] = 0;

			for (int D = 0; D <= MAX; D++) {

				for (int k = -D; ((mProgressMonitor != null) ? (!mProgressMonitor.isCanceled()) : true)
						&& (k <= D); k += 2) {

					int x;
					int kOffset = offset + k;
					int kPlus = kOffset + 1;
					int kMinus = kOffset - 1;

					EditCommand editCommand = null;
					Path prev = null;

					if ((k == -D) || (k != D) && (V[kMinus] < V[kPlus])) {
						x = V[kPlus];
						prev = pathArray[kPlus];
						editCommand = EditCommand.INSERT;
					} else {
						x = V[kMinus] + 1;
						prev = pathArray[kMinus];
						editCommand = EditCommand.DELETE;
					}

					int y = x - k;

					if (kMinus > -1) {
						// LOG.info("Setting kMinus: " + kMinus
						// + " x:" + x + " y:" + y);
						pathArray[kMinus] = null;
					}

					// counter++;
					//
					// if (counter % 10000000L == 0) {
					// LOG.info("Counter: " + counter);
					// }

					Path path = new Path(editCommand, prev);

					while ((x < N) && (y < M) && (matcher.match(listA.get(x), listB.get(y)))) {

						x++;
						y++;

						path = new Path(EditCommand.SNAKE, path);
					}

					V[kOffset] = x;
					pathArray[kOffset] = path;

					if ((x >= N) && (y >= M)) {

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

				pathArray[offset + D - 1] = null;
			}

			throw new Exception("not able to find the LCS/SES");

		}

		return editScript;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Matcher<String> matcher = new Matcher<String>() {

			@Override
			public boolean match(String o1, String o2) {
				return o1.equals(o2);
			}
		};

		// String[] A = { "A", "B", "C", "A", "B", "B", "A" };
		// String[] B = { "C", "B", "A", "B", "A", "C" };

		String[] A = { "C", "D", "E", "F", "G", "H", "I" };
		String[] B = { "A", "B", "C", "D", "E", "F", "G" };

		// String[] B = { "A", "B", "E", "F", "G", "H", "I" };
		// String[] A = { "A", "B", "C", "D", "G", "H", "I" };

		// String[] A = { "A", "C", "E", "G", "I`", "L", "M" };

		// String[] A = { "A", "B", "C", "D" };
		// String[] B = { "E", "F", "G", "H" };

		// String[] A = { "A", "B", "C", "D" };
		// String[] B = { "E", "F", "G", "H", "I" };

		// String[] A = { "A", "B", "C", "D", "X", };
		// String[] B = { "E", "F", "G", "H" };

		// String[] A = { "A", "B", "C", "D", "X", };
		// String[] B = {};

		try {
			diffGreedyLCS(null, Arrays.asList(A), Arrays.asList(B), matcher);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
