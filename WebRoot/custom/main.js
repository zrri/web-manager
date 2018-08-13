/**
 * Created by jiangcheng on 2016/08/09.
 * @updated by liujie on 2018/03/13 根据环境设置调用是否压缩的js
 * @description 全局配置入口
 */
(function (window, fox) {
  // yu配置
  fox.config({
    namespace:['yu','yufp'],
    // path配置
    paths: {
      'plugins': './custom/plugins'
    },
    // 别名配置
    alias: {
      'config': './custom/config.js',
      'vue': './libs/vue/vue-2.3.4.js',
      'jquery': './libs/jquery/jquery-1.8.0.js',
      'echarts': './libs/echarts/echarts.min.js'
    },
    // 文件编码
    charset: 'UTF-8',
    // 版本号
    version: '1.0.3'
  });

  // css依赖库
  var libsCss = [
    './libs/element-ui/index.css',
    './libs/swiper/idangerous.swiper.css',
    './themes/common/icoFonts/icoFonts.css',
    './themes/common/tree-menu.css',
    './themes/default/main.css',
    './custom/templates/silk-1.0.0/css/silk.ui.layer.css',
      "./custom/widgets/css/components.css",
  ];

    // js依赖库
  var libsJs = [
    'config',
    'vue',
    'jquery',
    './libs/element-ui/index.js',
    './libs/swiper/idangerous.swiper.min.js',
    './libs/js-xlsx/xlsx.full.min.js',
    './custom/common/app.data.service.js',
    './custom/templates/silk-1.0.0/js/silk.ui.layer.js'
  ];

  // 路由表
  var routeTables = [
    './custom/route-tables/route.common.js',
    './custom/route-tables/route.pad_rolemanagement.js',
    './custom/route-tables/route.pad_usermanagement.js',
    './custom/route-tables/route.pad_funcmanagement.js',
    './custom/route-tables/route.pad_orgmanagement.js',
    './custom/route-tables/route.pad_applicationmanagement.js',





  ];

  var env = 'DEV'; // 设置当前环境 PRD/UAT/DEV
  // 根据运行环境参数 选择读取不同的js 原始js/合并js/压缩js
  switch (env) {
    case 'DEV':
      libsJs = libsJs.concat([
        './custom/plugins/yufp.settings.js',
        './custom/plugins/yufp.base64.js',
        './custom/plugins/yufp.localstorage.js',
        './custom/plugins/yufp.sessionstorage.js',
        './custom/plugins/yufp.service.js',
        './custom/plugins/yufp.validator.js',
        './custom/plugins/yufp.util.js',
        './custom/plugins/yufp.lookup.js',
        './custom/plugins/yufp.frame.js',
        './custom/plugins/yufp.session.js',
        './custom/common/app.js',
          './custom/plugins/fox.service.js',
          "./custom/widgets/js/components.js",
      ]);
      break;
    case 'UAT':
      libsJs = libsJs.concat([
        './custom/build/packagejs/yu-custom-debug.js'
      ]);
      break;
    case 'PRD':
      libsJs = libsJs.concat([
        './custom/build/packagejs/yu-custom-min.js'
      ]);
      break;
  }

  // route_tables信息和app.js 打包一起导致平台加载先后顺序异常
  // 合并lib
  var libs = libsCss.concat(libsJs, routeTables);
  yu.require.use(libs).done(function () {
    yu.$ = $;

    // 导入配置
    var config = yu.require.require('config');
    // 设置配置
    yu.settings.config(config);

    // mock加载
    if (config.debugModel) {
      yu.require.require('./mocks/index.js');
    }

    // 加入请求过滤器
    yu.service.addFilter({

      // 过滤器名称
      name: 'messageParser',

      // 请求前触发
      before: function (event) {
        // 定义请求头
        var headers = {};
        // 定义请求数据
        var reqData = {
          // 请求头
          headers: headers,
          // 请求数据
          data: event.data
        };
        // 保存导出数据
        event.code = 0;
        event.data = reqData;
        // 返回处理标志，true则继续处理，false则中断处理
        return true;
      },

      // 数据返回后触发
      after: function (event) {
        // 只处理JSON对象
        if (yu.type(event.data) == 'object' && yu.type(event.data.header) != 'undefined') {
          // 获取响应头
          var rspHeader = event.data.header;
          // 获取响应数据
          var rspData = event.data.data;

          if (yu.type(rspHeader.code) == 'undefined' || rspHeader.code == 0) {
            // 保存导出数据
            event.code = 0;
            event.message = '';
            event.data = rspData;
            // 返回处理标志，true则继续处理，false则中断处理
            return true;
          } else {
            // 保存导出数据
            event.code = rspHeader.code;
            event.message = rspHeader.msg;
            event.data = rspData;
            // 返回处理标志，true则继续处理，false则中断处理
            return true;
          }
        }

        // 返回处理标志，true则继续处理，false则中断处理
        return true;
      },
      // HTTP请求异常
      exception: function (event) {
        var status = event.status;
        var flag = true;
        var me = yu.custom.vue({});
        switch (status) {
        case 401:
          yu.session.logout();
          flag = false;
          break;
        case 403:
          me.$message({
            message: '您无权限访问，请联系系统管理员!',
            type: 'warning'
          });
          flag = false;
          break;
        case 404:
          me.$message({
            message: '系统错误，请联系系统管理员!',
            type: 'error'
          });
          flag = false;
          break;
        default:
          me.$message({
            message: '系统错误，请联系系统管理员!',
            type: 'error'
          });
          flag = false;
          break;
        }
        return flag;
      }
    });
    // 加入请求过滤器
    yufp.service1.addFilter({

      // 过滤器名称
      name: 'messageParser',

      // 请求前触发
      before: function (event) {
        // 定义请求头
        var headers = {};
        // 定义请求数据
        var reqData = {
          // 请求头
          headers: headers,
          // 请求数据
          data: event.data
        };
        // 保存导出数据
        event.code = 0;
        event.data = reqData;
        // 返回处理标志，true则继续处理，false则中断处理
        return true;
      },

      // 数据返回后触发
      after: function (event) {
        // 只处理JSON对象
        if (yu.type(event.data) == 'object' && yu.type(event.data.header) != 'undefined') {
          // 获取响应头
          var rspHeader = event.data.header;
          // 获取响应数据
          var rspData = event.data.data;

          if (yu.type(rspHeader.code) == 'undefined' || rspHeader.code == 0) {
            // 保存导出数据
            event.code = 0;
            event.message = '';
            event.data = rspData;
            // 返回处理标志，true则继续处理，false则中断处理
            return true;
          } else {
            // 保存导出数据
            event.code = rspHeader.code;
            event.message = rspHeader.msg;
            event.data = rspData;
            // 返回处理标志，true则继续处理，false则中断处理
            return true;
          }
        }

        // 返回处理标志，true则继续处理，false则中断处理
        return true;
      },
      // HTTP请求异常
      exception: function (event) {
        var status = event.status;
        var flag = true;
        var me = yu.custom.vue({});
        switch (status) {
        case 401:
          yu.session.logout();
          flag = false;
          break;
        case 403:
          me.$message({
            message: '您无权限访问，请联系系统管理员!',
            type: 'warning'
          });
          flag = false;
          break;
        case 404:
          me.$message({
            message: '系统错误，请联系系统管理员!',
            type: 'error'
          });
          flag = false;
          break;
        default:
          me.$message({
            message: '系统错误，请联系系统管理员!',
            type: 'error'
          });
          flag = false;
          break;
        }
        return flag;
      }
    });

    // 设置默认root id
    yu.router.setDefaultRootId(config.defaultRootId);
    // 加入路由过滤器
    yu.router.addFilter({

      /**
       * 过滤器名称
       */
      name: 'default',

      /**
       * 路由跳转前执行
       * @param code
       * @param cite
       */
      before: function (code, data, cite) {
        if (config.debugModel) {
          var route = yu.router.getRoute(code) || {};
          yu.logger.info('【Router-JS】【' + code + '】: ' + route.js);
        }
        return true;
      },

      /**
       * 加载路由内容前执行
       * @param code
       * @param cite
       */
      mount: function (code, cite) {
      },

      /**
       * ready函数执行
       * @param exports
       * @param code
       * @param data
       * @param cite
       */
      ready: function (exports, code, data, cite) {
      },

      /**
       * 卸载路由内容前执行
       * @param code
       * @param cite
       */
      unMount: function (code, cite) {

      },

      /**
       * destroy函数执行
       * @param exports
       * @param code
       * @param cite
       */
      destroy: function (exports, code, cite) {

      }

    });

    //创建hash处理事件
    var hashFn=function(defaultHash){
        var sIndex = location.hash.indexOf("!");
        var eIndex = location.hash.indexOf("?");

        if (sIndex != -1) {
            //定义hash
            var hash = "";
            //定义data
            var data = {};

            if (eIndex == -1) {
                //获取hash
                hash = location.hash.slice(sIndex + 1);
            } else {
                //获取hash
                hash = location.hash.substring(sIndex + 1, eIndex);
                var queryStr = location.hash.slice(eIndex + 1);
                var items = queryStr.split("&");
                //解析数据
                for (var i = 0; i < items.length; i++) {
                    var ss = items[i].split("=");
                    data[ss[0]] = ss[1];
                }
            }
            fox.logger.info("触发hash事件,hash:" + hash);
            //路由跳转
            fox.router.to(hash, data);

        }else if(defaultHash && $.type(defaultHash)==="string"){
            //路由跳转
            fox.router.to(defaultHash);
        }

    };
    // 添加hash change事件
    if (window.addEventListener) {
      window.addEventListener('hashchange', hashFn, false);
    } else if (window.attachEvent) {
      window.attachEvent('on' + 'hashchange', hashFn);
    } else {
      window['onhashchange'] = hashFn;
    }
    // 页面跳转
    hashFn(config.startPage);
  })
}(window, fox));
