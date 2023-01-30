package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.DataDiskPartInfo;
import com.example.globalcachesdk.entity.MemInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;

/**
 * 请求节点数据盘分区信息
 * @author 章睿彬
 */
@Configure(path="/configure/QueryDataDiskPartInfo.xml")
public class QueryDataDiskPartInfo extends AbstractCommandExecutor {


    public QueryDataDiskPartInfo() {
        super(QueryDataDiskPartInfo.class);
    }

    /*public long SubstringStr(String str){
        int length=str.length();
        String numberStr=str.substring(0,length-1);
        String charStr=str.substring(str.length()-1);
        long by=0;
        if(charStr.equals('K')){
            by=Long.parseLong(numberStr)*1024;
        }
        else if(charStr.equals('M')){
            by=Long.parseLong(numberStr)*1024*1024;
        }
        else if(charStr.equals('G')){
            by=Long.parseLong(numberStr)*1024*1024*1024;
        }
        else if(charStr.equals('T')){
            by=Long.parseLong(numberStr)*1024*1024*1024*1024;
        }
        return by;

    }*/

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/datadiskpart.sh";

        String returnValue = execInternal(sshSession, command);

        String[] returnValueList = returnValue.split("\n");

        ArrayList<DataDiskPartInfo.Part> partList=new ArrayList<>();

        DataDiskPartInfo dataDiskPartInfo = new DataDiskPartInfo();

        DataDiskPartInfo.Part part=dataDiskPartInfo.new Part();

        for(int i=0;i<returnValueList.length;i++){
            String [] str=returnValueList[i].split("\\s+");
            part.setDiskName(str[0]);
            part.setCapacity(Long.parseLong(str[1]));
            part.setUsed(Long.parseLong(str[2]));
            part.setAvail(Long.parseLong(str[3]));
        }



        //String[] returnValueList={"I am   a    loser","You are   a    loser"};
        //String sentence = "I am   a    loser";
       // String[] words = returnValueList[0].split("\\s+");
        //System.out.println(Arrays.toString(words)); // [I, am, a, loser]


        //DataDiskPartInfo.Part part=dataDiskPartInfo.new Part();
        //part.setDiskName(returnValueList[0]);
        //part.setCapacity(Long.parseLong(returnValueList[1]));
        //part.setUsed(Long.parseLong(returnValueList[2]));
        //part.setAvail(Long.parseLong(returnValueList[3]));

        return dataDiskPartInfo;
        //return null;
    }
}
