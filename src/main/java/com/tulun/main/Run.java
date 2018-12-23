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

public class Run{

    private ArrayList<Person> maleList;
    private ArrayList<Person> femaleList;
    private ArrayList<Person> playersList;
    private HashMap<Person,ArrayList<Person>> firstBoyChess ;
    private int id ;

    public Run(String malePath,String femalePath,String playersPath) throws IOException {


        this.maleList = new <Person>ArrayList();
        this.femaleList = new <Person>ArrayList();
        this.playersList = new <Person>ArrayList();
        this.firstBoyChess = new <Person, ArrayList<Person>>HashMap();

        this.femaleList.addAll(saveCount(femalePath));
        this.maleList.addAll(saveCount(malePath));
        this.playersList.addAll(saveCount(playersPath));
        this.firstBoyChess = chooseGirl();
        this.id = 0;

    }


    public void start(){


        for (Person p : playersList) {

            marryArithmetic(p,cloneDome(firstBoyChess), (ArrayList<Person>) this.femaleList.clone());

        }

    }

    private HashMap<Person,ArrayList<Person>> cloneDome(HashMap<Person, ArrayList<Person>> pList){

        HashMap<Person,ArrayList<Person>> list = new HashMap<>();

        Iterator<Map.Entry<Person, ArrayList<Person>>> iterator = pList.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Person, ArrayList<Person>> next = iterator.next();
            ArrayList<Person> value = next.getValue();
            Person key = next.getKey();
            ArrayList<Person> arrayList = new ArrayList<>();
            Person person = new Person(key.getId(),key.getAppearance(),key.getTreasure(),key.getCharacter(),
                    key.getAppearanceLook(),key.getTreasureLook(),key.getCharacterLook());
            Iterator<Person> iterator1 = value.iterator();
            while (iterator1.hasNext()) {
                Person next1 = iterator1.next();
                arrayList.add(new Person(next1.getId(),next1.getAppearance(),next1.getTreasure(),next1.getCharacter(),next1.getAppearanceLook()
                ,next1.getTreasureLook(),next1.getCharacterLook()));
            }

            list.put(person,arrayList);
        }
        return list;
    }

    /**
     * 把一个主角加入，让所有男生选出自己中意的女生
     * @param// person Person
     * @throws Exception Exception
     */
    private void marryArithmetic(Person person,HashMap<Person, ArrayList<Person>> girls,ArrayList<Person> girlList) {

        int k = person.getId();
        person.setId(-1);

        if (k==1) {

            Person person1 = mateSort(person,girlList);

            if (girls.containsKey(person1)) {
                ArrayList a = girls.get(person1);
                a.add(person);
                girls.put(person1, a);
            } else {
                ArrayList<Person> objects = new ArrayList<>();
                objects.add(person);
                girls.put(person1, objects);
            }

        }else {

            girlList.add(person);
            girls = restartCount(girls, person);

        }

        ArrayList<Person> boyList;



        for (int i = 0; i < this.maleList.size(); i++) {

            Person bastGirl = getBastGirl(girls);   //

            Person smartBoy = girlsChoose(bastGirl, girls.get(bastGirl));

            if (k==1&&smartBoy==person){
                id++;
                System.out.println("第"+id+"组加入： "+"-1 | "+bastGirl.getId());
                return;
            }else if (k==0&&bastGirl==person){
                id++;
                System.out.println("第"+id+"组加入： "+smartBoy.getId()+" | "+"-1 ");
                return;
            }

            boyList = girls.get(bastGirl);

            boyList.remove(smartBoy);

            girls.remove(bastGirl);

            girlList.remove(bastGirl);


            girls = restartGirls(girls, girlList, boyList);

        }
        id++;
        System.out.println("第"+id+"组加入： "+"单身狗");
    }

    public HashMap<Person,ArrayList<Person>> restartGirls(HashMap<Person,ArrayList<Person>> girls,ArrayList<Person> girlsList,ArrayList<Person> boyList){

        for (Person person : boyList) {

            Person person1 = mateSort(person, girlsList);

            if (!girls.containsKey(person1)) {

                ArrayList<Person> list = new ArrayList<>();

                list.add(person);

                girls.put(person1, list);

            } else {
                ArrayList<Person> people = girls.get(person1);

                people.add(person);
                girls.put(person1, people);
            }

        }

        return girls;
    }



    public HashMap<Person,ArrayList<Person>> restartCount(HashMap<Person,ArrayList<Person>> girls,Person protagonist){

        ArrayList<Person> a = new ArrayList<>();

        Iterator<Map.Entry<Person, ArrayList<Person>>> iterator = girls.entrySet().iterator();

        while (iterator.hasNext()){

            Map.Entry<Person, ArrayList<Person>> next = iterator.next();
            ArrayList<Person> list = next.getValue();
            Person p = next.getKey();
            Iterator<Person> iterator1 = list.iterator();

            while (iterator1.hasNext()){

                Person next1 = iterator1.next();

                if(countScore(next1,p)<countScore(next1,protagonist)){

                    a.add(next1);
                    iterator1.remove();

                }
            }

            girls.put(p,list);

        }

        if (a.size()!=0){
            girls.put(protagonist,a);
        }

        return girls;
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
     * @return HashMap <Person,ArrayList<Person>>   女生列表
     */
    private HashMap<Person,ArrayList<Person>> chooseGirl(){

        HashMap<Person,ArrayList<Person>> girls = new HashMap<>();
        ArrayList<Person> list;
        Person likePerson;

        for (Person next :  this.maleList){              //遍历男生列表 让男生选出中意女生
            likePerson =  mateSort(next,this.femaleList);         //让男生选出中意女子
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
    private Person mateSort(Person person,ArrayList<Person> girlsList){
        int max = 0;
        Person likePerson = null;
        int maxScore = 0;
        int score=0;

        for (Person next : girlsList){
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
     * 把给定路径的文本读到HashMap中并储存
     * @param path String 文本路径
     * @return HashMap<Person,Person> 已存储的数据
     * @throws Exception FileNotFoundException->IOException
     */
    public ArrayList<Person> saveCount(String path) throws IOException {

        ArrayList<Person> arrayList = new ArrayList();

        //获取文件
        BufferedReader bf = new BufferedReader(new FileReader(new File(path)));
        String line;

        while ((line= bf.readLine() )!= null) {

            String[] split = line.split(",");
            arrayList.add(new Person(Integer.valueOf(split[0]),Integer.valueOf(split[1]),Integer.valueOf(split[2]),Integer.valueOf(split[3]),
                    Integer.valueOf(split[4]),Integer.valueOf(split[5]),Integer.valueOf(split[6])));
        }
        return arrayList;
    }

    /**
     * count score <br/>
     * count person to next
     * @param person - Person ---- Person 's Look
     * @param next   -Person     ----need itselves count
     * @return int  --- next 's score
     */
    private int countScore(Person person,Person next){


        return person.getTreasureLook()*next.getTreasure()+
                person.getCharacterLook()*next.getCharacter()+
                person.getAppearanceLook()*next.getAppearance();
    }
}