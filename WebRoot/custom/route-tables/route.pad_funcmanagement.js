/**
 * Created by Mr_Jiang on 2018/7/17
 */
define(function (require) {
    //定义路由表
    var routeTable = {
        pad_funcmanagement: {
            html: 'pages/auth/pad_funcmanagement/pad_funcmanagement.html',
            // css: 'pages/auth/fox_rolemanagement/fox_rolemanagement.css',
            js: 'pages/auth/pad_funcmanagement/pad_funcmanagement.js'
        },
    };
    //注册路由表
    yufp.router.addRouteTable(routeTable);
});
