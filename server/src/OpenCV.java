
import ImageService.*;
import org.opencv.core.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class OpenCV {
    public static boolean isSuite(int numbers[]){
        Arrays.sort(numbers);
        for (int i = 0; i < numbers.length - 1; i += 2) {
            if (!(numbers[i] + 1 == numbers[i + 1])) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println(isSuite(new int[]{5, 5, 5}));
        System.out.println(isSuite(new int[]{1,2,3,4,5}));
        System.out.println(isSuite(new int[]{5, 3, 8}));
        System.out.println(isSuite(new int[]{5, 4, 5}));
    }
}

/*
Cluster 1: 0011
Cluster 1: 0026
Cluster 1: 0026
Cluster 2: 0006
Cluster 2: 0001
Cluster 3: 0033
Cluster 3: 0030
Cluster 3: 0030
Cluster 4: 0018
Cluster 4: 0015
Cluster 5: 0021
Cluster 5: 0007
Cluster 5: 0007
test

 */