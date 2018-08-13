/**
 * Created by helin3 on 2017/11/25.
 */
define(function (require, exports) {

    function isInArray(arr,value){

        for(var i = 0; i < arr.length; i++){
            var v=arr[i];
            if(arr[i].ROLEID){
                v=arr[i].ROLEID;
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

    //page加载完成后调用ready方法
    exports.ready = function (hashCode, data, cite) {

        //创建virtual model
        var data={
            dataHasGroup:[],
            dataNoGroup:[]
        };
        var vm =  yufp.custom.vue({
            el: "#el_applicationmanagement",
            data:data ,
            currentLeftSelectedData:null,
            currentRightSelectedData:null,
            computed:{},
            methods: {
                //添加新的功能组
                addGroup:function(){
                    vmGroup.resetAllData();
                    vmGroup.addorEditGroupLayer=true;
                    vmGroup.titleName="添加";
                    vmGroup.isAddGroupRole=true;
                },
                editGroup: function () {
                    if(vm.currentLeftSelectedData==null||vm.currentLeftSelectedData.isLeaf){
                        vm.$alert("请选择一个功能组", '', {
                            confirmButtonText: '提示',
                            callback: function() {}
                        });
                        return;
                    }
                    else if(vm.currentLeftSelectedData.id){
                    vmGroup.resetAllData();
                    vmGroup.addorEditGroupLayer=true;
                    vmGroup.titleName="编辑";
                    vmGroup.isAddGroupRole=false;
                    setTimeout(function(){
                        vmGroup.bindEditData(vm.currentLeftSelectedData.id);
                    },500);
                    }
                },
                removeGroup:function(){
                    if(vm.currentLeftSelectedData==null||vm.currentLeftSelectedData.isLeaf){
                        vm.$alert("请选择一个功能组", '', {
                            confirmButtonText: '提示',
                            callback: function() {}
                        });
                        return;
                    }
                    else if(vm.currentLeftSelectedData.id){
                    this.$confirm('确定要删除这个功能组吗?分组下的功能也会被删除', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function(){
                        vm.deleteGroupAndGroupFuncs(vm.currentLeftSelectedData.id)
                    });
                    }
                },
                deleteGroupAndGroupFuncs:function(groupid){
                    yufp.service1.request({
                        id:"deleteGroupsAndGroupFuncs",
                        data:{'GROUPID':groupid},
                        name:"auth/user/deleteGroupsAndGroupFuncs",
                        callback:function (code,message,data) {
                            if (code === 0) {
                                vm.$alert("删除成功", '', {
                                    confirmButtonText: '提示',
                                    callback: function() {
                                        vm.queryApplicationListInfo();
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

                ////添加新的功能
                //addFunction:function(){
                //    var nodeArr=this.$refs.left_tree
                //  if(vm.currentLeftSelectedData.id){//选中了一个节点
                //      var groupId=vm.currentLeftSelectedData.parentGroup;
                //      //弹出添加功能的layer
                //  }
                //
                //},
                leftTreeNodeClick:function(data,node,tree){//点击node时候触发
                    vm.currentLeftSelectedData=data;
                },
                rightTreeNodeClick:function(data,node,tree){//点击node时候触发
                    vm.currentRightSelectedData=data;
                },
                queryApplicationListInfo:function() {
                    yufp.service1.request({
                        id: "queryGroupsFullTreeList",
                        data: {},
                        name: "auth/user/queryGroupsFullTreeList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                var list=data.list;
                                var groupList=[];
                                var unGroupList=[];
                                for(var i=0;i<list.length;i++){
                                    var obj=list[i];
                                    if((obj.GROUP_ID==''||obj.GROUP_ID==undefined)&&obj.FUNC_ID){//没有分组的功能，当成一个组
                                        obj.GROUP_ID=obj.FUNC_ID;
                                        obj.GROUP_NAME=obj.FUNC_NAME;
                                        obj.isLeaf=true;//是叶子节点
                                        if(!isInArray(unGroupList,obj.GROUP_ID)){
                                            var ungroup={
                                                id:obj.GROUP_ID,
                                                label:obj.GROUP_NAME,
                                                isLeaf:obj.isLeaf,
                                                parentGroup:null,
                                                children: []
                                            };
                                            unGroupList.push(ungroup);
                                        }
                                    }

                                    else if(obj.GROUP_ID&&obj.GROUP_ID.length){

                                       if(!isInArray(groupList,obj.GROUP_ID)){
                                        var group={
                                            id:obj.GROUP_ID,
                                            label:obj.GROUP_NAME,
                                            isLeaf:false,
                                            parentGroup:obj.GROUP_ID,
                                            children: []
                                        };
                                        groupList.push(group);
                                    }
                                    }
                                }
                                //筛选分组数据的children
                                for(var i=0;i<groupList.length;i++){
                                    var group=groupList[i];
                                   // if(group.isLeaf)continue;
                                    for(var j=0;j<list.length;j++){
                                        var funcObj=list[j];
                                        if(funcObj.GROUP_ID==group.id&&funcObj.FUNC_ID){
                                            var obj={
                                                id:funcObj.FUNC_ID,
                                                label:funcObj.FUNC_NAME,
                                                parentGroup:group.id,
                                                isLeaf:true
                                            }
                                            group.children.push(obj);
                                        }
                                    }
                                }

                                vm.dataHasGroup=groupList;
                                vm.dataNoGroup=unGroupList;


                            } else {
                                vm.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '提示',
                                    callback: function() {}
                                });

                            }
                        }
                    });
                },
                removeFromGroup:function(){
                    if(vm.currentLeftSelectedData.isLeaf){
                       if(vm.currentLeftSelectedData.id){
                           var Id=vm.currentLeftSelectedData.id;
                           var groupId=vm.currentLeftSelectedData.parentGroup
                           yufp.service1.request({
                               id:"deleteFunctionFromGroup",
                               data:{
                                   FUNCID:Id,
                                   GROUPID:groupId
                               },
                               name:"auth/user/deleteFunctionFromGroup",
                               callback:function (code,message,data) {
                                   if (code === 0) {
                                       vm.$alert("移除分组成功", '', {
                                           confirmButtonText: '提示',
                                           callback: function() {
                                               vm.currentLeftSelectedData={};
                                               vm.currentRightSelectedData={};
                                               vm.queryApplicationListInfo();
                                           }
                                       });


                                   } else {
                                       vm.$alert("移除分组失败，请稍后重试", '', {
                                           confirmButtonText: '提示',
                                           callback: function() {}
                                       });

                                   }
                               }
                           })


                       }
                    }
                    else{
                        vm.$alert("请选择一个已分组的功能", '', {
                            confirmButtonText: '提示',
                            callback: function() {}
                        });
                        return;
                    }

                },
                addToGroup:function(){
                    if(vm.currentRightSelectedData==null||vm.currentLeftSelectedData==null){
                        vm.$alert("请选择一个功能和分组", '', {
                            confirmButtonText: '确定',
                            callback: function() {}
                        });
                        return;
                    }
                    if(vm.currentRightSelectedData.isLeaf==undefined||vm.currentRightSelectedData.isLeaf=="")
                    {
                        vm.$alert("请选择一个未分组的功能", '', {
                            confirmButtonText: '确定',
                            callback: function() {}
                        });
                        return;
                    }
                    if(vm.currentLeftSelectedData.isLeaf){
                        vm.$alert("请选择一个分组", '', {
                            confirmButtonText: '确定',
                            callback: function() {}
                        });
                        return;
                    }
                        var Id=vm.currentRightSelectedData.id;
                        var groupId=vm.currentLeftSelectedData.parentGroup;
                        yufp.service1.request({
                            id:"addFunctionToGroup",
                            data:{
                                FUNCID:Id,
                                GROUPID:groupId
                            },
                            name:"auth/user/addFunctionToGroup",
                            callback:function (code,message,data) {
                                if (code === 0) {
                                    vm.$alert("添加分组成功", '', {
                                        confirmButtonText: '提示',
                                        callback: function() {
                                            vm.currentLeftSelectedData=null;
                                            vm.currentRightSelectedData=null;
                                            vm.queryApplicationListInfo();
                                        }
                                    });


                                } else {
                                    vm.$alert("添加分组失败，请稍后重试", '', {
                                        confirmButtonText: '提示',
                                        callback: function() {}
                                    });

                                }
                            }
                        })




                }
            },
            watch: {},
            mounted: function(){
                var headers = {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                    "Authorization": "Basic d2ViX2FwcDo="
                };
                this.queryApplicationListInfo();
            }
        });
        var isLoading=false;

        var validateIdAndNameMethod=function(rule, value, callback)  {

            var Gid='';
            var Gname='';
            var idPass=false;
            var namePass=false;
            value=value.replace(/(^\s*)|(\s*$)/g, "");
            if(rule.field=='addGroupId'){
                if(value==''){
                    callback(new Error('组编号不能为空'));
                    return;
                }
                Gid=value;
            }
            if(rule.field=='addGroupName'){
                if(value==''){
                    callback(new Error('组名称不能为空'));
                    return;
                }
                Gname=value;
            }

            //编辑模式不进行网络校验
            if(!vmGroup.isAddGroupRole)
            {
                return  callback();
            }

            if(isLoading)return;
            //判断是否组名和组编号是否已经存在
            isLoading=true;
            yufp.service1.request({
                id:"isExistGroupIDorName",
                data:{
                   // GROUPNAME:Gname,
                    GROUPNAME:"",//组名暂时不验证唯一性
                    GROUPID:Gid
                },
                name:"auth/user/isExistGroupIDorName",
                callback:function (code,message,data) {
                    isLoading=false;
                    if (code === 0) {
                      if(data.count>0){
                          callback(new Error('组编号已存在'));
                      }
                      else{
                          callback();
                      }
                    } else {
                        callback();
                    }
                }
            });

        };
        var dataGroup={
            addorEditGroupLayer:false,
            isAddGroupRole:true,
            titleName:'添加',

            addeditGroupForm:{
                addGroupName:'',
                addGroupId:'',
                addGroupDescription:''
            },
            rules: {
                addGroupId: [
                    { validator: validateIdAndNameMethod, trigger: 'blur' }
                   // {required: true, message: '组编号不能为空', trigger: 'blur'},
                ],
                addGroupName: [
                    { validator: validateIdAndNameMethod, trigger: 'blur' }
                   // {required: true, message: '组名称不能为空', trigger: 'blur'}
                ]

            }
        };
        /* 添加和编辑 功能组图层*/
        var vmGroup =  yufp.custom.vue({
            el: "#el_addorEditGroupLayer",
            data:dataGroup ,
            computed:{},
            watch:{},
            methods: {
                cancelAddGroupData:function(){
                    vmGroup.addorEditGroupLayer=false;
                    this.resetAllData();
                },
                resetAllData:function(){
                    vmGroup.addeditGroupForm.addGroupName='';
                    vmGroup.addeditGroupForm.addGroupId='';
                    vmGroup.addeditGroupForm.addGroupDescription='';
                },
                submitAddGroupData:function(){

                    //vmGroup.$refs["addeditGroupForm"].validate(function(valid) {
                    //    if (valid) {
                           if(vmGroup.addeditGroupForm.addGroupName.length==0)
                           {
                               vmGroup.$alert('组名称不能为空!', '', {
                                   confirmButtonText: '确定',
                                   callback: function() { }
                               });
                               return;
                           }

                          if(vmGroup.addeditGroupForm.addGroupId.length==0){
                            vmGroup.$alert('组编号不能为空!', '', {
                              confirmButtonText: '确定',
                              callback: function() { }
                             });
                              return;
                            }

                            if( vmGroup.isAddGroupRole){
                                var reqData = {
                                    "GROUPID": vmGroup.addeditGroupForm.addGroupId,
                                    "GROUPNAME":vmGroup.addeditGroupForm.addGroupName,
                                    "GROUPDESCRIBTION":vmGroup.addeditGroupForm.addGroupDescription,
                                    "STATUS":'1'
                                };
                                yufp.service1.request({
                                    id: "addGroupInfo",
                                    data: reqData,
                                    name: "auth/user/addGroupInfo",
                                    callback: function (code, message, data) {
                                        if (code === 0) {
                                            if(data.count!=0) {
                                                vmGroup.$alert('请输入正确的字段!', '', {
                                                    confirmButtonText: '确定',
                                                    callback: function () {
                                                    }
                                                });
                                                return;
                                            }


                                            vmGroup.$alert('插入成功!', '', {
                                                confirmButtonText: '确定',
                                                callback: function() {
                                                    vmGroup.resetAllData();
                                                    vmGroup.addorEditGroupLayer=false;
                                                    vm.queryApplicationListInfo();
                                                }
                                            });
                                        } else {
                                            vmGroup.$alert('服务端请求失败!', '', {
                                                confirmButtonText: '提示',
                                                callback: function() {}
                                            });

                                        }
                                    }
                                })
                            }
                            else{
                                var reqData = {
                                    "GROUPID": vmGroup.addeditGroupForm.addGroupId,
                                    "GROUPNAME":vmGroup.addeditGroupForm.addGroupName,
                                    "GROUPDESCRIBTION":vmGroup.addeditGroupForm.addGroupDescription,
                                    "STATUS":'1'
                                };
                                yufp.service1.request({
                                    id: "editGroupInfo",
                                    data: reqData,
                                    name: "auth/user/editGroupInfo",
                                    callback: function (code, message, data) {
                                        if (code === 0) {
                                            vmGroup.$alert('更新成功!', '', {
                                                confirmButtonText: '确定',
                                                callback: function() {
                                                    vmGroup.resetAllData();
                                                    vmGroup.addorEditGroupLayer=false;
                                                    vm.queryApplicationListInfo();
                                                }
                                            });
                                        } else {
                                            vmGroup.$alert('服务端请求失败!', '', {
                                                confirmButtonText: '提示',
                                                callback: function() {}
                                            });

                                        }
                                    }
                                });


                            }

                //    } else {
                //            return false;
                //    }
                //});
                },
                bindEditData:function(groupid){
                    var reqData = {
                        "GROUPID":groupid
                    };
                    yufp.service1.request({
                        id: "queryGroupInfoList",
                        data: reqData,
                        name: "auth/user/queryGroupInfoList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                var obj=  data.list[0];

                                vmGroup.addeditGroupForm.addGroupName=obj.GROUP_NAME;
                                vmGroup.addeditGroupForm.addGroupId=obj.GROUP_ID;
                                vmGroup.addeditGroupForm.addGroupDescription=obj.GROUP_DESCRIBTION;
                            } else {
                                vm.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '提示',
                                    callback: function() {}
                                });

                            }
                        }
                    })
                }
            },

            mounted: function(){
                var headers = {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                    "Authorization": "Basic d2ViX2FwcDo="
                };

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
