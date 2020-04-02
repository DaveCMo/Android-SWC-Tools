package com.swctools.common.enums;

public enum Statuses {
    DROIDEKA_READY("READY"),
    DROIDEKA_REPAIRING("REPAIRING"),
    DROIDEKA_DOWN("DOWN"),
    DROIDEKA_UPGRADING("UPGRADING"),
    UNKNOWN("UNKNOWN")

    ;

    private String status;
    private Statuses(String s){
        this.status = s;
    }

    public String toString(){
        return status;
    }
}
