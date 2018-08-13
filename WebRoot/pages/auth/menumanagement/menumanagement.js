/**
 * Created by ZhangPengFei on 2018/5/10
 */
define(function (require, exports) {

    //page加载完成后调用ready方法
    exports.ready = function (hashCode, data, cite) {

        //对应功能数据获取
        yufp.service1.request({
          id: 'correspondFun',
          name: 'auth/user/queryModuleInfoList',
          data: '',
          callback: function (code, message, data) {
             if (code == 0) {
                console.log(data)
                vmData.correspondFun.options=data.list
              } else {
                alert("对应功能数据获取失败，code:"+code)
                console.log(code,message)
              }
          }
        });

        var vmData = {

            data2: [],
            defaultProps: {
                children: 'children',
                label: 'label'
            },

            nodeKey: [],
            //校验控制
            rules: {
                DISPLAYNAME: [
                    {required: true, message: '请输入菜单名称', trigger: 'blur'}
                ],
                NODETYPE: [
                    {required: true, message: '请选菜单类型', trigger: 'blur'}
                ],
                NODEORDER: [
                    {required: true, message: '请选输入排序序号', trigger: 'blur'}
                ],
                correspondFunction: [
                    {required: true, message: '请选对应功能', trigger: 'blur'}
                ],


            },

            //菜单类型选择
            menuTypeChoose: {
                options: [{
                    value: 'L',
                    label: '菜单节点'
                }, {
                    value: 'F',
                    label: '菜单组分类'
                }],
                value2: ''//选择后显示的值
            },

            correspondFun: {
                options: [],
                value3: ''//选择后显示的值

            },

            //是否显示对话框
            dialogVisible: false,
            dialogFlag: '',//对话框类型

            //对应菜单节点
            menuDetail: {
                FUNCID: '',//功能编号
                DISPLAYNAME: '',//菜单名称
                NODETYPE: '',//节点类型（F-目录节点，L-叶子节点即菜单节点）
                NODEORDER: '',//排序序号（小到大排序）
                NODELEVEL: '',//节点级别
                PARENTID: '',//父节点ID
                MENUID: ''  //菜单ID
            },
            //选中的菜单项
            //选中的菜单项
            //currSelectionMenu:{
            //    id: '',//功能编号
            //    label: '',//菜单名称
            //    NODETYPE: '',//节点类型（F-目录节点，L-叶子节点即菜单节点）
            //    NODEORDER: '',//排序序号（小到大排序）
            //    NODELEVEL: '',//节点级别
            //    PARENTID: '',//父节点ID
            //    MENUID: '' , //菜单ID
            //    children:[]
            //},

            //子节点数
            nodeSum:0,

            changeToUpdate:false,
            menuName:'',
            nodeType:'',
            funcId:'',
            sortNo:''

            //  ,submitBtnName:'更新',//按钮名称：更新 或 添加


            //// 功能列表字段
            // funDetail:{
            //     CSS:'',
            //     FUNCID:'',
            //     HTML:'',
            //     JS:'',
            //     NAME:'',
            //     OPERATOR:''
            // }

        };


        //创建virtual model
        var vm = yufp.custom.vue({
            el: "#el_menumanagement",

            data: vmData,

            methods: {
                //菜单点击
                nodeClick: function (data, node, tree) {
                    console.log('点击了节点', node)
                    vmData.nodeSum=data.children.length;

                    vmData.menuDetail.MENUID = data.id;
                    vmData.menuDetail.DISPLAYNAME = data.label;
                    vmData.menuDetail.FUNCID = data.FUNCID;
                    vmData.menuDetail.NODETYPE = data.NODETYPE;
                    vmData.menuDetail.NODEORDER = data.NODEORDER;
                    vmData.menuDetail.NODELEVEL = data.NODELEVEL;
                    vmData.menuDetail.PARENTID = data.PARENTID;

                    vmData.menuTypeChoose.value2 = data.NODETYPE;
                    vmData.correspondFun.value3 = data.FUNCID;

                    vmData.menuName=data.label;
                    vmData.nodeType=data.NODETYPE;
                    vmData.funcId=data.FUNCID;
                    vmData.sortNo=data.NODEORDER;
                },

                /**
                 * 添加根菜单
                 */
                addRootMenu: function () {
                    console.log("添加根菜单")
                    //父节点为 -1；
                    menuDialogData.menuDetail.PARENTID = '-1';

                    menuDialogData.menuTypeChoose.value2 = vmData.menuTypeChoose.value2;
                    menuDialogData.menuDetail.NODETYPE = vmData.menuDetail.NODETYPE;

                    menuDialogData.menuDetail.FUNCID = vmData.menuDetail.FUNCID;

                    menuDialogData.correspondFun.options = vmData.correspondFun.options;
                    menuDialogData.correspondFun.value3 =  vmData.correspondFun.value3;

                    menuDialogData.menuDetail.DISPLAYNAME='';
                    menuDialogData.menuDetail.NODELEVEL='1';
                    menuDialogData.menuInfoDialogVisible = true;

                },


                /**
                 * 添加菜单
                 */
                addMenu: function () {

                    if (vmData.menuDetail.NODETYPE != 'F') {
                        vm.showMsg("提示","只允许在菜单组下创建");
                        return;
                    }

                    menuDialogData.menuDetail.PARENTID = vmData.menuDetail.MENUID

                    menuDialogData.menuTypeChoose.value2 = 'L';
                    menuDialogData.menuDetail.NODETYPE = 'L';

                    menuDialogData.menuDetail.FUNCID = vmData.menuDetail.FUNCID;
                    menuDialogData.correspondFun.options = vmData.correspondFun.options;
                    menuDialogData.correspondFun.value3 =  vmData.correspondFun.value3;

                    menuDialogData.menuDetail.DISPLAYNAME='';
                    menuDialogData.menuDetail.NODELEVEL=(Number(vmData.menuDetail.NODELEVEL)+1).toString();
                    menuDialogData.menuInfoDialogVisible = true;


                },

                /**
                 * 删除菜单提示框
                 */
                showDeleteMenuDialog:function() {

                    if(vmData.menuDetail.MENUID==''){
                      vm.showMsg("提示","请先选择菜单节点");
                      return;
                    }

                    if(vmData.nodeSum>0){
                        vm.showMsg("提示","请先删除相应子菜单");
                        return;
                    }

                    this.$confirm('确定要删除吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function(){
                        vm.removeMenu();
                    });
                },

                /**
                 * 删除菜单
                 */
                removeMenu: function () {
                    console.log("删除的数据："+JSON.stringify(vmData.menuDetail))
                    yufp.service1.request({
                        id: 'removeMenuInfo',
                        data: vmData.menuDetail,
                        name: 'auth/menu/removeMenuInfo',
                        callback: function (code, message, data) {
                            if (code == 0) {
                                vm.showMsg('提示','删除成功');
                                vm.queryMenuList();//修改完 更新一下菜单列表
                            } else {
                                vm.showMsg('提示','删除失败，' + message);
                            }
                        }
                    })

                },


                /**
                 * 清除菜单详情里的数据
                 */
                resetMenu: function () {

                    vmData.menuDetail.DISPLAYNAME = "";
                    vmData.menuTypeChoose.value2='';
                    vmData.correspondFun.value3='';
                    vmData.menuDetail.FUNCID = "";
                    vmData.menuDetail.NODETYPE = "";
                    vmData.menuDetail.NODEORDER = 1;
                },

                /**
                 * 查询当前菜单列表
                 */
                queryMenuList: function () {

                    yufp.service1.request({
                        id: "queryMenuInfo",
                        data: {},
                        name: "auth/menu/queryMenuInfo",
                        callback: function (code, message, data) {
                            //alert(JSON.stringify(data));
                            if (code == 0) {
                                vmData.data2 = toTreeData(data.list);
                            } else {
                                alert("查询失败" + code + message);

                            }
                        }
                    })
                },


                /**
                 * 修改菜单
                 */
                updateMenuInfo: function () {
                    vmData.menuDetail.NODETYPE=vmData.menuTypeChoose.value2;
                    vmData.menuDetail.FUNCID=vmData.correspondFun.value3;
                    if(vmData.menuDetail.MENUID==''){
                      vm.showMsg("提示","请先选择菜单节点");
                      return;
                    }
                    //判断表单数据是否修改过
                    if(vmData.menuDetail.DISPLAYNAME.trim()==vmData.menuName.trim()&&vmData.menuTypeChoose.value2==vmData.nodeType&&
                    vmData.correspondFun.value3==vmData.funcId&&vmData.menuDetail.NODEORDER==vmData.sortNo){
                      vm.showMsg("提示","数据未改动，无需更新");
                      return;
                    }
                    if(vmData.menuDetail.DISPLAYNAME==''||vmData.menuDetail.NODETYPE==''||vmData.menuDetail.FUNCID==''||
                    vmData.menuDetail.NODEORDER==''){
                      vm.showMsg("提示","请先填写完整再更新");
                      return;
                    }

                    console.log("更新的数据："+JSON.stringify(vmData.menuDetail));
                    yufp.service1.request({
                        id: 'updateMenuInfo',
                        data: vmData.menuDetail,
                        name: 'auth/menu/updateMenuInfo',
                        callback: function (code, message, data) {
                            if (code == 0) {
                                vm.showMsg('提示','修改成功');
                                vm.queryMenuList();//修改完 更新一下菜单列表
                            } else {
                                vm.showMsg('提示','修改失败，' + message);


                            }
                        }
                    })
                },

                //生成随机数
                randomFrom: function (lowerValue, upperValue) {
                    return Math.floor(Math.random() * (upperValue - lowerValue + 1) + lowerValue);
                },
                //显示提示框
                showMsg:function(msgTilte,msgContent){
                    this.$alert(msgContent, msgTilte, {
                        confirmButtonText: '确定',
                        type: 'warning'
                    });
                }


            },

            watch: {

            },
            /**
             *  界面加载成功
             */
            mounted: function () {
                this.queryMenuList();//查询 菜单

            }
        });


        var menuDialogData = {
            menuInfoDialogVisible: false,


            //对应菜单节点
            menuDetail: {
                FUNCID: '',//功能编号
                DISPLAYNAME: '',//菜单名称
                NODETYPE: '',//节点类型（F-目录节点，L-叶子节点即菜单节点）
                NODEORDER: '',//排序序号（小到大排序）
                NODELEVEL: '',//节点级别
                PARENTID: '',//父节点ID
                MENUID: ''  //菜单ID
            },

            //校验控制
            rules: {
                DISPLAYNAME: [
                    {required: true, message: '请输入菜单名称', trigger: 'blur'}
                ],
                NODETYPE: [
                    {required: true, message: '请选菜单类型', trigger: 'blur'}
                ],
                NODEORDER: [
                    {required: true, message: '请选输入排序序号', trigger: 'blur'}
                ],
                correspondFunction: [
                    {required: true, message: '请选对应功能', trigger: 'blur'}
                ],


            },

            //菜单类型选择
            menuTypeChoose: {
                options: [{
                    value: 'L',
                    label: '菜单节点'
                }, {
                    value: 'F',
                    label: '菜单组分类'
                }],
                value2: ''//选择后显示的值
            },
            correspondFun: {
                options: [],
                value3: ''//选择后显示的值

            },


        };
        //新建菜单的弹窗
        var vmDialog = yufp.custom.vue({
            el: "#el_menuInfoDialog",
            data: menuDialogData,
            methods: {

                addMenuInfoSubmit: function () {
                    menuDialogData.menuDetail.MENUID = "m" + vm.randomFrom(100, 1000) + vm.randomFrom(10, 100); //生成父节点菜单ID
                    menuDialogData.menuDetail.NODETYPE=menuDialogData.menuTypeChoose.value2;
                    menuDialogData.menuDetail.FUNCID = menuDialogData.correspondFun.value3;
                    var reqData = menuDialogData.menuDetail;

                    yufp.service1.request({
                        id: 'addMenuInfo',
                        data: reqData,
                        name: 'auth/menu/addMenuInfo',
                        callback: function (code, message, data) {
                            console.log("code:"+code);
                            if (code == 0) {
                                vm.showMsg("提示","添加菜单成功");
                                menuDialogData.menuInfoDialogVisible = false;
                                vm.queryMenuList();//修改完 更新一下菜单树

                            } else {
                                vm.showMsg("提示","添加失败，"+message);
                            }


                        }
                    })

                },


            },
            watch: {},
            mounted: function () {

            }
        });

    };
    /**
     * 将父子关系数据转化成树结构数据
     * @param data
     * @returns {Array}
     */
    function  toTreeData(data){
        var resData=data
        var tree=[]
        //获取一级节点
        for (var i = 0; i < resData.length; i++) {
          if(resData[i].NODE_LEVEL=='1'){
            var obj = {
                id: resData[i].MENU_ID,
                label: resData[i].MENU_NAME,
                FUNCID: resData[i].FUNC_ID,
                NODEORDER: resData[i].SORT_NO,
                NODETYPE: resData[i].NODE_TYPE,
                PARENTID: resData[i].PARENT_ID,
                NODELEVEL: resData[i].NODE_LEVEL,
                children: []
            }
            tree.push(obj)
            resData.splice(i,1)
            i--
          }
        }
        bindChildrenData(tree,resData)
        return tree
    }

    function bindChildrenData(tree,resData){
      nodeSort(tree)
      if(resData.length!=0){
        for (var i = 0; i < tree.length; i++) {
          for (var j = 0; j < resData.length; j++) {
            if(resData[j].PARENT_ID==tree[i].id){
              var obj = {
                  id: resData[j].MENU_ID,
                  label: resData[j].MENU_NAME,
                  FUNCID: resData[j].FUNC_ID,
                  NODEORDER: resData[j].SORT_NO,
                  NODETYPE: resData[j].NODE_TYPE,
                  PARENTID: resData[j].PARENT_ID,
                  NODELEVEL: resData[j].NODE_LEVEL,
                  children: []
              }
              tree[i].children.push(obj)
              resData.splice(j,1)
              j--
            }
          }
          bindChildrenData(tree[i].children,resData)
        }
      }
    }

    //节点排序，从小到大冒泡
    function nodeSort(arr){
      var tmp={};
      for(var i=0;i<arr.length-1;i++)
        for(var j=i+1;j<arr.length;j++){
          if(Number(arr[i].NODEORDER)>Number(arr[j].NODEORDER)){
            tmp=arr[i];
            arr[i]=arr[j];
            arr[j]=tmp;
          }
        }
    }

    /**
     * 获取对应功能组列表
     */
    function getFunGroup(funId) {
        console.log("查询功能-id-" + funId);
        var funTree = [];

        yufp.service1.request({
            id: 'queryAllFuncFromGroup',
            data: {FUNCID: funId},
            name: 'auth/user/queryAllFuncFromGroup',
            callback: function (code, message, data) {
                // alert(JSON.stringify(data));
                if (code == 0) {
                    console.log("查询功能列表成功")
                    var funArr = data.list;
                    if (funArr.length > 0) {
                        for (var i = 0; i < funArr.length; i++) {
                            var obj = {
                                value: funArr[i].FUNCID,
                                label: funArr[i].NAME,
                            };
                            funTree.push(obj);
                        }
                        // vmData.correspondFun.options=funTree;

                    } else {
                        var obj = {
                            value: funId,
                            label: '应用功能',
                        };
                        funTree.push(obj);
                    }

                } else {
                    console.log("查询功能列表失败")
                }
            }
        })

        return funTree;

    }


});
