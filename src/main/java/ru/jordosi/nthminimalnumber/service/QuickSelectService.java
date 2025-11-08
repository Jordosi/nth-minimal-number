package ru.jordosi.nthminimalnumber.service;

import org.springframework.stereotype.Service;

/**
 * Service implementing the QuickSelect algorithm for finding the N-th smallest element.
 * <p>
 * Uses the Hoare's selection algorithm with average time complexity O(n) and
 * worst-case O(n^2). The algorithm partially sorts the array in-place to find
 * the k-th smallest element without fully sorting the entire array.
 * </p>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Quickselect">QuickSelect Algorithm</a>
 */
@Service
public class QuickSelectService {
    public Integer findNthMinimalNumber(Integer[] arr, int k) {
        if (k < 1 || k > arr.length) {
            throw new IllegalArgumentException("N must be in range from 1 to " + arr.length);
        }

        return quickSelect(arr, 0, arr.length - 1, k - 1);
    }

    public Integer quickSelect(Integer[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }

        int pivotIndex = partition(arr, left, right);

        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return quickSelect(arr, left, pivotIndex - 1, k);
        } else {
            return quickSelect(arr, pivotIndex + 1, right, k);
        }
    }

    private int partition(Integer[] arr, int left, int right) {
        int pivot = arr[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, right);
        return i;
    }

    private void swap(Integer[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
