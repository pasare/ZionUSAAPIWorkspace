package org.zionusa.biblestudy.util;

import org.zionusa.biblestudy.domain.Reformer;

import java.time.LocalDate;
import java.util.Comparator;

public class Utilities {

    public static Comparator<Reformer> reformerComparatorDesc = (o1, o2) -> {
        LocalDate o1StartDate = LocalDate.parse(o1.getStartDate());
        LocalDate o2StartDate = LocalDate.parse(o2.getStartDate());
        return o1StartDate.compareTo(o2StartDate) + -1;
    };

}
