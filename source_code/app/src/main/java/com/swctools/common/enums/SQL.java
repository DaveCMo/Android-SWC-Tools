package com.swctools.common.enums;

public enum SQL {

    FAVOURITE_LAYOUT( "SELECT *, COUNT(%1$s) As count " +
            "FROM %2$s " +
            "INNER JOIN %3$s " +
            "ON %4$s = " +
            "%5$s " +
            "WHERE %6$s " +
            "= %7$s GROUP BY " +
            "%8$s, " +
            "%9$s " +
            "ORDER BY count DESC")


    ;

    private final String sql;

    SQL(String sql) {
        this.sql = sql;
    }

    public String toString(){
        return this.sql;
    }
}
