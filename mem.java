import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class mem {
    static class PCB {
        int id;
        int startAddress;
        int length;
        public PCB(int id, int start, int length) {
            this.id = id;
            this.startAddress = start;
            this.length = length;
        }
    }
    static class emptySpace {
        int startAddress;
        int length;
        public emptySpace(int startAddress, int length) {
            this.startAddress = startAddress;
            this.length = length;
        }
    }

    public static void output(LinkedList<PCB> processList,LinkedList<emptySpace> emptySpaceList ){
        check();
        sortList();
        System.out.println("============================== \n" +
                            "进程id 起始地址 长度");

        if (processList.isEmpty()){
            System.out.println("无");
        }else {
            for (int i = 0 ; i<processList.size(); i++){
                System.out.println(processList.get(i).id + "       " +  processList.get(i).startAddress + "       " + processList.get(i).length );
            }
        }
        System.out.println("============================== \n" +
                             "空闲区序号 起始地址 长度");
        if (emptySpaceList.isEmpty()){
            System.out.println("无");
        }else {
            for (int i = 0 ; i<emptySpaceList.size(); i++){
                System.out.println(i+1 + "       " +emptySpaceList.get(i).startAddress + "       " + emptySpaceList.get(i).length );
            }
        }
        System.out.println("==============================");
        System.out.println("");System.out.println("");
    }
    public static void allow_mem(int id, int pcbLength){
        for (int i = 0;i<emptySpaceList.size();i++){
            if (emptySpaceList.get(i).length >= pcbLength){
                //添加进程
                PCB pcb = new PCB(id,emptySpaceList.get(i).startAddress,pcbLength);
                processList.add(pcb);
                //
                emptySpaceList.get(i).length -= pcbLength;
                emptySpaceList.get(i).startAddress += pcbLength;
                System.out.println("进程:"+ id +" 分配成功");
                return;
            }
        }
        System.out.println("进程:"+ id +" 分配失败");
    }
    public static void free_mem(int id){
        emptySpace freeSpace = null;
        for (int i = 0 ; i < processList.size(); i++){
            if (processList.get(i).id == id){
                //初始化空闲区域
                freeSpace = new emptySpace(processList.get(i).startAddress,processList.get(i).length);
                processList.remove(i);
            }
        }
        for (int i = 0 ; i < emptySpaceList.size();i++){
            //有上有下都合并
            if (i != emptySpaceList.size()-1){
                if (freeSpace.startAddress == emptySpaceList.get(i).startAddress + emptySpaceList.get(i).length &&
                        freeSpace.startAddress + freeSpace.length == emptySpaceList.get(i+1).startAddress){
                    emptySpaceList.get(i).length += freeSpace.length + emptySpaceList.get(i+1).length;
                    //合并俩个 删除一个
                    emptySpaceList.remove(i+1);
                    return;
                }
            }
            //合并上
            if (freeSpace.startAddress == emptySpaceList.get(i).startAddress + emptySpaceList.get(i).length){
                emptySpaceList.get(i).length += freeSpace.length;
                return;
            }
            //合并下
            if (freeSpace.startAddress + freeSpace.length == emptySpaceList.get(i).startAddress){
                emptySpaceList.get(i).startAddress = freeSpace.startAddress;
                emptySpaceList.get(i).length += freeSpace.length;
                return;
            }
        }
        emptySpaceList.add(freeSpace);
    }
    public static void change(int id,int address){

        for (int i = 0 ; i < processList.size(); i++){
            if (processList.get(i).id == id){
                int realAddress = processList.get(i).startAddress+address;
                if (realAddress > processList.get(i).startAddress+ processList.get(i).length){
                    System.out.println("地址越界");
                }else {
                    System.out.println("进程：" + id +"逻辑地址："+address +"为物理地址：" + realAddress);
                }

            }
        }
    }
    public static void sortList(){
        //排序
        emptySpaceList.sort(Comparator.comparingInt(o -> o.startAddress));
    }
    public static void check(){
        for (int i = 0 ; i < emptySpaceList.size();i++){
            if (emptySpaceList.get(i).length == 0) {
                emptySpaceList.remove(i);
            }
        }
    }
    static LinkedList<PCB> processList = new LinkedList<>();
    static LinkedList<emptySpace> emptySpaceList = new LinkedList<>();
    public static void main(String[] args) {
        //内存总大小
        emptySpace max = new emptySpace(0,1024);
        emptySpaceList.add(max);

        output(processList,emptySpaceList);
        while (true){
            Scanner sc = new Scanner(System.in);
            String str =  sc.next();
            if (str.equals("a")){
                int id =  sc.nextInt();
                int length = sc.nextInt();
                allow_mem(id,length);
                output(processList,emptySpaceList);
            }
            if (str.equals("f")){
                int id = sc.nextInt();
                free_mem(id);
                output(processList,emptySpaceList);
            }
            if (str.equals("y")){
                int id = sc.nextInt();
                int address = sc.nextInt();
                change(id,address);
                output(processList,emptySpaceList);
            }
        }
    }
}
