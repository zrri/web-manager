/**
 * @created by helin3 2017-11-30
 * @updated by
 * @description 菜单、控制点、数据权限模拟数据
 */

/**
 * 模拟菜单数据
 * @type
 */
var demoMenus = [
    //一级菜单
    { menuId: 'gm-10000', menuName: '首页', upMenuId: '0', menuIcon: 'el-icon-yx-home', funcId: 'dashboard', funcUrl: 'pages/common/dashboard/dashboard', isIndex: true},
    // { menuId: 'gm-20000', menuName: '基础教程', upMenuId: '0', menuIcon: 'el-icon-yx-books'},
    { menuId: 'gm-30000', menuName: '公共系统', upMenuId: '0', menuIcon: 'el-icon-yx-books'},
    { menuId: 'gm-40000', menuName: '监控模块', upMenuId: '0', menuIcon: 'el-icon-yx-books'},
    { menuId: 'gm-50000', menuName: '测试模块', upMenuId: '0', menuIcon: 'el-icon-yx-books'},



    { menuId: 'gm-51000', menuName: '测试页面', upMenuId: 'gm-50000', menuIcon: '', funcId: 'test', funcUrl: 'pages/test/test'},

    //二级菜单
    { menuId: 'gm-41000', menuName: '主机信息', upMenuId: 'gm-40000', menuIcon: '', funcId: 'host', funcUrl: 'pages/cm/host/host'},
    { menuId: 'gm-42000', menuName: '节点信息', upMenuId: 'gm-40000', menuIcon: '', funcId: 'node', funcUrl: 'pages/cm/node/node'},

    //二级菜单
    // { menuId: 'gm-21000', menuName: '空白模板', upMenuId: 'gm-20000', menuIcon: '', funcId: 'blank', funcUrl: 'pages/example/blank/blank'},
    // { menuId: 'gm-22000', menuName: '常用组件', upMenuId: 'gm-20000', menuIcon: 'el-icon-yx-stack'},
    // { menuId: 'gm-23000', menuName: '模板示例', upMenuId: 'gm-20000', menuIcon: 'el-icon-yx-folder-plus'},
    // { menuId: 'gm-24000', menuName: '原生(不建议)', upMenuId: 'gm-20000', menuIcon: 'el-icon-yx-price-tag'},

    //二级菜单
    { menuId: 'gm-31001', menuName: '功能组管理', upMenuId: 'gm-30000', menuIcon: '', funcId: 'applicationmanagement', funcUrl: 'pages/auth/applicationmanagement/applicationmanagement'},
    { menuId: 'gm-31000', menuName: '机构管理', upMenuId: 'gm-30000', menuIcon: '', funcId: 'blank', funcUrl: 'pages/auth/orgmanagement/orgmanagement'},
    { menuId: 'gm-32000', menuName: '功能管理', upMenuId: 'gm-30000', menuIcon: 'el-icon-yx-stack',funcId: 'funcmanage', funcUrl: 'pages/auth/funcmanagement/funcmanagement'},
    { menuId: 'gm-33000', menuName: '角色管理', upMenuId: 'gm-30000', menuIcon: 'el-icon-yx-folder-plus',funcId: 'rolemanage', funcUrl: 'pages/auth/rolemanagement/rolemanagement'},
    { menuId: 'gm-34000', menuName: '用户管理', upMenuId: 'gm-30000', menuIcon: 'el-icon-yx-price-tag', funcId: 'userManager', funcUrl: 'pages/auth/usermanagement/usermanagement'},
    { menuId: 'gm-35000', menuName: '菜单管理', upMenuId: 'gm-30000', menuIcon: 'el-icon-yx-price-tag',  funcId: 'menuManager',funcUrl: 'pages/auth/menumanagement/menumanagement'},

    //二级菜单
    { menuId: 'gm-42000', menuName: '服务器集群状态', upMenuId: 'gm-40000', menuIcon: '', funcId: 'gm-41000', funcUrl: 'pages/cm/servercluster/servercluster'},
    { menuId: 'gm-43000', menuName: '节点信息', upMenuId: 'gm-40000',menuIcon: '', funcId: 'gm-42000', funcUrl: 'pages/cm/nodeinfo/nodeinfo'},




    //三级菜单
    // { menuId: 'gm-22001', menuName: '封装字典管理器', upMenuId: 'gm-22000', menuIcon: '', funcId: 'lookup', funcUrl: 'pages/example/package/lookup'},
    // { menuId: 'gm-22002', menuName: '封装树', upMenuId: 'gm-22000', menuIcon: '', funcId: 'elTreeX', funcUrl: 'pages/example/package/elTreeX'},
    // { menuId: 'gm-22003', menuName: '封装表格', upMenuId: 'gm-22000', menuIcon: '', funcId: 'elTableX', funcUrl: 'pages/example/package/elTableX'},
    // { menuId: 'gm-22004', menuName: '封装表格-文档', upMenuId: 'gm-22000', menuIcon: '', funcId: 'elTableXDoc', funcUrl: 'pages/example/package/elTableXDoc'},
    // { menuId: 'gm-22005', menuName: '自定义选择器', upMenuId: 'gm-22000', menuIcon: '', funcId: 'demoSelector', funcUrl: 'pages/example/package/demoSelector'},
    // { menuId: 'gm-22006', menuName: '封装下拉框', upMenuId: 'gm-22000', menuIcon: '', funcId: 'elSelect', funcUrl: 'pages/example/package/elSelect'},
    // { menuId: 'gm-22007', menuName: '封装级联下拉框', upMenuId: 'gm-22000', menuIcon: '', funcId: 'elCascader', funcUrl: 'pages/example/package/elCascader'},
    // { menuId: 'gm-22008', menuName: '富文本组件', upMenuId: 'gm-22000', menuIcon: '', funcId: 'tinymce', funcUrl: 'pages/example/native/tinymce'},
    //{ menuId: 'gm-22009', menuName: 'FormInput输入框', upMenuId: 'gm-22000', menuIcon: '', funcId: 'elforminput', funcUrl: 'pages/example/package/elforminput'},
    //{ menuId: 'gm-22010', menuName: '日期、时间选择器', upMenuId: 'gm-22000', menuIcon: '', funcId: 'elformtimeselect', funcUrl: 'pages/example/package/elformtimeselect'},
    // { menuId: 'gm-22011', menuName: 'Form组件', upMenuId: 'gm-22000', menuIcon: '', funcId: 'elformx', funcUrl: 'pages/example/package/elformx'},
    // { menuId: 'gm-22012', menuName: '拖拽', upMenuId: 'gm-22000', menuIcon: '', funcId: 'exampleDraggable', funcUrl: 'pages/example/template/exampleDraggable'},
    // { menuId: 'gm-22013', menuName: '普通文件上传', upMenuId: 'gm-22000', menuIcon: '', funcId: 'normalUpload', funcUrl: 'pages/example/native/normalUpload'},
    // { menuId: 'gm-22014', menuName: '异步树', upMenuId: 'gm-22000', menuIcon: '', funcId: 'asynctree', funcUrl: 'pages/example/template/exampleTree'},
    // { menuId: 'gm-22015', menuName: '打开（菜单/自定义）页签', upMenuId: 'gm-22000', menuIcon: '', funcId: 'demoAddMenuTab', funcUrl: 'pages/example/package/demoAddMenuTab'},
    // { menuId: 'gm-22016', menuName: '图标Icons', upMenuId: 'gm-22000', menuIcon: '', funcId: 'icons', funcUrl: 'pages/example/package/icons'},
    // { menuId: 'gm-22017', menuName: '文件上传附带MD5', upMenuId: 'gm-22000', menuIcon: '', funcId: 'uploadWidthMD5', funcUrl: 'pages/example/native/uploadWidthMD5'},
    // { menuId: 'gm-22018', menuName: '常见图表', upMenuId: 'gm-22000', menuIcon: '', funcId: 'lineBarPie', funcUrl: 'pages/example/native/lineBarPie'},

    //三级菜单
    // { menuId: 'gm-23100', menuName: '查询类', upMenuId: 'gm-23000', menuIcon: 'el-icon-yx-search'},
    // { menuId: 'gm-23200', menuName: '表单类', upMenuId: 'gm-23000', menuIcon: 'el-icon-yx-ungroup'},

    //四级菜单
    // { menuId: 'gm-23101', menuName: '普通查询', upMenuId: 'gm-23100', menuIcon: '', funcId: 'exampleQuery', funcUrl: 'pages/example/template/exampleQuery'},
    // { menuId: 'gm-23102', menuName: '树+查询', upMenuId: 'gm-23100', menuIcon: '', funcId: 'exampleTree', funcUrl: 'pages/example/template/exampleTree'},
    // { menuId: 'gm-23103', menuName: '查询+表单（编辑）', upMenuId: 'gm-23100', menuIcon: '', funcId: 'exampleEdit', funcUrl: 'pages/example/template/exampleEdit'},
    // { menuId: 'gm-23104', menuName: '查询+表格（主从表）', upMenuId: 'gm-23100', menuIcon: '', funcId: 'searchTable', funcUrl: 'pages/example/template/searchTable'},
    // { menuId: 'gm-23105', menuName: 'Tab页签+查询', upMenuId: 'gm-23100', menuIcon: '', funcId: 'tabsearch', funcUrl: 'pages/example/template/tabsearch'},
    // { menuId: 'gm-23106', menuName: '查询嵌套表格', upMenuId: 'gm-23100', menuIcon: '', funcId: 'queryNestedTable', funcUrl: 'pages/example/template/queryNestedTable'},
    // { menuId: 'gm-23107', menuName: '查询嵌套表单', upMenuId: 'gm-23100', menuIcon: '', funcId: 'queryNestedForm', funcUrl: 'pages/example/template/queryNestedForm'},
    // { menuId: 'gm-23108', menuName: '高级查询', upMenuId: 'gm-23100', menuIcon: '', funcId: 'exampleMoreQuery', funcUrl: 'pages/example/template/exampleMoreQuery'},

    //四级菜单
    // { menuId: 'gm-23201', menuName: '普通表单（编辑）', upMenuId: 'gm-23200', menuIcon: '', funcId: 'exampleForm', funcUrl: 'pages/example/template/exampleForm'},
    // { menuId: 'gm-23202', menuName: '普通表单（详情）', upMenuId: 'gm-23200', menuIcon: '', funcId: 'exampleFormInfo', funcUrl: 'pages/example/template/exampleFormInfo'},
    // { menuId: 'gm-23203', menuName: '分组表单', upMenuId: 'gm-23200', menuIcon: '', funcId: 'exampleGroup', funcUrl: 'pages/example/template/exampleGroup'},
    // { menuId: 'gm-23204', menuName: '表单+列表', upMenuId: 'gm-23200', menuIcon: '', funcId: 'tableList', funcUrl: 'pages/example/template/tableList'},
    // { menuId: 'gm-23205', menuName: 'Tab页签表单', upMenuId: 'gm-23200', menuIcon: '', funcId: 'tabform', funcUrl: 'pages/example/template/tabform'},
    // { menuId: 'gm-23206', menuName: '表单内嵌套Tabs', upMenuId: 'gm-23200', menuIcon: '', funcId: 'formNestTabs', funcUrl: 'pages/example/template/formnesttab'},
    // { menuId: 'gm-23207', menuName: 'Steps步骤表单', upMenuId: 'gm-23200', menuIcon: '', funcId: 'exampleSteps1', funcUrl: 'pages/example/template/exampleSteps1'},

    //三级菜单
    // { menuId: 'gm-24101', menuName: '增删改查', upMenuId: 'gm-24000', menuIcon: '', funcId: 'gridCrud', funcUrl: 'pages/example/native/gridCrud'},
    // { menuId: 'gm-24102', menuName: '普通多表头', upMenuId: 'gm-24000', menuIcon: '', funcId: 'multiplegrid', funcUrl: 'pages/example/native/multiplegrid'},
    // { menuId: 'gm-24103', menuName: '动态多表头', upMenuId: 'gm-24000', menuIcon: '', funcId: 'dynamicMultipleGrid', funcUrl: 'pages/example/native/dynamicMultipleGrid'},
    // { menuId: 'gm-24104', menuName: '可编辑表格', upMenuId: 'gm-24000', menuIcon: '', funcId: 'editorGrid', funcUrl: 'pages/example/native/editorGrid'},
    // { menuId: 'gm-24105', menuName: '左树右表格', upMenuId: 'gm-24000', menuIcon: '', funcId: 'treedemo', funcUrl: 'pages/example/native/treedemo'},
    // { menuId: 'gm-24106', menuName: 'TAB页签', upMenuId: 'gm-24000', menuIcon: '', funcId: 'tab', funcUrl: 'pages/example/native/tab'},



// -------------------------修改By Mr_Jiang--------------------------
    //自定义目录一级菜单
    { menuId: 'gm-60000', menuName: 'Pad-系统管理', upMenuId: '0', menuIcon: 'el-icon-yx-books'},


    //自定义目录二级菜单
    { menuId: 'gm-61000', menuName: 'Pad角色管理', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus',funcId: 'pad_rolemanagement', funcUrl: 'pages/auth/pad_rolemanagement/pad_rolemanagement'},
    { menuId: 'gm-62000', menuName: 'Pad员工管理', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus',funcId: 'pad_usermanagement', funcUrl: 'pages/auth/pad_usermanagement/pad_usermanagement'},
    { menuId: 'gm-63000', menuName: 'Pad功能管理', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus',funcId: 'pad_funcmanagement', funcUrl: 'pages/auth/pad_funcmanagement/pad_funcmanagement'},
    { menuId: 'gm-64000', menuName: 'Pad功能组管理', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus',funcId: 'pad_applicationmanagement', funcUrl: 'pages/auth/pad_applicationmanagement/pad_applicationmanagement'},
    { menuId: 'gm-65000', menuName: 'Pad机构信息管理', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus',funcId: 'pad_orgmanagement', funcUrl: 'pages/auth/pad_orgmanagement/pad_orgmanagement'},
    // { menuId: 'gm-66000', menuName: '问题反馈', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus', funcId: 'pad_question', funcUrl: 'pages/auth/pad_questionfeedback/pad_questionfeedback'},
    // { menuId: 'gm-67000', menuName: '需求反馈', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus', funcId: 'pad_demand', funcUrl: 'pages/auth/pad_demandfeedback/pad_demandfeedback'},
    // { menuId: 'gm-68000', menuName: '首页产品展示', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus', funcId: 'pad_productdisplay', funcUrl: 'pages/auth/pad_questionfeedback/pad_questionfeedback'},
    // { menuId: 'gm-69000', menuName: '产品信息', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus', funcId: 'pad_productmessage', funcUrl: 'pages/auth/pad_questionfeedback/pad_questionfeedback'},
    // { menuId: 'gm-69100', menuName: '数据字典', upMenuId: 'gm-60000', menuIcon: 'el-icon-yx-folder-plus', funcId: 'pad_datadictionary', funcUrl: 'pages/auth/pad_questionfeedback/pad_questionfeedback'},













];

/**
 * 模拟菜单控制点数据
 * @type {Array}
 */
var demoCtrls = [
    { menuId: 'gm-23101', funcId: 'exampleQuery', ctrlCode: 'create', ctrlName: '新增' },
    { menuId: 'gm-23101', funcId: 'exampleQuery', ctrlCode: 'edit', ctrlName: '修改' },
    { menuId: 'gm-23101', funcId: 'exampleQuery', ctrlCode: 'detail', ctrlName: '详情' },
    { menuId: 'gm-23101', funcId: 'exampleQuery', ctrlCode: 'delete', ctrlName: '删除' },
    { menuId: 'gm-23101', funcId: 'exampleQuery', ctrlCode: 'export', ctrlName: '导出' },

    { menuId: 'gm-23102', funcId: 'exampleTree', ctrlCode: 'create', ctrlName: '新增' },
    { menuId: 'gm-23102', funcId: 'exampleTree', ctrlCode: 'edit', ctrlName: '修改' },
    { menuId: 'gm-23102', funcId: 'exampleTree', ctrlCode: 'detail', ctrlName: '详情' }
];

/**
 * 模拟数据权限数据
 * @type
 */
var demoDataContr = [
    { authId: '', authTmplId: '', contrId: '', contrInclude: '', contrUrl: '', sqlName: '', sqlString: '', sysId: '' }
];
