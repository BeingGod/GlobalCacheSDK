package com.hw.globalcachesdk.executorImpl;

import com.hw.globalcachesdk.entity.AbstractEntity;
import com.hw.globalcachesdk.entity.NodeStatusInfo;
import com.hw.globalcachesdk.exception.ReturnValueParseException;
import com.hw.globalcachesdk.executor.AbstractCommandExecutorSync;
import com.hw.globalcachesdk.executor.Configure;
import com.hw.globalcachesdk.executor.Script;

import java.util.ArrayList;

/**
 * 查询节点状态
 * @author 章睿彬, 李金泽
 */
@Configure(path = "/configure/QueryClusterStatusInfo.xml")
@Script(path = "/home/GlobalCacheScripts/data/node_status/node_status.sh")
public class QueryNodeStatusInfo extends AbstractCommandExecutorSync {
    public QueryNodeStatusInfo() {
        super(QueryNodeStatusInfo.class);
    }

    @Override
    public AbstractEntity parseOf(String returnValue) throws ReturnValueParseException {
        //sh脚本执行返回值按行切分
        String[] returnValueList = returnValue.split("\n");
        for (int i=0;i< returnValueList.length;i++){
            returnValueList[i]=returnValueList[i].replaceAll("\t","").trim();
        }        //返回对象
        NodeStatusInfo nodeStatusInfo = new NodeStatusInfo();
        //添加CurrentNodeNum
        nodeStatusInfo.setCurrentNodeNum(Integer.parseInt(returnValueList[2].substring(19)));
        //添加ConfigNodeNum
        nodeStatusInfo.setConfigNodeNum(Integer.parseInt(returnValueList[3].substring(18)));
        //返回对象中的Node ArrayList
        ArrayList<NodeStatusInfo.Node> nodeArrayList=new ArrayList<>();
        for (int i = 4; i < returnValueList.length; i++) {

            //获取节点信息

            String[] nodeInfoList = returnValueList[i].split(",");
            NodeStatusInfo.Node node = new NodeStatusInfo.Node();
            node.setNodeId(Integer.parseInt(nodeInfoList[0].substring(8)));
            ArrayList<NodeStatusInfo.NodeState> nodeStateArrayList=new ArrayList<>();
            nodeStateArrayList.add(NodeStatusInfo.NodeState.valueOf(nodeInfoList[1].substring(8)));
            int j=2;
            //仍然是状态码，继续添加
            for (;j<nodeInfoList.length;j++){
                if(!"ip".equals(nodeInfoList[j].substring(1, 3))){
                    nodeStateArrayList.add(NodeStatusInfo.NodeState.valueOf(nodeInfoList[j].trim()));
                }else{
                    //退出状态码循环
                    break;
                }
            }
            //获取PublicIp
            node.setPublicIp(nodeInfoList[j+1].substring(12));
            //获取ClusterIp
            node.setClusterIp(nodeInfoList[j+2].substring(13));

            //获取磁盘信息

            //磁盘数
            int diskNum= Integer.parseInt(nodeInfoList[j+3].substring(10));
            //设置节点磁盘数
            node.setDiskNum(diskNum);
            //当前节点磁盘列表
            ArrayList<NodeStatusInfo.Disk> diskArrayList=new ArrayList<>();
            //接下来的diskNum行读取下一个节点信息
            for (j=0;j<diskNum;j++){
                //行号+1
                i++;
                //按行读取磁盘
                NodeStatusInfo.Disk disk=new NodeStatusInfo.Disk();
                String[] diskInfoList = returnValueList[i].split(",|:");
                //diskInfoList第一位（0开始计算）为Id
                disk.setDiskId(Integer.parseInt(diskInfoList[1].trim()));
                //第三位为Name
                disk.setDiskName(diskInfoList[3].trim());
                //第五位为SN号
                disk.setDiskSn(diskInfoList[5].trim());
                //第七位为容量，单位MB
                disk.setCapacity(Integer.parseInt(diskInfoList[7].substring(1,diskInfoList[7].length()-4)));
                if("VDISK_STATE_UP".equals(diskInfoList[9].trim())){
                    disk.setState(NodeStatusInfo.DiskState.VDISK_STATE_UP);
                }else{
                    disk.setState(NodeStatusInfo.DiskState.VDISK_STATE_DOWN);
                }
                diskArrayList.add(disk);
            }
            node.setDisks(diskArrayList);
            node.setStateList(nodeStateArrayList);
            nodeArrayList.add(node);
        }
        nodeStatusInfo.setNodeList(nodeArrayList);

        return nodeStatusInfo;
    }
}
