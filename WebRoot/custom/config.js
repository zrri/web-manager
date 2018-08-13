/**
 * Created by 江成 on 2016/08/09.
 */

define(function (require, exports, module) {

    // 设置配置
    var config = {
        // 请求URL
        // url: 'localhost:63342',
        //url: '192.144.128.218:9191',
         // url: '139.199.79.118:9191',
       url:'139.199.79.118:9193',
       // url:'132.7.47.119:9191',
       // url:'127.0.0.1:9191',
       // url:'172.20.10.10:9191',

        // 是否启用SSL
        ssl: false,
        // web socket 通信方式
        webSocketType: 'get',
        //默认root id
        defaultRootId: '_rootDiv',
        //开始页面
        startPage: 'login',
        //录制模式
        recorderModel: false,
        //录制范围
        recorderScope: ['yufp.service'],
        //调试模式
        debugModel: true,
        //调试范围
        debugScope: ['yufp.service'],
        //紧凑模式
        compactMode: false
    };
    //保存配置
    module.exports = config;

});
