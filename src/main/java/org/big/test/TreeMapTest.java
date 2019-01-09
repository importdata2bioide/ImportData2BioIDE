package org.big.test;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class TreeMapTest {
	
	public static void main(String[] args) {
		Map<String,Double> map  = new TreeMap<>();
		
		for(int i = 0;i<6;i++) {
			
			map.put(String.valueOf((int)(Math.random()*100)) , (double) i);
		}
		
        //Map集合循环遍历二  通过迭代器的方式
        System.out.println("第二种：通过Map.entrySet使用iterator遍历key和value：");
        Iterator<Entry<String, Double>> it = map.entrySet().iterator();
        while(it.hasNext()){
             Entry<String, Double> entry = it.next();
             System.out.println("key:"+entry.getKey()+"  value:"+entry.getValue());
       }
        
        Map<Integer,Integer> map1 = new TreeMap<Integer,Integer>();  //默认的TreeMap升序排列
		
//        map1 = ((TreeMap) map1).descendingMap();
		
		map1.put(1,2);
		map1.put(2,4);
		map1.put(7, 1);
		map1.put(5,2);
		System.out.println("map1="+map1);

	}

}
