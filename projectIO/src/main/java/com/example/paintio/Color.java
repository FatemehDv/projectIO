package com.example.paintio;

public class Color {
    String ownColor, moveColor, backgroundColor;

    public Color(String ownColor, String moveColor, String backgroundColor) {
        this.ownColor = ownColor;
        this.moveColor = moveColor;
        this.backgroundColor = backgroundColor;
    }


    static String[] ownColorList = {"#024d02", "#690303", "#071852", "#756504"} ;
    static String[] moveColorList = {"#6dc46d", "#b66363", "#6376b6", "#b3a65d"};
    static String[] backgroundColorList = {"#1b811b", "#be1717", "#213d9c", "#9c8a21"};
}
