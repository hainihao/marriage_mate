package com.tulun.people;

import lombok.*;
/**
 * @author:liguozheng
 * @Date:2018/10/24
 * @time:17:01
 * @description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Person {

    private int id;        //ID
    private int appearance ;  //样貌
    private int treasure ;    //品格
    private int character ;   //财富
    private int appearanceLook;   //样貌期望
    private int treasureLook;     // 品格期望
    private int characterLook;    //财富期望


}
