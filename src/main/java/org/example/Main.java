package org.example;


import org.example.annotation.MikhailScanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@MikhailScanner("org.example.bean")
public class Main {
    public static void main(String[] args) {
        MikhailBoot.run(Main.class, args);
    }
}