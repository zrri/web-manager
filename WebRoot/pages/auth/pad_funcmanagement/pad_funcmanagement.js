/**
 * Created by helin3 on 2017/11/25.
 */
define(function (require, exports) {



    //page加载完成后调用ready方法
    exports.ready = function (hashCode, data, cite) {
        var height = yufp.custom.viewSize().height;
        var  idArr=[];//id 数组
        var idArrTmp=[];
        var selectedCount=0;//当前选中的最大数目
        var funcDataAll=[];//保存初始数据


        //创建virtual model
        var data={
            filterfuncName:'',//筛选name字段
            filterfuncId:'',//筛选Id字段
            funcData:[],
            pageSize:6,//每页显示3条
            currentPage:1,//当前页，从1开始
            multipleFuncSelection:[]
        }
        var vm =  yufp.custom.vue({
            el: "#pad_funcmanagement",
            data:data ,
            computed:{
                getfuncData:function(){
                    //在这里处理分页
                    var funcData=data.funcData;
                    var pageSize =data.pageSize;
                    var currentPage=data.currentPage;
                    var offset = (currentPage - 1) * pageSize;
                    if(offset + pageSize >= funcData.length){
                        return funcData.slice(offset, funcData.length);
                    }
                    else{
                        return funcData.slice(offset, offset + pageSize);
                    }
                }
            },
            methods: {
                handleSelectionChange: function (val) {
                    //处理多选操作
                    data.multipleFuncSelection = val;

                },
                //单行删除
                deleteSigleRow:function(index, row) {
                    this.$confirm('确定要删除吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function(){
                        vm.deleteFunData([row.FUNC_ID]);
                    });
                },
                deleteFunData:function(idArr){
                    var listArr=[];
                    for(var i=0;i<idArr.length;i++){
                        var rowid=idArr[i];
                        listArr.push(rowid);
                    }
                    var funcList = {"list":listArr};
                    yufp.service1.request({
                        id:"deleteFuncList",
                        data:funcList,
                        name:"auth/padFunc/deleteFuncList",
                        callback:function (code,message,data) {
                            if (code === 0) {
                                vm.$alert("删除成功", '', {
                                    confirmButtonText: '确定',
                                    callback: function() {
                                        data.multipleFuncSelection=[];
                                        vm.queryOrgInfo();
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
                editSigleRow:function(index,row){
                    var id=row.FUNC_ID;
                    this.editMethod(id);
                },
                createfunc:function(){
                    addfuncData.isAddRole=true;
                    addfuncData.addfuncLayerVisible=true;//让 创建角色图层弹出来
                    addfuncData.titleName="创建";
                    addfuncData.buttonName="保存";
                },
                editfunc:function(){
                    if(data.multipleFuncSelection.length!=1){
                        vm.$alert("请选择1条记录", '', {
                            confirmButtonText: '确定',
                            callback: function() {}
                        });
                        return;
                    }
                    var id=data.multipleFuncSelection[0].FUNC_ID;
                    this.editMethod(id);
                },
                editMethod:function(id){
                    addfuncData.isAddRole=false;
                    addfuncData.addfuncLayerVisible=true;//让 创建角色图层弹出来

                    addfuncData.titleName="编辑";
                    addfuncData.buttonName="更新";
                    //做个延时,不知道什么原因，第一次绑定数据不显示
                    setTimeout(function(){
                        vmaddfunc.bindEditData(id);
                    },500);
                },
                //批量删除
                deletefunc:function(){
                    if(data.multipleFuncSelection.length==0){
                        vm.$alert("至少选择一项", '', {
                            confirmButtonText: '确定',
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
                           for(var i=0;i<data.multipleFuncSelection.length;i++){
                               idArr.push(data.multipleFuncSelection[i].FUNC_ID);
                           }
                            vm.deleteFunData(idArr);
                        }
                    ).catch(function(){});
                },
                resetData:function(){
                    idArr=[];
                    selectedCount=0;//选中的数组为0
                    idArrTmp=[];
                },
                getStatusName:function(row,column){
                    if(row.status=='1'){
                        return "有效";
                    }
                    else{
                        return "无效";
                    }

                },
                queryOrgInfo:function() {
                    //通过名称查询卡号
                    var reqData = {
                        "FUNC_ID":data.filterfuncId,
                        "FUNC_NAME":data.filterfuncName
                    };
                    yufp.service1.request({
                        id: "queryFuncInfoList",
                        data: reqData,
                        name: "auth/padFunc/queryGroupInfoList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                var temp=[];
                                for (var i = 0; i < data.list.length; i++) {
                                  if(data.list[i].ISROOT=='1'){
                                    temp.push(data.list[i]);
                                  }
                                }
                                vm.funcData=temp;
                            } else {
                                vm.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '确定',
                                    callback: function() {}
                                });

                            }
                        }
                    })
                },
                filterHandle:function(){
                    vm.queryOrgInfo();
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
                filterfuncName:function (value) {

                    vm.filterHandle();
                },
                filterfuncId:function (val) {

                    vm.filterHandle();
                }
            },
            mounted: function(){
                var headers = {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                    "Authorization": "Basic d2ViX2FwcDo="
                };
                this.queryOrgInfo();
            }
        });



        /*************************************  功能系列************************************/
        var validateFuncMethod=function(rule, value, callback)  {
            var FuncID='';
            var FuncName='';
            value=value.replace(/(^\s*)|(\s*$)/g, "");
            if(rule.field=='addfilterfuncId'){
                if(value==''){
                    callback(new Error('功能编号不能为空'));
                    return;
                }
                FuncID=value;
            }
            if(rule.field=='addfilterfuncName'){
                if(value==''){
                    callback(new Error('功能名称不能为空'));
                    return;
                }
                FuncName=value;
            }
            if(rule.field=='addfilterfuncRoute'){
                if(value==''){
                    callback(new Error('路由ID不能为空'));
                    return;
                }
            }
            //编辑模式不进行网络校验
            if(!vmaddfunc.isAddRole)
            {
                return  callback();
            }
            if(rule.field=='addfilterfuncId'){
              yufp.service1.request({
                  id:"isExistFuncID",
                  data:{
                      FUNC_ID:FuncID
                  },
                  name:"auth/padFunc/isExistGroupIDorName",
                  callback:function (code,message,data) {
                      if (code === 0) {
                        console.log(data)
                          if(data.count>0){
                              callback(new Error('功能编号已存在'));
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
            if(rule.field=='addfilterfuncName'){
              yufp.service1.request({
                  id:"isExistFuncName",
                  data:{
                      FUNC_NAME:FuncName
                  },
                  name:"auth/padFunc/isExistGroupIDorName",
                  callback:function (code,message,data) {
                      if (code === 0) {
                          if(data.count>0){
                              callback(new Error('功能名称已存在'));
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

        var addfuncData={

            isAddRole:true,
            titleName:'',
            buttonName:'',
            addfuncLayerVisible:false,//添加角色图层是否可见
            checkedfuncbuttonArr:[],//选中的功能数组
            funcbuttonArr: [//{ id: '1', label: '添加' },
            ],
            funcGroupOptions:[],
            data: [],
            funcForm:{
                addfilterfuncName:'',
                addfilterfuncId:'',
                addfilterfuncDes:'',
                addfilterfuncRoute:'',
                addfilterfuncIsRoot:'',
                funcGroupValue:'',
                addfilterfuncSortNo:''

            },
            rules: {
                addfilterfuncId: [
                    { validator: validateFuncMethod, trigger: 'blur' }
                ],
                addfilterfuncName: [
                    { validator: validateFuncMethod, trigger: 'blur' }
                ],
                addfilterfuncRoute:[
                    { validator: validateFuncMethod, trigger: 'blur' }
                ]

            }

        };

        var vmaddfunc= yufp.custom.vue({
            el: "#pad_funcDialog",
            data:addfuncData,
            watch:{
                addfuncLayerVisible:function(value){//页面弹出的时候执行
                    if(value){

                        //请求接口,获取操作选项

                        vmaddfunc.funcbuttonArr=[];
                        yufp.service1.request({
                            id: "queryOperatorList",
                            data: {},
                            name: "auth/user/queryOperatorList",
                            callback: function (code, message, data) {
                                if (code === 0) {
                                    var data=data.list[0];
                                    for (var key in data) {
                                        var obj={id:key,label:data[key]};
                                        vmaddfunc.funcbuttonArr.push(obj);
                                    }


                                } else {
                                    vm.$alert('服务端请求失败!', '', {
                                        confirmButtonText: '确定',
                                        callback: function() {}
                                    });

                                }
                                vmaddfunc.resetAllData();
                            }
                        });
                        vmaddfunc.queryFunGroup();
                    }
                }
            },
            methods: {
                getCanEdit:function(){
                   return !this.isAddRole;
                },
                handleCheckedFuncChange:function(value){
                    //  addfuncData.checkedfuncbuttonArr=value;//不需要赋值，两个是同步的
                },
                queryFunGroup:function(){
                    vmaddfunc.funcGroupOptions=[];
                    //请求接口，获取功能分组集合
                    yufp.service1.request({
                        id: "queryFuncGroupList",
                        data: {},
                        name: "auth/padFunc/queryGroupsFullTreeList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                var data=data.list;
                                for (var i=0;i<data.length;i++) {
                                    if(data[i].ISROOT=='0'){
                                      var obj={id:data[i]["FUNC_ID"],label:data[i]["FUNC_NAME"]};
                                      vmaddfunc.funcGroupOptions.push(obj);
                                    }
                                }
                            } else {
                                vm.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '确定',
                                    callback: function() {}
                                });

                            }
                        }
                    })
                },
                bindEditData:function(id){//从列表传过来id，用于显示编辑页面

                    //查询单条数据
                    var reqData = {
                        "FUNC_ID":id
                    };
                    yufp.service1.request({
                        id: "queryFuncInfo",
                        data: reqData,
                        name: "auth/padFunc/queryGroupsFullTreeList",
                        callback: function (code, message, data) {
                            if (code === 0) {
                               var obj=  data.list[0];

                                vmaddfunc.funcForm.addfilterfuncName=obj.FUNC_NAME;
                                vmaddfunc.funcForm.addfilterfuncId=obj.FUNC_ID;
                                vmaddfunc.funcForm.addfilterfuncDes=obj.FUNC_DESCRIPTION;
                                vmaddfunc.funcForm.addfilterfuncRoute=obj.ROUTE_ID;
                                vmaddfunc.funcForm.funcGroupValue=obj.PARENT_ID;
                                vmaddfunc.funcForm.addfilterfuncSortNo=obj.SORT_NO;
                            } else {
                                vm.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '确定',
                                    callback: function() {}
                                });

                            }
                        }
                    })
                   // vmaddfunc.queryFunGroup();
                },
                cancelAddfuncData:function(){
                    //清空数据
                    this.resetAllData();
                    addfuncData.addfuncLayerVisible=false;
                },
                addModulMethod:function(){
                    var reqData = {
                        "FUNC_ID": vmaddfunc.funcForm.addfilterfuncId,
                        "FUNC_NAME": vmaddfunc.funcForm.addfilterfuncName,
                        "FUNC_DESCRIPTION":vmaddfunc.funcForm.addfilterfuncDes,
                        "ROUTE_ID":vmaddfunc.funcForm.addfilterfuncRoute,
                        "ISROOT":'1',
                        "PARENT_ID":vmaddfunc.funcForm.funcGroupValue,
                        "FUNC_GROUP_ID":vmaddfunc.funcForm.funcGroupValue,
                        "SORT_NO":vmaddfunc.funcForm.addfilterfuncSortNo
                    };
                    yufp.service1.request({
                        id: "addFuncInfo",
                        data: reqData,
                        name: "auth/padFunc/addGroupInfo",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                if(data.count!=0) {
                                    vmaddfunc.$alert('已存在功能编号或名称，请重新输入!', '', {
                                        confirmButtonText: '确定',
                                        callback: function () {
                                        }
                                    });
                                    return;
                                }
                                vmaddfunc.$alert('添加成功!', '', {
                                    confirmButtonText: '确定',
                                    callback: function() {
                                        vmaddfunc.resetAllData();
                                        addfuncData.addfuncLayerVisible=false;
                                        //重新查询，刷新列表数据
                                        vm.queryOrgInfo();
                                    }
                                });
                            } else {
                                vmaddfunc.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '确定',
                                    callback: function() {}
                                });

                            }
                        }
                    })
                },
                editModulMethod:function(){

                    var reqData = {
                      "FUNC_ID": vmaddfunc.funcForm.addfilterfuncId,
                      "FUNC_NAME": vmaddfunc.funcForm.addfilterfuncName,
                      "FUNC_DESCRIPTION":vmaddfunc.funcForm.addfilterfuncDes,
                      "ROUTE_ID":vmaddfunc.funcForm.addfilterfuncRoute,
                      "PARENT_ID":vmaddfunc.funcForm.funcGroupValue,
                      "FUNC_GROUP_ID":vmaddfunc.funcForm.funcGroupValue,
                      "SORT_NO":vmaddfunc.funcForm.addfilterfuncSortNo
                    };
                    yufp.service1.request({
                        id: "updateFuncInfo",
                        data: reqData,
                        name: "auth/padFunc/editGroupInfo",
                        callback: function (code, message, data) {
                            if (code === 0) {
                                vmaddfunc.$alert('更新成功!', '', {
                                    confirmButtonText: '确定',
                                    callback: function() {
                                        vmaddfunc.resetAllData();
                                        addfuncData.addfuncLayerVisible=false;
                                        //重新查询，刷新列表数据
                                        vm.queryOrgInfo();
                                    }
                                });
                            } else {
                                vmaddfunc.$alert('服务端请求失败!', '', {
                                    confirmButtonText: '确定',
                                    callback: function() {}
                                });

                            }
                        }
                    })
                },
                submitAddfuncData:function(){
                    if( addfuncData.isAddRole){
                        //走添加接口
                        this.addModulMethod();
                    }
                    else{
                        //走编辑接口
                        this.editModulMethod();
                    }
                },
                resetChecked:function() {
                   addfuncData.checkedfuncbuttonArr=[];
                },
                resetAllData:function(){

                        vmaddfunc.$refs["funcForm"].resetFields();
                        addfuncData.funcForm.funcGroupValue="";
                        this.resetChecked();
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
