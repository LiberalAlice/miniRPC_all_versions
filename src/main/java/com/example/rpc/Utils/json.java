package com.example.rpc.Utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class  json {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\Liberal Wu\\Desktop\\1.txt");
        readerMethod(file);
    }
    private static void readerMethod(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        fileReader.close();
        reader.close();
        String s = sb.toString();
        StringBuffer buffer = new StringBuffer();

//        String prefix = "请求的类和方法</cls_highlight>";
//        String suffix = "\"]}],\"Logs";

        String prefix = "请求的类和方法:com.yijiupi";
        String suffix = "\\\",\\\"type\\\":\\";
//        String suffix = "</cls_highlight>\"";

        HashSet<String> set = new HashSet<>();


        while (s.length() > 1 && s.contains(prefix)){

            int st = s.indexOf(prefix);

            int en = s.indexOf(suffix);


            String substring = s.substring(st, en);
            set.add(substring);

//            s = s.substring(en + 3, s.length() - 1);
            s = s.substring(en + 3, s.length() - 1);

        }
        System.out.println(set.size());
        set.stream().forEach(System.out::println);
    }
}
