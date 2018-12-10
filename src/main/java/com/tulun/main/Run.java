package com.tulun.main;

import com.tulun.people.Person;
import java.io.*;
import java.util.*;

/**
 * @author:liguozheng
 * @Date:2018/10/24
 * @time:21:46
 * @description:
 */

@SuppressWarnings("all")
public class Run <K,V>{

    private ArrayList<Person> maleList;
    private ArrayList<Person> femaleList;
    private ArrayList<Person> playersList;
    private ArrayList<Person> man ;
    private ArrayList<Person> mon ;
    int id;

    public Run(String malePath,String femalePath,String playersPath) throws IOException {

        this.mon = new <Person>ArrayList();
        this.man = new <Person>ArrayList();
        this.maleList = new <Person>ArrayList();
        this.femaleList = new <Person>ArrayList();
        this.playersList = new<Person> ArrayList();

        this.mon.addAll(saveCount(femalePath));
        this.man.addAll(saveCount(malePath));
        this.playersList.addAll(saveCount(playersPath));
        this.id = 0;
    }

    public void start(){
        Person bestPeople;
        for (Person next : playersList){
            marryArithmetic(next);
        }
    }

    /**
     * 把一个主角加入，让所有男生选出自己中意的女生
     * @param// person Person
     * @throws Exception Exception
     */
    private void marryArithmetic(Person person) {

        Person smartBoy;
        Person bastGirl;
        Person p;
        Person p_;
        HashMap<Person, ArrayList<Person>> girls = new HashMap<>();

        int k = person.getId();

        if ( k ==0 ){           //主角加入
            person.setId(-1);
            this.mon.add(person);
        }else {
            person.setId(-1);
            this.man.add(person);
        }

        this.maleList =(ArrayList) man.clone();
        this.femaleList =(ArrayList) mon.clone();
        int size = this.man.size()>this.mon.size()? this.mon.size() : this.man.size();

        for (int i = 0; i < size; i++) {

            girls = chooseGirl(girls);        //选出女生列表

            bastGirl= getBastGirl(girls);      //选出最受欢迎的女生

            smartBoy = girlsChoose(bastGirl, girls.get(bastGirl));         //让女生选出中意的男生

            if (k==1){
                p  = smartBoy;
                p_ = bastGirl;
            }else {
                p  = bastGirl;
                p_ = smartBoy;
            }
            if ( p ==person ) {
                id++;
                if (k == 0){
                    mon.remove(p);
                    System.out.println("第"+id+"组player加入："+p_.getId()+" | "+" -1 ");
                }else {
                    man.remove(p);
                    System.out.println("第"+id+"组player加入："+"-1 "+" | "+p_.getId());
                }
                return ;
            } else {
                maleList.remove(smartBoy);
                femaleList.remove(bastGirl);
            }
            girls.clear();
        }
        id++;
        System.out.println("第"+id+"组player加入："+"     ");
    }

    /**
     * 女生在男生中选择一位男生
     * @param person person --获得男生选择最多的女生
     * @param arrayList ArrayList --男生列表
     * @return Person smartBoy --中意男生
     */
    private Person girlsChoose(Person person,ArrayList<Person> arrayList){

        Person smartBoy = null;
        int scout = 0;
        int maxScore = 0;

        for (Person next : arrayList){
            int score = next.getAppearance()+next.getCharacter()+next.getTreasure();
            if (scout<=countScore(person,next)){
                if (scout==countScore(person,next)){
                    if (score<maxScore){
                        continue;
                    }else if (score==maxScore){
                        if (next.getId()>smartBoy.getId()){
                            continue;
                        }
                    }
                }
                smartBoy = next;
                maxScore = score;
                scout = countScore(person,next);
            }
        }
        return smartBoy;
    }

    /**
     * 选出最受欢迎的女生
     * @param girls HashMap
     * @return Person
     */
    private Person getBastGirl(HashMap girls){

        int max = 0;                  //男生们的人数
        Person bastGirl = null;       //最受欢迎的女生
        int maxScore = 0;

        Iterator<Map.Entry<Person, ArrayList<Person>>> iterator1 = girls.entrySet().iterator();
        Map.Entry<Person,ArrayList<Person>> next;

        while (iterator1.hasNext()) {

            next = iterator1.next();
            int score = next.getKey().getAppearance()+next.getKey().getTreasure()+next.getKey().getCharacter();
            ArrayList arrayList = next.getValue();
            if (max <=arrayList.size()) {
                if (max == arrayList.size()){
                    if (score<maxScore){
                        continue;
                    }else if (score==maxScore){
                        if (next.getKey().getId()>bastGirl.getId()){
                            continue;
                        }
                    }
                }
                maxScore = score;
                bastGirl = next.getKey();
                max = arrayList.size();
            }
        }
        return bastGirl;
    }

