package ru.autosome;

public interface ResultFormatter {
    String format(double score, int pos_start, String strand);

    String formatNoMatch();

    boolean shouldOutputNoMatch();

    int getFlankLength();
}
