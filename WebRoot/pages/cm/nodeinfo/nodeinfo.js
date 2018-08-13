/**
 * Created by 樊苏超 on 2018/05/01.
 */
define(function (require, exports,cite) {

    var getinfoTimer;

    var param = yufp.bus.get("nodeinfo","param");

    var status = param.serverstatus;
    var serverstatus = "";
    if("true" == status) {
        serverstatus='已启动';
    } else {
        serverstatus='未启动';
    }


    // http://guangzhou.btop.mobi:9191/services/serviceInvoke?_$id=1&_$service=cm/node/getNodeDetailInfo&_$sessionid=1-23232&_$data={%22data%22:{%22HOSTIP%22:%22172.16.0.4%22,%22NAME%22:%22BIPS_A%22},%22header%22:{}}

    //数据源
    var vmData = {

        ip:param.ip,
        nodename:param.nodename,
        serverstatus:serverstatus,
        conncount:param.conncount,
        deployDialogVisiable:false,
        dialogFormVisible:false,
        deployFormData:{
            version:'',
            needRestart:'',
        },
        version_list:[],

        options:[{
            value: 'true',
            label: '需要重启'
        }, {
            value: 'false',
            label: '不需要重启'
        }],

        //运行时间
        RUNNINGTIME:'',


        //版本回退
        undeployDialogVisiable:false,
        unDeployFormData:{
            version:'',
        },
        undeploy_version_list:[],
        //回退步骤位置
        backstep:0,
        backDetailPageVisible:false,


        //数据库连接池信息
        dbpoolinfo:[],

        //步骤位置
        step:'',


        filedev:[],
        activedthread:'',
        THREADCOUNT:'',
        DAEMONTHREADCOUNT:'',
        STARTEDTHREDCOUNT:'',
        classloadnumber:'',
        LoadedClassCount:'',
        uninstalledclassescount:'',
        JvmInputArguments:'',


        defaultProps: {
            children: 'children',
            label: 'label'
        },
        // height: height,
        //属性说明，实际使用时不需要此部分
        currClickNode: '',
        data2:[],//机构树数据



        nodeKey:[],

        //校验控制
        rules: {
            parentNode:[
                { required: true, message: '请输入父机构', trigger: 'blur' }
            ],
            parentNodeName:[
                { required: true, message: '请输入父名称', trigger: 'blur' }
            ],
            orgShortName:[
                { required: true, message: '请输入机构简称', trigger: 'blur' }
            ],
            orgName:[
                { required: true, message: '请输入机构全称', trigger: 'blur' }
            ],
        }
};

    //page加载完成后调用ready方法
    exports.ready = function (hashCode, data, cite) {
        var height = yufp.custom.viewSize().height;
        //创建virtual model
        var vm =  yufp.custom.vue({
            el: "#el_servercluster_nodeinfo",
            data: vmData,


            methods: {
                //版本部署
                releaseDeploy:function() {
                    //弹出版本部署对话框
                    vmData.deployDialogVisiable=true;

                    //查询部署的版本
                    var reqData = {
                        type:'deploy'
                    };
                    //请求版本列表
                    yufp.service1.request({
                        id: "listVersion",
                        data: reqData,
                        name: "cm/deploy/listVersion",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code == 0) {
                                vmData.version_list = data.version_list;
                            } else {
                                alert("查询部署列表失败");
                            }
                        }
                    });

                },

                /**
                 * 版本回退
                 */
                releaseUnDeploy:function () {
                    //弹出版本部署对话框
                    vmData.undeployDialogVisiable=true;

                    //查询部署的版本
                    var reqData = {
                        type:'rollbacker',
                        ids:param.ip+"_"+param.nodename,
                    };
                    //请求版本列表
                    yufp.service1.request({
                        id: "listVersion",
                        data: reqData,
                        name: "cm/deploy/listVersion",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code == 0) {
                                vmData.undeploy_version_list = data.version_list;
                            } else {
                                alert("查询部署列表失败");
                            }
                        }
                    });
                },

                /**
                 * 版本部署
                 */
                deploy: function () {
                    //查询部署的版本
                    var reqData = {
                        ids:param.ip+"_"+param.nodename,
                        needRestart:vmData.deployFormData.needRestart,
                        userId:999999,
                        version:vmData.deployFormData.version,
                    };
                    //开始版本部署
                    yufp.service1.request({
                        id: "listVersion",
                        data: reqData,
                        name: "cm/deploy/startDeploy",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code == 0) {
                                vmData.deployDialogVisiable = false;
                                vmData.dialogFormVisible = true;
                            } else {
                                alert("部署失败");
                            }
                        }
                    });
                },

                /**
                 * 版本回退
                 */
                unDeploy: function () {
                    //查询部署的版本
                    var reqData = {
                        ids:param.ip+"_"+param.nodename,
                        userId:999999,
                        version:vmData.unDeployFormData.version,
                    };
                    //开始版本部署
                    yufp.service1.request({
                        id: "startUnDeploy",
                        data: reqData,
                        name: "cm/deploy/startUnDeploy",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code == 0) {
                                vmData.undeployDialogVisiable = false;
                                vmData.backDetailPageVisible = true;
                            } else {
                                alert("回退失败");
                            }
                        }
                    });
                },


                /**
                 * 开启服务器
                 */
                startServer:function () {
                    var nodeList = [];
                    var obj = {};
                    obj.hostip= param.ip;
                    obj.nodeName= param.nodename;
                    nodeList.push(obj);

                    var reqData = {
                        userId:999999,
                        list:nodeList,
                    };

                    yufp.service1.request({
                        id: "startNodes",
                        data: reqData,
                        name: "cm/node/startNodes",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code == 0) {
                                alert("启动成功");
                            } else {
                                alert("启动失败");
                            }
                        }
                    });
                },

                /**
                 * 停止服务器
                 */
                stopServer:function() {
                    var nodeList = [];
                    var obj = {};
                    obj.hostip= param.ip;
                    obj.nodeName= param.nodename;
                    nodeList.push(obj);

                    var reqData = {
                        userId:999999,
                        list:nodeList,
                    };

                    yufp.service1.request({
                        id: "stopNodes",
                        data: reqData,
                        name: "cm/node/stopNodes",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code == 0) {
                                alert("停止成功");
                            } else {
                                alert("停止失败");
                            }
                        }
                    });
                }

            },
            watch: {
                filterText:function (val) {
                    this.$refs.orgTree.filter(val);
                }
            },
            /**
             *  界面加载成功
             */

            mounted: function(){

            }
        });

        yufp.eventproxy.bind("deploy",function (content) {
            //节点位置
            var action = content.action;
            //节点状态
            var taskStatus = content.taskStatus;
            //消息
            var detail = content.detail;

            addElementLi("parentUl",detail);

            //准备阶段
            if(action == "prepare") {

                //完成准备阶段
            } else if (action == "finishPrepare") {
                //01-节点成功
                if(taskStatus == "01") {
                    vmData.step = "1";
                }
                //上传成功
            } else if(action == "transmit") {

                //完成上传
            } else if(action == "finishTransmit") {
                //01-节点成功
                if(taskStatus == "01") {
                    vmData.step ="2";
                }
                //解压文件
            } else if(action == "uncompress") {

                //完成解压文件
            } else if (action == "finishUncompress") {
                //01-节点成功
                if(taskStatus == "01") {
                    vmData.step = "3";
                }
                //开始备份
            } else if (action == "backup") {

                //完成备份
            } else if (action == "finishBackup") {

                //更新成功
            } else if (action == "update") {

                //完成更新
            } else if (action == "finishUpdate") {

                //检验成功
            } else if (action == "check") {

                //校验完成
            } else if (action == "finishCheck") {

                //部署完成
            } else if (action == "finish") {
                //01-节点成功
                if(taskStatus == "01") {
                    vmData.step = "4";
                }
            }
        },true);

        yufp.eventproxy.unbind("deploy");

        yufp.eventproxy.unbind("unDelopy");

        yufp.eventproxy.bind("unDelopy",function (content) {
            //节点位置
            var action = content.action;
            //节点状态
            var taskStatus = content.taskStatus;
            //消息
            var detail = content.detail;

            addElementLi("backMsgPan",detail);

            //准备阶段
            if(action == "prepare") {

                //完成准备阶段
            } else if (action == "finishPrepare") {
                //01-节点成功
                if(taskStatus == "01") {
                    vmData.backstep = "1";
                }
                //停止服务器
            } else if(action == "shutdown") {

                //停止服务器
            } else if(action == "finishShutdown") {
                //01-节点成功
                if(taskStatus == "01") {
                    vmData.backstep ="2";
                }
                //回退
            } else if(action == "rollback") {

                //回退成功
            } else if (action == "finishRollback") {
                //01-节点成功
                if(taskStatus == "01") {
                    vmData.backstep = "3";
                }
                //开始启动
            } else if (action == "startup") {

                //开始启动
            } else if (action == "finishStartup") {
                //01-节点成功
                if(taskStatus == "01") {
                    vmData.backstep = "4";
                }
                //部署完成
            } else if (action == "check") {

                //校验完成
            } else if (action == "finishCheck") {

                //部署完成
            } else if (action == "finish") {
                //01-节点成功
                if(taskStatus == "01") {
                    vmData.backstep = "5";
                }
            }
        },true);


        //通过名称查询卡号
        var reqData = {
            HOSTIP:param.ip,
            NAME:param.nodename,
        }

        /**
         * 定时任务,每隔5秒刷新CPU，磁盘等信息
         */
        getinfoTimer=window.setInterval(function(){
            /**
             * 获取当前节点信息
             */
            yufp.service1.request({
                id: "getNodeDetailInfo",
                data: reqData,
                name: "cm/node/getNodeDetailInfo",
                callback: function (code, message, data) {
                    //登录成功
                    if (code == 0) {
                        //CPU
                        vmData.CPUUSAGE = data.CPUUSAGE;//CPU使用

                        //线程监控
                        vmData.DAEMONTHREADCOUNT = data.DAEMONTHREADCOUNT;//daemon线程数
                        vmData.PEEKTHREADCOUNT = data.PEEKTHREADCOUNT;//
                        vmData.STARTEDTHREDCOUNT = data.STARTEDTHREDCOUNT;//开始的线程数量
                        vmData.THREADCOUNT = data.THREADCOUNT;//线程数量

                        //JVM监控
                        vmData.JvmInputArguments = data.JvmInputArguments;//JVM参数
                        vmData.LoadedClassCount = data.LoadedClassCount;//已经加载的class数量

                        if(data.RUNNINGTIME != undefined &&　''!=data.RUNNINGTIME ) {

                            var h = parseInt(data.RUNNINGTIME/3600000);
                            var m = parseInt(data.RUNNINGTIME/60000)-60*h;

                            vmData.RUNNINGTIME = h+"小时"+m+"分钟"
                        }



                        vmData.TOTALMEMORY = data.TOTALMEMORY;//总的内存

                        //文件系统
                        vmData.filedev = data.PartitionState;

                        //数据库连接池
                        vmData.dbpoolinfo = data.connpool;
                    } else {
                        alert("请求节点信息失败");
                    }
                }
            });
        },5000);
    };

    //消息处理
    exports.onmessage = function (type, message) {

    };

    //page销毁时触发destroy方法
    exports.destroy = function (id, cite) {
        window.clearTimeout(getinfoTimer);
    }

    function addElementLi(obj,msg) {
        var ul = document.getElementById(obj);

        //添加 li
        var li = document.createElement("li");

        //设置 li 属性，如 id
        li.setAttribute("id", "newli");

        li.innerHTML = msg;
        ul.appendChild(li);
    }


});