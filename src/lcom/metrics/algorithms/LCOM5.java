package lcom.metrics.algorithms;

import lcom.sourceModel.SM_Method;
import lcom.sourceModel.SM_Type;

public class LCOM5 implements ILCOM {
    @Override
    public double compute(SM_Type type) {
        int m = type.getMethodList().size();
        int a = type.getFieldList().size();
        int sumMu = 0;
        for(SM_Method method : type.getMethodList()){
            sumMu += method.getDirectFieldAccesses().size();
        }
        if(a==0)
            return 0; //avoid divide by zero
        if(m==1)
            return 0; //avoid divide by zero
        double lcom = (((double)sumMu/(double)a) - m)/(double)(1-m);
        return lcom;
    }
}
