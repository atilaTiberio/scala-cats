package mx.horace.cats;

import java.util.*;
import java.io.*;
import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.List;

public class Application {

    public static Set<Integer> Foo() {

        List<Integer> x =  Arrays.asList(-1,-2,3,4,5);
        Set<Integer> data = x.stream().filter(y -> y > 0).collect(toSet());
        Set<Integer> data2 = x.stream().filter(y -> y < 0).map(item -> item*-1).collect(toSet());
        data.retainAll(data2);

        return data;
    }

    public static void main (String[] args) {


        System.out.print(Foo());
    }

}