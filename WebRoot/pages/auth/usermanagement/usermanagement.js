/**
 * Created by helin3 on 2017/11/25.
 */
define(function (require, exports) {

    var multipleSelection = [];//保存多选的用户

    //页面初始化数据
    var initData = {
        isAddUser: true,
        pageSize: 5,//每页显示5条
        currentPage: 1,//当前页，从1开始
        userInfoDialogVisible: false,//查看弹窗口
        userInfoSearch: {//搜索用户
            userId: '',
            username: '',
            identifyNO: '',
            sex: '',
            orgChiName: '',
            status: ''
        },
        userTypeOptions: [{id: "", label: "无"}],
        statusOptions: [{id: "", label: "所有"}, {id: "0", label: "启用"}, {id: "1", label: "停用"}
            , {id: "2", label: "注销"}, {id: "8", label: "锁定"}, {id: "9", label: "被交接"}],
        userManagementData: [
            /*{
                userId: '',
                username: '',
                userType: '',
                identifyNO: '',
                orgId: '',
                orgChiName:'',
                loginType: '',
                status: '',
                englishName: '',
                sex: '',
                cellphone: '',
                phone: '',
                address: '',
                email: '',
                userLevel: '',
                lastLoginTime: '',
                lastLogoutTime: ''
            }*/
        ]

    };

    //弹窗初始化数据
    var userInfoDialog = {
        userInfoDialogVisible: false,
        title: '',
        dialogType: '1',//弹窗类型，0——新增；1——查看；2——编辑；  现在把“新增”、“查看”、“编辑”功能整合到一起，复用一个弹窗
        isDisabled: false,//输入框是否可编辑，“新增”、“编辑”时可输入，“查看”时不可输入
        updateUsrId: '',//要修改的用户Id
        rules: {
            userId: [
                {required: true, message: '用户编号不能为空', trigger: 'blur'}
            ],
            username: [
                {required: true, message: '用户名不能为空', trigger: 'blur'}
            ],
            identifyNO: [{required: true, message: '用户识别码不能为空', trigger: 'blur'}],
            englishName: [{required: true, message: '用户英文名不能为空', trigger: 'blur'}],
            orgId: [
                {required: true, message: '机构不能为空', trigger: 'change'}
            ],
            sex: [
                {required: true, message: '性别不能为空', trigger: 'change'}
            ],
            status: [
                {required: true, message: '用户状态不能为空', trigger: 'change'}
            ]
        },
        selectUserOrgId: '',
        userInfoDialogData: {
            userId: '',
            username: '',
            userType: '',
            identifyNO: '',
            orgId: '',
            orgChiName: '',
            loginType: '',
            status: '',
            englishName: '',
            sex: '',
            cellphone: '',
            phone: '',
            address: '',
            email: '',
            userLevel: '',
            roleId:'',
            lastLoginTime: '',
            lastLogoutTime: ''
        },
        selectRoleData:[],
    };


    //page加载完成后调用ready方法
    exports.ready = function (hashCode, data, cite) {
        var height = yufp.custom.viewSize().height;

        //创建virtual model
        var vm = yufp.custom.vue({
            el: "#el_usermanagement",
            data: initData,
            computed: {
                getUserData: function () {
                    //在这里处理分页
                    var userData = initData.userManagementData;
                    var pageSize = initData.pageSize;
                    var currentPage = initData.currentPage;
                    var offset = (currentPage - 1) * pageSize;
                    if (offset + pageSize >= userData.length) {
                        return userData.slice(offset, userData.length);
                    }
                    else {
                        return userData.slice(offset, offset + pageSize);
                    }
                }
            },
            methods: {
                showStatus: function (row, column) {
                    switch (row.status) {
                        case '0':
                            return '启用';
                        case '1':
                            return '停用';
                        case '2':
                            return '注销';
                        case '8':
                            return '锁定';
                        case '9':
                            return '被交接';
                    }
                },
                onSubmit: function () {
                    //查询按钮事件
                    this.queryUserInfoList(initData.userInfoSearch);
                },
                onClear: function () {
                    //重置搜索条件
                    for (var key in initData.userInfoSearch) {
                        initData.userInfoSearch[key] = '';
                    }
                },
                handleSelectionChange: function (val) {
                    //处理多选操作
                    multipleSelection = val;
                },
                handleShow: function (index, row) {
                    //vmInfo.showInfo(row.userId);
                    userInfoDialog.userInfoDialogData = JSON.parse(JSON.stringify(initData.userManagementData[index]));
                    vmDialog.showUserInfoDialog(userInfoDialog.userInfoDialogData);
                },
                handleEdit: function (index, row) {
                    //编辑单条数据
                    // userInfoDialog.userInfoDialogData = JSON.parse(JSON.stringify(initData.userManagementData[index]));
                    // vmDialog.editUserInfoDialog(userInfoDialog.userInfoDialogData);
                    vmDialog.editUserInfoDialog(row);
                },
                handleDelete: function (index, row) {
                    //删除单条数据
                    this.$confirm('确定要删除吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function () {
                        var deleteArray = new Array(1);
                        deleteArray[0] = initData.userManagementData[index];
                        vm.deleteUserInfoList(deleteArray);
                    });
                },
                createFn: function () {
                    vmDialog.addUserInfoDialog();
                },
                deleteFn: function () {
                    //删除按钮
                    if (multipleSelection.length === 0) {
                        this.$alert("请至少选择1个用户", '提示', {
                            confirmButtonText: '提示',
                            type: 'warning'
                        });
                    }
                    else {
                        this.$confirm('确定要删除吗?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(function () {
                            vm.deleteUserInfoList(multipleSelection);
                        });
                    }
                },
                changeFn: function () {
                    if (multipleSelection.length < 1) {
                        this.$alert("请选择一个用户", "提示", {
                            confirmButtonText: '确定', type: 'warning'
                        });
                        return;
                    } else if (multipleSelection.length > 1) {
                        this.$alert("只能选择一个用户", "提示", {
                            confirmButtonText: '确定', type: 'warning'
                        });
                        return;
                    }
                    vmDialog.editUserInfoDialog(multipleSelection[0]);
                },
                changeStatusFn: function (status) {
                    if (multipleSelection.length === 0) {
                        this.$alert("请选择一个用户", "提示", {
                            confirmButtonText: '确定', type: 'warning'
                        });
                    } else {
                        var that = this;
                        this.$confirm('确定要停用所选用户??', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(function () {
                            /*for (var index = 0; index < multipleSelection.length; i++) {
                                multipleSelection[index].status = status;
                            }
                            vmDialog.editUserInfoDialog(multipleSelection);*/
                        });
                    }
                },
                //分页触发事件
                sizeChange: function (pageSize) {
                    vm.resetData();
                },
                currentChange: function (pageIndex) {
                    initData.currentPage = pageIndex;
                    //换页清空选择的数据
                    vm.resetData();
                },
                prevClick: function (pageIndex) {
                    initData.currentPage = pageIndex;
                    vm.resetData();
                },
                nextClick: function (pageIndex) {
                    initData.currentPage = pageIndex;
                    vm.resetData();
                },
                queryUserInfoList: function (queryData) {
                    yufp.service1.request({
                        id: "queryUserList",
                        data: queryData,
                        name: "auth/user/queryUserInfoList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                console.log(data)
                                initData.userManagementData = data.list;
                            } else {
                                alert("查询失败");
                            }
                        }
                    })

                },
                deleteUserInfoList: function (deleteData) {
                    var userInfoList = {"list": deleteData};
                    yufp.service1.request({
                        id: "deleteUserInfoList",
                        data: userInfoList,
                        name: "auth/user/deleteUserInfoList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                vm.queryUserInfoList();
                                alert("删除成功");
                            } else {
                                alert("删除失败");
                            }
                        }
                    })
                }
            },
            watch: {},
            /**
             *  界面加载成功
             */

            mounted: function () {
                var headers = {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                    "Authorization": "Basic d2ViX2FwcDo="
                };
                this.queryUserInfoList();
            }
        });


        /**************************** 新增、修改、查看用户弹窗 ****************************/
        var vmDialog = yufp.custom.vue({
            el: "#el_userInfoDialog",
            data: userInfoDialog,
            watch:{
              // showMessage:function(value){
              //   console.log(value);
              //   let obj = {};
              //   obj = this.selectRoleData.find((item)=>{//这里的userList就是上面遍历的数据源
              //       return item.id === value;//筛选出匹配数据
              //   });
              //   console.log(obj.label);
              // },
              userInfoDialogVisible:function(value){
                  if(value){
                    yufp.service1.request({
                      id: 'queryRoleInfoList',
                      name: 'auth/user/queryRolesInfo',
                      data: '',
                      callback: function (code, message, data) {
                          if(code===0){
                            userInfoDialog.selectRoleData=[];
                            var rolesData = data.list;
                            for(var key in rolesData){
                              var obj = {id:rolesData[key]["ROLE_ID"],label:rolesData[key]["ROLE_NAME"]};
                              userInfoDialog.selectRoleData.push(obj);
                            }

                          }

                      }
                    });
                  }
              }
            },
            methods: {

                userInfoDialogDataReset: function () {
                    //清空对象属性值
                    for (var key in userInfoDialog.userInfoDialogData) {
                        userInfoDialog.userInfoDialogData[key] = '';
                    }
                    console.log(userInfoDialog.userInfoDialogData)
                },
                showUserInfoDialog: function (userInfo) {
                    userInfoDialog.title = "查看用户详情";
                    userInfoDialog.dialogType = 1;
                    userInfoDialog.userInfoDialogData = userInfo;
                    userInfoDialog.userInfoDialogVisible = true;
                    userInfoDialog.isDisabled = true;
                },
                addUserInfoDialog: function () {
                    userInfoDialog.title = "新增用户";
                    userInfoDialog.dialogType = 0;
                    this.userInfoDialogDataReset();//清空对象属性值
                    userInfoDialog.userInfoDialogVisible = true;
                    userInfoDialog.isDisabled = false;
                },
                addUserInfoSubmit: function () {
                  // console.log(userInfoDialog.userInfoDialogData.associationRole);
                    if (userInfoDialog.dialogType === 0) {
                        //todo 调用新增用户接口
                        yufp.service1.request({
                            id: "addUserInfo",
                            data: userInfoDialog.userInfoDialogData,
                            name: "auth/user/addUserInfo",
                            callback: function (code, message, data) {
                                if (code === 0) {
                                    vm.queryUserInfoList();
                                    userInfoDialog.userInfoDialogVisible = false;
                                    alert("新增成功");
                                } else {
                                    alert("新增失败");
                                }
                            }
                        })
                    } else if (userInfoDialog.dialogType === 2) {
                        //todo 调用修改用户接口
                        yufp.service1.request({
                            id: "updateUserInfo",
                            data: userInfoDialog.userInfoDialogData,
                            name: "auth/user/updateUserInfo",
                            callback: function (code, message, data) {
                                if (code === 0) {
                                    vm.queryUserInfoList();
                                    userInfoDialog.userInfoDialogVisible = false;
                                    alert("修改成功");
                                } else {
                                    alert("修改失败");
                                }
                            }
                        })
                    }
                },
                editUserInfoDialog: function (userInfo) {
                    userInfoDialog.title = "修改用户";
                    userInfoDialog.dialogType = 2;
                    userInfoDialog.userInfoDialogVisible = true;
                    userInfoDialog.userInfoDialogData = userInfo;
                    userInfoDialog.isDisabled = false;
                },
                chooseOrg: function () {
                    //选择机构
                    chooseOrgVmInfo.chooseOrgDialogVisible = true;
                    chooseOrgVmInfo.queryOrgTreeData();
                }
            }
        });


        //********************************选择所属机构*******************************/
        var chooseOrgData = {
            orgTreeProps: {id: 'orgId', label: 'orgChiName', children: 'children'},
            chooseOrgDialogVisible: false,
            orgTreeData: [],
            orgId: '',
            orgName: '',
            filterText: ''
        };
        var chooseOrgVmInfo = yufp.custom.vue({
            el: "#el_userOrgDialog",
            data: chooseOrgData,
            computed: {},
            methods: {
                queryOrgTreeData: function () {
                    //查询机构信息 如果没数据，查询
                    if (chooseOrgData.orgTreeData.length === 0) {
                        yufp.service1.request({
                            id: "queryOrgTree",
                            data: "",
                            name: "auth/org/queryOrgTree",
                            callback: function (code, message, data) {
                                if (code === 0) {
                                    chooseOrgData.orgTreeData = JSON.parse(data.orgTreeData);
                                } else {
                                    alert("查询机构信息失败");
                                }
                            }
                        })
                    }
                },
                filterNode: function (value, data) {
                    if (!value) return true;
                    return data.label.indexOf(value) !== -1;
                },
                handleNodeClick: function (data) {
                    if (data.status !== '1') {
                        this.$alert("此机构不可选", "提示", {
                            confirmButtonText: '确定', type: 'warning'
                        });
                        return;
                    }

                    //不包含子节点
                    userInfoDialog.userInfoDialogData.orgId = data.orgId;
                    userInfoDialog.userInfoDialogData.orgChiName = data.orgChiName;
                    this.chooseOrgDialogVisible = false;

                }
            },
            watch: {
                filterText: function (val) {
                    this.$refs.orgTree.filter(val);
                },

            },

            /**
             *  界面加载成功
             */
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


});
