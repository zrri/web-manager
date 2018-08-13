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
            el: "#pad_applicationmanagement",
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
                            confirmButtonText: '确定',
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
                            confirmButtonText: '确定',
                            callback: function() {}
                        });
                        return;
                    }
                    else if(vm.currentLeftSelectedData.id){
                    this.$confirm('确定要删除这个功能组吗?', '提示', {
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
                        data:{'FUNC_ID':groupid},
                        name:"auth/padFunc/deleteGroupsAndGroupFuncs",
                        callback:function (code,message,data) {
                            if (code === 0) {
                                vm.$alert("删除成功", '', {
                                    confirmButtonText: '确定',
                                    callback: function() {
                                        vm.queryApplicationListInfo();
                                    }
                                });


                            } else {
                                vm.$alert("删除失败，请联系系统管理员", '', {
                                    confirmButtonText: '确定',
                                    callback: function() {}
                                });

                            }
                        }
                    })
                },

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
                        name: "auth/padFunc/queryGroupsFullTreeList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                var list=data.list;
                                var groupList=[];
                                var unGroupList=[];
                                for(var i=0;i<list.length;i++){
                                    var obj=list[i];
                                    if((obj.PARENT_ID==''||obj.PARENT_ID==undefined)&&obj.ISROOT=='1'){//没有分组的功能，当成一个组
                                        if(!isInArray(unGroupList,obj.FUNC_ID)){
                                            var ungroup={
                                                id:obj.FUNC_ID,
                                                label:obj.FUNC_NAME,
                                                isLeaf:true,
                                                sortNo:obj.SORT_NO,
                                                children: []
                                            };
                                            unGroupList.push(ungroup);
                                        }
                                    }
                                    //获取功能分组一级节点
                                    else if(obj.ISROOT=='0'){

                                       if(!isInArray(groupList,obj.FUNC_ID)){
                                        var group={
                                            id:obj.FUNC_ID,
                                            label:obj.FUNC_NAME,
                                            isLeaf:false,
                                            sortNo:obj.SORT_NO,
                                            children: []
                                        };
                                        groupList.push(group);
                                    }
                                    }
                                }
                                nodeSort(groupList);
                                //获取分组节点的children
                                for(var i=0;i<groupList.length;i++){
                                    var group=groupList[i];
                                    for(var j=0;j<list.length;j++){
                                        if(list[j].ISROOT=='0') continue;
                                        if(list[j].PARENT_ID==group.id){
                                            var obj={
                                                id:list[j].FUNC_ID,
                                                label:list[j].FUNC_NAME,
                                                parentId:group.id,
                                                sortNo:list[j].SORT_NO,
                                                isLeaf:true
                                            }
                                            group.children.push(obj);
                                        }
                                    }
                                    nodeSort(group.children)
                                }
                                nodeSort(unGroupList);
                                vm.dataHasGroup=groupList;
                                vm.dataNoGroup=unGroupList;


                            } else {
                                vm.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '确定',
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
                           yufp.service1.request({
                               id:"deleteFunctionFromGroup",
                               data:{
                                   FUNC_ID:Id
                               },
                               name:"auth/padFunc/deleteFunctionFromGroup",
                               callback:function (code,message,data) {
                                   if (code === 0) {
                                       vm.$alert("移除成功", '', {
                                           confirmButtonText: '确定',
                                           callback: function() {
                                               vm.currentLeftSelectedData={};
                                               vm.currentRightSelectedData={};
                                               vm.queryApplicationListInfo();
                                           }
                                       });


                                   } else {
                                       vm.$alert("移除失败，请联系系统管理员", '', {
                                           confirmButtonText: '确定',
                                           callback: function() {}
                                       });

                                   }
                               }
                           })


                       }
                    }
                    else{
                        vm.$alert("请选择一个已分组的功能", '', {
                            confirmButtonText: '确定',
                            callback: function() {}
                        });
                        return;
                    }

                },
                addToGroup:function(){
                    if(vm.currentRightSelectedData==null&&vm.currentLeftSelectedData==null){
                        vm.$alert("请选择一个功能和分组", '', {
                            confirmButtonText: '确定',
                            callback: function() {}
                        });
                        return;
                    }
                    if(vm.currentRightSelectedData==null&&vm.currentLeftSelectedData!=null)
                    {
                        vm.$alert("请选择一个未分组的功能", '', {
                            confirmButtonText: '确定',
                            callback: function() {}
                        });
                        return;
                    }
                    if(vm.currentRightSelectedData!=null&&vm.currentLeftSelectedData==null){
                        vm.$alert("请选择一个分组", '', {
                            confirmButtonText: '确定',
                            callback: function() {}
                        });
                        return;
                    }
                        var Id=vm.currentRightSelectedData.id;
                        var groupId=vm.currentLeftSelectedData.id;
                        yufp.service1.request({
                            id:"addFunctionToGroup",
                            data:{
                                FUNC_ID:Id,
                                PARENT_ID:groupId
                            },
                            name:"auth/padFunc/addFunctionToGroup",
                            callback:function (code,message,data) {
                                if (code === 0) {
                                    vm.$alert("加入成功", '', {
                                        confirmButtonText: '确定',
                                        callback: function() {
                                            vm.currentLeftSelectedData=null;
                                            vm.currentRightSelectedData=null;
                                            vm.queryApplicationListInfo();
                                        }
                                    });


                                } else {
                                    vm.$alert("加入失败，请联系系统管理员", '', {
                                        confirmButtonText: '确定',
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

        //节点排序，从小到大冒泡
        function nodeSort(arr){
          var tmp={};
          for(var i=0;i<arr.length-1;i++)
            for(var j=i+1;j<arr.length;j++){
              if(Number(arr[i].sortNo)>Number(arr[j].sortNo)){
                tmp=arr[i];
                arr[i]=arr[j];
                arr[j]=tmp;
              }
            }
        }

        // var isLoading=false;

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

            // if(isLoading)return;
            //判断是否组名和组编号是否已经存在
            // isLoading=true;
            if(rule.field=='addGroupId'){
              yufp.service1.request({
                  id:"isExistGroupIDorName",
                  data:{
                      FUNC_ID:Gid
                  },
                  name:"auth/padFunc/isExistGroupIDorName",
                  callback:function (code,message,data) {
                      // isLoading=false;
                      if (code === 0) {
                        console.log(data)
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
            }

        };
        var dataGroup={
            addorEditGroupLayer:false,
            isAddGroupRole:true,
            titleName:'',

            addeditGroupForm:{
                addGroupName:'',
                addGroupId:'',
                sortNo:''
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
            el: "#pad_applicationDialog",
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
                    vmGroup.addeditGroupForm.sortNo='';
                },
                submitAddGroupData:function(){

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
                              "FUNC_ID": vmGroup.addeditGroupForm.addGroupId,
                              "FUNC_NAME":vmGroup.addeditGroupForm.addGroupName,
                              "SORT_NO":vmGroup.addeditGroupForm.sortNo,
                              "ROUTE_ID":'0',
                              "ISROOT":'0',
                          };
                          yufp.service1.request({
                              id: "addGroupInfo",
                              data: reqData,
                              name: "auth/padFunc/addGroupInfo",
                              callback: function (code, message, data) {
                                  if (code === 0) {
                                      if(data.count!=0) {
                                          vmGroup.$alert('已存在组编号或组名，请重新输入!', '', {
                                              confirmButtonText: '确定',
                                              callback: function () {
                                              }
                                          });
                                          return;
                                      }

                                      vmGroup.$alert('添加成功!', '', {
                                          confirmButtonText: '确定',
                                          callback: function() {
                                              vmGroup.resetAllData();
                                              vmGroup.addorEditGroupLayer=false;
                                              vm.queryApplicationListInfo();
                                          }
                                      });
                                  } else {
                                      vmGroup.$alert('服务端请求失败!', '', {
                                          confirmButtonText: '确定',
                                          callback: function() {}
                                      });

                                  }
                              }
                          })
                      }
                      else{
                          var reqData = {
                            "FUNC_ID": vmGroup.addeditGroupForm.addGroupId,
                            "FUNC_NAME":vmGroup.addeditGroupForm.addGroupName,
                            "SORT_NO":vmGroup.addeditGroupForm.sortNo,
                          };
                          yufp.service1.request({
                              id: "editGroupInfo",
                              data: reqData,
                              name: "auth/padFunc/editGroupInfo",
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
                                          confirmButtonText: '确定',
                                          callback: function() {}
                                      });

                                  }
                              }
                          });

                      }
                },
                bindEditData:function(groupid){
                    var reqData = {
                        "FUNC_ID":groupid
                    };
                    yufp.service1.request({
                        id: "queryGroupInfoList",
                        data: reqData,
                        name: "auth/padFunc/queryGroupInfoList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                var obj=  data.list[0];
                                vmGroup.addeditGroupForm.addGroupName=obj.FUNC_NAME;
                                vmGroup.addeditGroupForm.addGroupId=obj.FUNC_ID;
                                vmGroup.addeditGroupForm.sortNo=obj.SORT_NO;
                            } else {
                                vm.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '确定',
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
