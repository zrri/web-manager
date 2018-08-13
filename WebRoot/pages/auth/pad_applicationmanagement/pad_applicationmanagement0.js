define(function (require, exports) {
   //page加载完成后调用ready方法
   exports.ready = function (code, data, cite){
    var select_data={

    }

    var vm = yufp.custom.vue({
      el:'#pad_applicationmanagement',
      data:select_data,
      methods:{
        addNewApplication:function(){
          dialog_data.isShowDialog=true;
          dialog_data.titleName="新增功能组";
          dialog_data.buttonName="保存";
        },
        updateApplication:function(){
          dialog_data.isShowDialog=true;
          dialog_data.titleName="更新功能组";
          dialog_data.buttonName="更新";
        },
        multApplicationsDelete:function(){
          this.$alert('是否删除该信息?', '提示', {
            confirmButtonText: '确认',
            callback: function(){

            }
          });
        },
      }

    });




    // 功能组弹出层窗口数据信息
    var dialog_data={
      isShowDialog:false,
      titleName:'',
      buttonName:'',
      formData:{
        applicationId:'',
        applicationName:'',
      },
    };

    var dvm = yufp.custom.vue({
      el:'#pad_applicationDialog',
      data:dialog_data,
      methods:{
        cancelDialog:function(){
          dialog_data.isShowDialog=false;
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
