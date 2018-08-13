define(function (require, exports) {
   //page加载完成后调用ready方法
   exports.ready = function (code, data, cite){
     var ISVOID_DATA=[
       {}
     ]


     var vmData={
       //按名称过滤机构信息
       filterName:'',
       padOrgTreeProps:{id:'ORGID',label:'ORG_CN_NAME',children:'children'},
       padOrgTreeData:[], //机构树的数据源
       padOrgTreeCurrentData: '',//选中的条目
       padOrgTreeCurrentNode: '',//选中的树形Node

       updateOrgData:{
         ORGID:'',
         ORG_CN_NAME:'',
         ORG_ENG_NAME:'',
         PARENT_ORGID:'',
         PARENT_ORG_NAME:'',
         ORG_LEVEL:'',
         ORG_TYPE:'',
         SORT_NO:'',
         CREATE_DATE:'',
         UPDATE_DATE:'',
         ISVOID:'',
       },
       //校验控制
       formRules: {
         ORGID: [
             {required: true, message: '请输入机构编号', trigger: 'blur'}
         ],
         ORG_CN_NAME: [
             {required: true, message: '请输入中文机构全称', trigger: 'blur'}
         ],
         ISVOID: [{required: true, message: '请选择机构状态', trigger: 'change'}]
       }

     };
     var vm = yufp.custom.vue({
       el:'#pad_orgmanagement',
       data:vmData,
       methods:{
         resetData:function(inputData){
           for(var key in inputData){
             inputData[key]='';
           }
         },
         /**
          * 新增机构操作
          */
         addNewOrg:function(){
           if (vmData.padOrgTreeCurrentData === '') {
             this.$alert('请选择一条记录操作', '提示', {
               confirmButtonText: '确定',
               callback:function(){
                 return;
               }
             });
           }
           dialog_data.isShowDialog = true;
           dialog_data.titleName="新增机构";
           dialog_data.buttonName="保存";
           this.resetData(dialog_data.formData);
           //上级机构赋值
           dialog_data.formData.PARENT_ORGID = vmData.padOrgTreeCurrentData.ORGID;
           dialog_data.formData.PARENT_ORG_NAME = vmData.padOrgTreeCurrentData.ORG_CN_NAME;
           //机构级别+1
           dialog_data.formData.ORG_LEVEL = Number(vmData.padOrgTreeCurrentData.ORG_LEVEL) + 1;
         },
         /**
          * 新增根机构操作
          */
         addNewRootOrg:function(){
           dialog_data.isShowDialog=true;
           this.resetData(dialog_data.formData);
           dialog_data.formData.ORG_LEVEL='1';
           dialog_data.formData.PARENT_ORGID='-1';
           dialog_data.formData.PARENT_ORG_NAME='';
           dialog_data.titleName="新增根机构";
           dialog_data.buttonName="保存";
         },

         // handleCheckChange:function(data, checked, indeterminate){
         //   if (checked) {
         //       vmData.padOrgTreeData.push(data);
         //   } else {
         //       for (var i = vmData.orgTreeCheckData.length - 1; i >= 0; i--) {
         //           if (vmData.orgTreeCheckData[i] === data) {
         //               vmData.orgTreeCheckData.splice(i, 1);
         //           }
         //       }
         //   }
         // },
         handleNodeClick:function(data, node, nodeWidget){
           vmData.padOrgTreeCurrentData = data;//添加时用到
           vmData.padOrgTreeCurrentNode = node;
           //更新时用到     替换时间格式
           // vmData.updateOrgData = aaaa;
         },

         filterNode:function (value, data) {
             if (!value) return true;
             return data.ORG_CN_NAME.indexOf(value) !== -1;
         },
         /**
          * 删除机构信息操作
          */
         deleteOrgInfoList:function(){
           if (vmData.padOrgTreeCurrentData === '') {
             this.$alert('请选择一条记录操作', '提示', {
               confirmButtonText: '确定',
               callback:function(){
                 return;
               }
             });
           }

           var str;
           if (vmData.padOrgTreeCurrentNode.data.children === undefined
               || vmData.padOrgTreeCurrentNode.data.children.length === 0) {
               str = '确定要删除' + vmData.padOrgTreeCurrentNode.data.ORG_CN_NAME + '吗?'

           } else {
               str = '确定要删除' + vmData.padOrgTreeCurrentNode.data.ORG_CN_NAME + '及其机构下的所有子机构吗?'
           }
           var padOrgTreeCheckData=[];

           flatData(padOrgTreeCheckData,vmData.padOrgTreeCurrentNode.data);
           console.log(padOrgTreeCheckData);
           this.$confirm(str, '提示', {
               confirmButtonText: '确定',
               cancelButtonText: '取消',
               type: 'warning',
           }).then(function () {
               yufp.service1.request({
                   id: "deleteOrgInfoList",
                   data: {'list': padOrgTreeCheckData},
                   name: "auth/pad_org/deleteOrgInfoList",
                   callback: function (code, message, data) {
                       if (code === 0) {
                         vm.$alert("删除成功", '提示', {
                             confirmButtonText: '确定',
                             callback: function() {
                               vm.queryPadOrgInfos();
                               vmData.padOrgTreeCurrentData = '';
                             }
                         });
                       } else {
                         vm.$alert("删除失败", '提示', {
                             confirmButtonText: '确定',
                             callback: function() {
                             }
                         });
                       }
                   }
               });
           });
         },
         /**
          * 提交更新机构信息操作
          */
         updateOrgSubmit:function(){
           var reqData = {
             ORGID:vmData.updateOrgData.ORGID,
             ORG_CN_NAME:vmData.updateOrgData.ORG_CN_NAME,
             ORG_ENG_NAME:vmData.updateOrgData.ORG_ENG_NAME,
             ORG_TYPE:vmData.updateOrgData.ORG_TYPE,
             ISVOID:vmData.updateOrgData.ISVOID,
           };
           yufp.service1.request({
             id: 'updatePadOrgInfo',
             name: 'auth/pad_org/updatePadOrgInfo',
             data: {"reqData":reqData},
             callback: function (code, message, data) {
                if(code===0){
                  vm.$alert("更新成功", '提示', {
                      confirmButtonText: '确定',
                      callback: function() {
                        vm.queryPadOrgInfos();
                      }
                  });
                }else{
                  vm.$alert("更新失败", '提示', {
                      confirmButtonText: '确定',
                      callback: function() {
                      }
                  });
                }
             }
           });


         },
         //查询机构信息并显示树状
         queryPadOrgInfos:function(){
           var reqDate={
             filterName : vmData.filterName,
           }
           yufp.service1.request({
             id: 'queryPadOrgInfos',
             name: 'auth/pad_org/queryPadOrgInfos',
             data: reqDate,
             callback: function (code, message, data) {
               if (code === 0) {
                   vmData.padOrgTreeData = JSON.parse(data.padOrgTreeData);
                   console.log(vmData.padOrgTreeData);
                   // org_tree_data.orgData = vmData.orgTreeData;//赋值，用户管理功能需要用到
               } else {
                 vm.$alert("查询失败", '提示', {
                     confirmButtonText: '确定',
                     callback: function() {
                     }
                 });
               }
             }
           });
         },
       },
       watch:{
         filterName:function(val){
           this.$refs.padOrgTree.filter(val);
         },
       },
       //页面加载成功的时候执行
       mounted: function () {
           var headers = {
               "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
               "Authorization": "Basic d2ViX2FwcDo="
           };
           this.resetData(vmData.updateOrgData);
           this.queryPadOrgInfos();
       }
     });



     // 添加,修改机构信息的Dialog
    var dialog_data={
      isShowDialog:false,
      titleName:'',
      buttonName:'',
      formData:{
        ORGID:'',
        ORG_CN_NAME:'',
        ORG_ENG_NAME:'',
        PARENT_ORGID:'',
        PARENT_ORG_NAME:'',
        ORG_LEVEL:'',
        ORG_TYPE:'',
        SORT_NO:'',
        ISVOID:'',
      },
      //校验控制
      formRules: {
        ORGID: [
            {required: true, message: '请输入机构编号', trigger: 'blur'}
        ],
        ORG_CN_NAME: [
            {required: true, message: '请输入中文机构全称', trigger: 'blur'}
        ],
        ISVOID: [{required: true, message: '请选择机构状态', trigger: 'change'}]
      }
    };

    var dvm = yufp.custom.vue({
      el:'#pad_orgDialog',
      data:dialog_data,
      methods:{
        cancelDialog:function(){
          this.isShowDialog=false;
        },
        addOrgInfoSubmit:function(){
          var reqData =dialog_data.formData;
          yufp.service1.request({
            id: 'addPadOrgInfo',
            name: 'auth/pad_org/addPadOrgInfo',
            data: reqData,
            callback: function (code, message, data) {
              if(code===0){
                vm.$alert("添加成功", '提示', {
                    confirmButtonText: '确定',
                    callback: function() {
                      dialog_data.isShowDialog=false;
                      vm.queryPadOrgInfos();
                    }
                });
              }else{
                vm.$alert("添加失败", '提示', {
                    confirmButtonText: '确定',
                    callback: function() {
                    }
                });
              }
            }
          });
        },
      },
    });



   },
   //消息处理
   exports.onmessage = function (type, message, cite) {

   },
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

});
