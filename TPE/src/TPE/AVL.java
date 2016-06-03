package TPE;

import java.util.Comparator;
import java.util.Iterator;

public class AVL<T> implements Iterable<T> {
 
  private static class Node<T> {
	  private T elem;
	  private Node<T> left;
	  private Node<T> right;
	  private Node<T> parent;
	  private int height;

    
  
    public Node (T elem){
      this.elem = elem;
    } 
    
    public String toString(){
    	return this == null ? null : this.elem.toString();
    }
  }

  public Node<T> root;
  private Comparator<T> cmp;


  
  public AVL(Comparator<T> cmp) {
	  this.cmp = cmp;
  }
  public int height (Node<T> t){
    return t == null ? -1 : t.height;
  }
  
 
  public int max (int a, int b){
    return a > b ? a : b;
  }
  

  public boolean insert(T elem){
	  Node<T> insert = new Node<T>(elem);
      root = insert(insert, root);
     
      return true;
  }
  
 

private Node<T> insert (Node<T> insert, Node<T> current) {
	    if (current == null)
	    	return insert;
	    else if (cmp.compare(insert.elem,current.elem) <= 0){
	    	current.left = insert(insert, current.left);
	    	current.left.parent = current;
	      
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
		      current.right = insert(insert ,current.right); 
		      current.right.parent = current;
		      if ( height(current.right) - height(current.left) > 1){
		        if (cmp.compare(insert.elem,current.right.elem) > 0){
		          current = rotateWithRightChild(current);		       
		        }
		        else{
		          current = doubleWithRightChild(current);
		        }
		      }
	    }       
	    current.height = max (height(current.left), height(current.right)) + 1;
	    //System.out.println("insertando "+insert.elem +"elem "+current.elem+" "+current.height+"  "+height(current.left)+" "+height(current.right));
	    return current;
  }
  

private Node<T> rotateWithLeftChild (Node<T> n){
    Node<T> aux = n.left;
    aux.parent = n.parent;
    n.parent = aux;
    if(aux.right != null)
    	aux.right.parent = n;
    n.left = aux.right;
    aux.right = n;
    n.height = max (height (n.left), height(n.right)) + 1;
    aux.height = max(height (aux.left), height(aux.right)) + 1;  
    return aux;
  }
  

  private Node<T> doubleWithLeftChild (Node<T> n){
    n.left = rotateWithRightChild(n.left);
    return rotateWithLeftChild (n);
  }
  
  private Node<T> rotateWithRightChild (Node<T> n){
	    Node<T> aux = n.right;
	    aux.parent = n.parent;
	    n.parent = aux;
	    n.right = aux.left;
	    if(aux.left != null)
	    	aux.left.parent = n;
	    aux.left = n;   
	    n.height = max (height (n.right), height(n.left)) + 1; 
	    aux.height = max (height(aux.left), height(aux.right)) + 1;
	    return aux;
	  }

 
  private Node<T> doubleWithRightChild (Node<T> n){
    n.right = rotateWithLeftChild (n.right);
    return rotateWithRightChild (n);
  }

  public boolean isEmpty(){
    return root == null;
  }

  private class Box<T>{
	  private Node<T> node;
	  private T max;
	
	  public Box(Node<T> node, T max) {
		super();
		this.node = node;
		this.max = max;
	}
	  
	  
  }

 

 
    private Box<T> findMaxAndRemoveNode(Node<T> node) {
        Node<T> current = node;
        if(current == null)
        	return null;
        while(current.right!=null){
        	current = current.right;
        }
        T max = current.elem;
        if(current.left != null)
        	current.left.parent = current.parent;
        if(current != node){
        	current.parent.right = current.left;
        	current = current.parent;
        	while(current != node){
        		current.height = Math.max(height(current.left), height(current.right))+1;
        		current = current.parent;
        	}
        }
        else{
        	node = current.left;
        }
       
        return new Box<T>(node,max);    
       }



  public void remove( T x ) {
      root = remove(x, root);
  }

  private Node<T> remove(T elem, Node<T> current) {
	  if(current == null){
			return null;
		}
		int c = cmp.compare(elem,current.elem);
		if(c > 0){
			current.right = remove(elem,current.right);
			if ( height(current.left) - height(current.right) > 1){
		       current = doubleWithLeftChild(current);
		      }
			current.height = max (height(current.left), height(current.right)) + 1;
			return current;
		}else if(c < 0){
			current.left = remove(elem,current.left);
			if (height(current.right) - height(current.left) > 1){
		        current = rotateWithRightChild(current);
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
		Box<T> b = findMaxAndRemoveNode(current.left);
		current.left = b.node;
		current.elem = b.max;
		if (height(current.right) - height(current.left) > 1){
	        current = rotateWithRightChild(current);
	      }
	 current.height = max (height(current.left), height(current.right)) + 1;	
		return current;
  } 
  



 
public boolean contains(T elem){
    return contains(elem, root); 
  }


  private boolean contains(T elem, Node<T> current) {
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
	private  void print(Node<T> current){
		if(current == null){
			System.out.println("trolled");
			return;
		}
		if(current.left == null && current.right == null ){
			System.out.println(current.elem + " padre : "+current.parent+" altura "+ current.height);
			return;
		}else if (current.left!= null && current.right != null){
			System.out.println("Padre: "+current.elem+", Hijo izquierdo: "+current.left.elem+",Hijo derecho: "+current.right.elem+ " padre : "+current.parent +" altura "+ current.height);
			print(current.left);
			print(current.right);
		}else if(current.left != null){
			System.out.println("Padre: "+current.elem+", Hijo izquierdo: "+current.left.elem+ " padre : "+current.parent+" altura "+ current.height);
			print(current.left);
		}else{
			System.out.println("Padre: "+current.elem+",Hijo derecho: "+current.right.elem+ " padre : "+current.parent+" altura "+ current.height);
			print(current.right);
		}
		
	}
	
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public T updateMax() {               
		    Node<T> current = root;
		    if(current == null)
		        return null;
		     while(current.right!=null){
		        current = current.right;
		      }
		       T max = findMax(current.left);
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
	
	
	private T findMax(Node<T> node) {     
		        Node<T> current = node;
		        if(current == null)
		        	return null;
		        while(current.right!=null){
		        	current = current.right;
		        }
		       return current.elem;   
	}
	public static void main(String[] args) {
		AVL<Integer> tree = new AVL<Integer>(new Comparator<Integer>(){

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
			
		});
		tree.insert(2);
		tree.insert(3);
		tree.insert(4);
		tree.insert(5);
		tree.insert(6);
		tree.insert(1);
		tree.insert(0);
		tree.insert(7);
		tree.remove(5);
		tree.remove(3);
		tree.remove(1);
		tree.remove(0);
		tree.print();
		

	}
	


 
}