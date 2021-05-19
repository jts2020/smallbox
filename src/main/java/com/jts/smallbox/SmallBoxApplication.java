package com.jts.smallbox;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
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
        speak();
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

    private static void speak() {
        //-Djava.library.path=D:/jacob-1.18/x64
        ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
        try {
            // 音量 0-100
            sap.setProperty("Volume", new Variant(100));
            // 语音朗读速度 -10 到 +10
            sap.setProperty("Rate", new Variant(-2));
            // 获取执行对象
            Dispatch sapo = sap.getObject();
            // 执行朗读
            Dispatch.call(sapo, "Speak", new Variant("您好，正在启动SmallBox"));
            // 关闭执行对象
            sapo.safeRelease();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭应用程序连接
            sap.safeRelease();

        }
    }

    private void streamOp(){
        IntStream.range(0,100).parallel()
                .skip(0L)
                .filter(val -> val % 2 == 0)
                .map(val -> val + new Random().nextInt(100))
                .filter(val -> val>2)
                .collect(ArrayList<Integer>::new,(list, val) -> {list.add(val);},(m,n) -> {m.addAll(n);})
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
