/**
 * fox service
 * Created by 江成 on 2016/08/23.
 */
(function (fox, window, factory) {

    // 判断是否支持模块定义
    var hasDefine = (typeof define === 'function');
    if (hasDefine) {
        //获取对象
        var exports = factory(fox, window, window.document);
        //定义模块
        define(exports);
        //安装插件(兼容非模块的访问方式)
        window.yufp.service1 = exports;

    } else {
        //获取对象
        var exports = factory(fox, window, window.document);
        //安装插件
        window.yufp.service1 = exports;
    }

}(fox, window, function (fox, window, document) {

        /**
         * Http Client
         * @constructor
         */
        function HttpClient() {
            //配置
            this.options = {};
            //服务调用次数统计
            this._count = 0;
            //令牌token种子
            this._tokenSeed = 0;
            //过滤器集合
            this._filters = [];
            //token注册表
            this._tokenRegister = {};
            //message service
            this._msgService = {};
            //记录session id
            this._sessionId = undefined;
        }

        /**
         * 显示cover
         * @private
         */
        HttpClient.prototype._showCover = function _showCover() {
            //计算重复调用次数
            this._count += 1;
            //只有count为1，才允许绘制界面
            if (this._count > 1) {
                return;
            }
            //打开cover
            this.coverId=fox.layer.open({
                type: 2,
                shadeClose:false
            });
        };

        /**
         * 隐藏cover
         * @private
         */
        HttpClient.prototype._hideCover = function _hideCover() {
            //计算重复调用次数
            this._count -= 1;
            //只有count为0，才允许关闭界面
            if (this._count > 0) {
                return;
            }
            //隐藏
            fox.layer.close(this.coverId);
        };


        /**
         * 获取request URL
         * @param args
         * @returns {any}
         */
        HttpClient.prototype.getRequestUrl = function getRequestUrl(args) {

            if (args && args.requestUrl) {
                return args.requestUrl;
            }

            var path = "serviceInvoke";
            if (args && args.path) {
                path = args.path + ".do";
            }

            if (!fox.settings.ssl) {
                var url = "http://" + fox.settings.url + "/services/" + path;
                return url;
            } else {
                var url = "https://" + fox.settings.url + "/services/" + path;
                return url;
            }
        };

        /**
         * 获取web socket URL for put
         * @param args
         * @returns {any}
         */
        HttpClient.prototype.getPutWebSocketUrl = function getWebSocketUrl(args) {

            if (args && args.webSocketUrl) {
                return args.webSocketUrl;
            }

            if (!fox.settings.ssl) {
                var url = "ws://" + fox.settings.url + "/services/webSocketAcceptor";
                return url;
            } else {
                var url = "wss://" + fox.settings.url + "/services/webSocketAcceptor";
                return url;
            }

        };

        /**
         * 获取web socket URL for get
         * @param args
         * @returns {any}
         */
        HttpClient.prototype.getGetWebSocketUrl = function getWebSocketUrl(args) {

            if (args && args.webSocketUrl) {
                return args.webSocketUrl;
            }

            if (!fox.settings.ssl) {
                var url = "http://" + fox.settings.url + "/services/webSocketAcceptor";
                return url;
            } else {
                var url = "https://" + fox.settings.url + "/services/webSocketAcceptor";
                return url;
            }

        };

        /**
         * 获取session id
         * @returns {*}
         */
        HttpClient.prototype.getSessionId = function getSessionId() {
            if (this._sessionId == undefined) {
                this._sessionId = fox.sessionStorage.get("_$sessionId");
            }
            return this._sessionId;
        };

        /**
         * 设置session id
         * @param sessionid
         */
        HttpClient.prototype.setSessionId = function setSessionId(sessionId) {
            this._sessionId = sessionId;
            fox.sessionStorage.put("_$sessionId", sessionId, true);
        };

        /**
         * 获取session id
         * @returns {{}}
         */
        HttpClient.prototype._getSessionId = function _getSessionId() {
            //定义deferred
            var deferred = new fox.core.Deferred();
            //获取session id
            var sessionId = this.getSessionId();
            //判断session是否已经存在
            if (sessionId) {
                //成功通知
                deferred.resolve(sessionId);
            } else {
                //获取地址
                var requestUrl = this.getRequestUrl();

                var _this = this;
                //发起请求
                fox.core.ajax({
                    url: requestUrl,
                    async: true,
                    type: 'POST',
                    cache: false,
                    data: {_$type: "options", _$actions: "uuid"},
                    //成功处理
                    success: function (data, status, xhr) {
                        if(xhr.responseText===""){
                            //失败通知
                            deferred.reject(status);
                            return;
                        }
                        //解析数据
                        var resData = JSON.parse(xhr.responseText);
                        //重新获取session id
                        var sessionId = _this.getSessionId();
                        if (sessionId == undefined) {
                            //保存session id
                            sessionId = resData["uuid"];
                            //更新session id
                            _this.setSessionId(sessionId);
                        }
                        //成功通知
                        deferred.resolve(sessionId);
                    },
                    //错误处理
                    error: function (xhr, status) {
                        //失败通知
                        deferred.reject(status);
                    }

                });
            }
            return deferred;
        };

        /**
         * 获取token
         * @returns {string}
         * @private
         */
        HttpClient.prototype._getToken = function () {
            this._tokenSeed += 1;
            if (this._tokenSeed < 0) {
                this._tokenSeed = 0;
            }
            var curData = new Date();
            var token = this._tokenSeed + "-" + curData.getTime();
            return token;
        };

        /**
         * 增加filter
         * @param args
         */
        HttpClient.prototype.addFilter = function (args) {

            //判断是否为json对象
            if (typeof args != "object") {
                logger.error("filter args must been json object");
                return;
            }

            //判断是否有name属性
            if (!args["name"]) {
                logger.error("filter args must have name attribute");
                return;
            }

            //判断是否有before函数
            if (!args["before"] || typeof args["before"] != "function") {
                fox.logger.error("filter args muse have before function");
                return;
            }

            //判断是否有before函数
            if (!args["after"] || typeof args["after"] != "function") {
                fox.logger.error("filter args muse have after function");
                return;
            }

            //判断是否有exception函数
            if (!args["exception"] || typeof args["exception"] != "function") {
                fox.logger.error("filter args muse have exception function");
                return;
            }

            this._filters.push(args);
        };

        /**
         * 移除过滤器
         * @param args
         */
        HttpClient.prototype.removeFilter = function (args) {
            //获取过滤器名称
            var name;
            if (typeof args == "string") {
                name = args;
            } else {
                name = args["name"];
            }

            //查找待删除过滤器
            var i = 0;
            for (; i < this._filters.length; i++) {
                if (name == this._filters[i].name) {
                    break;
                }
            }

            //删除过滤器
            if (i < this._filters.length) {
                this._filters.splice(i, 1);
            }
        };

        /**
         * 执行过滤器
         * @param type
         * @param data
         * @param exports
         * @returns {*}
         * @private
         */
        HttpClient.prototype._doFilter = function (type, event) {
            if (type == 0) {
                //按顺序执行
                for (var i = 0; i < this._filters.length; i++) {
                    if (!this._filters[i].before) {
                        continue;
                    }
                    var res = this._filters[i].before(event);
                    //如果返回false，则中断处理
                    if (res == false) {
                        return false;
                    }
                }
                return true;
            } else if (type == 1) {
                //按顺序执行
                for (var i = 0; i < this._filters.length; i++) {
                    if (!this._filters[i].after) {
                        continue;
                    }
                    var res = this._filters[i].after(event);
                    //如果返回false，则中断处理
                    if (res == false) {
                        return false;
                    }
                }
                return true;
            } else {
                //按顺序执行
                for (var i = 0; i < this._filters.length; i++) {
                    if (!this._filters[i].exception) {
                        continue;
                    }
                    var res = this._filters[i].exception(event);
                    //如果返回false，则中断处理
                    if (res == false) {
                        return false;
                    }
                }
                return true;

            }
        };

        /**
         * 获取json对象参数
         * @param args
         * @returns {any}
         */
        function getJsonArg(args) {
            if (args.length == 1 && typeof args[0] == "object") {
                var jsonArg = args[0];
                return jsonArg;
            } else {
                var jsonArg = {};
                for (var i = 0; i < args.length; i++) {
                    if (i == 0) {
                        jsonArg.name = args[i];
                    } else if (i == 1) {
                        jsonArg.data = args[i];
                    } else if (i == 2) {
                        jsonArg.callback = args[i];
                    } else if (i == 3) {
                        jsonArg.id = args[i];
                    } else if (i == 4) {
                        jsonArg.dataType = args[i];
                    } else if (i == 5) {
                        jsonArg.timeout = args[i];
                    }
                }
                return jsonArg;
            }

        }

        /**
         * 请求服务
         * @returns {{}}
         */
        HttpClient.prototype.request = function () {
            //获取调用参数
            var arg = getJsonArg(arguments);
            //定义deferred
            var deferred = new fox.core.Deferred();

            arg.cover = false;

            //判断是否启用cover
            if (arg.cover === undefined || arg.cover === true) {
                //显示cover
                this._showCover();
            }

            //-------------------------------- debug 模式 ---------------------------------------------
            if (fox.settings.debugModel && fox.settings.debugScope.indexOf("fox.service")!=-1) {
                //调试请求模拟
                var debugRequestURL;
                //服务配置映射
                var serviceMapURL = "debug/service/services.json";
                //获取服务映射配置
                fox.core.ajax({
                    url: serviceMapURL,
                    async: false,
                    type: 'GET',
                    cache: false,
                    data: {},
                    //成功处理
                    success: function (data, status, xhr) {
                        if ("string" == fox.type(data)) {
                            data = JSON.parse(data);
                        }
                        //获取模拟数据
                        debugRequestURL = data[arg.name];
                    },
                    //错误处理
                    error: function (xhr, status) {
                        //判断是否启用cover
                        if (arg.cover == undefined || arg.cover == true) {
                            //隐藏cover
                            _this._hideCover();
                        }
                        //通知调用失败
                        deferred.reject(2, status,"");
                        //判断是否存在回调函数
                        if (arg.callback) {
                            //失败回调
                            arg.callback(2, status, "");
                        }
                    }
                });

                //调试请求
                if (debugRequestURL) {
                    //解决引用关系
                    var _this = this;
                    //发起调试请求
                    fox.core.ajax({
                        url: debugRequestURL,
                        async: true,
                        type: 'GET',
                        cache: false,
                        data: {},
                        //成功处理
                        success: function (data, status, xhr) {
                            //判断是否启用cover
                            if (arg.cover == undefined || arg.cover == true) {
                                //隐藏cover
                                _this._hideCover();
                            }

                            if ("string" == fox.type(data)) {
                                data = JSON.parse(data);
                            }

                            //定义过滤事件
                            var event = {
                                code: 0,
                                message: 'success',
                                data: data
                            };
                            //执行调用后过滤器
                            var res = _this._doFilter(1, event);
                            //过滤器中断调用处理
                            if (res == false) {
                                var code = event.code ? event.code : 1;
                                //通知调用失败
                                deferred.reject(code, event.message, event.data);
                                //判断是否存在回调函数
                                if (arg.callback) {
                                    //失败回调
                                    arg.callback(code, event.message, event.data);
                                }
                                return;
                            }
                            //通知调用成功
                            deferred.resolve(event.code, event.message, event.data);
                            //判断是否存在回调函数
                            if (arg.callback) {
                                //执行回调函数
                                arg.callback(event.code, event.message, event.data);
                            }
                        },
                        //错误处理
                        error: function (xhr, status) {
                            //判断是否启用cover
                            if (arg.cover == undefined || arg.cover == true) {
                                //隐藏cover
                                _this._hideCover();
                            }
                            var msg = xhr.responseText;
                            if (!msg || msg == "") {
                                msg = status;
                            }

                            //定义过滤事件
                            var event = {
                                code: 1,
                                message: msg,
                            };
                            //执行调用后过滤器
                            var res = _this._doFilter(2, event);
                            //过滤器中断调用处理
                            if (res == false) {
                                return;
                            }

                            //重复提交处理
                            if ("resubmit" == msg) {
                                fox.logger.error("resubmit error");
                                return true;
                            }

                            var code = event.code ? event.code : 1;
                            //通知调用失败
                            deferred.reject(code, event.message, event.data);
                            //判断是否存在回调函数
                            if (arg.callback) {
                                //失败回调
                                arg.callback(code, event.message, event.data);
                            }
                        }
                    });
                    //进入调试，直接返还
                    return deferred;
                }
            }
            //-------------------------------- debug 模式---------------------------------------------

            try {
                if (!arg.dataType) {
                    arg.dataType = "json";
                }
                //设置请求超时时间
                if (!arg.timeout) {
                    arg.timeout = 60000;
                }

                //获取id
                if (!arg.id) {
                    arg.id = "0";
                } else if (typeof arg.id != "string") {
                    arg.id = arg.id.toString();
                }

                //获取token id
                var token = this._tokenRegister[arg.id];
                if (!token) {
                    //获取新的token id
                    token = this._getToken();
                    //注册token id
                    this._tokenRegister[arg.id] = token;
                }

                //获取地址
                var requestUrl = this.getRequestUrl();

                //获取数据
                var data = arg.data;
                if (!data) {
                    data = {};
                }

                //定义过滤事件
                var event = {
                    data: data
                };
                //执行调用前过滤器
                var res = this._doFilter(0, event);
                //过滤器中断调用处理
                if (res == false) {
                    var code = event.code ? event.code : 2;
                    //通知调用失败
                    deferred.reject(code, event.message, event.data);
                    //判断是否存在回调函数
                    if (arg.callback) {
                        //失败回调
                        arg.callback(code, event.message, event.data);
                    }
                    return deferred;
                }
                //获取请求数据
                var reqData = event.data;
                var jsonStr = undefined;
                //请求数据转换为字符串
                if (fox.type(reqData) == "object") {
                    jsonStr = JSON.stringify(reqData);
                } else {
                    jsonStr = reqData;
                }

                //解决引用关系
                var _this = this;
                //获取session id 并调用service
                this._getSessionId().done(function (sessionId) {
                    //发起ajax请求
                    fox.core.ajax({
                        url: requestUrl,
                        data: {
                            _$type: 'service',
                            _$id: arg.id,
                            _$service: arg.name,
                            _$data: jsonStr,
                            _$sessionid: sessionId,
                            _$token: token
                        },
                        type: 'POST',
                        cache: false,
                        contentType: "application/x-www-form-urlencoded",
                        dataType: arg.dataType,
                        timeout: arg.timeout,
                        //消息发送前处理
                        beforeSend:function(xhr,reqArgs){
                            //-------------------------------- recorder 模式 -----------------------------------------
                            if(fox.settings.recorderModel && fox.settings.recorderScope.indexOf("fox.service")!=-1) {
                                fox.logger.info("[service send]  name:" + arg.name + " ,request:" + JSON.stringify(reqArgs.data));
                            }
                            //-------------------------------- recorder 模式 -----------------------------------------
                        },
                        //成功处理
                        success: function (data, status, xhr) {
                            //移除token id
                            delete _this._tokenRegister[arg.id];
                            //判断是否启用cover
                            if (arg.cover == undefined || arg.cover == true) {
                                //隐藏cover
                                _this._hideCover();
                            }

                            //-------------------------------- recorder 模式 -----------------------------------------
                            if(fox.settings.recorderModel && fox.settings.recorderScope.indexOf("fox.service")!=-1) {
                                fox.logger.info("[service receive]  name:" + arg.name + " ,response:" + JSON.stringify(data));
                            }
                            //-------------------------------- recorder 模式 -----------------------------------------


                                //定义过滤事件
                            var event = {
                                code: 0,
                                message: "success",
                                data: data
                            };
                            //执行调用后过滤器
                            var res = _this._doFilter(1, event);
                            //过滤器中断调用处理
                            if (res == false) {
                                var code = event.code ? event.code : 1;
                                //通知调用失败
                                deferred.reject(code, event.message, event.data);
                                //判断是否存在回调函数
                                if (arg.callback) {
                                    //失败回调
                                    arg.callback(code, event.message, event.data);
                                }
                                return;
                            }
                            //通知调用成功
                            deferred.resolve(event.code, event.message, event.data);
                            //判断是否存在回调函数
                            if (arg.callback) {
                                //执行回调函数
                                arg.callback(event.code, event.message, event.data);
                            }

                        },
                        //错误处理
                        error: function (xhr, status) {
                            //移除token id
                            delete _this._tokenRegister[arg.id];
                            //判断是否启用cover
                            if (arg.cover == undefined || arg.cover == true) {
                                //隐藏cover
                                _this._hideCover();
                            }
                            var msg = xhr.responseText;
                            if (!msg || msg == "") {
                                msg = status;
                            }

                            //定义过滤事件
                            var event = {
                                code: 1,
                                message: msg,
                            };
                            //执行调用后过滤器
                            var res = _this._doFilter(2, event);
                            //过滤器中断调用处理
                            if (res == false) {
                                return;
                            }

                            //重复提交处理
                            if ("resubmit" == msg) {
                                logger.error("resubmit error");
                                return true;
                            }

                            var code = event.code ? event.code : 1;
                            //通知调用失败
                            deferred.reject(code, event.message, event.data);
                            //判断是否存在回调函数
                            if (arg.callback) {
                                //失败回调
                                arg.callback(code, event.message, event.data);
                            }
                        }
                    });
                }).fail(function (msg) {
                    //移除token id
                    delete _this._tokenRegister[arg.id];
                    //判断是否启用cover
                    if (arg.cover == undefined || arg.cover == true) {
                        //隐藏cover
                        _this._hideCover();
                    }

                    //定义过滤事件
                    var event = {
                        code: 1,
                        message: msg,
                    };
                    //执行调用后过滤器
                    var res = _this._doFilter(2, event);
                    //过滤器中断调用处理
                    if (res == false) {
                        return;
                    }
                    var code = event.code ? event.code : 1;
                    //通知调用失败
                    deferred.reject(code, event.message, event.data);
                    //判断是否存在回调函数
                    if (arg.callback) {
                        //失败回调
                        arg.callback(code, event.message, event.data);
                    }
                });
                //返回defferred
                return deferred;
            } catch (e) {
                //判断是否启用cover
                if (arg.cover == undefined || arg.cover == true) {
                    //隐藏cover
                    this._hideCover();
                }

                //定义过滤事件
                var event = {
                    code: 1,
                    message: e.message,
                };
                //执行调用后过滤器
                var res = this._doFilter(2, event);
                //过滤器中断调用处理
                if (res == false) {
                    return;
                }
                var code = event.code ? event.code : 1;
                //通知调用失败
                deferred.reject(code, event.message, event.data);
                //判断是否存在回调函数
                if (arg.callback) {
                    //失败回调
                    arg.callback(code, event.message, event.data);
                }

                //抛出异常
                throw e;
            }
        };

        /**
         * 获取顺序请求对象
         */
        HttpClient.prototype.sequence = function() {
            return new RequestChain();
        };


        /**
         *  获取json对象参数
         * @param args
         * @returns {*}
         */
        function getJsonArgForUploadFile(args) {
            if (args.length == 1 && fox.type(args[0]) == "object") {
                var jsonArg = args[0];
                return jsonArg;
            } else {
                var jsonArg = {};
                for (var i = 0; i < args.length; i++) {
                    if (i == 0) {
                        jsonArg.name = args[i];
                    } else if (i == 1) {
                        jsonArg.data = args[i];
                    } else if (i == 2) {
                        jsonArg.callback = args[i];
                    } else if (i == 3) {
                        jsonArg.id = args[i];
                    } else if (i == 4) {
                        jsonArg.timeout = args[i];
                    } else if (i == 5) {
                        jsonArg.segmentSize = args[i];
                    }
                }
                return jsonArg;
            }

        }

        /**
         * 上传大文件
         */
        HttpClient.prototype.uploadFile = function uploadFile() {
            //获取参数
            var args = getJsonArgForUploadFile(arguments);
            //显示cover
            // this._showCover();
            try {
                //获取id
                if (!args.id) {
                    args.id = "0";
                } else if (typeof args.id != "string") {
                    args.id = args.id.toString();
                }

                //获取token id
                var token = this._tokenRegister[args.id];
                if (!token) {
                    //获取新的token id
                    token = this._getToken();
                    //注册token id
                    this._tokenRegister[args.id] = token;
                }

                //获取地址
                var requestUrl = this.getRequestUrl({
                    path: args.name
                });

                //-------------------------------- debug 模式 ---------------------------------------------
                if (fox.settings.debugModel && fox.settings.debugScope.indexOf("fox.service")!=-1) {
                    //调试请求模拟
                    var debugRequestURL;
                    //服务配置映射
                    var serviceMapURL = "debug/service/services.json";
                    //获取服务映射配置
                    fox.core.ajax({
                        url: serviceMapURL,
                        async: false,
                        type: 'GET',
                        cache: false,
                        data: {},
                        //成功处理
                        success: function (data, status, xhr) {
                            if ("string" == fox.type(data)) {
                                data = JSON.parse(data);
                            }
                            //获取模拟数据
                            debugRequestURL = data[args.name];
                        },
                        //错误处理
                        error: function (xhr, status) {
                            //移除token id
                            delete _this._tokenRegister[args.id];
                            //关闭cover
                            // _this._hideCover();
                            //回调
                            args.callback(0, status, "");
                        }
                    });

                    //调试请求
                    if (debugRequestURL) {
                        //解决引用关系
                        var _this = this;
                        //发起调试请求
                        fox.core.ajax({
                            url: debugRequestURL,
                            async: true,
                            type: 'GET',
                            cache: false,
                            data: {},
                            //成功处理
                            success: function (data, status, xhr) {
                                //移除token id
                                delete _this._tokenRegister[args.id];
                                //关闭cover
                                // _this._hideCover();
                                //执行回调函数
                                args.callback(0, "success", data);
                            },
                            //错误处理
                            error: function (xhr, status) {
                                //移除token id
                                delete _this._tokenRegister[args.id];
                                //关闭cover
                                // _this._hideCover();
                                //获取消息
                                var msg = xhr.responseText;
                                if (!msg || msg == "") {
                                    msg = status;
                                }
                                //执行回调函数
                                args.callback(1, msg, "");

                            }
                        });
                        //进入调试，直接返还
                        return;
                    }
                }
                //-------------------------------- debug 模式---------------------------------------------


                //定义form data
                var formData = new FormData();
                //获取上传控件ID
                var elId = undefined;

                var fileList = args.data["fileList"];

                if (fox.type(args.data) == "object") {
                    elId = args.data["file"];
                    delete args.data["file"];
                    for (var key in args.data) {
                        formData.append(key, args.data[key]);
                    }
                } else {
                    elId = args.data;
                }

                //获取files,如果已经传入了文件路径,则不需要去读取document的路径
                var files;
                if(fileList instanceof Array) {
                    files = fileList;
                } else {
                    files = document.getElementsByName(elId)[0].files;
                }

                for (var i = 0; i < files.length; i++) {
                    var fileObj = files[i];
                    var fileName = fileObj.name;
                    var pos = fileName.lastIndexOf("\\");
                    fileName = fileName.substring(pos + 1);
                    //获取file
                    var mov = fileObj;
                    var totalSize = mov.size;
                    //数据片段
                    var segment = mov.slice(0, totalSize);
                    formData.append("fileName-" + i, fileName);
                    formData.append("fileData-" + i, segment);
                }
                formData.append("fileCount", files.length);
                formData.append("_$id", args.id);
                formData.append("_$token", token);


                var _this = this;
                //获取session id
                this._getSessionId().done(function (seId) {
                    //设置session id
                    formData.append("_$sessionid", seId);

                    //发送请求
                    fox.core.ajax({
                        url: requestUrl,
                        type: 'POST',
                        data: formData,
                        cache: false,
                        contentType: false,
                        processData: false,
                        timeout: args.timeout,
                        async: true,
                        //成功处理
                        success: function (data, status, xhr) {
                            //移除token id
                            delete _this._tokenRegister[args.id];
                            //关闭cover
                            // _this._hideCover();

                            //-------------------------------- recorder 模式 -----------------------------------------
                            if(fox.settings.recorderModel && fox.settings.recorderScope.indexOf("fox.service")!=-1) {
                                fox.logger.info("[upload receive]  name:" + args.name + " ,response:" + JSON.stringify(data));
                            }
                            //-------------------------------- recorder 模式 -----------------------------------------

                            //执行回调函数
                            args.callback(0, "success", data);
                        },
                        //错误处理
                        error: function (xhr, status) {
                            //移除token id
                            delete _this._tokenRegister[args.id];
                            //关闭cover
                            // _this._hideCover();
                            //获取消息
                            var msg = xhr.responseText;
                            if (!msg || msg == "") {
                                msg = status;
                            }
                            //执行回调函数
                            args.callback(1, msg, "");

                        },
                        //完成后处理
                        complete: function (xhr, status) {

                        }
                    });

                }).fail(function () {
                    //关闭cover
                    // _this._hideCover();
                    var msg = "upload file fail";
                    args.callback(1, msg, "");
                })

            } catch (ex) {
                //关闭cover
                // this._hideCover();
                //移除token id
                delete this._tokenRegister[args.id];
                //错误日志
                fox.logger.error(ex.message, ex);
                //错误回调
                args.callback(1, ex.message, "");
            }
        };

        /**
         * 上传片段
         * @param args
         */
        HttpClient.prototype.uploadSegment = function uploadSegment(args) {
            //已经发生错误不再进行文件上传
            if (!args.status) {
                return;
            }

            //获取token id
            var token = this._tokenRegister[args.id];
            if (!token) {
                //获取新的token id
                token = this._getToken();
                //注册token id
                this._tokenRegister[args.id] = token;
            }

            var formData = new FormData();
            formData.append("_$id", args.id);
            formData.append("_$sessionid", args.sessionId);
            formData.append("_$token", token);
            formData.append("taskId", args.id);
            formData.append("index", args.index);
            formData.append("count", args.count);
            formData.append("start", args.start);
            formData.append("end", args.end);
            formData.append("fileName", args.fileName);
            //获取片段数据
            var segment = args.data.slice(args.start, args.end);
            formData.append("data", segment);

            var _this = this;
            //发送请求
            fox.core.ajax({
                url: args.requestUrl,
                type: 'POST',
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                timeout: args.timeout,
                async: true,
                //成功处理
                success: function (data, status, xhr) {
                    //移除token id
                    delete _this._tokenRegister[args.id];
                    //更新进度
                    args.processing.work(args.increment);

                    if (args.index == args.count) {
                        //关闭processing
                        args.processing.done();

                        //-------------------------------- recorder 模式 -----------------------------------------
                        if(fox.settings.recorderModel && fox.settings.recorderScope.indexOf("fox.service")!=-1) {
                            fox.logger.info("[uploadBig receive]  name:" + args.name + " ,response:" + JSON.stringify(data));
                        }
                        //-------------------------------- recorder 模式 -----------------------------------------

                        //执行回调函数
                        args.callback(0, "success", data);
                    } else {
                        //完成更新任务数
                        args.index++;
                        //更新开始结束位置
                        args.start = args.end;
                        args.end += args.segmentSize;
                        if (args.end > args.totalSize) {
                            args.end = args.totalSize;
                        }
                        //继续执行
                        setTimeout(function () {
                            _this.uploadSegment(args)
                        }, 0);
                    }
                },
                //错误处理
                error: function (xhr, status) {
                    //移除token id
                    delete _this._tokenRegister[args.id];
                    //记录文件上传失败
                    args.status = false;
                    //关闭processing
                    args.processing.done();
                    //获取消息
                    var msg = xhr.responseText;
                    if (!msg || msg == "") {
                        msg = status;
                    }
                    //执行回调函数
                    args.callback(1, msg, "");

                },
                //完成后处理
                complete: function (xhr, status) {

                }
            });
        };

        /**
         *  获取json对象参数
         * @param args
         * @returns {*}
         */
        function getJsonArgForUploadBigFile(args) {
            if (args.length == 1 && fox.type(args[0]) == "object") {
                var jsonArg = args[0];
                return jsonArg;
            } else {
                var jsonArg = {};
                for (var i = 0; i < args.length; i++) {
                    if (i == 0) {
                        jsonArg.name = args[i];
                    } else if (i == 1) {
                        jsonArg.data = args[i];
                    } else if (i == 2) {
                        jsonArg.callback = args[i];
                    } else if (i == 3) {
                        jsonArg.id = args[i];
                    } else if (i == 4) {
                        jsonArg.timeout = args[i];
                    } else if (i == 5) {
                        jsonArg.segmentSize = args[i];
                    }
                }
                return jsonArg;
            }

        }

        /**
         * 上传大文件(只支持单文件上传)
         */
        HttpClient.prototype.uploadBigFile = function uploadBigFile() {
            //创建processing
            var processing = new fox.$.Processing();
            //锁定屏幕
            processing.work(0);
            try {
                //获取参数
                var args = getJsonArgForUploadBigFile(arguments);
                //获取id
                if (!args.id) {
                    //关闭processing
                    processing.done();
                    var msg = "id can not benn NULL";
                    args.callback(1, msg, "");
                    return;
                }

                //设置请求超时时间
                if (!args.timeout) {
                    args.timeout = 60000;
                }

                //获取地址
                var requestUrl = this.getRequestUrl({
                    path: args.name
                });

                //-------------------------------- debug 模式 ---------------------------------------------
                if (fox.settings.debugModel && fox.settings.debugScope.indexOf("fox.service")!=-1) {
                    //调试请求模拟
                    var debugRequestURL;
                    //服务配置映射
                    var serviceMapURL = "debug/service/services.json";
                    //获取服务映射配置
                    fox.core.ajax({
                        url: serviceMapURL,
                        async: false,
                        type: 'GET',
                        cache: false,
                        data: {},
                        //成功处理
                        success: function (data, status, xhr) {
                            if ("string" == fox.type(data)) {
                                data = JSON.parse(data);
                            }
                            //获取模拟数据
                            debugRequestURL = data[args.name];
                        },
                        //错误处理
                        error: function (xhr, status) {
                            //关闭processing
                            processing.done();
                            callback(0, status, "");
                        }
                    });

                    //调试请求
                    if (debugRequestURL) {
                        //解决引用关系
                        var _this = this;
                        //发起调试请求
                        fox.core.ajax({
                            url: debugRequestURL,
                            async: true,
                            type: 'GET',
                            cache: false,
                            data: {},
                            //成功处理
                            success: function (data, status, xhr) {
                                //移除token id
                                delete _this._tokenRegister[args.id];
                                //关闭processing
                                processing.done();
                                //执行回调函数
                                args.callback(0, "success", data);
                            },
                            //错误处理
                            error: function (xhr, status) {
                                //移除token id
                                delete _this._tokenRegister[args.id];
                                //关闭processing
                                processing.done();
                                //获取消息
                                var msg = xhr.responseText;
                                if (!msg || msg == "") {
                                    msg = status;
                                }
                                //执行回调函数
                                args.callback(1, msg, "");

                            }
                        });
                        //进入调试，直接返还
                        return;
                    }
                }
                //-------------------------------- debug 模式---------------------------------------------

                //每次截取的片段大小
                var segmentSize = args.data.segmentSize;
                if (!segmentSize) {
                    segmentSize = 5 * 1024 * 1024;
                }

                //获取上传控件ID
                var elId = args.data.file;

                //获取File对象
                var fileObj = document.getElementById(elId).files[0];
                //获取file naem
                var fileName = fileObj.name;
                var pos = fileName.lastIndexOf("\\");
                fileName = fileName.substring(pos + 1);
                //获取file
                var mov = fileObj;
                var totalSize = mov.size;

                var startIndex = 0;
                var endIndex = startIndex + segmentSize;
                if (endIndex > totalSize) {
                    endIndex = totalSize;
                }

                var count = Math.ceil(totalSize / segmentSize);

                //获取任务增加量
                var increment = 100 / count;
                if (increment == 0) {
                    increment = 1;
                }
                //调用者
                var params = {
                    //id
                    id: args.id,
                    //请求URL
                    requestUrl: requestUrl,
                    //session id
                    sessionId: "",
                    //文件名
                    fileName: fileName,
                    //文件数据,
                    data: mov,
                    //文件大小
                    totalSize: totalSize,
                    //当前片段开始位置
                    start: startIndex,
                    //当前片段结束位置
                    end: endIndex,
                    //切片数量
                    count: count,
                    //当前索引
                    index: 1,
                    //超时时间
                    timeout: args.timeout,
                    //回调函数
                    callback: args.callback,
                    //进程processing
                    processing: processing,
                    //片段大小
                    segmentSize: segmentSize,
                    //运行状态
                    status: true,
                    //进度增长量
                    increment: increment
                };

                var _this = this;
                //获取session id
                var sessionId = this._getSessionId().done(function (seId) {
                    params.sessionId = seId;
                    setTimeout(function () {
                        _this.uploadSegment(params)
                    }, 0);
                }).fail(function () {
                    //关闭processing
                    processing.done();
                    var msg = "upload file fail";
                    args.callback(1, msg, "");
                })

            } catch (e) {
                //关闭processing
                processing.done();
                // 打印日志
                fox.logger.error(e.message, e);
            }
        };

        /**
         * 注册message service
         * @param name
         * @param callback
         * @param interval
         */
        HttpClient.prototype.registerMessageService = function registerMessageService(name, callback, interval) {

            //记录
            var rec = {};
            rec.name = name;
            rec.callback = callback;

            if (this.options.webSocketType && this.options.webSocketType == "put") {
                //保存引用
                var _this = this;
                //获取地址
                var webSocketUrl = this.getPutWebSocketUrl();
                //加入注册表
                this._msgService[name] = rec;
                // 创建WebSocket
                rec.ws = new WebSocket(webSocketUrl);
                // 收到消息时在消息框内显示
                rec.ws.onmessage = function (evt) {
                    //解析
                    var jsonData = JSON.parse(evt.data);
                    // 获取消息类型
                    var msgType = jsonData.msgType;
                    if ("#heart" == msgType) {
                        var responseObj = {"msgType": "#heart", "content": ""};
                        var responseJson = JSON.stringify(responseObj);
                        rec.ws.send(responseJson); //回应心跳
                        return;
                    }

                    //处理信息
                    if ("#info" == msgType) {
                        //记录id
                        rec.id = jsonData.content;
                        //回调函数
                        callback(rec.name, jsonData);
                    }
                    //处理消息
                    else {
                        //回调函数
                        callback(rec.name, jsonData);
                    }

                };
                // 连接上时走这个方法
                rec.ws.onopen = function (evt) {

                };
                // 断开时会走这个方法
                rec.ws.onclose = function (evt) {
                    //判断是否为正常断开，如果不是重新连接
                    if (_this._msgService[name]) {
                        //删除旧记录
                        delete _this._msgService[name];
                        //3秒后重试量级
                        window.setTimeout(function () {
                            //重新连接
                            _this.registerMessageService(rec.name, rec.callback);

                        }, 3000)

                    }

                };
            } else {
                //获取获取数据时间间隔
                rec.interval = interval == undefined ? 10000 : interval;
                //保存引用
                var _this = this;
                //请求函数
                var requestFunc = function (first) {
                    //如果没注册，不进行请求
                    if (_this._msgService[name] == undefined) {
                        return;
                    }
                    //获取地址
                    var webSocketUrl = _this.getGetWebSocketUrl();
                    //记录地址
                    rec.url = webSocketUrl;

                    //获取session id 并调用注册
                    _this._getSessionId().done(function (sessionId) {
                        //发起连接请求
                        fox.core.ajax({
                            url: webSocketUrl,
                            async: true,
                            type: 'POST',
                            cache: false,
                            data: {_$type: "get", _$name: rec.name,_$sessionid:sessionId},
                            //成功处理
                            success: function (data, status, xhr) {
                                //解析数据
                                var resData = JSON.parse(xhr.responseText);
                                //接受数据
                                receiveFunc(resData);
                            },
                            //错误处理
                            error: function (xhr, status) {
                                var msg = xhr.statusText;
                                if (!msg || msg == "") {
                                    msg = status;
                                }
                                var jsonData = {
                                    msgType: "#exception",
                                    code: 2,
                                    content: msg
                                };
                                //回调函数
                                try {
                                    callback(rec.name, jsonData);
                                } catch (ex) {
                                    fox.logger.error(ex.message, ex);
                                }
                                //如果不第一发起的连接请求，则继续重试
                                if (!first) {
                                    window.setTimeout(requestFunc, rec.interval);
                                }
                            }

                        });

                    });


                };
                //接受函数
                var receiveFunc = function (jsonData) {
                    // 获取消息类型
                    var msgType = jsonData.msgType;
                    if ("#heart" == msgType) {
                        //继续获取信息
                        window.setTimeout(requestFunc, rec.interval);
                        return;
                    }

                    //处理信息
                    if ("#info" == msgType) {
                        //记录id
                        rec.id = jsonData.content;
                        //回调函数
                        try {
                            callback(rec.name, jsonData);
                        } catch (ex) {
                            fox.logger.error(ex.message, ex);
                        }
                        //继续获取信息
                        window.setTimeout(requestFunc, rec.interval);
                    }
                    //处理消息
                    else {
                        var remaining = jsonData.remaining;
                        //回调函数
                        try {
                            callback(rec.name, jsonData);
                        } catch (ex) {
                            fox.logger.error(ex.message, ex);
                        }
                        //如果服务器还有信息，立刻获取
                        if (remaining > 0) {
                            window.setTimeout(requestFunc, 500);
                        } else {
                            window.setTimeout(requestFunc, rec.interval);
                        }
                    }
                };

                //加入注册表
                this._msgService[name] = rec;

                //调用请求函数
                requestFunc(true);
            }
        };

        /**
         * 获取message service id
         * @param name
         * @returns {any|string|number}
         */
        HttpClient.prototype.getMessageServiceId = function getMessageServiceId(name) {
            return this._msgService[name].id;
        };

        /**
         * 注销message service
         * @param name
         */
        HttpClient.prototype.unRegisterMessageService = function unRegisterMessageService(name) {
            if (this._msgService[name]) {
                //获取记录
                var rec = this._msgService[name];
                //删除记录
                delete  this._msgService[name];

                if (rec.ws) {
                    rec.ws.close();
                } else {
                    //获取session id 并调用注销
                    this._getSessionId().done(function (sessionId) {
                        //发起连接请求
                        fox.core.ajax({
                            url: rec.url,
                            async: true,
                            type: 'POST',
                            cache: false,
                            data: {_$type: "disconnect", _$name: rec.name,_$sessionid:sessionId},
                            //成功处理
                            success: function (data, status, xhr) {

                            },
                            //错误处理
                            error: function (xhr, status) {

                            }
                        });
                    });
                }
            }
        };


        /**
         * 请求链路
         * @constructor
         */
        function RequestChain(){
            //创建事件链路
           this.chain=new fox.EventChain();
        }

        /**
         * 请求服务
         */
        RequestChain.prototype.request=function(){
            //获取调用参数
            var arg = getJsonArg(arguments);
            //保存arg的callback
            var callback=arg.callback;
            //去除callback
            arg.callback=undefined;

            //保存this
            var _this=this;
            //定义链路函数
            var fn=function(){
                //发起service请求
                fox.service.request(arg).done(function(code,msg,data){
                    try {
                        //调用回调函数
                        callback.call(arg, code, msg, data);
                        //中断链路调用
                        _this.chain.fire(false);
                    }catch(ex){
                        //中断链路调用
                        _this.chain.reject(false);
                        //抛出异常
                        throw ex;
                    }
                }).fail(function(code,msg,data){
                    try {
                        //调用回调函数
                        callback.call(arg, code, msg, data);
                        //中断链路调用
                        _this.chain.reject(false);
                    }catch(ex){
                         //中断链路调用
                        _this.chain.reject(false);
                        //抛出异常
                        throw ex;
                    }
                });
            };
            //加入链路
            this.chain.post(fn);

            //返回本对象
            return this;
        };

        /**
         * 加入等待任务
         */
        RequestChain.prototype.wait=function wait(fn){
            this.chain.wait(fn);
        };

        //创建http client
        var httpClient = new HttpClient();
        //返回对象
        return httpClient;
    })
);
