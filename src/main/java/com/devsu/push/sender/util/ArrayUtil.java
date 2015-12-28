package com.devsu.push.sender.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Array Utils.
 */
public class ArrayUtil {

	/**
	 * Splits an array in a list of subarrays of size <i>maxSubArraySize</i>.
	 * @param elementArray The array to be split.
	 * @param maxSubArraySize The final size of the subarrays.
	 * @return The list containing the subarrays.
	 */
	public static <T> List<T[]> splitArray(T[] elementArray, int maxSubArraySize) {
		List<T[]> result = new ArrayList<T[]>();
		if (elementArray == null || elementArray.length == 0)
			return result;
		int indexFrom = 0;
		int indexTo = 0;
		int slicedItems = 0;
		while (slicedItems < elementArray.length) {
			indexTo = indexFrom + Math.min(maxSubArraySize, elementArray.length - indexTo);
			T[] slice = Arrays.copyOfRange(elementArray, indexFrom, indexTo);
			result.add(slice);
			slicedItems += slice.length;
			indexFrom = indexTo;
		}
		return result;
	}
}