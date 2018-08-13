/**
 * Created by helin3 on 2017/11/25.
 */
define(function (require, exports) {

    var vmData = {
        filterText: '',
        // height: height,
        orgTreeProps: {id: 'orgId', label: 'orgChiName', children: 'children'},
        orgTreeData: [],
        orgTreeCurrentData: '',//选中的条目
        orgTreeCurrentNode: '',//选中的树形Node
        // orgTreeCheckData: [],//树形结构里勾选的数据
        orgInfoFormData: {
            orgId: '',
            orgChiName: '',
            orgEngName: '',
            parentOrgId: '',
            parentOrgName: '',
            orgLvl: '',
            orgType: '',
            createDate: '',
            createTime: '',
            updateDate: '',
            updateTime: '',
            status: ''
        },
        //校验控制
        rules: {
            orgId: [
                {required: true, message: '请输入机构编号', trigger: 'blur'}
            ],
            orgName: [
                {required: true, message: '请输入机构全称', trigger: 'blur'}
            ],
            status: [{required: true, message: '请选择机构状态', trigger: 'change'}]
        }
    };

    /***************** 弹窗数据 *************/
    var orgDialogData = {
        orgInfoDialogVisible: false,
        orgInfoFormData: {
            orgId: '',
            orgChiName: '',
            orgEngName: '',
            parentOrgId: '',
            parentOrgName: '',
            orgLvl: '',
            orgType: '',
            createDate: '',
            createTime: '',
            updateDate: '',
            updateTime: '',
            status: ''
        },
        rules: {
            orgId: [
                {required: true, message: '请输入机构编号', trigger: 'blur'}
            ],
            orgName: [
                {required: true, message: '请输入机构全称', trigger: 'blur'}
            ],
            status: [{required: true, message: '请选择机构状态', trigger: 'change'}]
        }
    };

    //page加载完成后调用ready方法
    exports.ready = function (hashCode, data, cite) {
        var height = yufp.custom.viewSize().height;
        //创建virtual model
        var vm = yufp.custom.vue({
            el: "#el_orgmanagement",
            data: vmData,
            methods: {
                handleCheckChange: function (data, checked, indeterminate) {
                    if (checked) {
                        vmData.orgTreeCheckData.push(data);
                    } else {
                        for (var i = vmData.orgTreeCheckData.length - 1; i >= 0; i--) {
                            if (vmData.orgTreeCheckData[i] === data) {
                                vmData.orgTreeCheckData.splice(i, 1);
                            }
                        }
                    }
                },
                filterNode: function (value, data) {
                    if (!value) return true;
                    return data.orgChiName.indexOf(value) !== -1;
                },
                handleNodeClick: function (data, node, nodeWidget) {
                    vmData.orgTreeCurrentData = data;//添加时用到
                    vmData.orgTreeCurrentNode = node;
                    //更新时用到     替换时间格式
                    vmData.orgInfoFormData = data;
                    var dataCreateTime = data.createTime;
                    var dataCreateDate = data.createDate;
                    if (dataCreateTime.indexOf(':') < 0) {
                        vmData.orgInfoFormData.createTime = dataCreateTime.substring(0, 2) + ':'
                            + dataCreateTime.substring(2, 4) + ":" + dataCreateTime.substring(4, 6);
                    }
                    if (dataCreateDate.indexOf("年") < 0) {
                        vmData.orgInfoFormData.createDate = dataCreateDate.substring(0, 4) + '年'
                            + dataCreateDate.substring(4, 6) + '月' + dataCreateDate.substring(6, 8) + '日';
                    }

                    //更新时用到   替换日期格式
                    var dataUpdateTime = data.updateTime;
                    var dataUpdateDate = data.updateDate;
                    if (dataUpdateDate.indexOf("年") < 0) {
                        vmData.orgInfoFormData.updateDate = dataUpdateDate.substring(0, 4) + '年'
                            + dataUpdateDate.substring(4, 6) + '月' + dataUpdateDate.substring(6, 8) + '日';
                    }
                    if (dataUpdateTime.indexOf(":") < 0) {
                        vmData.orgInfoFormData.updateTime = dataUpdateTime.substring(0, 2) + ':'
                            + dataUpdateTime.substring(2, 4) + ":" + dataUpdateTime.substring(4, 6);
                    }

                },
                add: function () {
                    if (vmData.orgTreeCurrentData === '') {
                        alert("请先选择一条");
                        return;
                    }
                    orgDialogData.orgInfoDialogVisible = true;
                    this.resetData(orgDialogData.orgInfoFormData);
                    //上级机构赋值
                    orgDialogData.orgInfoFormData.parentOrgId = vmData.orgTreeCurrentData.orgId;
                    orgDialogData.orgInfoFormData.parentOrgName = vmData.orgTreeCurrentData.orgChiName;
                    //机构级别+1
                    orgDialogData.orgInfoFormData.orgLvl = Number(vmData.orgTreeCurrentData.orgLvl) + 1;
                },
                addRootOrg: function () {
                    //添加根机构
                    orgDialogData.orgInfoDialogVisible = true;
                    this.resetData(orgDialogData.orgInfoFormData);
                    //上级机构为空
                    orgDialogData.orgInfoFormData.parentOrgId = -1;
                    orgDialogData.orgInfoFormData.parentOrgName = '';
                    //机构级别为1
                    orgDialogData.orgInfoFormData.orgLvl = 1;
                },
                remove: function () {
                    if (vmData.orgTreeCurrentData === '') {
                        alert("请先选择一条");
                        return;
                    }

                    var str;
                    if (vmData.orgTreeCurrentNode.data.children === undefined
                        || vmData.orgTreeCurrentNode.data.children.length === 0) {
                        str = '确定要删除' + vmData.orgTreeCurrentNode.data.orgChiName + '吗?'

                    } else {
                        str = '确定要删除' + vmData.orgTreeCurrentNode.data.orgChiName + '及其机构下的所有子机构吗?'
                    }
                    var orgTreeCheckData=[];

                    flatData(orgTreeCheckData,vmData.orgTreeCurrentNode.data);

                    this.$confirm(str, '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning',
                    }).then(function () {
                        yufp.service1.request({
                            id: "deleteOrgInfoList",
                            data: {'list': orgTreeCheckData},
                            name: "auth/org/deleteOrgInfoList",
                            callback: function (code, message, data) {
                                //登录成功
                                if (code === 0) {
                                    vm.queryOrgInfo();
                                    alert("删除成功");
                                    vmData.orgTreeCurrentData = '';
                                } else {
                                    alert("删除失败");
                                }
                            }
                        });
                    });
                },
                queryOrgInfo: function () {
                    yufp.service1.request({
                        id: "queryOrgTree",
                        data: "",
                        name: "auth/org/queryOrgTree",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                vmData.orgTreeData = JSON.parse(data.orgTreeData);
                                org_tree_data.orgData = vmData.orgTreeData;//赋值，用户管理功能需要用到
                            } else {
                                alert("查询失败");
                            }
                        }
                    })
                },
                /**
                 * 提交更新
                 */
                updateOrgInfo: function () {
                    yufp.service1.request({
                        id: "updateOrgInfo",
                        data: vmData.orgInfoFormData,
                        name: "auth/org/updateOrgInfo",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code === 0) {
                                vm.queryOrgInfo();
                                alert("更新成功");
                            } else {
                                alert("更新失败");
                            }
                        }
                    });
                },
                /*
                  清空添加机构信息的弹出层的数据
                 */
                resetData:function(inputData){
                  for(item in inputData){
                    inputData[item]='';
                  }
                },

            },
            watch: {
                filterText: function (val) {
                    this.$refs.orgTree.filter(val);
                }
            },
            /**
             *  界面加载成功
             */

            mounted: function () {
                var headers = {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                    "Authorization": "Basic d2ViX2FwcDo="
                };
                this.resetData(vmData.orgInfoFormData);
                this.queryOrgInfo();
            }
        });


        /********************************* 添加机构时的弹窗 **********************/


        var vmDialog = yufp.custom.vue({
            el: "#el_orgInfoDialog",
            data: orgDialogData,
            methods: {
                addOrgInfoSubmit: function () {
                    orgDialogData.orgInfoDialogVisible = false;
                    yufp.service1.request({
                        id: "addOrgInfo",
                        data: orgDialogData.orgInfoFormData,
                        name: "auth/org/addOrgInfo",
                        callback: function (code, message, data) {
                            //登录成功
                            if (code === 0) {
                                vm.queryOrgInfo();
                                alert("添加成功");
                                vmDialog.clearFormData();
                            } else {
                                alert("添加失败");
                            }
                        }
                    });
                },
                clearFormData: function () {
                    //清空表单数据
                    for (var key in orgDialogData.orgInfoFormData) {
                        orgDialogData.orgInfoFormData[key] = '';
                    }
                }
            },
            watch: {},
            mounted: function () {

            }
        });


    };

    //消息处理
    exports.onmessage = function (type, message) {

    };

    //page销毁时触发destroy方法
    exports.destroy = function (id, cite) {

    }


    //树形结构转数组
    function flatData(list,data) {
        list.push(data);
        if (data.children !== undefined && data.children.length !== 0) {
            for (var i = 0; i < data.children.length; i++) {
                flatData(list,data.children[i]);
            }
        }
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
                    ORGCHINAME: resData[i].ORGCHINAME,
                    BRANCHID: resData[i].BRANCHID,
                    PARENTORGID: resData[i].PARENTORGID,
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
                                ORGCHINAME: resData[j].ORGCHINAME,
                                BRANCHID: resData[j].BRANCHID,
                                PARENTORGID: resData[j].PARENTORGID,
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
