package lcom.testsubject;

public class Case6 {
    int f1, f2, f3;

    public void m1() {
        f1 = 0;
        f2 = 0;
        f3 = 0;
    }

    void m2() {
        f2 = 0;
        m3();
    }

    void m3() {
    }
}
