import java.util.Scanner;
import java.util.Random;

public class TestSplay
{

   public static Splay_BST height;

   public static BSTEntry root;	
   public static void main(String[] args)
   {
      Scanner in = new Scanner(System.in);

      Splay_BST x = new Splay_BST();  
                  // Key = String, Value = String

	for (int i = 0; i < 100; i++) {
		Random rando = new Random();
		int z = rando.nextInt(900); // generates number between 0 and 899
		z += 100; // generates number between 100 and 999
		String str1 = Integer.toString(z);

		Random rand = new Random();
		int n = rand.nextInt(90); // generates number between 0 and 89
		n += 10; // generates number between 10 and 99
		
		x.put(str1, n);
		//height = x.getHeight(root);
	}
	  x.printBST();
	  System.out.println("Height:" + x.getHeight(x.root));

      String k;
      Integer v;
      while ( true )
      {
         System.out.print("\n\nEnter a key to search on ==> ");
         k = in.next();
         v = x.get( k );
         System.out.println("Key = " + k + " ==> get() returns: " + v);

         System.out.println("\nSplay tree is now:");
         x.printBST();
	 System.out.println("Height:" + x.getHeight(x.root));
      }
   }
}
