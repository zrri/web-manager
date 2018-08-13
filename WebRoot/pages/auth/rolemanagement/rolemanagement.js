/**
 * Created by helin3 on 2017/11/25.
 */
define(function (require, exports) {

    function isInArray(arr,value){

        for(var i = 0; i < arr.length; i++){
            var v=arr[i];
            if(arr[i].ROLE_ID){
                v=arr[i].ROLE_ID;
            }
            else if(arr[i].id){
                v=arr[i].id;
            }
            if(value === v){
                return true;
            }
        }
        return false;
    }
    function removeArray(arr,value)
    {
        var curIndex=-1;
        for (var i = 0; i < arr.length; i++) {
            var v=arr[i];
            if(arr[i].id){
                v=arr[i].id;
            }
            if (v == value)
            {
                curIndex=i;
                break;
            }
        }
        arr.splice(curIndex,1);

    }

    //page加载完成后调用ready方法
    exports.ready = function (hashCode, data, cite) {

        var height = yufp.custom.viewSize().height;



        var roleDataAll=[];//保存初始数据

        // var roleStatusOption = [
        //   {label:'有效',value:'1'},
        //   {label:'无效',value:'0'}]

        //创建virtual model
        var data={
            filterRoleName:'',//筛选name字段
            filterRoleId:'',//筛选Id字段
            // filterRoleStatus:'',//筛选status字段
            // filterRoleStatusOptions:roleStatusOption,
            roleData:[],
            pageSize:6,//每页显示3条
            currentPage:1,//当前页，从1开始
            multipleRoleSelection:[],
            editID:''
        }
        var vm =  yufp.custom.vue({
            el: "#el_rolemanagement",
            data:data ,
            computed:{
                getRoleData:function(){
                    //在这里处理分页
                    var roleData=data.roleData;
                    var pageSize =data.pageSize;
                    var currentPage=data.currentPage;
                    var offset = (currentPage - 1) * pageSize;
                    if(offset + pageSize >= roleData.length){
                        return roleData.slice(offset, roleData.length);
                    }
                    else{
                        return roleData.slice(offset, offset + pageSize);
                    }
                }
            },
            methods: {
                handleSelectionChange: function (val) {
                    //处理多选操作
                    data.multipleRoleSelection = val;

                },
                //单行删除
                deleteSigleRow:function(index, row) {
                    this.$confirm('确定要删除吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function(){
                        var rowid=row.ROLE_ID;
                        vm.deleteRoleData([rowid]);


                    });
                },
                editSigleRow:function(index,row){
                    this.editMethod(row.ROLE_ID);
                },
                createrole:function(){
                    addRoleData.isAddRole=true;
                    addRoleData.addroleLayerVisible=true;//让 创建角色图层弹出来
                    addRoleData.titleName="创建角色";
                },
                editrole:function(){
                    if(vm.multipleRoleSelection.length!=1){
                        this.$alert("请选择1条记录", '', {
                            confirmButtonText: '提示',
                            callback: function() {}
                        });
                        return;
                    }
                    this.editMethod(vm.multipleRoleSelection[0].ROLE_ID);
                },
                editMethod:function(id2){
                    addRoleData.isAddRole=false;
                    addRoleData.addroleLayerVisible=true;//让 创建角色图层弹出来
                    vm.editID=id2;
                    addRoleData.titleName="编辑角色";

                    //做个延时,不知道什么原因，第一次树绑定数据不显示
                    // setTimeout(function(){
                    //     vmaddRole.bindEditData(id2);
                    // },500);

                },
                //批量删除
                deleterole:function(){
                    if(vm.multipleRoleSelection.length==0){
                        this.$alert("至少选择一项", '', {
                            confirmButtonText: '提示',
                            callback: function() {}
                        });
                        return;
                    }
                    this.$confirm('确定要删除吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function(){
                            //开始删除
                            var idArr=[];
                            for(var i=0;i<data.multipleRoleSelection.length;i++){
                                idArr.push(data.multipleRoleSelection[i].ROLE_ID);
                            }
                            vm.deleteRoleData(idArr);
                        }
                    ).catch(function(){});
                },
                resetData:function(){


                },
                deleteRoleData:function(idArr){
                    var listArr=[];
                    for(var i=0;i<idArr.length;i++){
                        var rowid=idArr[i];
                        listArr.push(rowid);
                    }
                    var userInfoList = {"list":listArr};
                    yufp.service1.request({
                        id:"deleteRoleInfoList",
                        data:userInfoList,
                        name:"auth/user/deleteRoleInfoList",
                        callback:function (code,message,data) {
                            if (code === 0) {
                                vm.$alert("删除成功", '', {
                                    confirmButtonText: '提示',
                                    callback: function() {
                                        data.multipleRoleSelection=[];
                                        vm.queryRoleInfo();
                                    }
                                });


                            } else {
                                vm.$alert("删除失败，请稍后重试", '', {
                                    confirmButtonText: '提示',
                                    callback: function() {}
                                });

                            }
                        }
                    })
                },

                getStatusName:function(row,column){
                    if(row.STATUS=='1'){
                        return "有效";
                    }
                    else{
                        return "无效";
                    }

                },
                queryRoleInfo:function() {
                    //通过名称查询卡号
                    var reqData = {
                        "ROLEID":data.filterRoleId,
                        "ROLENAME":data.filterRoleName,
                        "STATUS":data.filterRoleStatus
                    };
                    yufp.service1.request({
                        id: "queryRoleInfoList",
                        data: reqData,
                        name: "auth/user/queryRoleInfoList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                var filterArr=[];
                                var list= data.list;
                                for(var i=0;i<list.length;i++){
                                     if(!isInArray(filterArr,list[i].ROLE_ID))
                                        filterArr.push(list[i]);
                                }
                                vm.roleData= filterArr;
                            } else {
                                this.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '提示',
                                    callback: function() {}
                                });

                            }
                        }
                    })



                },
                filterHandle:function(){
                    vm.queryRoleInfo();
                },
                //分页触发事件
                sizeChange:function(pageSize){
                    vm.resetData();
                },
                currentChange:function(pageIndex){
                    data.currentPage=pageIndex;
                    //换页清空选择的数据
                    vm.resetData();
                },
                prevClick:function(pageIndex){
                    data.currentPage=pageIndex;
                    vm.resetData();
                },
                nextClick:function(pageIndex){
                    data.currentPage=pageIndex;
                    vm.resetData();
                }
            },
            watch: {
                filterRoleName:function (value) {

                    vm.filterHandle();
                },
                filterRoleId:function (val) {

                    vm.filterHandle();
                }
            },
            mounted: function(){
                var headers = {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                    "Authorization": "Basic d2ViX2FwcDo="
                };
                this.queryRoleInfo();
            }
        });



        /*************************************添加角色系列************************************/

        var validateRoleMethod=function(rule, value, callback)  {
            var RoleID='';
            value=value.replace(/(^\s*)|(\s*$)/g, "");
            if(rule.field=='addfilterRoleId'){
                if(value==''){
                    callback(new Error('角色编号不能为空'));
                    return;
                }
                RoleID=value;
            }
            if(rule.field=='addfilterRoleName'){
                if(value==''){
                    callback(new Error('角色名称不能为空'));
                    return;
                }
            }

             //编辑模式不进行网络校验
            if(!vmaddRole.isAddRole)
            {
                return  callback();
            }
            if(rule.field=='addfilterRoleId'){
                yufp.service1.request({
                    id:"isExistRoleID",
                    data:{
                        ROLEID:RoleID
                    },
                    name:"auth/user/isExistRoleID",
                    callback:function (code,message,data) {
                        if (code === 0) {
                            if(data.count>0){
                                callback(new Error('角色编号已存在'));
                            }
                            else{
                                callback();
                            }
                        } else {
                            callback();
                        }
                    }
                });
            }else{
                return  callback();
            }
        };
        var addRoleData={
            isAddRole:true,
            titleName:'',
            addroleLayerVisible:false,//添加角色图层是否可见
            funcbuttonArr:[],
            data: [],//树的数据
            defaultProps: {
                children: 'children',
                label: 'label'
            },
            roleForm:{
                addfilterRoleDescibtion:'',
                addfilterRoleId:'',
                addfilterRoleName:''
            },
            rules: {
                addfilterRoleId: [
                    { validator: validateRoleMethod, trigger: 'blur' }
                ],
                addfilterRoleName: [
                    { validator: validateRoleMethod, trigger: 'blur' }
                ]
            }


        };
        var vmaddRole= yufp.custom.vue({
            el: "#el_addorEditrole",
            data:addRoleData,
            watch:{
                addroleLayerVisible:function(value){//页面弹出的时候执行
                    if(value){
                       //组装树
                        setTimeout(function(){
                            //请求操作列表
                            // yufp.service1.request({
                            //     id: "queryOperatorList",
                            //     data: {},
                            //     name: "auth/user/queryOperatorList",
                            //     callback: function (code, message, data) {
                            //         if (code === 0) {
                            //             vmaddRole.funcbuttonArr=[];
                            //             var data=data.list[0];
                            //             for (var key in data) {
                            //                 var obj={id:key,label:data[key]};
                            //                 vmaddRole.funcbuttonArr.push(obj);
                            //             }

                                        //请求功能树
                                        yufp.service1.request({
                                            id: "queryFuncGroupTreeList",
                                            data: {},
                                            name: "auth/user/queryFuncGroupTreeList",
                                            callback: function (code, message, data) {
                                                if (code === 0) {
                                                    var list=data.list;
                                                    var groupList=[];//{id:,label:...}
                                                    for(var i=0;i<list.length;i++){
                                                        var obj=list[i];
                                                        if(obj.GROUP_ID==''||obj.GROUP_ID==undefined){//没有分组的功能，当成一个组
                                                            obj.GROUP_ID=obj.FUNC_ID;
                                                            obj.GROUP_NAME=obj.FUNC_NAME;
                                                            obj.isLeaf=true;//是叶子节点
                                                        }
                                                        if(!isInArray(groupList,obj.GROUP_ID)){
                                                            var group={
                                                                id:obj.GROUP_ID,
                                                                label:obj.GROUP_NAME,
                                                                isLeaf:obj.isLeaf,
                                                                children: obj.isLeaf?
                                                                    // vmaddRole.combineFuncAndSub(obj.GROUP_ID,vmaddRole.funcbuttonArr)
                                                                    '':[]
                                                            };
                                                            groupList.push(group);
                                                        }
                                                    }
                                                    for(var i=0;i<groupList.length;i++){
                                                        var group=groupList[i];
                                                        if(group.isLeaf)continue;
                                                        for(var j=0;j<list.length;j++){
                                                            var funcObj=list[j];
                                                            if(funcObj.GROUP_ID==group.id){
                                                                var obj={
                                                                    id:funcObj.FUNC_ID,
                                                                    label:funcObj.FUNC_NAME,
                                                                    isLeaf:true,
                                                                    // children:vmaddRole.combineFuncAndSub(funcObj.FUNC_ID,vmaddRole.funcbuttonArr)
                                                                }
                                                                group.children.push(obj);
                                                            }
                                                        }
                                                    }
                                                    vmaddRole.data=groupList;
                                                    if(addRoleData.isAddRole==false){
                                                        vmaddRole.bindEditData(vm.editID);
                                                    }
                                                } else {
                                                    this.$alert('服务端请求失败!', '', {
                                                        confirmButtonText: '提示',
                                                        callback: function() {}
                                                    });

                                                }
                                            }
                                        });
                                        //清空
                                        vmaddRole.resetAllData();

                                    // } else {
                                    //     vm.$alert('服务端请求失败!', '', {
                                    //         confirmButtonText: '提示',
                                    //         callback: function() {}
                                    //     });
                                    //
                                    // }

                                // }
                            // });





                        },500);
                    }
                }
            },
            methods: {
                combineFuncAndSub:function(funcId,operatorArr){
                    var newObjArr=[];
                   for(var i=0;i<operatorArr.length;i++){
                       var id=funcId+'-'+operatorArr[i].id;
                       var label=operatorArr[i].label;
                       newObjArr.push({id:id,label:label});
                   }
                    return newObjArr;
                },
                bindEditData:function(id){//从列表传过来id，用于显示编辑页面
                    //应该从服务端查询，暂时假数据
                    var reqData = {
                        "ROLEID":id
                    };
                    yufp.service1.request({
                        id: "queryRoleInfoList",
                        data: reqData,
                        name: "auth/user/queryRoleInfoList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                vmaddRole.resetAllData();
                                var checkedArr=[];
                                var list= data.list;
                                for(var i=0;i<list.length;i++){
                                    var funcid=list[i].FUNC_ID;
                                    // var operator=list[i].OPERATOR;
                                    // if(funcid) {
                                    //     var funcid = operator.split(',');
                                    //     if (opeArr) {
                                    //         for (var j = 0; j < opeArr.length; j++) {
                                    //             checkedArr.push(funcid + '-' + opeArr[j]);
                                    //         }
                                    //     }
                                    // }
                                    checkedArr.push(funcid);
                                }
                                if(list.length>0) {
                                    var roleCode = list[0].ROLE_ID;
                                    var roleName = list[0].ROLE_NAME;
                                    var roleDesc=list[0].ROLE_DESCRIBTION;
                                    vmaddRole.roleForm.addfilterRoleName = roleName;
                                    vmaddRole.roleForm.addfilterRoleId = roleCode;
                                    vmaddRole.roleForm.addfilterRoleDescibtion=roleDesc;
                                    //设置选择哪些id
                                    vmaddRole.$refs.tree.setCheckedKeys(checkedArr);
                                }
                            } else {
                                vmaddRole.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '提示',
                                    callback: function() {}
                                });

                            }
                        }
                    })
                },
                // searchLeafNode:function(containerArr,arr){
                //     for(var i=0;i<arr.length;i++){
                //         var obj=arr[i];
                //         if(obj.isLeaf){
                //             containerArr.push(obj.id);
                //         }
                //         else{
                //          this.searchLeafNode(containerArr,obj.children);
                //         }
                //     }
                // },
                // ChangeChecked:function(){
                    //var curData=vmaddRole.data;
                    //var funcIDArr= this.$refs.tree.getCheckedKeys(true);
                    //var totalLeafIDArr=[];
                    //this.searchLeafNode(totalLeafIDArr,curData);
                    //for(var i=0;i<funcIDArr.length;i++){
                    //    var id=funcIDArr[i];
                    //    removeArray(totalLeafIDArr,id);
                    //}
                    //this.$refs.tree.setCheckedKeys(totalLeafIDArr,true);
                // },
                // deleteSeqNodeByID: function (arr,id) {
                //     for(var i=0;i<arr.length;i++){
                //
                //     }
                // },
                getCheckedNodes:function() {
                    var fff=this.$refs.tree.getCheckedNodes();
                },
                getCheckedKeys:function() {
                    var fsffs=this.$refs.tree.getCheckedKeys();
                },
                setCheckedNodes:function() {
                    this.$refs.tree.setCheckedNodes([{
                       id: 5,
                       label: '二级 2-1'
                    }, {
                       id: 9,
                       label: '三级 1-1-1'
                    }]);
                },
                setCheckedKeys:function() {
                    this.$refs.tree.setCheckedKeys([3]);
                },
                resetChecked:function() {
                    if(this.$refs.tree)
                    this.$refs.tree.setCheckedKeys([]);
                },
                cancelAddRoleData:function(){
                    //清空数据
                    this.resetAllData();
                    addRoleData.addroleLayerVisible=false;
                },
                handleFuncTreeData:function(arr){//["00333-1","00333-2","0034-1"]
                    var funcArr=[];
                    for(var i=0;i<arr.length;i++){
                          // var strArr=arr[i].split("-");
                          var funcId=arr[i];
                          // var operaId=strArr[1];
                          // if(!isInArray(funcArr,funcId)){
                          //     var obj={id:funcId,operator:[]};
                          //     funcArr.push(obj);
                          //     obj.operator.push(operaId);
                          // }
                          // else{
                          //     var curObj;
                          //     for(var j=0;j<funcArr.length;j++){
                          //         var obj=funcArr[j];
                          //         if(obj.id===funcId){
                          //             curObj=obj;
                          //             break;
                          //         }
                          //     }
                          //     if(!isInArray(curObj.operator,operaId)){
                          //         curObj.operator.push(operaId);
                          //     }
                          //
                          // }
                          funcArr.push(funcId);
                    }
                    return funcArr;
                }

                ,
                submitAddRoleData:function(){
                    var roleName = vmaddRole.roleForm.addfilterRoleName;
                    var roleCode= vmaddRole.roleForm.addfilterRoleId;
                    var roleDesc=vmaddRole.roleForm.addfilterRoleDescibtion;
                    var funArr=this.$refs.tree.getCheckedKeys(true);

                    funArr=this.handleFuncTreeData(funArr);


                    if( addRoleData.isAddRole){
                        //走添加接口
                        var reqData = {
                            "ROLEID": roleCode,
                            "ROLENAME": roleName,
                            "ROLEDESCRIBTION":roleDesc,
                            "FUNCIDARR":funArr
                        };
                        yufp.service1.request({
                            id: "addRoleInfo",
                            data: reqData,
                            name: "auth/user/addRoleInfo",
                            callback: function (code, message, data) {
                                if (code === 0) {
                                    if(data.count!=0) {
                                        vmaddRole.$alert('请输入正确的字段!', '', {
                                            confirmButtonText: '确定',
                                            callback: function () {
                                            }
                                        });
                                        return;
                                    }

                                    vmaddRole.$alert('插入成功!', '', {
                                        confirmButtonText: '确定',
                                        callback: function() {
                                            vmaddRole.resetAllData();
                                            vmaddRole.addroleLayerVisible=false;
                                            //重新查询，刷新列表数据
                                            vm.queryRoleInfo();
                                        }
                                    });
                                } else {
                                    vmaddRole.$alert('服务端请求失败!', '', {
                                        confirmButtonText: '提示',
                                        callback: function() {}
                                    });

                                }
                            }
                        })
                    }
                    else{
                        //走编辑接口
                        var reqData = {
                            "ROLEID": roleCode,
                            "ROLENAME": roleName,
                            "ROLEDESCRIBTION":roleDesc,
                            "FUNCIDARR":funArr,
                            "STATUS":'1'
                        };
                        yufp.service1.request({
                            id: "editRoleInfo",
                            data: reqData,
                            name: "auth/user/editRoleInfo",
                            callback: function (code, message, data) {
                                if (code === 0) {
                                    vmaddRole.$alert('更新成功!', '', {
                                        confirmButtonText: '确定',
                                        callback: function() {
                                            vmaddRole.resetAllData();
                                            vmaddRole.addroleLayerVisible=false;
                                            //重新查询，刷新列表数据
                                            vm.queryRoleInfo();
                                        }
                                    });
                                } else {
                                    vmaddRole.$alert('服务端请求失败!', '', {
                                        confirmButtonText: '提示',
                                        callback: function() {}
                                    });

                                }
                            }
                        })
                    }
                },
                resetAllData:function(){
                    this.resetChecked();
                    this.$refs['roleForm'].resetFields();
                }
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
