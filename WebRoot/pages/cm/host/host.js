/**
 * Created by helin3 on 2017/12/07.
 */
define(function(require, exports) {
  exports.ready = function(hashCode, data, cite) {
    //  yufp.lookup.reg("NATIONALITY,PUBLISH_STATUS");
    var data=  {
          queryFields: [{
            placeholder: '日期',
            field: 'date',
            type: 'input'
          },
            {
              placeholder: '姓名',
              field: 'name',
              type: 'input'
            },
            {
              placeholder: '地址',
              field: 'address',
              type: 'input'
            }
          ],
          filterDate: '',
          filterName: '',
          filterAddress:'',
          hostData: [{
            HOSTIP: '',
            FILEMODE: '',
            FILEPORT: '',
            FILEUSERNAME: '',
            LOGINMODE: '',
            LOGINPORT: '',
            LOGINUSERNAME: ''
          }],
          pageSize: 5, //每页显示2条
          currentPage: 1, //当前页，从1开
          multipleFuncSelection: [],
          timer:null,

        };
    var vm = yufp.custom.vue({
      el: "#el_host",
      data:data,
      watch:{
        filterDate:function (value) {
          vm.fileterHostInfo();
        },
        filterName:function (value) {
          vm.fileterHostInfo();
        },
        filterAddress:function (value) {
          vm.fileterHostInfo();
        }
      },
      computed: {
        //isShowOnly:function(){
        //  return 'none';
        //}
      },
      methods: {
        fileterHostInfo:function(){
          if(vm.timer){
            window.clearTimeout(vm.timer);
          }
          vm.timer= window.setTimeout(function () {
            vm.queryHostInfo();
          },500);

        },
        handleSelectionChange: function(val) {
          //处理多选操作
          vm.multipleFuncSelection = val;
        },
        handleShow:function(index, row) {
          vmDialog.showOnly=true;
          vmDialog.showDetail(row);
        },
        //修改信息
        handleEdit:function(index, row) {
          vmDialog.showOnly=false;
          vmDialog.showEdit(row);
        },
        handleDelete:function(index,row){
          this.$confirm('确定要删除吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(function(){

            vm.deleteHostData([row.HOSTIP]);
          });
        },
        deleteHostData:function(idArr){
          var listArr=[];
          for(var i=0;i<idArr.length;i++){
            var rowid=idArr[i];
            listArr.push(rowid);
          }
          var infoList = {"list":listArr};

          yufp.service1.request({
            id:"deleteHostByList",
            data:infoList,
            name:"cm/host/deleteHostByList",
            callback:function (code,message,data) {
              if (code === 0) {
                vm.$alert("删除成功", '', {
                  confirmButtonText: '提示',
                  callback: function() {
                    data.multipleFuncSelection=[];
                    vm.queryHostInfo();
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


        createFn: function() {
          vmDialog.showOnly=false;
          vmDialog.showNew();

        },
        editFn: function() {
          if (vm.multipleFuncSelection.length !== 1) {
            this.$alert("只需要选择一行", '提示');
            return;
          }
          vmDialog.editMethod(vm.multipleFuncSelection[0].HOSTIP)

        },
        //批量删除
        batchDelete: function() {
          if (vm.multipleFuncSelection.length == 0) {
            this.$alert("至少选中一项", '提示', {
              confirmButtonText: "确定",
              callback: function() {}
            });
            return;
          }
          this.$confirm('确定要删除吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(function() {
            //开始删除
            var hostipList = [];
            for (var i = 0; i < vm.multipleFuncSelection.length; i++) {
              hostipList.push(vm.multipleFuncSelection[i].HOSTIP);
            }
            vm.deleteHostData(hostipList);
          }).catch(function() {});
        },
        deleteFn: function() {
          this.$alert('删除', '提示');
        },
        rowClick: function(row, event, column) {
          //  this.$alert('你选择的行ID值为：'+row.id, '提示');
        },

        queryHostInfo: function() {
          var reqData = {
            "LOGINUSERNAME":data.filterName,
            "HOSTIP":data.filterAddress
          };
          yufp.service1.request({
            id: "queryHostInfoList",
            data: reqData,
            name: "cm/host/queryHostInfoList",
            callback: function(code, message, data) {
              if (code === 0) {
                vm.hostData = data.list;
              } else {
                this.$alert('服务端请求失败!', '', {
                  confirmButtonText: '提示',
                  callback: function() {}
                });
              }
            }
          });
        },
        //分页触发事件
        sizeChange:function(pageSize){
        },
        currentChange:function(pageIndex){
          data.currentPage=pageIndex;
        },
        prevClick:function(pageIndex){
          data.currentPage=pageIndex;
        },
        nextClick:function(pageIndex){
          data.currentPage=pageIndex;
        },
        getTransTypeName:function(row,column){
            var id=row.FILEMODE;
            for(var i=0;i<vmDialog.tansportGroupOptions.length;i++){
              if(vmDialog.tansportGroupOptions[i].id===id){
                return  vmDialog.tansportGroupOptions[i].label;
              }
            }
        },
        getLoginTypeName:function(row,column){
          var id=row.LOGINMODE;
          for(var i=0;i<vmDialog.loginGroupOptions.length;i++){
            if(vmDialog.loginGroupOptions[i].id===id){
              return  vmDialog.loginGroupOptions[i].label;
            }
          }
        }
      },
      computed: {
        getHostData: function() {
          //在这里处理分页
          var hostData = this.hostData;
          var pageSize = this.pageSize;
          var currentPage = this.currentPage;
          var offset = (currentPage - 1) * pageSize;
          if (offset + pageSize >= hostData.length) {
            return hostData.slice(offset, hostData.length);
          } else {
            return hostData.slice(offset, offset + pageSize);
          }
        }

      },
      mounted: function() {
        this.queryHostInfo();
      }
    });



    var vmDialog = yufp.custom.vue({
      el: "#el_addHost",
      data: function() {
        var validateIP = function(rule, value, callback)  {
          var re = /((25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))/; //正则表达式
          if (value === '') {
            callback(new Error('IP地址不能为空!'));
          } else {
            if (re.test(value))
              callback();
            else {
              callback(new Error('IP地址格式错误'));
            }
          }
        };
        return {
          operatorType: 'detail', //detail edit new
          isDisabled: false,
          addHostLayerVisible: false,
          titleName: '新建主机',
          submitButtonName: '提 交',
          host: {
            HOSTIP: '',
            FILEMODE: '',
            FILEPORT: '',
            FILEUSERNAME: '',
            LOGINMODE: '',
            LOGINPORT: '',
            LOGINUSERNAME: '',
            LOGINPASSWORD: '',
            FILEPASSWORD: ''
          },
          showOnly:true,
          rules: {
            HOSTIP: [{
              validator: validateIP,
              trigger: 'blur'
            }, {
              required: true,
              message: 'IP地址不能为空',
              trigger: 'blur'
            }],
            LOGINMODE: [{
              required: true,
              message: '登录方式不能为空',
              trigger: 'blur'
            }],
            LOGINPORT: [{
              required: true,
              message: '登录端口不能为空',
              trigger: 'blur'
            }],
            LOGINUSERNAME: [{
              required: true,
              message: '登录用户名不能为空',
              trigger: 'blur'
            }],
            LOGINPASSWORD: [{
              required: true,
              message: '登录密码不能为空',
              trigger: 'change'
            }],
            FILEMODE: [{
              required: true,
              message: '文件传输模式不能为空',
              trigger: 'change'
            }],
            FILEPORT: [{
              required: true,
              message: '文件传输端口不能为空',
              trigger: 'change'
            }],
            FILEUSERNAME: [{
              required: true,
              message: '文件传输用户不能为空',
              trigger: 'change'
            }],
            FILEPASSWORD: [{
              required: true,
              message: '文件传输密码不能为空',
              trigger: 'change'
            }]
          },
          tansportGroupOptions:[{id:'1',label:"1-sftp"},{id:'2',label:"2-ftp"}],
          loginGroupOptions:[{id:'1',label:"1-ssh"},{id:'2',label:"2-telnet"}]
        }
      },
      watch: {
        /*这是什么意思？？？*/
        //'host.LOGINMODE': function(val, oldVal) {
        //  if(val==1){
        //    this.host.LOGINPORT=22;
        //    this.host.FILEMODE=1;
        //    this.host.FILEPORT=22;
        //  }
        //},
        //'host.LOGINUSERNAME': function(val, oldVal) {
        //  this.host.FILEUSERNAME = val;
        //},
        //'host.LOGINPASSWORD': function(val, oldVal) {
        //  console.log('val' + val + ',oldVal' + oldVal);
        //  this.host.FILEPASSWORD = val;
        //}

      },
      methods: {

        showDetail: function(row) {
          this.titleName = '主机详情';
          this.submitButtonName = '确 定';
          this.operatorType = 'detail';
          this.addHostLayerVisible = true;
          this.bindEditData(row.HOSTIP);
        },
        showEdit: function(row) {
          var id=row.HOSTIP;
          this.editMethod(id);
        },
        editMethod:function(id){
          this.titleName = '修改';
          this.submitButtonName = '保 存';
          this.isDisabled = true;
          this.operatorType = 'edit';
          this.addHostLayerVisible = true;
          setTimeout(function(){
            vmDialog.bindEditData(id);
          },500);
        },
        bindEditData:function(id){//从列表传过来id，用于显示编辑页面

          //查询单条数据
          var reqData = {
            "HOSTIP":id
          };
          yufp.service1.request({
            id: "queryHostInfoList",
            data: reqData,
            name: "cm/host/queryHostInfoList",
            callback: function(code, message, data) {
              if (code === 0) {
                var obj= data.list[0];
                vmDialog.host=obj;
              } else {
                this.$alert('服务端请求失败!', '', {
                  confirmButtonText: '提示',
                  callback: function() {}
                });
              }
            }
          });

        },
        showNew: function() {

          vmDialog.showOnly=false;
          this.titleName = '新增';
          this.submitButtonName = '保 存';
          this.operatorType = 'new';
          this.addHostLayerVisible = true;
          vmDialog.$refs["hostInfoForm"].resetFields();

        },
        //编辑表单
        editHostInfo: function() {
          var that = this;
          yufp.service1.request({
            id: "EditHostInfoList",
            data: this.host,
            name: "cm/host/editHostInfo",
            callback: function(code, message, data) {
              if (code === 0) {
                that.$alert('修改成功', '', {
                  callback: function() {
                    that.addHostLayerVisible = false;
                    vm.queryHostInfo();

                  }
                });

              } else {
                that.$alert('服务端请求失败!', '', {
                  confirmButtonText: '提示',
                  callback: function() {}
                });
              }
            }
          });
        },
        //增加主机节点
        addHostInfo: function() {
          var that = this;
           var hostip=  this.host.HOSTIP;
          yufp.service1.request({
            id:"isExistHostID",
            data:{
              HOSTIP:hostip
            },
            name:"cm/host/isExistHostID",
            callback:function (code,message,data) {
              if (code === 0) {
                if(data.count>0){
                  that.$alert('该IP已经存在', '', {
                    callback: function() {
                    }
                  });
                }
                else{
                  yufp.service1.request({
                    id: "AddHostInfoList",
                    data: that.host,
                    name: "cm/host/addHostInfo",
                    callback: function(code, message, data) {
                      if (code === 0) {
                        that.$alert('添加成功', '', {
                          callback: function() {
                            vm.queryHostInfo();
                            that.addHostLayerVisible = false;
                          }
                        });

                      } else {
                        that.$alert('服务端请求失败!', '', {
                          confirmButtonText: '提示',
                          callback: function() {}
                        });
                      }
                    }
                  });
                }
              } else {

              }
            }
          });



        },
        submitHost: function(formName) {
          var that=this;
          this.$refs["hostInfoForm"].validate(function(valid)  {
            if (valid) {
              if (that.operatorType === 'edit') {
                that.editHostInfo();
              } else if (that.operatorType === 'new') {
                 that.addHostInfo();
              }
          } else {
              return false;
          }
        });

        },
        cancelHost: function() {
          this.addHostLayerVisible = false;
        }
      }
    });
  };

});
