import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

public class ZFTest {

    @Test
    public void forSort(){
        int[] arr = new int[]{1, 1, 34, 9, 45, 8, 3, 21, 6, 1, 8, 3, 7, 6, 22, 88, 34, 21};
        Map<Integer,Integer> map = new HashMap<>();

        for (int i = 0;i<arr.length;i++) {
            int count = 1;
            if (map.containsKey(arr[i])) {
                map.put(arr[i],map.get(arr[i]) + 1);
            } else {
                map.put(arr[i],count);
            }
        }
        Iterator<Integer> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            Integer next = iterator.next();
            Integer value = map.get(next);
            if (value != 1) {
                System.out.println(next);
            }
        }
    }

    @Test
    public void testArr(){
        int[] arr = new int[]{1,2,3,4,5,6,7,8,9,10};
        //int i = arrIndex(arr, 6);
        int i = arrIndex2(arr,8);
        System.out.println(i);
    }

    //递归，需是递增有序数组
    public int arrIndex(int[] arr, int value){
        int leng = arr.length;
        if (leng == 0) {
            return -1;
        }

        int middleIndex = leng / 2;
        int middleValue = arr[middleIndex];
        if (value == middleValue) {
            return middleIndex;
        } else {
            while (value < middleValue) {
                int[] leftArr = Arrays.copyOfRange(arr, 0, middleIndex);
                int index = arrIndex(leftArr, value);
                if(arr[index] == value) {
                    return index;
                } else {
                    return -1;
                }
            }
            while (value > middleValue){
                int[] rightArr = Arrays.copyOfRange(arr,middleIndex,leng);
                int index = arrIndex(rightArr, value);
                if(arr[index + middleIndex] == value) {
                    return index + middleIndex;
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    //二分法，需是递增有序数组
    public int arrIndex2(int[] arr, int value){
        int leng = arr.length;
        if (leng == 0) {
            return -1;
        }

        int leftIndex = 0;
        int rightIndex = leng - 1;
        while (leftIndex <= rightIndex){
            int middleIndex = leftIndex + (rightIndex-leftIndex)/2;
            if (value == arr[middleIndex]) {
                return middleIndex;
            } else if (value < arr[middleIndex]){
                rightIndex = middleIndex - 1;
            } else if (value > arr[middleIndex]) {
                leftIndex = middleIndex + 1;
            }
        }
        return -1;
    }

    @Test
    public void disTest(){
        distributeCandy(10);
    }

    /**
     * 理论上，整数的次方值最大，即 5*5 > 4*6 > 3*7，本方法所有分配方式都是基于这个前提
     * @param candyCount 糖果总数，也是可以分配堆数的最大值
     */
    public void distributeCandy(int candyCount){
        //二维数组，用来存放乘积最大的分配方式
        int[][] earr = new int[candyCount][];
        //存放乘积最大值
        int[] yarr = new int[candyCount];

        for (int i=1;i<candyCount;i++) {
            //分配为i堆时，每堆的数量
            int m = candyCount / i;
            int n = candyCount % i;
            int[] arr = new int[i];
            for (int j = 0;j<i;j++) {
                if (n > 0 && j < n) {
                    arr[j] = m + 1;
                } else {
                    arr[j] = m;
                }
            }
            earr[i] = arr;

            //每种分配方式的乘积
            int mul = 1;
            for (int j = 0;j<arr.length;j++){
                mul *= arr[j];
            }
            yarr[i] = mul;
        }

        //乘积最大值
        int maxValue = yarr[0];
        for (int i = 1;i<yarr.length;i++) {
            if (maxValue < yarr[i]) {
                maxValue = yarr[i];
            }
        }

        System.out.println("最大积：" + maxValue);

        //有可能某个乘积最大值对应1个以上的分配方式
        for (int i = 0;i<yarr.length;i++) {
            if (yarr[i] == maxValue) {
                System.out.println("分配方法：" + Arrays.toString(earr[i]));
            }
        }
    }
}
