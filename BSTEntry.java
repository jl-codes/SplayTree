public class BSTEntry
{
   public String key;
   public Integer value;

   public BSTEntry parent;
   public BSTEntry left;
   public BSTEntry right;

   public BSTEntry(String k, Integer v)
   {
      key = k;
      value = v;

      parent = null;
      left = null;
      right = null;
   }
}

