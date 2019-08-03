package com.tsystems.javaschool.tasks.pyramid;

import java.util.Arrays;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        // TODO : Implement your solution here
        //Sum of first n elements of arithmetic progression is S(n)=a(1)*n+d*(n-1)*n/2, where a(1) - first element of progression, d - difference of successive members,
        //n - number of element. We are going to have one element of list in first row, two elemnts in second row and so on. It means, we are going to have arithmetic progression,
        //where a(1) = 1, d = 1, n - number of elements in last row, S(n) - length of list. Making arithmetical transformations, we have n = (-1+-sqrt(1+8*S(n))/2
        //We take positive value of n = (-1+sqrt(1+8*S(n)))/2
        double n = (-1+Math.sqrt(1+8*inputNumbers.size()))/2;
        if (n != Math.round(n) || inputNumbers.contains(null)) {//If n is not integer or input list contains or input list contains null, it's not ok
            throw new CannotBuildPyramidException();
        }
        int[] inputNumbersAsArray = new int[inputNumbers.size()];
        for (int i = 0; i < inputNumbers.size(); i++) {
            inputNumbersAsArray[i] = inputNumbers.get(i);
        }
        Arrays.sort(inputNumbersAsArray);
        int[][] result = new int[(int)n][2*(int)n-1];
        int index = inputNumbers.size() - 1;
        for (int i = (int)n - 1; i >= 0; i--) {
            final int BEGINNING_J = 2*(int)n-2- ((int)n - 1 - i);
            for (int j = BEGINNING_J; j >= 2*(int)n-2-BEGINNING_J; j = j - 2) {
                result[i][j] = inputNumbersAsArray[index];
                index--;
            }
        }
        return result;
    }

}
