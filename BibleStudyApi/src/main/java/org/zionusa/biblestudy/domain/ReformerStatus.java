package org.zionusa.biblestudy.domain;

public enum ReformerStatus {
    Advancing("Advancing"),
    Fulfilled("Fulfilled"),
    MakingEffort("MakingEffort"),
    NoTaskCompleted("NoTaskCompleted");

    ReformerStatus(String status) {
    }

    public static ReformerStatus getByName(String name) {
        return ReformerStatus.valueOf(name);
    }
}
