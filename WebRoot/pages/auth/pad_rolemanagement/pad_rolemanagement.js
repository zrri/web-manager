define(function (require, exports) {
    function isGroup(groupList,obj){
      if(obj.ISROOT=='0'){
        return true;
      }else if(obj.ISROOT=='1' && obj.PARENT_ID=='0'){
        return true;
      }
      return false;
    }




   //page加载完成后调用ready方法
   exports.ready = function (code, data, cite){




     var idArr=[];//id 数组
     var idArrTmp=[];
     var selectedCount=0;//当前选中的最大数目


     var  vmData  = {
       roleId_select:'',
       roleName_select:'',
       //所有角色的数据
       selectRoleInfos:[],
       //编辑选中的行
       selectRoleId:'',
       //多选的角色行
       multiSelectRoleRows:[],
       //分页
       pageSize:6,
       currentPage:1,
       // currentPage:'1';
       // pageSize:'6';
       // roles:[],
     };
     var vm = yufp.custom.vue({
       el:'#pad_rolemanagement',
       data:vmData,
       computed:{
         getSelectConditionData: function(){
           //把数据源赋值给DataSource
           var dataSource = vmData.selectRoleInfos;
           var pageSize=vmData.pageSize;
           var currentPage = vmData.currentPage;
           //???????????
           var offset = (currentPage-1)*pageSize;
           if(offset + pageSize >= dataSource.length){
             return dataSource.slice(offset,dataSource.length);
           }else{
             return dataSource.slice(offset,pageSize);
           }
         }
       },
       mounted:function(){
         var headers = {
             "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
             "Authorization": "Basic d2ViX2FwcDo="
         };
         this.queryPadRoleInfo();
       },
       watch:{
         roleId_select:function(){
           this.queryPadRoleInfo();
         },
         roleName_select:function(){
           this.queryPadRoleInfo();
         },
       },
       methods:{
         resetData:function(){
             idArr=[];
             selectedCount=0;//选中的数组为0
             idArrTmp=[];
         },
         //分页触发事件
         sizeChange:function(pageSize){
             // vm.resetData();
         },
         currentChange:function(pageIndex){
             vmData.currentPage=pageIndex;
             //换页清空选择的数据
         },
         prevClick:function(pageIndex){
             vmData.currentPage=pageIndex;
             // vm.resetData();
         },
         nextClick:function(pageIndex){
             vmData.currentPage=pageIndex;
             // vm.resetData();
         },
         //获取选中数据
         handleSelectionChange:function(val){
           vmData.multiSelectRoleRows=val;
         },
         //查询角色信息
         queryPadRoleInfo:function(){
           var condition={
             ROLE_ID:vmData.roleId_select,
             ROLE_NAME:vmData.roleName_select,
             SELECTTYPE:"query",
           };
           yufp.service1.request({
             id: 'queryRoleInfoList',
             name: 'auth/pad_role/queryRoleInfoList',
             data: condition,
             callback: function (code, message, data) {
                if(code===0){
                    vmData.selectRoleInfos=data.list;
                }else{
                  vm.$alert('查询失败', '提示', {
                    confirmButtonText: '确认',
                    callback: function(){
                    }
                  });
                }
             }
           });
         },
         addNewRole:function(){
           dialog_data.isAddRule=true;
           dialog_data.isShowDailog=true;
           dialog_data.titleName="新增角色";
           dialog_data.buttonName="保存";
         },
         //勾选编辑 进行批量删除角色
         multiDeleteRoles:function(){
           vm.$confirm('是否删除选中数据?', '提示', {
             confirmButtonText: '确定',
             cancelButtonText: '取消',
           }).then(function(){
             vm.deleteRoles(vmData.multiSelectRoleRows);
           });
         },
         //直接选中删除按钮删除角色
         singleDeleteRole:function(index,row){
           vm.$confirm("请选择1条记录", '', {
               confirmButtonText: '确定',
               cancelButtonText: '取消',
           }).then(function(){
             var toArr = [];
             toArr.push(row);
             vm.deleteRoles(toArr);
           });
         },
         //执行删除操作的方法
         deleteRoles:function(roles){
           var deleteRoleArr = [];
           for(var i=0;i<roles.length;i++){
             deleteRoleArr.push(roles[i].ROLE_ID);
           }
           yufp.service1.request({
             id: 'deleteRolesInfo',
             name: 'auth/pad_role/deleteRolesInfo',
             data: {'deleteRolesArr':deleteRoleArr},
             callback: function (code, message, data) {
                if(code===0){
                  vm.$alert("删除成功", '提示', {
                      confirmButtonText: '确认',
                      callback: function() {
                        vmData.multiSelectRoleRows=[];
                        vm.queryPadRoleInfo();
                      }
                  });
                }else{
                  vm.$alert("删除失败,请稍后重试", '提示', {
                      confirmButtonText: '确认',
                      callback: function() {
                      }
                  });
                }
             }
           });
         },
         //勾选编辑 进行更新角色
         selectEditRole:function(){
           if(vm.multiSelectRoleRows.length!=1){
               vm.$alert("请选择1条记录", '', {
                   confirmButtonText: '提示',
                   callback: function() {
                     return;
                   }
               });
           }
           this.showEditRoleDialog(vm.multiSelectRoleRows[0].ROLE_ID);
         },
         //直接选中更新按钮更新角色
         singleEditRole:function(index,row){
           this.showEditRoleDialog(row.ROLE_ID);
         },
         //打开更新视图
         showEditRoleDialog:function(roleId){
           dialog_data.isAddRule=false;
           dialog_data.isShowDailog=true;
           vm.selectRoleId=roleId;
           dialog_data.titleName="更新角色";
           dialog_data.buttonName="更新";
         },
         //根据ISVOID来判断该角色是否有效
         showIsVoidName:function(row,column){
           if(row.ISVOID=='1'){
             return "有效";
           }
           return "无效";
         }
       },
     });

     var dialog_data={
       isShowDailog:false,
       isAddRule:false,
       titleName:'',
       buttonName:'',
       formData:{
         ROLE_ID:'',
         ROLE_NAME:'',
         ROLE_DESCRIPTION:'',
       },
       //PAD功能树的数据
       treeData:[],
     };

     var dvm = yufp.custom.vue({
       el:'#pad_roleDialog',
       data:dialog_data,
       watch: {
         isShowDailog: function (value) {
           if (value) {
             //组装树
             setTimeout(function () {
               //请求功能树
               yufp.service1.request({
                 id: "queryPadFuncGroupTreeList",
                 data: '',
                 name: "auth/pad_role/queryPadFuncGroupTreeList",
                 callback: function (code, message, data) {
                   if (code === 0) {
                     var list = data.list;
                     var groupList = [];//{id:,label:...}
                     for (var i = 0; i < list.length; i++) {
                       var obj = list[i];
                       if (obj.ISROOT == '1' || obj.ISROOT==undefined) {//没有分组的功能，当成一个组
                         obj.isLeaf = true;//是叶子节点
                       }
                       if (isGroup(groupList, obj)) {
                         var group = {
                           id: obj.FUNC_ID,
                           label: obj.FUNC_NAME,
                           isLeaf: obj.isLeaf,
                           children: obj.isLeaf ? '' : []
                         };
                         groupList.push(group);
                       }
                     }
                     for (var i = 0; i < groupList.length; i++) {
                       var group = groupList[i];
                       if (group.isLeaf) continue;
                       for (var j = 0; j < list.length; j++) {
                         var funcObj = list[j];
                         if (funcObj.PARENT_ID == group.id) {
                           var obj = {
                             id: funcObj.FUNC_ID,
                             label: funcObj.FUNC_NAME,
                             isLeaf: true,
                           }
                           group.children.push(obj);
                         }
                       }
                     }
                     dialog_data.treeData = groupList;
                     if (dialog_data.isAddRule == false) {
                       dvm.bindEditData(vm.selectRoleId);
                     }
                   } else {
                     dvm.$alert('服务端请求失败!', '', {
                       confirmButtonText: '提示',
                       callback: function () { }
                     });

                   }
                 }
               });
               //清空
               dvm.resetDialogData();
             }, 500);
           }
         }
       },
       methods:{
         closeDialog:function(){
           //关闭试图
           dialog_data.isShowDailog=false;
         },
         bindEditData:function(roleId){
           var reqData = {
             ROLE_ID:roleId,
             SELECTTYPE:"update",
           }
           yufp.service1.request({
             id: 'queryRoleInfoList',
             name: 'auth/pad_role/queryRoleInfoList',
             data: reqData,
             callback: function (code, message, data) {
                if(code===0){
                    var checkedRoles=[];
                    var list = data.list;
                    for(var i=0;i<list.length;i++){
                        var funcid=list[i].FUNC_ID;
                        checkedRoles.push(funcid);
                    }
                    if(list.length>0) {
                        dialog_data.formData.ROLE_ID = list[0].ROLE_ID;
                        dialog_data.formData.ROLE_NAME = list[0].ROLE_NAME;
                        dialog_data.formData.ROLE_DESCRIPTION = list[0].ROLE_DESCRIPTION;
                        //设置选择哪些id
                        dvm.$refs.checkedTree.setCheckedKeys(checkedRoles);
                    }
                }else{
                  dvm.$alert('服务端请求失败!', '提示', {
                      confirmButtonText: '确定',
                      callback: function() {}
                  });
                }
             }
           });
         },

         setCheckedKeys:function(){
           this.$refs.checkedTree.setCheckedKeys([3]);
         },
         getCheckedKeys:function(){
           this.$refs.checkedTree.getCheckedKeys();
         },
         setCheckedNodes:function(){
           this.$refs.checkedTree.setCheckedNodes();
         },
         getCheckedKeys:function(){
           this.$refs.checkedTree.getCheckedNodes();
         },
         resetChecked :function(){
           if(this.$refs.checkedTree)
           this.$refs.checkedTree.setCheckedKeys([]);
         },
         resetDialogData:function(){
           this.resetChecked();
           this.$refs['roleForm'].resetFields();
         },
         checkedTreeToList:function(checkedTreeData){
           var checkedList = [];
           for(var i=0;i<checkedTreeData.length;i++){
             checkedList.push(checkedTreeData[i]);
           }
           return checkedList;
         },



         addOrUpdateSubmit:function(){
           var checkedList = this.checkedTreeToList(this.$refs.checkedTree.getCheckedKeys(true));
           if(dialog_data.isAddRule){
             var reqData={
               ROLE_ID:dialog_data.formData.ROLE_ID,
               ROLE_NAME:dialog_data.formData.ROLE_NAME,
               ROLE_DESCRIPTION:dialog_data.formData.ROLE_DESCRIPTION,
               checkedArr:checkedList,
             }
             yufp.service1.request({
               id: 'addRoleInfo',
               name: 'auth/pad_role/addRoleInfo',
               data: reqData,
               callback: function (code, message, data) {
                  if(code===0){
                    dvm.$alert('添加成功', '提示', {
                      confirmButtonText: '确认',
                      callback: function(){
                        dialog_data.isShowDailog=false;
                        vm.queryPadRoleInfo();
                      }
                    });
                  }else {
                    dvm.$alert('添加失败', '提示', {
                      confirmButtonText: '确认',
                      callback: function(){
                      }
                    });
                  }
               }
             });
           }else{
             var reqData={
               ROLE_ID:dialog_data.formData.ROLE_ID,
               ROLE_NAME:dialog_data.formData.ROLE_NAME,
               ROLE_DESCRIPTION:dialog_data.formData.ROLE_DESCRIPTION,
               checkedArr:checkedList,
             }
             yufp.service1.request({
               id: 'updateRoleInfo',
               name: 'auth/pad_role/updateRoleInfo',
               data: reqData,
               callback: function (code, message, data) {
                  if(code===0){
                    dvm.$alert('更新成功', '提示', {
                      confirmButtonText: '确认',
                      callback: function(){
                        dialog_data.isShowDailog=false;
                        vm.queryPadRoleInfo();
                      }
                    });
                  }else {
                    dvm.$alert('更新失败', '提示', {
                      confirmButtonText: '确认',
                      callback: function(){
                      }
                    });
                  }
               }
             });
           }
         }
       }


     });




   },
   //消息处理
   exports.onmessage = function (type, message, cite) {

   },
   //page销毁时触发destroy方法
   exports.destroy = function (id, cite) {

   }
});
