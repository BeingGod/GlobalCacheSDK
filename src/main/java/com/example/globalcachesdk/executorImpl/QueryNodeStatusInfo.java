package com.example.globalcachesdk.executorImpl;

import com.example.globalcachesdk.entity.AbstractEntity;
import com.example.globalcachesdk.entity.ClusterStatusInfo;
import com.example.globalcachesdk.entity.NodeStatusInfo;
import com.example.globalcachesdk.exception.CommandExecException;
import com.example.globalcachesdk.executor.AbstractCommandExecutor;
import com.example.globalcachesdk.executor.Configure;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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
        //按行切分
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

            //磁盘数
            int diskNum= Integer.parseInt(nodeInfoList[j+3].substring(10));
            node.setDiskNum(diskNum);
            //跳过接下来的diskNum行读取下一个节点信息
            i+=diskNum;

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
