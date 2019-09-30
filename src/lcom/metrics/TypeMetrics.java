package lcom.metrics;

import lcom.metrics.algorithms.*;
import lcom.sourceModel.SM_Type;

public class TypeMetrics {
    private double yalcom;
    private SM_Type type;
    private double lcom1, lcom2, lcom3, lcom4, lcom5;

    public TypeMetrics(SM_Type type) {
        this.type = type;
    }

    public void extractMetrics() {
        ILCOM lcomAlgorithm = new YALCOM();
        yalcom = lcomAlgorithm.compute(type);
        lcomAlgorithm = new LCOM1();
        lcom1 = lcomAlgorithm.compute(type);
        lcomAlgorithm = new LCOM2();
        lcom2 = lcomAlgorithm.compute(type);
        lcomAlgorithm = new LCOM3();
        lcom3 = lcomAlgorithm.compute(type);
        lcomAlgorithm = new LCOM4();
        lcom4 = lcomAlgorithm.compute(type);
        lcomAlgorithm = new LCOM5();
        lcom5 = lcomAlgorithm.compute(type);
    }

    public double getYalcom() {
        return yalcom;
    }
    public double getLcom1() {
        return lcom1;
    }
    public double getLcom2() {
        return lcom2;
    }
    public double getLcom3() {
        return lcom3;
    }
    public double getLcom4() {
        return lcom4;
    }
    public double getLcom5() {
        return lcom5;
    }
}
