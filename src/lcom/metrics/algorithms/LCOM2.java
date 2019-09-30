package lcom.metrics.algorithms;

import lcom.sourceModel.SM_Field;
import lcom.sourceModel.SM_Method;
import lcom.sourceModel.SM_Type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LCOM2 implements ILCOM {
    @Override
    public double compute(SM_Type type) {
        int p = 0, q = 0;
        boolean bAtLeastOneFieldAccess = false;
        SM_Method[] methods = type.getMethodList().toArray(new SM_Method[type.getMethodList().size()]);
        for (int i = 0; i < methods.length; i++) {
            for (int j = i + 1; j < methods.length; j++) {
                List<SM_Field> fields1 = methods[i].getNonStaticFieldAccesses();
                List<SM_Field> fields2 = methods[j].getNonStaticFieldAccesses();
                Set<SM_Field> fieldSet1 = new HashSet<>(fields1);
                Set<SM_Field> fieldSet2 = new HashSet<>(fields2);
                if (fields1.size()>0 || fields2.size()>0)
                    bAtLeastOneFieldAccess = true;
                Set<SM_Field> intersectSet = fieldSet1.stream()
                        .filter(fieldSet2::contains)
                        .collect(Collectors.toSet());
                if (intersectSet.isEmpty())
                    p++;
                else
                    q++;
            }
        }
        if (!bAtLeastOneFieldAccess) //meaning all methods do not access any field
            p=0;
        if (p>q)
            return p-q;
        else
        return 0;
    }
}
