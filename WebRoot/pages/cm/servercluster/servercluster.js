/**
 * Created by 樊苏超 on 2018/05/01.
 */
define(function (require, exports) {

    //数据源
    var vmData = {
        filterText:'',
        defaultProps: {
            children: 'children',
            label: 'label'
        },

        serverstatus:[],

        // fileList: [{name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}, {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}],
        fileList: [],
        fileList1: [{name:'I:\\nanhai_local.jks',size:'1'}],
        checkList:[],

        checked:false,

        fileUploadVisible :false,

        //节点信息
        nodeInfos : [],
        nodeinfo:{
            index:3,
            nodename:"BIPSA",
            serverstatus:'停止', //图片最大宽度
            conncount:0,//图片最小宽度
            ip: "10.229.169.65" ,   //图片质量 30~100
            checked:true,
        },

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

        // height: height,
        //属性说明，实际使用时不需要此部分
        currClickNode: '',
        data2:[],//机构树数据

        //POPWindow
        dialogFormVisible: false,

        step:'',

        //版本回退
        undeployDialogVisiable:false,
        unDeployFormData:{
            version:'',
        },
        undeploy_version_list:[],
        //回退步骤位置
        backstep:0,
        backDetailPageVisible:false,



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
            el: "#el_orgmanagement",
            data: vmData,


            methods: {
                nodeClickFn: function (nodeData, node, self) {
                    this.currClickNode = nodeData.id + '|' + nodeData.label
                },

                test:function() {
                    for(var i=0;i< vmData.nodeInfos.length;i++) {
                        //节点索引
                        vmData.nodeInfos[i].disabled=false ;
                    }
                },

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

                    var ids = "";

                    for(var i=0;i< vmData.nodeInfos.length;i++) {

                        //被选中状态才加入
                        if(vmData.nodeInfos[i].checked) {
                            ids+=vmData.nodeInfos[i].ip+"_"+vmData.nodeInfos[i].nodename+";";
                        }
                    }

                    if(ids.indexOf(";") >0) {
                        ids = ids.substr(0,ids.length-1);
                    }

                    //查询部署的版本
                    var reqData = {
                        type:'rollbacker',
                        ids:ids,
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

                    var ids = "";

                    for(var i=0;i< vmData.nodeInfos.length;i++) {

                        //被选中状态才加入
                        if(vmData.nodeInfos[i].checked) {
                           ids+=vmData.nodeInfos[i].ip+"_"+vmData.nodeInfos[i].nodename+";";
                        }
                    }

                    if(ids.indexOf(";") >0) {
                        ids = ids.substr(0,ids.length-1);
                    }

                    //查询部署的版本
                    var reqData = {
                        ids:ids,
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

                    var ids = "";

                    for(var i=0;i< vmData.nodeInfos.length;i++) {

                        //被选中状态才加入
                        if(vmData.nodeInfos[i].checked) {
                            ids+=vmData.nodeInfos[i].ip+"_"+vmData.nodeInfos[i].nodename+";";
                        }
                    }

                    if(ids.indexOf(";") >0) {
                        ids = ids.substr(0,ids.length-1);
                    }
                    //查询部署的版本
                    var reqData = {
                        ids:ids,
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
                    for(var i=0;i< vmData.nodeInfos.length;i++) {

                        //被选中状态才加入
                        if(vmData.nodeInfos[i].checked) {
                            var obj = {};
                            obj.hostip= vmData.nodeInfos[i].ip;
                            obj.nodeName= vmData.nodeInfos[i].nodename;
                            nodeList.push(obj);
                        }
                    }

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
                    for(var i=0;i< vmData.nodeInfos.length;i++) {

                        //被选中状态才加入
                        if(vmData.nodeInfos[i].checked) {
                            var obj = {};
                            obj.hostip= vmData.nodeInfos[i].ip;
                            obj.nodeName= vmData.nodeInfos[i].nodename;
                            nodeList.push(obj);
                        }
                    }
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
                },

                /**
                 * 版本文件上传服务器
                 */
                submitUpload:function() {
                    this.$refs.upload.submit();

                    //上传文件
                    // yufp.service1.uploadFile({
                    //     id:"upload",
                    //     name:"trade/file/upload",
                    //     data:{
                    //         file:"file",
                    //         tellerId:999999,
                    //         // fileList:vmData.fileList1,
                    //     },
                    //     callback:function(code,message,data){
                    //         //请求成功
                    //         if(code=="0"){
                    //             fox.layer.open("文件上传成功 response:"+data);
                    //         }else{
                    //             fox.layer.open("文件上传失败 response:"+message);
                    //         }
                    //     }
                    // });
                },

                //全选
                selectAll:function () {
                    for(var i=0;i< vmData.nodeInfos.length;i++) {
                        vmData.nodeInfos[i].checked = true;
                    }
                },

                //节点点击事件
                tap: function (item, index) {
                    var customKey = 'custom_nodeinfo'; //请以custom_前缀开头，并且全局唯一
                    var routeId = 'nodeinfo';   //模板示例->普通查询的路由ID

                    //数据
                    var data= {
                        ip:item.ip,
                        nodename:item.nodename,
                        serverstatus:item.serverstatus,
                        conncount:item.conncount,

                    }    //传递的业务数据，可选配置

                    yufp.bus.put("nodeinfo","param",data);

                    //添加新的标签页
                    yufp.frame.addTab({
                        id: routeId,       //菜单功能ID（路由ID）
                        key: customKey,           //自定义唯一页签key,请统一使用custom_前缀开头
                        title: '查看节点',   //页签名称
                    });

                },

                getCheckedNodes: function () {
                    this.$alert(this.$refs.mytree.getCheckedNodes(), '提示')
                },
                getCheckedKeys: function () {
                    this.$alert(this.$refs.mytree.getCheckedKeys());
                },
                setCheckedKeys: function () {
                    this.$refs.mytree.setCheckedKeys(["503"]);
                },
                resetChecked: function () {
                    this.$refs.mytree.setCheckedKeys([]);
                },
                filterNode:function (value, data) {
                    if (!value) return true;
                    return data.label.indexOf(value) !== -1;
                },
                resetForm:function(formName) {
                    this.$refs[formName].resetFields();
                },
                nodeClick:function (data,node,nodeWidget) {


                    this.$refs.updateBtn.disabled = true;
                    this.$refs.addBtn.disabled = false;


                    var orgid = data.id;

                    vmData.orginfoForm.parentnode = data.PARENTORGID;
                    vmData.orginfoForm.orgid=orgid;
                    vmData.orginfoForm.orgshortname=data.ORGCHINAME;
                    vmData.orginfoForm.orgname=data.ORGCHINAME;

                },

                remove:function() {
                    //清空数据
                    this.resetForm();

                    var selectedId =  this.$refs.orgTree.currentNode.node.data.id;

                    // var selectedId = this.$refs.orgTree.getCheckedNodes()[0].id;
                    vmData.orginfoForm.orgid=selectedId;

                    this.$refs.updateBtn.disabled = false;
                    this.$refs.addBtn.disabled = false;

                    //通过名称查询卡号
                    var reqData = vmData.orginfoForm;

                    yufp.service1.request({
                        id: "removeOrgInfo",
                        data: reqData,
                        name: "commonsystem/org/removeOrgInfo",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code == 0) {
                                vm.queryOrgInfo();
                                alert("删除成功");
                            } else {
                                alert("删除失败");
                            }
                        }
                    });
                },
                /**
                 * 添加根机构
                 */
                addRootOrg:function() {
                    //清空数据
                    this.resetForm();

                    vmData.orginfoForm.parentnode=0;

                    this.$refs.updateBtn.disabled = false;
                    this.$refs.addBtn.disabled = true;
                },

                queryOrgInfo:function() {
                    //通过名称查询卡号
                    var reqData = {

                    };
                    yufp.service1.request({
                        id: "queryorginfo",
                        data: reqData,
                        // name: "commonsystem/org/queryorginfo",
                        name:"auth/org/queryorginfo",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code == 0) {
                                vm.data2 = [];
                                var data_tmp=[];
                                for(var i=0;i<data.data.data.length;i++) {
                                    var obj = {};
                                    obj.id=data.data.data[i].ORGID;
                                    obj.label= data.data.data[i].ORGCHINAME;
                                    obj.ORGCHINAME= data.data.data[i].ORGCHINAME;
                                    obj.BRANCHID= data.data.data[i].BRANCHID;
                                    obj.PARENTORGID= data.data.data[i].PARENTORGID;
                                    vmData.nodeKey.push(i);

                                    // vmData.data2.push(obj);
                                    data_tmp.push(obj);
                                }


                                vmData.data2 =  toTreeData(data_tmp)
                            } else {
                                alert("查询失败");
                            }
                        }
                    })
                },

                handleRemove:function(file, fileList) {
                    console.log(file, fileList);
                },
                handlePreview:function(file) {
                    console.log(file);
                },
                handleExceed:function(files, fileList) {
                    this.$message.warning("当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件");
                },
                beforeRemove:function(file, fileList) {
                    return this.$confirm("确定移除 ${ file.name }？");
                },

                /**
                 * 提交更新
                 */
                submitForm:function (flag) {
                    //通过名称查询卡号
                    var reqData = vmData.orginfoForm;

                    var serviceName = "";

                    if(flag == "add") {
                        // serviceName = "commonsystem/org/addOrgInfo";
                        serviceName = "auth/org/addOrgInfo";
                    } else {
                        serviceName = "commonsystem/org/updateOrgInfo";
                    }

                    yufp.service1.request({
                        id: "updateOrgInfo",
                        data: reqData,
                        name: serviceName,
                        callback: function (code, message, data) {
                            //登录成功
                            if (code == 0) {
                                vm.queryOrgInfo();
                                alert("更新成功");
                            } else {
                                alert("更新失败");
                            }
                        }
                    });
                },

                /**
                 * 重置
                 */
                resetForm:function () {
                    vmData.orginfoForm= {
                        name: '',
                        region: '',
                        date1: '',
                        date2: '',
                        resource: '',


                        parentnode:'',
                        orgid:'',
                        orgshortname:'',
                        orgname:'',
                        admit:'',
                        order:'',
                        createby:'',
                        desc: '',

                    };


                },


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

                //通过名称查询卡号
                var reqData = {};

                yufp.service1.request({
                    id: "queryNodeInfoList",
                    data: reqData,
                    name: "cm/node/queryNodeInfoList",
                    callback: function (code, message, data) {
                        vmData.nodeInfos = data.list;


                        for(var i=0;i< vmData.nodeInfos.length;i++) {
                            //节点名称
                            vmData.nodeInfos[i].nodename=vmData.nodeInfos[i].NAME;
                            //节点状态
                            vmData.nodeInfos[i].serverstatus=false;
                            //连接数量
                            vmData.nodeInfos[i].conncount=vmData.nodeInfos[i].ISLINK;
                            //ip
                            vmData.nodeInfos[i].ip=vmData.nodeInfos[i].HOSTIP;
                            //节点索引
                            vmData.nodeInfos[i].index=i+1 ;

                            //节点索引
                            vmData.nodeInfos[i].checked=false ;


                            vmData.nodeInfos[i].disabled=false ;
                            // if(i==2) {
                            //     //节点索引
                            //
                            // } else {
                            //     //节点索引
                            //     vmData.nodeInfos[i].disabled=true ;
                            // }

                        }

                        //获取服务器状态
                        var nodeList = [];
                        for(var i=0;i< vmData.nodeInfos.length;i++) {
                            var obj = {};
                            obj.hostip= vmData.nodeInfos[i].ip;
                            obj.nodeName= vmData.nodeInfos[i].nodename;
                            nodeList.push(obj);


                        }

                        //获取节点状态
                        var reqData = {
                            userId:999999,
                            list:nodeList,
                        };

                        yufp.service1.request({
                            id: "getNodeStatus",
                            data: reqData,
                            name: "cm/node/getNodeStatus",
                            callback: function (code, message, data) {
                                //登录成功
                                if (code == 0) {
                                    console.log("获取节点信息成功");
                                } else {
                                    console.log("获取节点信息失败");
                                }
                            }
                        });
                    }
                });

            }
        });

        yufp.eventproxy.unbind("deploy");

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

        //版本回退监听
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

        //节点状态监听
        yufp.eventproxy.unbind("nodestatus");

        yufp.eventproxy.bind("nodestatus",function (content) {
            //IP
            var hostip = content.hostip;
            //节点名称
            var nodeName = content.nodeName;
            //节点状态 01-启动
            var status = content.status;

           if(status == '01') {

           }
        },true);
    };

    //消息处理
    exports.onmessage = function (type, message) {

    };

    //page销毁时触发destroy方法
    exports.destroy = function (id, cite) {

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

    /**
     * 将父子关系数据转化成树结构数据
     * @param data
     * @returns {Array}
     */
    function toTreeData(data) {
        var resData = data;
        var tree = [];

        //找到顶层节点
        for (var i = 0; i < resData.length; i++) {
            if (resData[i].PARENTORGID === '0') {
                var obj = {
                    id: resData[i].id,
                    label: resData[i].label,
                    ORGCHINAME:resData[i].ORGCHINAME,
                    BRANCHID:resData[i].BRANCHID,
                    PARENTORGID:resData[i].PARENTORGID,
                    children: []
                };
                tree.push(obj);
                //移除当前数据
                resData.splice(i, 1);
                i--;
            }
        }
        //执行转化
        convertTreeData(tree);

        /**
         * 转化数据
         * @param chiArr
         */
        function convertTreeData(chiArr) {
            if (resData.length !== 0) {
                for (var i = 0; i < chiArr.length; i++) {
                    for (var j = 0; j < resData.length; j++) {
                        if (chiArr[i].id === resData[j].PARENTORGID) {

                            var obj = {
                                id: resData[j].id,
                                label: resData[j].label,
                                ORGCHINAME:resData[j].ORGCHINAME,
                                BRANCHID:resData[j].BRANCHID,
                                PARENTORGID:resData[j].PARENTORGID,
                                children: []
                            };
                            chiArr[i].children.push(obj);
                            resData.splice(j, 1);
                            j--;
                        }
                    }
                    // console.log(chiArr[i].children);
                    convertTreeData(chiArr[i].children);
                }
            }
        }
        return tree;
    }



});
