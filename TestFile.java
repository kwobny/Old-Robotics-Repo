abstract class Base {
  abstract void Test(Base lol);

  int b = 3;
}

class Derived extends Base {
  @Override
  void Test(Derived lola) {
    System.out.println(lola.b);
  }

  int b = 5;
}

public class TestFile {
  public static void main(String[] args) {
    /*int[] lol;
    lol = new int[4];
    System.out.println(lol[2]);*/
    //
  }

  public static void TestFunc(Derived intsas) {
    System.out.println(intsas);
  }
}