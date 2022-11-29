package edu.undav.research.tinygarden;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Factorial {

    
public static void main(String[] args) {
    int N = 45;    
    long inicio = System.nanoTime();
    long result = fibonacci(N,new HashMap<Long, Long>());
    long fin = System.nanoTime();
    System.out.println(result + " " + (fin - inicio));

    inicio = System.nanoTime();
    result= fibonacci2(N);
    fin = System.nanoTime();
    System.out.println(result + " " + (fin - inicio));
}

private static long fibonacci(long n, HashMap<Long,Long> l) {
    if (n == 1 || n == 2)
        return 1;
    else if (l.get(n) == null) 
        l.put(n, fibonacci(n-1,l) + fibonacci(n-2,l));
    return l.get(n);
}

private static long fibonacci2(long n) {
    return (n == 1 || n == 2) ? 1 : fibonacci2(n-1) + fibonacci2(n-2);
}

















    // public static void main(String[] args) {
    //     int fact = 1;
    //     int N = 16;
    //     for (int i = 1; i <= N; i++) {
    //         fact = fact * i;
    //     }
    //     System.out.println(fact);
    // }
}
