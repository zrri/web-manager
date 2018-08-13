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
        // height: height,
        //属性说明，实际使用时不需要此部分
        currClickNode: '',
        data2:[],//机构树数据

        orginfo: {//机构机芯
            pass: '',
            checkPass: '',
            name: '',
            parentnode:123,
        },
        //机构信息表单
        orginfoForm: {
            name: '',
            region: '',
            date1: '',
            date2: '',
            resource: '',


            parentnode:'',//父节点
            orgid:'',//机构ID
            orgshortname:'',//机构名称
            orgname:'',//机构名称
            admit:'',
            order:'',
            createby:'',
            desc: '',

        },

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
                nodeClickFn: function (nodeData, node, self) {
                    this.currClickNode = nodeData.id + '|' + nodeData.label
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
                add:function () {
                    //清空数据
                    this.resetForm();

                    var selectedId =  this.$refs.orgTree.currentNode.node.data.id;

                    // var selectedId = this.$refs.orgTree.getCheckedNodes()[0].id;
                    vmData.orginfoForm.parentnode=selectedId;

                    this.$refs.updateBtn.disabled = false;
                    this.$refs.addBtn.disabled = true;
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
    };

    //消息处理
    exports.onmessage = function (type, message) {

    };

    //page销毁时触发destroy方法
    exports.destroy = function (id, cite) {

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