public class Splay_BST
{
   public BSTEntry root;	// References the root node of the BST

   public Splay_BST()
   {
      root = null;
	  int height = 0;
   }


   /* ================================================================
      findEntry(k): find entry with key k 

      Return:  reference to (k,v) IF k is in BST
               reference to parent(k,v) IF k is NOT in BST (for put)
      ================================================================ */
   public BSTEntry findEntry(String k)
   {
       BSTEntry curr_node;   // Help variable
       BSTEntry prev_node;   // Help variable

       /* --------------------------------------------
	  Find the node with key == "k" in the BST
          -------------------------------------------- */
       curr_node = root;  // Always start at the root node
       prev_node = root;  // Remember the previous node for insertion

       while ( curr_node != null )
       {
          if ( k.compareTo( curr_node.key ) < 0 )
	  {
	     prev_node = curr_node;       // Remember prev. node
	     curr_node = curr_node.left;  // Continue search in left subtree
	  }
          else if ( k.compareTo( curr_node.key ) > 0 )
	  {
	     prev_node = curr_node;       // Remember prev. node
	     curr_node = curr_node.right; // Continue search in right subtree
	  }
          else 
	  {
	     // Found key in BST 
	     return curr_node;
	  }
       }

       /* ======================================
	  When we reach here, k is NOT in BST
          ====================================== */
       return prev_node;		// Return the previous (parent) node
   }

   /* ================================================================
      get(k): find key k and return assoc. value
      ================================================================ */
   public Integer get(String k)
   {
       BSTEntry p;   // Help variable

       /* --------------------------------------------
	  Find the node with key == "key" in the BST
          -------------------------------------------- */
       p = findEntry(k);

       splay(p);

       if ( k.equals( p.key ) )
	  return p.value;
       else
 	  return null;
   }


   /* ================================================================
      put(k, v): store the (k,v) pair into the BST

         1. if the key "k" is found in the BST, we replace the val
            that is associated with the key "k"
         1. if the key "k" is NOT found in the BST, we insert
	    a new node containing (k, v)
      ================================================================ */
   public void put(String k, Integer v)
   {
       BSTEntry p;   // Help variable

       /* ----------------------------------------------------------
	  Just like linked list, insert in an EMPTY BST
	  must be taken care off separately by an if-statement
          ---------------------------------------------------------- */
       if ( root == null )
       {  // Insert into an empty BST

          root = new BSTEntry( k, v );
	  return;
       }

       /* --------------------------------------------
	  Find the node with key == "key" in the BST
          -------------------------------------------- */
       p = findEntry(k);

       if ( k.equals( p.key ) )
       {
          p.value = v;			// Update value
	  return;
       }

       /* --------------------------------------------
	  Insert a new entry (k,v) under p !!!
          -------------------------------------------- */
       BSTEntry q = new BSTEntry( k, v );

       q.parent = p;

       if ( k.compareTo( p.key ) < 0 )
	  p.left = q;            	// Add q as left child
       else 
	  p.right = q;           	// Add q as right child
   }


   /* =======================================================
      remove(k): delete entry containg key k
      ======================================================= */
   public void remove(String k)
   {
       BSTEntry p;        // Help variable
       BSTEntry parent;   // parent node
       BSTEntry succ;     // successor node

       /* --------------------------------------------
          Find the node with key == "key" in the BST
          -------------------------------------------- */
       p = findEntry(k);

       if ( ! k.equals( p.key ) )
          return;			// Not found ==> nothing to delete....


       /* ========================================================
	  Hibbard's Algorithm
	  ======================================================== */

       if ( p.left == null && p.right == null ) // Case 0: p has no children
       {
	  parent = p.parent;

	  /* --------------------------------
	     Delete p from p's parent
	     -------------------------------- */
	  if ( parent.left == p )
	     parent.left = null;
	  else
	     parent.right = null;

          return;
       }

       if ( p.left == null )                 // Case 1a: p has 1 (right) child
       {
          parent = p.parent;

	  /* ----------------------------------------------
	     Link p's right child as p's parent child
             ---------------------------------------------- */
          if ( parent.left == p )
             parent.left = p.right;
          else
             parent.right = p.right;

          return;
       }

       if ( p.right== null )                 // Case 1b: p has 1 (left) child
       {
          parent = p.parent;

          /* ----------------------------------------------
             Link p's left child as p's parent child
             ---------------------------------------------- */
          if ( parent.left == p )
             parent.left = p.left;
          else
             parent.right = p.left;

          return;
       }

       /* ================================================================
	  Tough case: node has 2 children - find successor of p

	  succ(p) is as as follows:  1 step right, all the way left

	  Note: succ(p) has NOT left child !
          ================================================================ */
       succ = p.right;			// p has 2 children....

       while ( succ.left != null )
	   succ = succ.left;

       p.key = succ.key;		// Replace p with successor
       p.value = succ.value;


       /* --------------------------------
          Delete succ from succ's parent
          -------------------------------- */
       parent = succ.parent;		// Prepare to delete

       parent.left = succ.right;	// Link right tree to parent's left

       return;

   }

