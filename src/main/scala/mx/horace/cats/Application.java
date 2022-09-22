package mx.horace.cats;

import java.util.*;
import java.io.*;
import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Application {

    public static Set<Integer> Foo() {

        List<Integer> x = Arrays.asList(-1, -2, 3, 4, 5);
        Set<Integer> data = x.stream().filter(y -> y > 0).collect(toSet());
        Set<Integer> data2 = x.stream().filter(y -> y < 0).map(item -> item * -1).collect(toSet());
        data.retainAll(data2);

        return data;
    }


    public static Set<Integer> PairOfPositiveAndNegative(int[] array) {

        HashMap<Integer, Object> validator = new HashMap();

        Set<Integer> result = new HashSet<>();

        Arrays.sort(array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 0) {
                validator.put(Math.abs(array[i]), "Done");
            }
            if (array[i] > 0 && validator.get(array[i]) != null) {
                result.add(array[i]);
            }
        }
        return result;
    }

    public static void main (String[] args) {

        try{
            throw new NullPointerException("Something wrong");
        }
        catch (Exception e) {
            System.out.println(e);

        }


    }

/*
    public static void main(String[] args) {
        boolean pair_exists = false;

        int arr[] = { 4, 8, 9, -4, 1, -1, -8, -9 };
        int result[] = new int[arr.length];

        Arrays.sort(arr);

        int k = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < 0) {
                if (java.util.Arrays.binarySearch(arr, -arr[i]) != -1) {
                    System.out.println(+-arr[i]);
                    result[k] = arr[i];
                    k++;
                    pair_exists = true;
                }
            }
            else
                break;
        }
        if (pair_exists == false)
            System.out.println("No such pair exists");
        System.out.println(Arrays.toString(result));
    }
    */


    public List getArrayOfNumbers(final int[] numberList){
        List result = new ArrayList();
        if(! (numberList.length > 0)){
            for(int i=0 ; i<= numberList.length - 1; i++){
                int currentNumber = numberList[i];
                for(int j=i+1;j<numberList.length;j++){
                    int value = Math.abs(numberList[j]);
                    if(currentNumber == value){
                        result.add(value);
                        break;
                    }
                }
            }
        }
        return result;
    }


}