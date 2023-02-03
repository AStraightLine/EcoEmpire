package com.core.GameDisplayUI;

public class LayoutHome {
    private Border b;
    public LayoutHome(){
        b = new Border();
        b.create();
        b.resize(100, 100);

    }
    public void render(){
        b.render();
    }
}