   /* =======================================================
      Splay operation
      ======================================================= */
   public void splay( BSTEntry x )
   {
      while ( x != root )
      {
         if ( x.parent == root )
         {
            zig1(x);
            printBST();
         }
         else
         {
            zig2( x );
            printBST();
         }
      }
   }

   /* =======================================================
      zig1() handles   Zig and zag
      ======================================================= */
   public void zig1( BSTEntry x )
   {
     BSTEntry y = x.parent;

     /* *******************************************************************
        Determine the parent child relationships between (x,y))
        ******************************************************************* */
     boolean xIsLeftChild = (x == y.left);

     BSTEntry yParent;
     BSTEntry T1, T2, T3, T4, W;

     yParent = y.parent;              // Save the parent of y (link later)

     /* =======================================================
        Determine the configuration:
        ======================================================= */
     if (xIsLeftChild)
     { /* Configuration zig 2 */
       System.out.println("Use zig(2)");

                        //       y                      x
                        //      / \                    / \
       W  = x.left;     //     x   T4                 W   y
       T3 = x.right;    //    / \                        / \
       T4 = y.right;    //   W   T3                     T3  T4

       x.left = W;    if ( W != null ) W.parent = x;
       x.right = y;   y.parent = x;
       y.left = T3;   if ( T3 != null ) T3.parent = y;
       y.right = T4;  if ( T4 != null ) T4.parent = y;
     }
     else 
     { /* Configuration zig(1) */
       System.out.println("Use zig(1)");

                                  //          y                x  
       T1 = y.left;               //        /  \              / \
       T2 = x.left;               //       T1   x            y   W
                                  //          /  \          / \
       W  = x.right;              //         T2   W        T1  T2

       x.left = y;      y.parent = x;
       x.right = W;     if ( W != null ) W.parent = x;
       y.left = T1;     if ( T1 != null ) T1.parent = y;
       y.right = T2;    if ( T2 != null ) T2.parent = y;
     }

     root = x;            // We only use zig1() when y is root !!!
     x.parent = null;
   }


