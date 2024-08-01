package com.mramirez;

import java.util.Queue;

public record TestConfiguration (
    Queue<String> inputs,
    Queue<String> answers,
    boolean encryptMode,
    Algorithms algorithm
){}