    /**
     * 选出选择女生的列表
     * @param girls HashMap<Person,ArrayList<Person>>   女生列表
     * @return HashMap <Person,ArrayList<Person>>   女生列表
     */
    private HashMap<Person,ArrayList<Person>> chooseGirl(HashMap<Person,ArrayList<Person>> girls){

        ArrayList<Person> list;
        Person likePerson;

        for (Person next :  this.maleList){              //遍历男生列表 让男生选出中意女生
            likePerson =  mateSort(next);         //让男生选出中意女子
            if (girls.containsKey(likePerson)) {
                list = girls.get(likePerson);
                list.add(next);
            } else {
                list = new ArrayList<>();
                list.add(next);
            }
            girls.put(likePerson, list);
        }
        return girls;
    }


    /**
     * 让指定男生在女生里面选一个中意女生
     * @param person Person 给定男生
     * @return Person 中意女生
     */
    private Person mateSort(Person person){
        int max = 0;
        Person likePerson = null;
        int maxScore = 0;
        int score=0;

        for (Person next : femaleList){
            score = next.getAppearance()+next.getTreasure()+next.getCharacter();
            if (max<=countScore(person,next)){
                if ((max==(countScore(person,next)))){
                    if (score<maxScore){
                        continue;
                    }else if (score==maxScore){
                        if (likePerson.getId()<next.getId()){
                            continue;
                        }
                    }
                }
                maxScore = score;
                max = countScore(person,next);
                likePerson = next;
            }
        }
        return likePerson;
    }

    /**
     * 把传入的字符串解析成数组；
     * @param str String
     * @return int[]
     */
    private int[] getNumber(String str) {
        int i = 0;
        int j=0;
        int sum = 0;
        int[] a = new int[7];
        while (i < str.length()) {
            if (Character.isDigit(str.charAt(i))) {
                sum = sum * 10 + Integer.valueOf(str.charAt(i))-48;
            } else {
                switch (j % 7) {
                    case 0: {
                        a[0] = sum;
                        sum = 0;
                        break;
                    }
                    case 1: {
                        a[1] = sum;
                        sum = 0;
                        break;
                    }
                    case 2: {
                        a[2] = sum;
                        sum = 0;
                        break;
                    }
                    case 3: {
                        a[3] = sum;
                        sum = 0;
                        break;
                    }
                    case 4: {
                        a[4] = sum;
                        sum = 0;
                        break;
                    }
                    case 5: {
                        a[5] = sum;
                        sum = 0;
                        break;
                    }
                    case 6: {
                        a[6] = sum;
                        sum = 0;
                        break;
                    }
                }
                j++;
            }
            i++;
        }
        a[a.length-1]=sum;
        return a;
    }

    /**
     * 把给定路径的文本读到HashMap中并储存
     * @param path String 文本路径
     * @return HashMap<Person,Person> 已存储的数据
     * @throws Exception FileNotFoundException->IOException
     */
    private ArrayList<Person> saveCount(String path) throws IOException {

        ArrayList<Person> hashmap = new ArrayList();
        BufferedReader bf = new BufferedReader(new FileReader(new File(path)));
        String line;

        while ((line= bf.readLine() )!= null) {
            hashmap.add(new Person (getNumber(line)[0],
                    getNumber(line)[1], getNumber(line)[2], getNumber(line)[3],
                    getNumber(line)[4], getNumber(line)[5], getNumber(line)[6]));
        }
        return hashmap;
    }

    /**
     * count score <br/>
     * count person to next
     * @param person Person ---- Person 's Look
     * @param next  Person  -----need itselves count
     * @return int  --- next 's score
     */
    private int countScore(Person person,Person next){

        return person.getTreasureLook()*next.getTreasure()+
                person.getCharacterLook()*next.getCharacter()+
                person.getAppearanceLook()*next.getAppearance();
    }
}