   /* =======================================================
      zig2() handles   Zig-zig  and Zig-zag
      ======================================================= */
   public void zig2( BSTEntry x )
   {
     BSTEntry y, z, zParent;
     BSTEntry T1, T2, T3, T4;

     y = x.parent;
     z = y.parent;
     zParent = z.parent;              // Save the parent of z (link later)

     /* *******************************************************************
        Determine the parent child relationships between (y,z) and (x,y))

                           z  -->  y -->  x
        ******************************************************************* */
     boolean yIsLeftChild = (y == z.left);
     boolean xIsLeftChild = (x == y.left);




     /* =======================================================
	Determine the configuration:
	======================================================= */
     if (xIsLeftChild && yIsLeftChild) 
     { /* Configuration zig-zig 2 */
       System.out.println("Use zig-zig(2)");

                        //          z                 x
                        //         /  \              / \
                        //       y    T4            T1  y
       T1 = x.left;     //      / \                    / \
       T2 = x.right;    //     x   T3                 T2  z
       T3 = y.right;    //    / \                        / \
       T4 = z.right;    //   T1  T2                     T3  T4

       x.left = T1;  if ( T1 != null ) T1.parent = x;
       x.right = y;  y.parent = x;
       y.left = T2;  if ( T2 != null ) T2.parent = y;
       y.right = z;  z.parent = y;
       z.left = T3;  if ( T3 != null ) T3.parent = z;
       z.right = T4; if ( T4 != null ) T4.parent = z;
     }
     else if (!xIsLeftChild && yIsLeftChild) 
     { /* Configuration zig-zag(2) */
       System.out.println("Use zig-zag(2)");

                         //       z                   x
                         //      / \                /   \
                         //    y    T4             y      z
       T1 = y.left;      //   / \                 / \    / \
       T2 = x.left;      //  T1  x               T1 T2  T3  T4
       T3 = x.right;     //     / \ 
       T4 = z.right;     //    T2  T3

       x.left = y;     y.parent = x;
       x.right = z;    z.parent = x;
       y.left = T1;    if ( T1 != null ) T1.parent = y;
       y.right = T2;   if ( T2 != null ) T2.parent = y;
       z.left = T3;    if ( T3 != null ) T3.parent = z;
       z.right = T4;   if ( T4 != null ) T4.parent = z;
     }
     else if (xIsLeftChild && !yIsLeftChild) 
     { /* Configuration zig-zag(1) */
       System.out.println("Use zig-zag(1)");

                                  //      z                  x
                                  //     /  \              /   \
                                  //    T1   y            z      y
       T1 = z.left;               //       /  \          / \    / \
       T2 = x.left;               //      x    T4      T1  T2  T3  T4
       T3 = x.right;              //     /  \  
       T4 = y.right;              //    T2  T3 

       x.left = z;      z.parent = x;
       x.right = y;     y.parent = x;
       y.left = T3;     if ( T3 != null ) T3.parent = y;
       y.right = T4;    if ( T4 != null ) T4.parent = y;
       z.left = T1;     if ( T1 != null ) T1.parent = z;
       z.right = T2;    if ( T2 != null ) T2.parent = z;
     }
     else 
     { /* Configuration zig-zig(1) */
       System.out.println("Use zig-zig(1)");

                                  //       z                     x
                                  //      /  \                  /  \
                                  //     T1   y                y    T4
       T1 = z.left;               //        /  \              / \
       T2 = y.left;               //       T2   x            z  T3
       T3 = x.left;               //          /  \          / \
       T4 = x.right;              //         T3  T4        T1  T2

       x.left = y;      y.parent = x;
       x.right = T4;    if ( T4 != null ) T4.parent = x;
       y.left = z;      z.parent = y;
       y.right = T3;    if ( T3 != null ) T3.parent = y;
       z.left = T1;     if ( T1 != null ) T1.parent = z;
       z.right = T2;    if ( T2 != null ) T2.parent = z;
     }
      
 
     /* ========================================================
        We always have:     z --> y --> x
        ======================================================== */
     if ( root == z )
     {  /* If z is the root node, handle the replacement  differently.... */

        root = x;                   // b is now root
        x.parent = null;
     }
     else 
     {
        if ( zParent.left == z ) 
        { /* Link x to the left branch of z's parent */
          x.parent = zParent;
          zParent.left = x;
        }
        else 
        { /* Link x to the right branch of z's parent */
          x.parent = zParent;
          zParent.right = x;
        }
     }
   }

   /* =======================================================
      Show what the BST look like....
      ======================================================= */

   public void printnode(BSTEntry x, int h)
   {
      for (int i = 0; i < h; i++)
         System.out.print("       ");

      System.out.println("[" + x.key + "," + x.value + "]");
   }

   void printBST()
   {
      showR( root, 0 );
      System.out.println("================================");
   }

   public void showR(BSTEntry t, int h)
   {
      if (t == null)
         return;

      showR(t.right, h+1);
      printnode(t, h);
      showR(t.left, h+1);
   }
   public int getHeight(BSTEntry root) {
	  if (root == null) {		 
		 return 0;
	  }
//	  else if (root.left == null && root.right != null) {
//		 return getHeightAux(root.right, 1);
//	  }
//	  else if (root.left != null && root.right == null) {
//		 return getHeightAux(root.left, 1);
//	  }
	  else {
//		 return (getHeightAux(root.left, 1) + getHeightAux(root.right, 1));
		 int lHeight = getHeight(root.left);
		 int rHeight = getHeight(root.right);
		 
		 if (lHeight > rHeight) {
			return (lHeight + 1);
		 }
		 else {
			return (rHeight + 1);
		 }
	  }
   }
   
//   public int getHeightAux(BSTEntry node, int level) {
//	  if (node.left == null && node.right == null) {
//		 return level;
//	  }
//	  else {
//		 if (root.left == null && root.right != null) {
//			return getHeightAux(root.right, level + 1);
//		 }
//		 else if (root.left != null && root.right == null) {
//			return getHeightAux(root.left, level + 1);
//		 }
//		 else {
//			int left = getHeightAux(root.left, level + 1);
//			int right = getHeightAux(root.right, level + 1);
//			return (right > left ? right : left);
//		 }
//	  }
   
}
