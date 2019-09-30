package lcom.testsubject;

public class Case2 {
    int f1, f2;
    static int f3; //f3 is static: this is the only difference between Case1 and Case2

    public void m1() {
        f1 = 0;
        f2 = 0;
        f3 = 0;
    }

    void m2() {
        f2 = 0;
    }

    void m3() {
        f3 = 0;
    }
}
