define(function (require, exports) {
  //page加载完成后调用ready方法
  exports.ready = function (code, data, cite) {

    var vmData = {
      userInfoSearch: {
        TELLER_ID: '',
        TELLER_NAME: '',
        ORG_CN_NAME: '',
        TELLER_TYPE: '',
        TELLER_STATUS: '',
        TELLER_LOGIN_TYPE: '',
      },
      //状态数据转化
      teller_Types: [
        { id: '', label: '所有' },
        { id: 1, label: '真实柜员' },
        { id: 2, label: '虚拟柜员' },
      ],
      teller_Status: [
        { id: '', label: '所有' },
        { id: 1, label: '正常' },
        { id: 2, label: '解雇' },
        { id: 3, label: '封锁' },
      ],
      teller_Login_Type: [
        { id: '', label: '所有' },
        { id: 1, label: '密码' },
        { id: 2, label: '指纹识别' },
        { id: 3, label: '人脸识别' },
      ],
      //分页信息
      pageSize: 6,
      currentPage: 1,
      //查询柜员信息数据
      selectTellerInfos: [],
    };

    var vm = yufp.custom.vue({
      el: '#pad_usermanagement',
      data: vmData,
      computed: {
        //分页查询
        getSelectConditionData: function () {
          var dataSource = vmData.selectTellerInfos;
          var currentPage = vmData.currentPage;
          var pageSize = vmData.pageSize;
          var offset = (currentPage - 1) * pageSize;
          if (offset + pageSize >= dataSource.length) {
            return dataSource.slice(offset, dataSource.length);
          } else {
            return dataSource.slice(offset, pageSize);
          }
        }
      },
      mounted: function () {
        var headers = {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
            "Authorization": "Basic d2ViX2FwcDo="
        };
        this.queryPadTellerInfoList();
      },
      methods: {
        //格式化表格显示数据
        tellerTypeFormat:function(row , column){
          switch (row.TELLER_TYPE) {
            case '1':
              return '真实柜员';
            case '2':
              return '虚拟柜员';
          }
        },
        //格式化表格显示数据
        tellerLoginTypeFormat:function(row,column){
          switch (row.TELLER_LOGIN_TYPE) {
            case '1':
              return '密码';
            case '2':
              return '指纹识别';
            case '3':
              return '人脸识别';
          }
        },
        //查询PAD端的用户信息
        queryPadTellerInfoList:function(){
          var reqData={
            TELLER_ID: vmData.userInfoSearch.TELLER_ID,
            TELLER_NAME: vmData.userInfoSearch.TELLER_NAME,
            ORG_CN_NAME: vmData.userInfoSearch.ORG_CN_NAME,
            TELLER_TYPE: vmData.userInfoSearch.TELLER_TYPE,
            TELLER_STATUS: vmData.userInfoSearch.TELLER_STATUS,
            TELLER_LOGIN_TYPE: vmData.userInfoSearch.TELLER_LOGIN_TYPE,
            SELECTTYPE:"query",
          }
          yufp.service1.request({
            id: 'queryPadTellerInfoList',
            name: 'auth/pad_user/queryPadTellerInfoList',
            data: reqData,
            callback: function (code, message, data) {
               if(code===0){
                vmData.selectTellerInfos = data.list;
                console.log(vmData.selectTellerInfos)
               }else{
                 vm.$alert('查询失败,请稍后再试', '提示', {
                   confirmButtonText: '确定',
                   callback: function() {
                   }
                 });
               }
            }
          });
        },

        sizeChange: function () {
          // vm.resetData();
        },
        currentChange: function (pageIndex) {
          vmData.currentPage = pageIndex;
          // vm.resetData();
        },
        prevClick: function (pageIndex) {
          vmData.currentPage = pageIndex;
          // vm.resetData();
        },
        nextClick: function (pageIndex) {
          vmData.currentPage = pageIndex;
          // vm.resetData();
        },

        addNewUser: function () {
          dialog_data.isShowDialog = true;
          dialog_data.titleName = "新增用户";
          dialog_data.buttonName = "保存";
        },
        updateUser: function () {
          dialog_data.isShowDialog = true;
          dialog_data.titleName = "更新用户";
          dialog_data.buttonName = "更新";
        },
        multUsersDelete: function () {
          this.$alert('你是否要删除选中的这些数据?', '提示', {
            confirmButtonText: 'confirm',
            callback: function () {
            }
          });
        },
      },

    });




    // Dialog弹窗层的Vue以及与之绑定的数据源
    var dialog_data = {
      isShowDialog: false,
      titleName: '',
      buttonName: '',
      formData: {
        TELLER_ID: '',
        TELLER_NAME: '',
        ORGID: '',
        IDCARD: '',
        TELLER_TYPE:'',
        PASSWORD:'',
        MOBILE_NUMBER:'',
        ROLE_ID:'',
        TELLER_STATUS:'',
      }

    };

    var dvm = yufp.custom.vue({
      el: '#pad_userDialog',
      data: dialog_data,
      methods: {
        cancelDialog: function () {
          dialog_data.isShowDialog = false;
        }
      },
    });









  },
    //消息处理
    exports.onmessage = function (type, message, cite) {

    },
    //page销毁时触发destroy方法
    exports.destroy = function (id, cite) {

    }
});
