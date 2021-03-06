package com.tulun.people;

/**
 * @author:liguozheng
 * @Date:2018/12/24
 * @time:17:01
 * @description:
 */
@SuppressWarnings("all")

public class Person {

    private int id;        //ID
    private int appearance ;  //样貌
    private int treasure ;    //品格
    private int character ;   //财富
    private int appearanceLook;   //样貌期望
    private int treasureLook;     // 品格期望
    private int characterLook;    //财富期望

    public Person(int id, int appearance, int treasure, int character, int appearanceLook, int treasureLook, int characterLook) {
        this.id = id;
        this.appearance = appearance;
        this.treasure = treasure;
        this.character = character;
        this.appearanceLook = appearanceLook;
        this.treasureLook = treasureLook;
        this.characterLook = characterLook;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppearance() {
        return appearance;
    }

    public void setAppearance(int appearance) {
        this.appearance = appearance;
    }

    public int getTreasure() {
        return treasure;
    }

    public void setTreasure(int treasure) {
        this.treasure = treasure;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public int getAppearanceLook() {
        return appearanceLook;
    }

    public void setAppearanceLook(int appearanceLook) {
        this.appearanceLook = appearanceLook;
    }

    public int getTreasureLook() {
        return treasureLook;
    }

    public void setTreasureLook(int treasureLook) {
        this.treasureLook = treasureLook;
    }

    public int getCharacterLook() {
        return characterLook;
    }

    public void setCharacterLook(int characterLook) {
        this.characterLook = characterLook;
    }

}
