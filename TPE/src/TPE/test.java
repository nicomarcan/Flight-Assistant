package TPE;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class test {
	public static void main(String[] args) {	
		HashMap<Integer,Integer> m = new HashMap<Integer,Integer>();
		AVL<Integer> avl = new AVL<Integer>(new Comparator<Integer>(){

			@Override
			public int compare(Integer arg0, Integer arg1) {
				return arg0.compareTo(arg1);
			}
			
		});
		Long initial = System.currentTimeMillis();		
		for(int i = 0; i < 500000;i++){
			avl.insert(i);
		}
		avl.updateMax();
		avl.updateMax();
		for(int i = 0 ;i<500000;i++){
			avl.remove(i);
		}
		Long finalT = System.currentTimeMillis();
		System.out.println(finalT - initial);
		initial = System.currentTimeMillis();		
		for(int i = 0; i < 500000;i++){
			m.put(i, i);
		}
		Integer max = Integer.MIN_VALUE;
		for(Integer i : m.values()){
			if(i > max)
				i = max;
		}
		m.remove(max);
		max = Integer.MIN_VALUE;
		for(Integer i : m.values()){
			if(i > max)
				i = max;
		}
		m.remove(max);
		for(int i = 0 ;i<500000;i++){
			m.remove(i);
		}
		finalT = System.currentTimeMillis();
		System.out.println(finalT - initial);
		

		
		

	}
}