class Base {
  void Test(final boolean a) {
    //
  }

  int b = 3;
}

class Derived extends Base {
  void Test(boolean b) {
    //
  }

  int b = 5;
}

public class TestFile {
  public static void main(String[] args) {
    /*int[] lol;
    lol = new int[4];
    System.out.println(lol[2]);*/

    Derived d = new Derived();
    Base b = (Base) d;

    System.out.println(d.b);
    System.out.println(b.b);
  }
}