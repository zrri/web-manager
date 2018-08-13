define(function (require, exports) {
   //page加载完成后调用ready方法
   exports.ready = function (code, data, cite){
     var select_data={
       funcId:'',
       funcName:'',
       currentPage:1,
       pageSize:6,
       table_data:[],
     };
     var vm = yufp.custom.vue({
      el:'#pad_funcmanagement',
      data:select_data,
      computed:{
        getSelectConditionData:function(){
          var dataSource = select_data.table_data;
          var currentPage = select_data.currentPage;
          var pageSize = select_data.pageSize;
          var offset= (currentPage-1)*pageSize;
          if(offset+pageSize>=dataSource.length){
            return dataSource.slice(offset,dataSource.length);
          }else{
            return dataSource.slice(offset,pageSize);
          }
        }
      },
      methods:{
        addNewFunc:function(){
          dialog_data.isShowDialog=true;
          dialog_data.titleName="新增功能";
          dialog_data.buttonName="保存";
        },
        updateFunc:function(){
          dialog_data.isShowDialog=true;
          dialog_data.titleName="更新功能";
          dialog_data.buttonName="更新";
        },
        multFuncsDelete:function(){
          this.$confirm('是否要删除选中的记录?', '提示', {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(function(){

          }).catch(function(){

          });
        },
      }
     });


     // 添加,修改功能的Dialog
     var dialog_data={
       isShowDialog:false,
       titleName:'',
       buttonName:'',
       formData:{
         funcId:'',
         funcName:'',
       }
     };
     var dvm = yufp.custom.vue({
      el:'#pad_funcDialog',
      data:dialog_data,
      methods:{
        cancelDialog:function(){
          dvm.isShowDialog=false;
        },
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
