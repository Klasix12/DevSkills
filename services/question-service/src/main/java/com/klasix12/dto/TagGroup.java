package com.klasix12.dto;

public enum TagGroup {
    PROGRAM_LANGUAGE("языки программирования"),
    BASICS_OF_PROGRAMMING("основы программирования"),
    DATABASE("базы данных"),
    ALGORITHM("алгоритмы"),
    FRAMEWORK("фреймворки"),
    LINUX("linux");

    public final String title;

    TagGroup(String title) {
        this.title = title;
    }
}
