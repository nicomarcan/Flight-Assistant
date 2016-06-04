package TPE;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class test {
	public static void main(String[] args) {	

		AVL<Integer> avl = new AVL<Integer>(new Comparator<Integer>(){

			@Override
			public int compare(Integer arg0, Integer arg1) {
				return arg0.compareTo(arg1);
			}
			
		});
		Random r = new Random();
		
		Set<Integer> s = new HashSet<Integer>();
		Set<Integer> set = new TreeSet<Integer>();
		for(int i = 0; i < 2000000;i++){
			s.add(r.nextInt());
		}
		System.out.println("insertando en avl");
		Long initial = System.currentTimeMillis();	
		for(Integer i : s){
			avl.insert(i);
		}
		Long finalT = System.currentTimeMillis();
		System.out.println(finalT - initial);
		System.out.println("insertando treeSet");
		initial = System.currentTimeMillis();	
		for(Integer i : s){
			set.add(i);
		}
		 finalT = System.currentTimeMillis();
		System.out.println(finalT - initial);
		System.out.println("removiendo en avl");
		initial = System.currentTimeMillis();	
		for(Integer i : s){
			avl.remove(i);
		
		}
		
		finalT = System.currentTimeMillis();
		System.out.println(finalT - initial);
		System.out.println("removiendo treeSet");
		initial = System.currentTimeMillis();	
		for(Integer i : s){
			set.remove(i);
		}
		 finalT = System.currentTimeMillis();
		System.out.println(finalT - initial);
	
		

		
		

	}
}
