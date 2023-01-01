package com.example.globalcachesdk.entity;

import java.util.ArrayList;

/**
 * 通用命令执行返回值
 * @author 李金泽
 */
public class GeneralOperationResult extends AbstractEntity{
    /**
     * 使用List分行以String保存运行结果
     */
    private ArrayList<String> returnValue;

    /**
     * @return 命令执行返回值list
     */
    public ArrayList<String> getReturnValue() {
        return returnValue;
    }

    /**
     * 以String类型输出命令执行返回值
     * @return 命令执行返回值String
     */
    public String getReturnValueInString(){
        StringBuilder stringBuilder=new StringBuilder();
        for (String str:returnValue
             ) {
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    /**
     * @param returnValue 命令返回值
     */
    public void setReturnValue(ArrayList<String> returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * @param string 单行返回值
     */
    public void addReturnValue(String string){
        returnValue.add(string);
    }


    /**
     * TODO:调试时使用，上线后请删除
     */
    @Override
    public String toString() {
        return "GeneralOperationResult{" +
                "returnValue=" + returnValue +
                '}';
    }
}
