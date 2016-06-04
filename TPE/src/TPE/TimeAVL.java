package TPE;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/** modifique las rotaciones les agregue la resta de los dias**/

/** Implementación particular de un AVL de vuelos, en la que se ordena por tiempo de llegada, 
 * pero cada nodo tiene referencia al maximo tiempo de salida de sus hijos, 
 * para facilitar la elección del vuelo que menor tiempo tarda pero que puedo tomar**/
public class TimeAVL implements Iterable<Flight>{
	private int dayIndex;
	 private static class Node {
		  private Flight elem;
		  private Node left;
		  private Node right;
		  private Node parent;
		  private Integer maxDepTime = Integer.MIN_VALUE;
		  private int height;
	    
	  
	    public Node (Flight elem){
	      this.elem = elem;
	    } 
	    
	    public String toString(){
	    	return this == null ? null : this.elem.toString();
	    }
	  }

	  public Node root;
	  private Comparator<Flight> cmp;

	  
	  public TimeAVL(Comparator<Flight> cmp,int day) {
		  this.cmp = cmp;
		  this.dayIndex = day;
	  }
	  public int height (Node t){
	    return t == null ? -1 : t.height;
	  }
	  
	 
	  public int max (int a, int b){
	    return a > b ? a : b;
	  }
	  

	  public boolean insert(Flight elem){
		  Node insert = new Node(elem);
	      root = insert(insert, root);
	      return true;
	  }
	  
	 

	private Node insert (Node insert, Node current) {
		    if (current == null)
		    	return insert;	
		    else if (cmp.compare(insert.elem,current.elem) <= 0){
		    	Integer lastMax = null;
		    	if(current.left != null){
		    		 lastMax = current.left.maxDepTime;
		    	}
		    	current.left = insert(insert, current.left);
		    	current.left.parent = current;
		    	   if(lastMax != null && lastMax != current.left.maxDepTime){
				    	  if(current.left.maxDepTime > current.maxDepTime)
				    		  current.maxDepTime = current.left.maxDepTime;
				      }
				      
		    	   if(lastMax == null){			    	
			    		current.maxDepTime = current.left.elem.getDepartureTime()-((dayIndex-current.left.elem.getCurrentDayIndex()+7)%7)*(60*24) > current.maxDepTime ? current.left.elem.getDepartureTime()-((dayIndex-current.left.elem.getCurrentDayIndex()+7)%7)*(60*24) : current.maxDepTime;			    	
			    	}	
		    	
		      
		     if (height(current.left) - height(current.right) > 1){
		        if (cmp.compare(insert.elem,current.left.elem) <= 0){
		          current = rotateWithLeftChild(current);
		        }
		        else {
		          current = doubleWithLeftChild(current);
		        }
		      }
		    }
		    else if (cmp.compare(insert.elem,current.elem) > 0){
		    	Integer lastMax = null;
		    	if(current.right != null){
		    		 lastMax = current.right.maxDepTime;
		    	}
			      current.right = insert(insert ,current.right); 
			      current.right.parent = current;
			      if(lastMax != null && lastMax != current.right.maxDepTime){
			    	  if(current.right.maxDepTime > current.maxDepTime)
			    		  current.maxDepTime = current.right.maxDepTime;
			      }
			 
			      if(lastMax == null){					    			
			    		current.maxDepTime = current.right.elem.getDepartureTime()-((dayIndex-current.right.elem.getCurrentDayIndex()+7)%7)*(60*24)> current.maxDepTime ? current.right.elem.getDepartureTime()-((dayIndex-current.right.elem.getCurrentDayIndex()+7)%7)*(60*24) : current.maxDepTime;			    	
			    	}			     
			      if ( height(current.right) - height(current.left) > 1)
			        if (cmp.compare(insert.elem,current.right.elem) > 0){
			          current = rotateWithRightChild(current);		       
			        }
			        else{
			          current = doubleWithRightChild(current);
			        }
		    }       
		    current.height = max (height(current.left), height(current.right)) + 1;
		    //System.out.println("insertando "+insert.elem +"elem "+current.elem+" "+current.height+"  "+height(current.left)+" "+height(current.right));
		    return current;
	  }
	  

	  private Node rotateWithLeftChild (Node n){
	    Node aux = n.left;
	    aux.parent = n.parent;
	    n.parent = aux;
	    if(aux.right != null)
	    	aux.right.parent = n;
	    n.left = aux.right;
	    aux.right = n;
	    n.height = max (height (n.left), height(n.right)) + 1;
	    aux.height = max(height (aux.left), height(aux.right)) + 1;  
	    updateMaxDep(n);
	    updateMaxDep(aux);
	    return aux;
	  }
	  

