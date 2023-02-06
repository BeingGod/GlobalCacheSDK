package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.ClusterStatusInfo;
import com.example.globalcachesdk.entity.NodeStatusInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;

/**
 * 查询节点状态
 * @author 章睿彬
 */
@Configure(path="/configure/QueryClusterStatusInfo.xml")
public class QueryNodeStatusInfo extends AbstractCommandExecutor {
    public QueryNodeStatusInfo() {
        super(QueryNodeStatusInfo.class);
    }

    @Override
    public AbstractEntity exec(Session sshSession, String args) throws CommandExecException {
        String command = "bash /home/GlobalCacheScripts/SDK/node_status/node_status.sh";
        //sh脚本执行返回值
        String returnValue = execInternal(sshSession, command);
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
            nodeStateArrayList.add(checkNodeState(nodeInfoList[1].substring(8)));
            int j=2;
            //仍然是状态码，继续添加
            for (;j<nodeInfoList.length;j++){
                if(!"ip".equals(nodeInfoList[j].substring(1, 3))){
                    nodeStateArrayList.add(checkNodeState(nodeInfoList[j].trim()));
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

    /**
     * 将脚本获取的字符串转码为Node标准状态码，无异常处理能力
     * @param state 从脚本获取的状态码字符串
     * @return 状态码
     */
    private NodeStatusInfo.NodeState checkNodeState(String state) {
        if(state!=null){
            switch (state) {
                case "NODE_STATE_RUNNING":
                    return NodeStatusInfo.NodeState.NODE_STATE_RUNNING;
                case "NODE_STATE_UP":
                    return NodeStatusInfo.NodeState.NODE_STATE_UP;
                case "NODE_STATE_INVALID":
                    return NodeStatusInfo.NodeState.NODE_STATE_INVALID;
                case "NODE_STATE_IN":
                    return NodeStatusInfo.NodeState.NODE_STATE_IN;
                case "NODE_STATE_OUT":
                    return NodeStatusInfo.NodeState.NODE_STATE_OUT;
                case "NODE_STATE_DOWN":
                    return NodeStatusInfo.NodeState.NODE_STATE_DOWN;
                default:
                    return null;
            }
        }
        return null;
    }
}
