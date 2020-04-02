package com.swctools.common.enums;

public enum ApplicationMessageTemplates {
    SERVER_ERROR("Server returned error: %1$s (number: %2$s)"),
    APP_ERROR("Error: %1$s "),
    VISIT_KEY("%1$s|%2$s"),
    DEFENCE_LOG_SUMMARRY("Wins: %1$s | Losses: %2$s"),
    TROOP_FORMAT("%1$s %2$s %3$s"),

    PROGRESS_BAR_LABEL("%1$s / %2$s"),

    SEMI_COLON_ITEM("%1$s : %2$s"),

    UUID("%1$s-%2$s-%3$s-%4$s-%5$s")

    ;



    ;
    //defenceSummary

    private final String templateString;
    ApplicationMessageTemplates(String templateString){
        this.templateString = templateString;
    }
    public String getemplateString() {
        return this.templateString;
    }

}