	  private Node doubleWithLeftChild (Node n){
	    n.left = rotateWithRightChild(n.left);
	    return rotateWithLeftChild (n);
	  }
	  
	  private Node rotateWithRightChild (Node n){
	    Node aux = n.right;
	    aux.parent = n.parent;
	    n.parent = aux;
	    n.right = aux.left;
	    if(aux.left != null)
	    	aux.left.parent = n;
	    aux.left = n;   
	    n.height = max (height (n.right), height(n.left)) + 1; 
	    aux.height = max (height(aux.left), height(aux.right)) + 1;
	    updateMaxDep(n);
	    updateMaxDep(aux);
	    return aux;
	  }

	 
	  private Node doubleWithRightChild (Node n){
	    n.right = rotateWithLeftChild (n.right);
	    return rotateWithRightChild (n);
	  }

	  public boolean isEmpty(){
	    return root == null;
	  }

	  private class Box{
		  private Node node;
		  private Flight max;
		
		  public Box(Node node, Flight max) {
			super();
			this.node = node;
			this.max = max;
		}
		  
		  
	  }

	 

	 
	  private Box findMaxAndRemoveNode(Node node) {
	        Node current = node;
	        if(current == null)
	        	return null;
	        while(current.right!=null){
	        	current = current.right;
	        }
	        Flight max = current.elem;
	        if(current.left != null)
	        	current.left.parent = current.parent;
	        if(current != node){
	        	current.parent.right = current.left;
	        	if(current.parent.right != null)
	        		current.parent.maxDepTime = current.parent.right.maxDepTime;
	        	current = current.parent;
	        	while(current != node){
	        		current.height = Math.max(height(current.left), height(current.right))+1;
	        		updateMaxDep(current);
	        		current = current.parent;
	        	}
	        }
	        else{
	        	node = current.left;
	        }
	       
	        return new Box(node,max);    
	       }



	
	  private void updateMaxDep(Node current) {
		  current.maxDepTime = Integer.MIN_VALUE;
		if(current.left != null ){
			current.maxDepTime = current.left.elem.getDepartureTime() -((dayIndex-current.left.elem.getCurrentDayIndex()+7)%7)*(60*24);
			current.maxDepTime = current.left.maxDepTime > current.maxDepTime ? current.left.maxDepTime : current.maxDepTime;
		}if(current.right != null){
			current.maxDepTime = current.right.elem.getDepartureTime() -((dayIndex-current.right.elem.getCurrentDayIndex()+7)%7)*(60*24)> current.maxDepTime ? current.right.elem.getDepartureTime() -((dayIndex-current.right.elem.getCurrentDayIndex()+7)%7)*(60*24) : current.maxDepTime;
			current.maxDepTime = current.right.maxDepTime > current.maxDepTime ? current.left.maxDepTime : current.maxDepTime;
		}	
	}
	public void remove( Flight x ) {
	      root = remove(x, root);
	  }

	  private Node remove(Flight elem, Node current) {
		  if(current == null){
				return null;
			}
			int c = cmp.compare(elem,current.elem);
			if(c > 0){
				current.right = remove(elem,current.right);
				updateMaxDep(current);			
				if (height(current.left) - height(current.right) > 1){
			        if (height(current.left.left) > height(current.left.right)){
			          current = rotateWithLeftChild(current);
			        }
			        else {
			          current = doubleWithLeftChild(current);
			        }
			      }																												
				current.height = max (height(current.left), height(current.right)) + 1;
				return current;
			}else if(c < 0){
				current.left = remove(elem,current.left);
				updateMaxDep(current);			
				 if ( height(current.right) - height(current.left) > 1){
				        if (height(current.right.right) > height(current.right.left)){
				          current = rotateWithRightChild(current);		       
				        }
				        else{
				          current = doubleWithRightChild(current);
				        }
				 }
				 current.height = max (height(current.left), height(current.right)) + 1;			
				return current;
			}
			if(!current.elem.equals(elem)){
				current.left = remove(elem,current.left);
				return current;
			}
			if(current.right == null && current.left == null)
				return null;
			if(current.right != null && current.left == null){
				current.right.parent = current.parent;
				return current.right;
			}
			if(current.left != null && current.right == null){
				current.left.parent = current.parent;
				return current.left;
			}
			Box b = findMaxAndRemoveNode(current.left);
			current.left = b.node;
			current.elem = b.max;
			if (height(current.right) - height(current.left) > 1){
		        current = rotateWithRightChild(current);
		      }
		 current.height = max (height(current.left), height(current.right)) + 1;	
			return current;
	  } 
	  
	  
	 


