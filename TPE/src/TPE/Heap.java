package TPE;

import java.util.ArrayList;
import java.util.Comparator;


public class Heap<T>{
	
	
	private static int getParent(int index) {
		return (index-1)/2;
	}

	private static int getLeft(int index) {
		return index*2+1;
	}

	private static int getRight(int index) {
		return index*2+2;
	}
	
	private static<E> int moveUp(ArrayList<E> array,int index,Comparator<E> cmp){
		int parent = getParent(index);
		E elem = array.get(index);
		while(index !=0 && cmp.compare(elem,array.get(parent)) > 0){
			array.set(index,array.get(parent));
			index = parent;
			parent = getParent(index);
		}
		array.set(index,elem) ;
		return index;
	}
	
	private static<E> int moveDown(ArrayList<E> array,int index,int size,Comparator<E> cmp){
		int maxChild = getMaxChild(array,index,size,cmp);
		if(maxChild == -1){
			return index;
		}
		E elem = array.get(index);
		while(maxChild != -1 && cmp.compare(elem, array.get(maxChild)) < 0){
			array.set(index, array.get(maxChild));
			index = maxChild;
			maxChild = getMaxChild(array,index,size,cmp);
		}
		array.set(index, elem);
		return index;
	}
	
	private static<E> int getMaxChild(ArrayList<E> array,int index,int size,Comparator<E> cmp){
		int leftChild = getLeft(index);
		int rightChild = getRight(index);
		if(leftChild >= size){
			return -1;//no tiene hijos
		}
		if(rightChild == size){
			return leftChild;
		}
		return cmp.compare(array.get(rightChild), array.get(leftChild)) > 0 ? rightChild : leftChild;
	}

/****************************** FIN FUNCIONES STATIC ***********************************/
	private ArrayList<T> heap;
	private int size;
	private Comparator<T> cmp;
	public Heap( Comparator<T> cmp) {
		this.heap = new ArrayList<T>() ;
		this.cmp = cmp;
	}
	public Heap( ArrayList<T> heap  ,Comparator<T> cmp) {
		this.heap = heap ;
		this.cmp = cmp;
	}
	public Heap( ) {
		this.heap = new ArrayList<T>() ;
	}
	
	public void insert(T elem){
		int elemIndex = size++;
		heap.add(elemIndex,elem);
		balanceHeapIn(elemIndex);
	}
	
	public void remove(T elem){
		int index = getIndexOf(elem);
		if(index == -1){
			return;
		}
		heap.set(index,heap.get(size-1));
		size--;
		balanceHeapRem(index);
	}
	
	public void remove(int index){
		heap.set(index,heap.get(size-1));;
		heap.set(--size,null);
		balanceHeapRem(index);
	}

	private void balanceHeapIn(int index) {
		moveUp(heap,index,cmp);
	}
	
	private void balanceHeapRem(int index){
		moveDown(heap,index,size,cmp);
	}
	
	private int getIndexOf(T elem){
		for(int i = 0;i< size;i++){
			if(cmp.compare(elem, heap.get(i)) == 0){
				return i;
			}
		}
		return -1;
	}
	public void print() {
		StringBuffer s = new StringBuffer();
		s.append('[');
		for(int i = 0; i < size; i++){
			String s2 = " " + heap.get(i)+",";
			s.append(s2);
		}
		s.append(']');
		System.out.println(s.toString());
	}
	



	public static void main(String[] args) {
		Heap<Integer> heap = new Heap<Integer>(new Comparator<Integer>(){

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
				
			}
			
		});
		heap.insert(2);
		heap.insert(3);
		heap.insert(4);
		heap.insert(5);
		heap.insert(6);
		heap.print();
	}
		
	}

	