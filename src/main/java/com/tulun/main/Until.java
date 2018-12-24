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
public class Until{

    private ArrayList<Person> maleList;             //存储男生列表
    private ArrayList<Person> femaleList;           //存储女生列表
    private ArrayList<Person> playersList;          //存储主角列表
    private HashMap<Person,ArrayList<Person>> firstBoyChess ;       //存储原始女生配对列表
    private int id ;                                //存储已匹配主角数

    /**
     * 构造方法： 初始化四个列表及个数并给各个列表原始数据
     * @param malePath  String
     * @param femalePath  String
     * @param playersPath  String
     * @throws IOException throws IOException
     */
    public Until(String malePath,String femalePath,String playersPath) throws IOException {

        this.maleList = new <Person>ArrayList();
        this.femaleList = new <Person>ArrayList();
        this.playersList = new <Person>ArrayList();
        this.firstBoyChess = new <Person, ArrayList<Person>>HashMap();

        this.femaleList.addAll(saveCount(femalePath));
        this.maleList.addAll(saveCount(malePath));
        this.playersList.addAll(saveCount(playersPath));
        this.firstBoyChess = chooseGirl();         //预先算出男生选择女生后生成的女生选择列表
        this.id = 0;

    }


    /**
     * Start()!!!
     */
    public void start(){

        for (Person p : playersList) {

            marryArithmetic(p,cloneDome(firstBoyChess), (ArrayList<Person>) this.femaleList.clone());  //把一个主角放入

        }

    }

    /**
     * 对HashMap的深拷贝
     * @param pList HashMap
     * @return HashMap
     */
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
     * 把一个主角放入进行匹配到完成
     * @param person 主角（Person）
     * @param girls  预匹配好的女生匹配列表的一份拷贝数据  HashMap
     * @param girlList  女生列表
     */
    private void marryArithmetic(Person person,HashMap<Person, ArrayList<Person>> girls,ArrayList<Person> girlList) {

        int k = person.getId();        //记录主角是男是女
        person.setId(-1);              //把主角ID设置为-1

        if (k==1) {                   //如果主角为男生

            Person person1 = mateSort(person,girlList);         //先选出主角匹配的女生

            if (girls.containsKey(person1)) {                   //如果所选女生在girls列表中存在
                ArrayList a = girls.get(person1);               //获取选择该女生的男生列表
                a.add(person);                                  //将该男生添加进获取的列表当中
                girls.put(person1, a);                          //放回
            } else {                                                    //不存在
                ArrayList<Person> objects = new ArrayList<>();          //创建列表
                objects.add(person);                                    //向该列表中添加
                girls.put(person1, objects);                            //放入
            }

        }else {                                                                    //如果主角是女生

            girlList.add(person);                                                  //先往女生列表中添加
            girls = restartCount(girls, person);                                   //对girls列表重新计算

        }

        ArrayList<Person> boyList;                             //剔除匹配后所剩男生列表



        for (int i = 0; i < this.maleList.size(); i++) {

            Person bastGirl = getBastGirl(girls);                                     //获取最受欢迎的女生

            Person smartBoy = girlsChoose(bastGirl, girls.get(bastGirl));             //让女生选择出一个男生

            if (k==1&&smartBoy==person){                                              //如果主角是男生，且女生选择的男生是主角
                id++;
                System.out.println("第"+id+"组加入： "+"-1 | "+bastGirl.getId());
                return;
            }else if (k==0&&bastGirl==person){                                         //如果主角是女生且最受欢迎的女生是主角
                id++;
                System.out.println("第"+id+"组加入： "+smartBoy.getId()+" | "+"-1 ");
                return;
            }

            boyList = girls.get(bastGirl);                         //得到选择最受欢迎女生的男生列表

            boyList.remove(smartBoy);                              //把女生选择的男生从得到的列表中删除

            girls.remove(bastGirl);                                //在girls列表中删除最受欢迎的女生

            girlList.remove(bastGirl);                             //把最受欢迎的女生从女生列表中删除

            girls = restartGirls(girls, girlList, boyList);        //对原先选择最受欢迎女生所剩下的男生们重新计算

        }
                                                                                       //走到该布说明其他人都匹配完成了，就只剩下主角了。
        id++;
        System.out.println("第"+id+"组加入： "+"单身狗");
    }

    /**
     * 对boyList列表里的男生重新计算
     * @param girls   HashMap
     * @param girlsList Arraylist
     * @param boyList ArrayList
     * @return  HashMap
     */
    public HashMap<Person,ArrayList<Person>> restartGirls(HashMap<Person,ArrayList<Person>> girls,
                                                                      ArrayList<Person> girlsList,
                                                                         ArrayList<Person> boyList){

        for (Person person : boyList) {                                 //遍历男生里表中的每一个男生

            Person person1 = mateSort(person, girlsList);               //选出当前男生所匹配的女生

            if (!girls.containsKey(person1)) {                          //如果该女生在girls列表中不存在

                ArrayList<Person> list = new ArrayList<>();             //创建列表

                list.add(person);                                       //往列表里添加

                girls.put(person1, list);                               //放回

            } else {                                                    //存在

                ArrayList<Person> people = girls.get(person1);          //获得列表

                people.add(person);                                     //往列表里添加

                girls.put(person1, people);                             //放回

            }

        }

        return girls;
    }

    /**
     * 由于主角是女生的缘故，所以要对主角和girls列表中的男生进行重计算
     * @param girls  HashMap
     * @param protagonist 主角
     * @return HashMap
     */
    public HashMap<Person,ArrayList<Person>> restartCount(HashMap<Person,ArrayList<Person>> girls,Person protagonist){

        ArrayList<Person> a = new ArrayList<>();                                  //用于存储重新计算后选择主角的男生

        Iterator<Map.Entry<Person, ArrayList<Person>>> iterator = girls.entrySet().iterator();

        while (iterator.hasNext()){                                               //遍历girls列表

            Map.Entry<Person, ArrayList<Person>> next = iterator.next();
            ArrayList<Person> list = next.getValue();
            Person p = next.getKey();
            Iterator<Person> iterator1 = list.iterator();

            while (iterator1.hasNext()){                                          //遍历每个女生下的男生列表

                Person next1 = iterator1.next();

                if(countScore(next1,p)<countScore(next1,protagonist)){            //比较每个男生当前对主角和next的满意度

                    a.add(next1);                                                 //往a列表中添加该男生
                    iterator1.remove();                                           //把该男生从当前女生的列表下闪出

                }
            }

            girls.put(p,list);                                                    //放回

        }

        if (a.size()!=0){                                                         //有男生选择主角
            girls.put(protagonist,a);                                             //往girls列表中添加主角及该列表
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

        for (Person next :  this.maleList){                        //遍历男生列表 让男生选出中意女生
            likePerson =  mateSort(next,this.femaleList);          //让男生选出中意女子
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