	  public boolean contains(Flight elem){
	    return contains(elem, root); 
	  }


	  private boolean contains(Flight elem, Node current) {
	    if (current == null)
	    	return false; 
	    
	    else if (cmp.compare(elem,current.elem) < 0)
	    	 return contains(elem, current.left);
	    
	    else if (cmp.compare(elem,current.elem) > 0)
	    	 return contains(elem, current.right);
	    
	    return true; 
	  }
	  public void print(){
			print(root);
		}
		private  void print(Node current){
			if(current == null){
				System.out.println("trolled");
				return;
			}
			if(current.left == null && current.right == null ){
				System.out.println(current.elem + " padre : "+current.parent+" altura "+ current.height);
				return;
			}else if (current.left!= null && current.right != null){
				System.out.println("Padre: "+current.elem+" maxDep: "+current.maxDepTime+", Hijo izquierdo: "+current.left.elem+",Hijo derecho: "+current.right.elem+ " padre : "+current.parent +" altura "+ current.height);
				print(current.left);
				print(current.right);
			}else if(current.left != null){
				System.out.println("Padre: "+current.elem+" maxDep: "+current.maxDepTime+", Hijo izquierdo: "+current.left.elem+ " padre : "+current.parent+" altura "+ current.height);
				print(current.left);
			}else{
				System.out.println("Padre: "+current.elem+" maxDep: "+current.maxDepTime+",Hijo derecho: "+current.right.elem+ " padre : "+current.parent+" altura "+ current.height);
				print(current.right);
			}
			
		}
		
	
		
		public Flight updateMax() {               
			    Node current = root;
			    if(current == null)
			        return null;
			     while(current.right!=null){
			        current = current.right;
			      }
			       Flight max = findMax(current.left);
			       if(max == null){
			    	   if(current.parent != null){
			    		   current.parent.right = null;
			    	   		return current.parent.elem;
			    	   }
			    	   return null;
			       }		    
			      current.left.parent = current.parent;
			      if(current.parent != null)
			    	  current.parent.right = current.left;
			      else
			    	  root = current.left;	
			      return max;    	      
			       
		}
		
		
		private Flight findMax(Node node) {     
			        Node current = node;
			        if(current == null)
			        	return null;
			        while(current.right!=null){
			        	current = current.right;
			        }
			       return current.elem;   
		}
		@Override
		public Iterator<Flight> iterator() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public static void main(String[] args) {
			ArrayList<Day> days = new ArrayList<Day>();
			Airport a = new Airport("BUE", -80.0, 100.0);
			Airport b = new Airport("LON", 80.0, 25.0);
			days.add(Day.MONDAY);
			//days.add(Day.FRIDAY);
			ArrayList<Day> d = new ArrayList<Day>();
			d.add(Day.SUNDAY);
			Flight f1 = new Flight("AA", "1234", days, b.getName(), a.getName(), 1200, 360,7.8 );
			Flight f2 = new Flight("ABA", "1234", d, b.getName(), a.getName(), 1200, 1680,7.8);
			Flight f3 = new Flight("ACA", "1234", days, b.getName(), a.getName(), 1300, 261,7.8);
			Flight f4 = new Flight("AZA", "1235", days, b.getName(), a.getName(), 1299, 331,7.8);
			Flight f5 = new Flight("AxA", "1235", days, b.getName(), a.getName(), 1300, 350,7.8);
			f1.setCurrentDayIndex(0);
			f2.setCurrentDayIndex(6);
			f3.setCurrentDayIndex(0);
			f4.setCurrentDayIndex(0);
			f5.setCurrentDayIndex(0);
			TimeAVL avl = new TimeAVL(new Comparator<Flight>(){

				@Override
				public int compare(Flight o1, Flight o2) {		
					return new Integer((o2.getDepartureTime()+o2.getFlightTime())%(60*24)).compareTo((o1.getFlightTime()+o1.getDepartureTime())%(60*24));
				}
				
			},1);
			avl.insert(f1);
			avl.insert(f2);
			avl.insert(f3);
			avl.insert(f4);
			avl.insert(f5);
			avl.remove(f3);
			avl.remove(f5);
			avl.print();
		}
	
}
