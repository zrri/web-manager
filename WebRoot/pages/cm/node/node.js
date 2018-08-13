define(function(require, exports) {
  exports.ready = function(hashCode, data, cite) {
    //  yufp.lookup.reg("NATIONALITY,PUBLISH_STATUS");
   var data={
     nodeData: [{
       HOSTIP: '',
       NODETYPE: '',
       APPPATH: '',
       NAME: '',
       DESCRIPTION: '',
       ISLINK: '',
       LINKDIRECTORY: '',
       UPDATEDIRECTORY: '',
       APPPORT: '',
       HTTPPORT: '',
       JVMPORT: ''
     }],
     pageSize: 5, //每页显示5条
     currentPage: 1, //当前页，从1
     filterIP: '',
     timer:null,
     multipleFuncSelection: []
   };
    var vm = yufp.custom.vue({
      el: "#el_node",
      data: data,
      watch:{
        filterIP:function (value) {
          vm.filterNode();
        },
      },
      methods: {
        createFn: function() {
          vmDialog.showOnly=false;
          vmDialog.showNew();

        },
        handleShow: function(index, row) {
          vmDialog.showOnly=true;
          vmDialog.showDetail(row);
        },
        //修改信息
        handleEdit: function(index, row) {
          vmDialog.showOnly=false;
          vmDialog.showEdit(row);
        },
        handleDelete:function(index,row){
          this.$confirm('确定要删除吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(function(){

            vm.deleteNodeData([row.HOSTIP]);
          });
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
            var nodeipList = [];
            for (var i = 0; i < vm.multipleFuncSelection.length; i++) {
              nodeipList.push(vm.multipleFuncSelection[i].HOSTIP);
            }
            vm.deleteNodeData(nodeipList);
          }).catch(function() {});
        },
        handleSelectionChange: function(val) {
          //处理多选操作
          vm.multipleFuncSelection = val;
        },
        editFn: function() {
          if (vm.multipleFuncSelection.length !== 1) {
            this.$alert("请选择一行信息", '提示');
            return;
          }
          vmDialog.editMethod(vm.multipleFuncSelection[0].HOSTIP)

        },
        deleteNodeData:function(idArr){
          var listArr=[];
          for(var i=0;i<idArr.length;i++){
            var rowid=idArr[i];
            listArr.push(rowid);
          }
          var infoList = {"list":listArr};

          yufp.service1.request({
            id:"deleteHostByList",
            data:infoList,
            name:"cm/node/deleteNodeByList",
            callback:function (code,message,data) {
              if (code === 0) {
                vm.$alert("删除成功", '', {
                  confirmButtonText: '提示',
                  callback: function() {
                    data.multipleFuncSelection=[];
                    vm.queryNodeData();
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
        filterNode:function(){
          if(vm.timer){
            clearTimeout(vm.timer);
          }
          vm.timer= window.setTimeout(function () {
            vm.queryNodeData();
          },500);
        },
        getNodeTypeName:function(row,column){
          var id=row.NODETYPE;
          for(var i=0;i<vmDialog.nodeTypeOptions.length;i++){
            if(vmDialog.nodeTypeOptions[i].id===id){
              return  vmDialog.nodeTypeOptions[i].label;
            }
          }
        },
        getLinkTypeName:function(row,column){
          var id=row.ISLINK;
          for(var i=0;i<vmDialog.linkOptions.length;i++){
            if(vmDialog.linkOptions[i].id===id){
              return  vmDialog.linkOptions[i].label;
            }
          }
        },
        queryNodeData: function() {
          var reqData = {
            "HOSTIP":data.filterIP
          };
          var that = this;
          yufp.service1.request({
            id: "queryNodeInfoList",
            data: reqData,
            name: "cm/node/queryNodeInfoList",
            callback: function(code, message, data) {
              if (code === 0) {
                vm.nodeData = data.list;
              } else {
                this.$alert('服务端请求失败!', '', {
                  confirmButtonText: '提示',
                  callback: function() {}
                });
              }
            }
          });
        }
      },
      computed: {
        getNodeData: function() {
          //在这里处理分页
          var nodeData = this.nodeData;
          var pageSize = this.pageSize;
          var currentPage = this.currentPage;
          var offset = (currentPage - 1) * pageSize;
          if (offset + pageSize >= nodeData.length) {
            return nodeData.slice(offset, nodeData.length);
          } else {
            return nodeData.slice(offset, offset + pageSize);
          }
        }
      },
      mounted: function() {
        this.queryNodeData();
      }
    });

    var vmDialog = yufp.custom.vue({
      el: "#el_nodeInfo",
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
          node: {
            HOSTIP: '',
            NODETYPE: '',
            APPPATH: '',
            NAME: '',
            DESCRIPTION: '',
            ISLINK: '',
            LINKDIRECTORY: '',
            UPDATEDIRECTORY: '',
            APPPORT: '',
            HTTPPORT: '',
            JVMPORT: ''
          },
          nodeInfoLayerVisible: false,
          titleName: '',
          submitButtonName: '',
          operatorType: 'new',
          showOnly:true,
          linkOptions:[{id:'1',label:"是"},{id:'2',label:"否"}],
          nodeTypeOptions:[{id:'1',label:"1-bip"},{id:'2',label:"2-bsp"}],
          rules: {
            HOSTIP: [{
              validator: validateIP,
              trigger: 'blur'
            }, {
              required: true,
              message: 'IP地址不能为空',
              trigger: 'blur'
            }],
            NODETYPE: [{
              required: true,
              message: '节点类型不能为空',
              trigger: 'blur'
            }],
            APPPATH: [{
              required: true,
              message: 'APP路径不能为空',
              trigger: 'blur'
            }],
            NAME: [{
              required: true,
              message: '节点名称不能为空',
              trigger: 'blur'
            }],
            DESCRIPTION: [{
              required: true,
              message: '是否软连接不能为空',
              trigger: 'change'
            }],
            ISLINK: [{
              required: true,
              message: '更新目录不能为空',
              trigger: 'change'
            }],
            LINKDIRECTORY: [{
              required: true,
              message: '应用端口不能为空',
              trigger: 'change'
            }],
            UPDATEDIRECTORY: [{
              required: true,
              message: 'HTTP端口不能为空',
              trigger: 'change'
            }],
            APPPORT: [{
              required: true,
              message: 'JVM端口不能为空',
              trigger: 'change'
            }],
            HTTPPORT: [{
              required: true,
              message: '文件传输密码不能为空',
              trigger: 'change'
            }],
            JVMPORT: [{
              required: true,
              message: '文件传输密码不能为空',
              trigger: 'change'
            }]
          },
        }
      },
      methods: {
        showDetail: function(row) {
          this.titleName = '节点详情';
          this.submitButtonName = '确 定';
          this.operatorType = 'detail';
          this.nodeInfoLayerVisible = true;
          this.bindEditData(row.HOSTIP);
        },
        showEdit: function(row) {
          var id=row.HOSTIP;
          this.editMethod(id);
        },
        editMethod:function(id){
          this.titleName = '修改';
          this.submitButtonName = '保 存';
          this.operatorType = 'edit';
          this.nodeInfoLayerVisible = true;
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
            id: "queryNodeInfoList",
            data: reqData,
            name: "cm/node/queryNodeInfoList",
            callback: function(code, message, data) {
              if (code === 0) {
                var obj= data.list[0];
                vmDialog.node=obj;
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
          this.nodeInfoLayerVisible = true;
          vmDialog.$refs["NodeInfoForm"].resetFields();

        },
        submit: function(formName) {

          var that=this;
          this.$refs["NodeInfoForm"].validate(function(valid)  {
            if (valid) {
              if (that.operatorType === 'edit') {
                that.editNodeInfo();
              } else if (that.operatorType === 'new') {
                that.addNodeInfo();
              }
            } else {
              return false;
            }
          });
        },
        cancelNode: function() {
          this.nodeInfoLayerVisible = false;
        },
        addNodeInfo:function(){
          var that = this;
          var hostip=  this.node.HOSTIP;
          yufp.service1.request({
            id:"isExistNodeID",
            data:{
              HOSTIP:hostip
            },
            name:"cm/node/isExistNodeID",
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
                    id: "AddNodeInfo",
                    data: that.node,
                    name: "cm/node/AddNodeInfo",
                    callback: function(code, message, data) {
                      if (code === 0) {
                        that.$alert('添加成功', '', {
                          callback: function() {
                            vm.queryNodeData();
                            that.nodeInfoLayerVisible = false;
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
        editNodeInfo:function(){
          var that = this;
          yufp.service1.request({
            id: "editNodeInfo",
            data: that.node,
            name: "cm/node/editNodeInfo",
            callback: function(code, message, data) {
              if (code === 0) {
                that.$alert('修改成功', '', {
                  callback: function() {
                    that.nodeInfoLayerVisible = false;
                    vm.queryNodeData();
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

      }
    });
  }
});
