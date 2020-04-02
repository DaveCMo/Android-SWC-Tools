package com.swctools.activity_modules.about.models;

import java.util.ArrayList;

public class FAQData {
    public String q;
    public ArrayList<String> answers;

    public FAQData(String q, String a) {
        answers = new ArrayList<>();
        this.q = q;
        answers.add(a);
    }
}
