/**
 * Created by Mr_Jiang on 2018/7/17
 */
define(function (require) {
    //定义路由表
    var routeTable = {
        pad_applicationmanagement: {
            html: 'pages/auth/pad_applicationmanagement/pad_applicationmanagement.html',
            // css: 'pages/auth/fox_rolemanagement/fox_rolemanagement.css',
            js: 'pages/auth/pad_applicationmanagement/pad_applicationmanagement.js'
        },
    };
    //注册路由表
    yufp.router.addRouteTable(routeTable);
});
