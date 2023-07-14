package com.jts.smallbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author jts
 */
@SpringBootApplication()
public class SmallBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmallBoxApplication.class,args);
        System.out.println(89 | 12);
        Integer[] t = new Integer[]{9,1,0,5,-1};
        Arrays.sort(t,(a,b) ->{
            if(a> b){
                return -1;
            }else if(a<b){
                return 1;
            }
            return 0;
        });
        System.out.println(Arrays.toString(t));
        System.out.println(Arrays.toString(sort(new int[]{9,1,0,5,-1})));
        System.out.println(halfSearch(new int[]{11},12));
    }

    private void streamOp(){
        IntStream.range(0,100).parallel()
                .skip(0L)
                .filter(val -> val % 2 == 0)
                .map(val -> val + new Random().nextInt(100))
                .filter(val -> val>2)
                .collect(ArrayList<Integer>::new, ArrayList::add, ArrayList::addAll)
                .stream()
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }

    private static int[] sort(int[] arr){
        for(int i = 1;i<arr.length;i++){
            boolean flag = true;
            for (int j = 0;j<arr.length-1;j++){
                if(arr[j]>arr[j+1]){
                    arr[j] = arr[j]+arr[j+1];
                    arr[j+1] = arr[j] - arr[j+1];
                    arr[j] = arr[j] - arr[j+1];
                    flag = false;
                }
            }
            if(flag){
                return arr;
            }
        }
        return arr;
    }

    private static int halfSearch(int[] arr,int target){
       int min = 0;
       int max = arr.length-1;
       int mid ;
       while (min <= max){
           mid = (min+max)/2;
           if(arr[mid]>target){
               max = mid-1;
           }else if(arr[mid] < target){
               min = mid+1;
           }else {
               return mid;
           }
       }
       return -1;
    }

}
