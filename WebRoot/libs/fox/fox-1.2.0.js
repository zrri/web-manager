/**
 * Created by jiangcheng on 2016/08/05.
 */
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 创建xml http request
         * @returns {any}
         */
        function createXHR() {
            let xmlHttpRequest;
            try {
                xmlHttpRequest = new XMLHttpRequest();
            }
            catch (e) {
                let IEXHRVers = ["Msxml3.XMLHTTP", "Msxml2.XMLHTTP", "Microsoft.XMLHTTP"];
                for (let i = 0, len = IEXHRVers.length; i < len; i++) {
                    try {
                        xmlHttpRequest = new ActiveXObject(IEXHRVers[i]);
                    }
                    catch (e) {
                        continue;
                    }
                }
            }
            return xmlHttpRequest;
        }
        /**
         * 编码数据
         * @param data
         * @param contentType
         */
        function encode(data, contentType) {
            if (contentType === void (0)) {
                contentType = "text";
            }
            contentType = contentType.trim().toLowerCase();
            //结果
            let res = "";
            if (contentType == "application/x-www-form-urlencoded") {
                //对象转换为form提交格式
                if (core.Utils.type(data) == "object") {
                    for (let name in data) {
                        let value = data[name];
                        //获取value数据类型
                        let valType = core.Utils.type(value);
                        if (valType == "array") {
                            for (let i = 0; i < value.length; i++) {
                                let item = name + "=" + encodeURIComponent(value[i]);
                                if (res.length > 0) {
                                    res += "&";
                                }
                                res += item;
                            }
                        }
                        else {
                            let item = name + "=" + encodeURIComponent(value);
                            if (res.length > 0) {
                                res += "&";
                            }
                            res += item;
                        }
                    }
                }
                else {
                    res = data;
                }
            }
            else if (contentType.indexOf("text") == 0) {
                //对象转换为text
                if (core.Utils.type(data) == "object") {
                    res = JSON.stringify(data);
                }
                else {
                    res = data;
                }
            }
            else if (contentType.indexOf("application/json") == 0) {
                //对象转换为text
                if (core.Utils.type(data) == "object") {
                    res = JSON.stringify(data);
                }
                else {
                    res = data;
                }
            }
            else {
                res = data;
            }
            return res;
        }
        /**
         * 解码数据
         * @param data
         * @param dataType
         */
        function decode(data, dataType) {
            if (dataType == "json" && core.Utils.type(data) == "string") {
                data = JSON.parse(data);
            }
            return data;
        }
        /**
         * 默认配置
         * @type {{type: string, async: boolean, contentType: string, dataType: string, cache: boolean, timeout: number, processData: boolean}}
         */
        const defaultSetting = {
            //请求类型
            type: "GET",
            //是否异步
            async: true,
            //内容类型
            contentType: "application/x-www-form-urlencoded",
            //数据类型
            dataType: "string",
            //是否缓存
            cache: false,
            //超时时间
            timeout: 30000,
            //是否处理数据
            processData: true
        };
        /**
         * ajax请求
         * @param args
         * @returns {*}
         */
        function ajax(args) {
            //创建XML Http Request
            let xmlHttpRequest = createXHR();
            // 若创建成功，则开始解析目标XML文件
            if (xmlHttpRequest == null || !xmlHttpRequest) {
                core.logger.error("not support XmlHttpRequest");
                return;
            }
            //设置默认配置
            let setting = {};
            core.Utils.extend(true, setting, defaultSetting);
            core.Utils.extend(true, setting, args);
            args = setting;
            //只有异步请求才能设置超时控制
            if (args.async) {
                //防止IE导致的问题
                try {
                    xmlHttpRequest.timeout = args.timeout;
                    xmlHttpRequest.ontimeout = function () {
                        //超时处理
                        if (args["error"]) {
                            args["error"](xmlHttpRequest, "timeout");
                        }
                        //完成处理
                        if (args["complete"]) {
                            args["complete"](xmlHttpRequest, "timeout");
                        }
                    };
                }
                catch (ex) {
                    //打印异常
                    core.logger.error(ex.Message, ex);
                }
            }
            // 指定ready state change事件句柄对应的函数
            xmlHttpRequest.onreadystatechange = function () {
                //loading处理
                if (xmlHttpRequest.readyState < 4) {
                    if (args["loading"]) {
                        args["loading"](xmlHttpRequest.readyState);
                    }
                    return;
                }
                if (args["success"] && (xmlHttpRequest.status == 0 || xmlHttpRequest.status >= 200 && xmlHttpRequest.status < 300)) {
                    let rspData = xmlHttpRequest.response;
                    if (rspData == undefined) {
                        rspData = xmlHttpRequest.responseText;
                    }
                    try {
                        //解码数据
                        rspData = decode(rspData, args.dataType);
                    }
                    catch (ex) {
                        //失败回调
                        if (args["error"]) {
                            args["error"](xmlHttpRequest, ex.message);
                        }
                        return;
                    }
                    //成功回调
                    args["success"](rspData, xmlHttpRequest.status, xmlHttpRequest);
                }
                else if (args["error"]) {
                    args["error"](xmlHttpRequest, xmlHttpRequest.status);
                }
                //完成处理
                if (args["complete"]) {
                    args["complete"](xmlHttpRequest, xmlHttpRequest.status);
                }
            };
            try {
                // 初始化HTTP请求参数
                xmlHttpRequest.open(args.type, args.url, args.async);
                //设置请求头
                if (core.Utils.type(args.beforeSend) == "function") {
                    //克隆数据
                    let cloneArgs = {};
                    core.Utils.extend(true, cloneArgs, args);
                    args.beforeSend.call(this, xmlHttpRequest, cloneArgs);
                }
                //设置缓存策略
                if (!args.cache) {
                    xmlHttpRequest.setRequestHeader("Cache-Control", "no-cache");
                }
                if (args.type && args.type.toUpperCase() === "POST") {
                    // 获取请求数据
                    let reqData = args.processData ? encode(args.data, args.contentType) : args.data;
                    //发送数据
                    xmlHttpRequest.send(reqData);
                }
                else {
                    //发送数据
                    xmlHttpRequest.send();
                }
            }
            catch (ex) {
                if (args["error"]) {
                    args["error"](xmlHttpRequest, ex.message);
                }
                //完成处理
                if (args["complete"]) {
                    args["complete"](xmlHttpRequest, ex.message);
                }
            }
            return xmlHttpRequest;
        }
        core.ajax = ajax;
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by 江成 on 2016/08/05.
 */
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * Logger
         */
        class Logger {
            /**
             * warn
             * @param message
             * @param ex
             */
            warn(message, ex) {
                return this;
            }
            /**
             * error
             * @param message
             * @param ex
             */
            error(message, ex) {
                return this;
            }
            /**
             * info
             * @param message
             * @returns {fox.core.Logger}
             */
            info(message) {
                return this;
            }
            /**
             * debug
             * @param message
             * @returns {fox.core.Logger}
             */
            debug(message) {
                return this;
            }
        }
        core.Logger = Logger;
        /**
         * 导出logger
         * @type {fox.Logger}
         */
        core.logger = !window.console ? new Logger() : window.console;
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by jiangcheng on 2016/10/31.
 */
///<reference path="Logger.ts"/>
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 树节点
         * @param id
         * @param data
         * @constructor
         */
        class TreeNode {
            /**
             * 构造函数
             * @param id
             */
            constructor(id) {
                /**
                 * children
                 * @type {{}}
                 */
                this.children = {};
                this.id = id;
            }
        }
        /**
         * 数据bus
         */
        class Bus {
            /**
             * 构造函数
             */
            constructor() {
                //创建root节点
                this.root = new TreeNode("_root_");
            }
            /**
             * 加入数据
             * @param args
             * @returns {boolean}
             */
            put(...args) {
                //获取参数长度
                let argLen = args.length;
                //参数至少必须有一个key和value
                if (argLen < 2) {
                    core.logger.error("参数至少包括一个(key,value)");
                    return false;
                }
                //当前节点
                let node = this.root;
                for (let i = 0; i < argLen - 1; i++) {
                    let name = args[i];
                    if (!node.children[name]) {
                        node.children[name] = new TreeNode(name);
                    }
                    node = node.children[name];
                }
                //保存数据
                node.data = arguments[argLen - 1];
                //返回操作成功标志
                return true;
            }
            /**
             * 获取内容
             * @param args
             * @returns {any}
             */
            get(...args) {
                //获取参数长度
                let argLen = args.length;
                if (argLen == 0) {
                    return undefined;
                }
                //当前节点
                let node = this.root;
                for (let i = 0; i < argLen; i++) {
                    let name = args[i];
                    if (node.children[name]) {
                        node = node.children[name];
                    }
                    else {
                        return undefined;
                    }
                }
                return node.data;
            }
            /**
             * 移除数据
             * @param args
             * @returns {any}
             */
            remove(...args) {
                //获取参数长度
                let argLen = args.length;
                if (argLen == 0) {
                    return false;
                }
                //当前节点
                let node = this.root;
                for (let i = 0; i < argLen - 1; i++) {
                    let name = args[i];
                    if (node.children[name]) {
                        node = node.children[name];
                    }
                    else {
                        return false;
                    }
                }
                let name = args[argLen - 1];
                //如果找到对应的数据,则删除
                if (node.children[name]) {
                    delete node.children[name];
                    return true;
                }
                return false;
            }
        }
        core.Bus = Bus;
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by 江成 on 2016/08/05.
 */
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 工具类
         */
        class Utils {
            /**
             * 判断类型
             * @param obj
             * @returns {any}
             */
            static type(obj) {
                let toString = Object.prototype.toString;
                let t = toString.call(obj);
                return obj == null ? String(obj) : Utils.class2type[t] || "object";
            }
            /**
             * 判断是否为函数
             * @param obj
             * @returns {boolean}
             */
            static isFunction(obj) {
                return Utils.type(obj) === "function";
            }
            /**
             * 判断是否为数组
             * @type {(function(any): boolean)|(function(any): boolean)}
             */
            static isArray(obj) {
                return Utils.type(obj) === "array";
            }
            /**
             * 创建数组
             * @param arr
             * @param results
             * @returns {Array<any>|Array}
             */
            static makeArray(arr, results) {
                let ret = results || [];
                if (arr != null) {
                    if (Utils.isArrayLike(Object(arr))) {
                        Utils.merge(ret, typeof arr === "string" ?
                            [arr] : arr);
                    }
                    else {
                        ret.push(arr);
                    }
                }
                return ret;
            }
            /**
             * 合并数组
             * @param first
             * @param second
             * @returns {Array<any>}
             */
            static merge(first, second) {
                let len = +second.length, j = 0, i = first.length;
                for (; j < len; j++) {
                    first[i++] = second[j];
                }
                first.length = i;
                return first;
            }
            /**
             * 判断是否为window对象
             * @param obj
             * @returns {any|boolean}
             */
            static isWindow(obj) {
                return obj && typeof obj === "object" && "setInterval" in obj;
            }
            /**
             * 是否为普通对象
             * @param obj
             * @returns {any}
             */
            static isPlainObject(obj) {
                if (!obj || Utils.type(obj) !== "object" || obj.nodeType || Utils.isWindow(obj)) {
                    return false;
                }
                let hasOwn = Object.prototype.hasOwnProperty;
                if (obj.constructor && !hasOwn.call(obj, "constructor")
                    && !hasOwn.call(obj.constructor.prototype, "isPrototypeOf")) {
                    return false;
                }
                let key;
                for (key in obj) {
                }
                return key === undefined || hasOwn.call(obj, key);
            }
            /**
             * 继承（是否深度拷贝,dest,src1,src2,src3...）
             * @returns {any|{}}
             */
            static extend(...args) {
                let target = args[0] || {}, i = 1, length = args.length, deep = false;
                // Handle a deep copy situation
                if (typeof target === "boolean") {
                    deep = target;
                    // Skip the boolean and the target
                    target = args[i] || {};
                    i++;
                }
                // Handle case when target is a string or something (possible in deep copy)
                if (typeof target !== "object" && !Utils.isFunction(target)) {
                    target = {};
                }
                // Extend itself if only one argument is passed
                if (i === length) {
                    target = this;
                    i--;
                }
                for (; i < length; i++) {
                    // Only deal with non-null/undefined values
                    if (args[i] != null && args[i] != undefined) {
                        let options = args[i];
                        // Extend the base object
                        for (let name in options) {
                            let src = target[name];
                            let copy = options[name];
                            // Prevent never-ending loop
                            if (target === copy) {
                                continue;
                            }
                            // Recurse if we're merging plain objects or arrays
                            let copyIsArray = false;
                            if (deep && copy && (Utils.isPlainObject(copy) || (copyIsArray = Utils.isArray(copy)))) {
                                let clone;
                                if (copyIsArray) {
                                    clone = src && Utils.isArray(src) ? src : [];
                                }
                                else {
                                    clone = src && Utils.isPlainObject(src) ? src : {};
                                }
                                // Never move original objects, clone them
                                target[name] = Utils.extend(deep, clone, copy);
                                // Don't bring in undefined values
                            }
                            else if (copy !== undefined) {
                                target[name] = copy;
                            }
                        }
                    }
                }
                // Return the modified object
                return target;
            }
            /**
             * 克隆
             * @param source
             * @param target
             * @returns {any}
             */
            static clone(src, dest) {
                dest = Utils.extend(true, dest, src);
                return dest;
            }
            /**
             * 是否数组
             * @param obj
             * @returns {boolean}
             */
            static isArrayLike(obj) {
                let length = !!obj && "length" in obj && obj.length, type = Utils.type(obj);
                if (type === "function" || Utils.isWindow(obj)) {
                    return false;
                }
                return type === "array" || length === 0 ||
                    typeof length === "number" && length > 0 && (length - 1) in obj;
            }
            /**
             * 循环访问
             * @param target
             * @param callback
             * @param hasOwnProperty
             * @returns {any}
             */
            static each(target, callback, hasOwnProperty) {
                if (Utils.isArrayLike(target)) {
                    let length = target.length;
                    for (let i = 0; i < length; i++) {
                        if (callback.call(target[i], i, target[i]) === false) {
                            break;
                        }
                    }
                }
                else {
                    for (let key in target) {
                        if (hasOwnProperty) {
                            if (target.hasOwnProperty(key)) {
                                if (callback.call(target[key], key, target[key]) === false) {
                                    break;
                                }
                            }
                        }
                        else {
                            if (callback.call(target[key], key, target[key]) === false) {
                                break;
                            }
                        }
                    }
                }
                return target;
            }
            //获dom对象的innerText的取值
            static getInnerText(element) {
                //判断浏览器是否支持innerText
                if (typeof element.innerText === "string") {
                    return element.innerText;
                }
                else {
                    return element.textContent;
                }
            }
            //设置dom对象innerText的值
            static setInnerText(element, content) {
                if (typeof element.innerText === "string") {
                    element.innerText = content;
                }
                else {
                    element.textContent = content;
                }
            }
            /**
             * 获取下一个element node
              * @param node
             * @returns {any}
             */
            //兼容浏览器   获取下一个兄弟元素
            static getNextElementSibling(element) {
                if (element.nextElementSibling) {
                    return element.nextElementSibling;
                }
                else {
                    //获取下一个兄弟节点
                    let node = element.nextSibling;
                    return node;
                }
            }
            //兼容浏览器 获取第一个子元素
            static getFirstElementChild(element) {
                if (element.firstElementChild) {
                    return element.firstElementChild;
                }
                else {
                    //获取下一个节点
                    let node = element.firstChild;
                    return node;
                }
            }
        }
        /**
         * 类型映射表
         */
        Utils.class2type = {
            '[object Boolean]': 'boolean',
            '[object Number]': 'number',
            '[object String]': 'string',
            '[object Function]': 'function',
            '[object Array]': 'array',
            '[object Date]': 'date',
            '[object RegExp]': 'regExp',
            '[object Object]': 'object'
        };
        core.Utils = Utils;
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by jiangcheng on 2016/08/05.
 */
///<reference path="Utils.ts"/>
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 状态
         */
        let Status;
        (function (Status) {
            Status[Status["Running"] = 0] = "Running";
            Status[Status["Done"] = 1] = "Done";
            Status[Status["Fail"] = 2] = "Fail";
        })(Status || (Status = {}));
        /**
         * Deffered类
         */
        class Deferred {
            /**
             * 构造函数
             */
            constructor() {
                /**
                 * 成功函数
                 */
                this.doneFns = [];
                /**
                 * 失败函数
                 */
                this.failFns = [];
                /**
                 * always函数
                 */
                this.alwaysFns = [];
                //运行状态
                this.status = Status.Running;
            }
            /**
             * fire
             */
            fire() {
                if (this.status == Status.Running) {
                    return;
                }
                if (this.alwaysFns) {
                    while (this.alwaysFns.length > 0) {
                        let fn = this.alwaysFns.shift();
                        fn.apply(this, this.args);
                    }
                }
                if (this.status == Status.Done) {
                    while (this.doneFns.length > 0) {
                        let fn = this.doneFns.shift();
                        fn.apply(this, this.args);
                    }
                }
                else if (this.status == Status.Fail) {
                    while (this.failFns.length > 0) {
                        let fn = this.failFns.shift();
                        fn.apply(this, this.args);
                    }
                }
            }
            /**
             * 注册成功函数
             * @param fn
             */
            done(fn) {
                this.doneFns.push(fn);
                this.fire();
                return this;
            }
            /**
             * 注册失败函数
             * @param fn
             */
            fail(fn) {
                this.failFns.push(fn);
                this.fire();
                return this;
            }
            /**
             * 注册always函数
             * @param fn
             */
            always(fn) {
                this.alwaysFns.push(fn);
                this.fire();
                return this;
            }
            /**
             * 成功
             * @param params
             */
            resolve(...params) {
                this.args = params;
                this.status = Status.Done;
                this.fire();
                return this;
            }
            /**
             * 拒绝
             * @param params
             */
            reject(...params) {
                this.args = params;
                this.status = Status.Fail;
                this.fire();
                return this;
            }
        }
        core.Deferred = Deferred;
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by 江成 on 2016/10/31.
 */
///<reference path="Utils.ts"/>
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 状态
         */
        class Status {
        }
        /**
         * 繁忙状态
         */
        Status.Busy = "busy";
        /**
         * 空闲状态
         */
        Status.Free = "free";
        /**
         * 任务链路
         */
        class EventChain {
            /**
             * 构造函数
             */
            constructor() {
                /**
                 * 等待函数
                 */
                this.waitFns = [];
                this.chain = [];
                this.status = Status.Free;
            }
            /**
             * 触发链路中的下一个事件
             */
            fire(...params) {
                //更新参数
                this.args = params;
                //设置状态为空闲
                if (this.chain.length == 0) {
                    //判断是否有等待回调函数
                    while (this.waitFns.length > 0) {
                        let fn = this.waitFns.shift();
                        //触发wait函数
                        fn.apply(this, this.args);
                    }
                    this.status = Status.Free;
                    return;
                }
                //设置状态为繁忙
                this.status = Status.Busy;
                if (this.chain.length > 0) {
                    //获取队列的第一个元素
                    let task = this.chain.shift();
                    //触发下一个任务
                    task.apply(this, this.args);
                }
            }
            /**
             * 结束事件链调用，直接触发wait函数
             */
            reject(...args) {
                //设置busy状态
                this.status = Status.Busy;
                //更新参数
                this.args = arguments;
                //清空事件队列
                this.chain = [];
                //判断是否有等待回调函数
                while (this.waitFns.length > 0) {
                    let fn = this.waitFns.shift();
                    //触发wait函数
                    fn.apply(this, this.args);
                }
                //设置空闲状态
                this.status = Status.Free;
            }
            /**
             * 加入任务
             * @param task
             * @returns {fox.core.TaskChain}
             */
            post(task) {
                if (core.Utils.type(task) == "array") {
                    //加入事件队列(数组)
                    this.chain = [].concat(this.chain, task);
                }
                else {
                    //加入事件队列(单元素)
                    this.chain.push(task);
                }
                //如果状态处于busy状态,则不进行处理，直接返回
                if (this.status == Status.Busy) {
                    return this;
                }
                //如果状态处于free状态，则执行任务
                this.fire(this.args);
                return this;
            }
            /**
             * 加入等待任务
             * @param task
             * @returns {fox.core.EventChain}
             */
            wait(task) {
                //保存wait回调函数
                this.waitFns.push(task);
                //如果状态处于busy状态,则不进行处理，直接返回
                if (this.status == Status.Busy) {
                    return this;
                }
                //触发事件链，继续执行
                this.fire(this.args);
                return this;
            }
            /**
             * 判断事件链是否已经执行完成
             * @returns {boolean}
             */
            isFinish() {
                if (this.status == Status.Free && this.chain.length == 0) {
                    return true;
                }
                return false;
            }
        }
        core.EventChain = EventChain;
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by jiangcheng on 2016/08/18.
 */
///<reference path="Logger.ts"/>
///<reference path="Utils.ts"/>
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 策略
         */
        let Policy;
        (function (Policy) {
            //无限制
            Policy[Policy["limitless"] = 0] = "limitless";
            //只触发一次
            Policy[Policy["once"] = 1] = "once";
        })(Policy || (Policy = {}));
        /**
         * 事件代理
         */
        class EventProxy {
            /**
             * 构造函数
             */
            constructor() {
                /**
                 * 注册表
                 * @type {{}}
                 */
                this.register = {};
            }
            /**
             * 绑定事件
             *
             * @param key
             * @param callback
             * @returns {fox.core.EventProxy}
             */
            bind(key, callback, once) {
                //尝试从注册表中获取
                let queue = this.register[key];
                if (!queue) {
                    //创建空队列
                    queue = [];
                    //加入记录
                    this.register[key] = queue;
                }
                let policy = (once == true) ? Policy.once : Policy.limitless;
                let node = {
                    policy: policy,
                    callback: callback
                };
                //加入队列
                queue.push(node);
                return this;
            }
            /**
             * 解除绑定
             * @param key
             */
            unbind(key) {
                //移除
                delete this.register[key];
            }
            /**
             * 绑定一次性触发函数
             * @param key
             * @param callback
             */
            once(key, callback) {
                return this.bind(key, callback, true);
            }
            /**
             * 绑定多条件触发函数
             * @param args
             */
            all(...args) {
                let len = args.length;
                if (len < 1) {
                    return;
                }
                //获取callback
                let callback = args[len - 1];
                if (!core.Utils.isFunction(callback)) {
                    core.logger.error("callback can not benn empty");
                    return;
                }
                //记录未触发次数
                let remaining = len - 1;
                //记录已经触发的值
                let values = new Array(remaining);
                //获取条件数组
                let keys = [].splice.call(args, 0, remaining);
                for (let i = 0; i < keys.length; i++) {
                    this.once(keys[i], (value) => {
                        values[i] = value;
                        remaining -= 1;
                        //判断是否具备触发条件
                        if (remaining == 0) {
                            callback.apply(this, values);
                        }
                    });
                }
                return this;
            }
            /**
             * 触发函数
             * @param key
             * @param value
             */
            trigger(key, ...value) {
                //获取callback记录
                let queue = this.register[key];
                //判断是否找到记录
                if (!queue) {
                    return;
                }
                for (let i = 0; i < queue.length; i++) {
                    //获取记录
                    let node = queue[i];
                    if (node.policy == Policy.once) {
                        queue.slice(i, 1);
                    }
                    if (node.callback) {
                        try {
                            //触发函数
                            node.callback.apply(this, value);
                        }
                        catch (ex) {
                            core.logger.error(ex.message, ex);
                        }
                    }
                }
                return this;
            }
            /**
             * 触发函数(别名)
             * @param key
             * @param value
             * @returns {EventProxy}
             */
            emit(key, ...value) {
                return this.trigger(key, value);
            }
        }
        core.EventProxy = EventProxy;
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by jiangcheng on 2017/4/17.
 */
///<reference path="Utils.ts"/>
///<reference path="Ajax.ts"/>
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 资源
         */
        class Resource {
            /**
             * 构造函数
             */
            constructor() {
                //初始化缓存
                this.cache = {};
            }
            /**
             * 获取资源
             * @param url
             * @returns {string}
             */
            get(url, timeout, callback) {
                //判断缓存中是否存在数据
                if (this.cache[url]) {
                    let data = this.cache[url];
                    callback.call(this, 0, data);
                    return;
                }
                let _this = this;
                //发起ajax请求
                core.ajax({
                    url: url,
                    type: 'GET',
                    cache: false,
                    data: {},
                    timeout: timeout,
                    async: true,
                    success: function (data, status, xhr) {
                        //保存缓存
                        _this.cache[url] = data;
                        callback(0, data);
                    },
                    error: function (xhr, status) {
                        callback(1, status);
                    }
                });
            }
        }
        core.Resource = Resource;
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by jiangcheng on 2017/4/18.
 */
///<reference path="Utils.ts"/>
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 模块状态
         */
        let ModuleStatus;
        (function (ModuleStatus) {
            /**
             * 加载中
             */
            ModuleStatus[ModuleStatus["Loading"] = 0] = "Loading";
            /**
             * 加载完成
             */
            ModuleStatus[ModuleStatus["Loaded"] = 1] = "Loaded";
            /**
         * 定义中状态
         */
            ModuleStatus[ModuleStatus["Defining"] = 2] = "Defining";
            /**
             * 定义完成状态
             */
            ModuleStatus[ModuleStatus["Defined"] = 3] = "Defined";
        })(ModuleStatus = core.ModuleStatus || (core.ModuleStatus = {}));
        /**
         * 模块
         */
        class Module {
        }
        core.Module = Module;
        /**
         * 模块管理器
         */
        class Modules {
            /**
             * 构造函数
             */
            constructor() {
                this.register = {};
            }
            /**
             * 获取模块
             * @param path
             * @returns {any}
             */
            get(path) {
                return this.register[path];
            }
            /**
             * 加入模块
             * @param path
             * @param module
             */
            put(path, module) {
                this.register[path] = module;
            }
            /**
             * 移除模块
             * @param path
             * @returns {any}
             */
            remove(path) {
                let module = this.register[path];
                delete this.register[path];
                return module;
            }
            /**
             * 判断模块是否存在
             * @param path
             */
            contains(path) {
                if (this.register[path]) {
                    return true;
                }
                return false;
            }
            /**
             * 清空模块
             */
            clear() {
                this.register = {};
            }
        }
        core.Modules = Modules;
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by jiangcheng on 2016/08/06.
 */
///<reference path="Utils.ts"/>
///<reference path="Modules.ts"/>
///<reference path="Ajax.ts"/>
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 状态
         */
        let Status;
        (function (Status) {
            /**
             * 繁忙状态
             */
            Status[Status["Busy"] = 0] = "Busy";
            /**
             * 空闲状态
             */
            Status[Status["Free"] = 1] = "Free";
        })(Status || (Status = {}));
        /**
         * 进程
         */
        class Progress {
            /**
             * 构造函数
             */
            constructor(n, fn) {
                this.size = n;
                this.callback = fn;
                this.status = true;
                this.values = [];
                this.lastIndex = -1;
            }
            /**
             * work
             * @param flag
             * @param index
             * @param value
             */
            work(flag, index, value) {
                this.status = flag;
                if (this.status == true) {
                    // 如何index不一样才更新size
                    if (this.lastIndex != index) {
                        //设置工作量完成度
                        this.size--;
                    }
                    //更新last index
                    this.lastIndex = index;
                    //保存值
                    this.values.push(value);
                }
                if (this.size == 0 || this.status == false) {
                    //调用回调函数
                    try {
                        this.callback.call(this, this.status, this.values);
                    }
                    catch (ex) {
                        //打印日志
                        core.logger.error(ex.Message, ex);
                    }
                }
            }
            /**
             * 判断进程是否为正常状态
             * @returns {boolean}
             */
            isOK() {
                return this.status;
            }
        }
        /**
         * 任务节点
         */
        class TaskNode {
            /**
             * 构造函数
             * @param index
             * @param src
             * @param params
             * @param progress
             */
            constructor(index, src, params, progress) {
                this.index = index;
                this.src = src;
                this.params = params;
                this.progress = progress;
            }
        }
        /**
         * 检查css是否已经加载
         * @param node
         * @param meta
         */
        function checkCss(node, meta) {
            let sheet = node.sheet;
            let isLoaded;
            // for WebKit < 536
            if (meta.isOldWebKit) {
                if (sheet) {
                    isLoaded = true;
                }
            }
            else if (sheet) {
                try {
                    if (sheet.cssRules) {
                        isLoaded = true;
                    }
                }
                catch (ex) {
                    // The value of `ex.name` is changed from "NS_ERROR_DOM_SECURITY_ERR"
                    // to "SecurityError" since Firefox 13.0. But Firefox is less than 9.0
                    // in here, So it is ok to just rely on "NS_ERROR_DOM_SECURITY_ERR"
                    if (ex.name === "NS_ERROR_DOM_SECURITY_ERR") {
                        isLoaded = true;
                    }
                }
            }
            setTimeout(() => {
                meta.time += 20;
                if (isLoaded) {
                    meta.success();
                }
                else if (meta.time > meta.timeout) {
                    meta.error();
                }
                else {
                    checkCss(node, meta);
                }
            }, 20);
        }
        /**
         * require
         */
        class Require {
            /**
             * 构造函数
             */
            constructor() {
                //获取window
                this.win = window;
                //获取document
                this.doc = document;
                //获取head
                this.head = this.doc.head || this.doc.getElementsByTagName("head")[0] || this.doc.documentElement;
                //判断是否为老的webkit浏览器
                this.isOldWebKit = +navigator.userAgent.replace(/.*(?:AppleWebKit|AndroidWebKit)\/(\d+).*/, "$1") < 536;
                //路径简称配置
                this.paths = {};
                //别名配置
                this.alias = {};
                //根路径
                this.base = "";
                //字符编码
                this.charset = "UTF-8";
                //超时
                this.timeout = -1;
                //模块管理器
                this.modules = new core.Modules();
                //资源管理器
                this.resource = new core.Resource();
                //任务队列
                this.queue = [];
                //空闲状态
                this.status = Status.Free;
                //版本
                this.version = "1.0.0";
            }
            /**
             * 配置静态参数，用于初始化
             * @param setting
             * @returns {fox.core.Require}
             */
            config(setting) {
                if (!setting) {
                    return;
                }
                //设置路径映射
                this.paths = setting.paths ? core.Utils.extend({}, setting.paths) : this.paths;
                //设置别名
                this.alias = setting.alias ? core.Utils.clone({}, setting.alias) : this.alias;
                //设置是base路径
                if (setting.base != undefined) {
                    this.base = setting.base;
                }
                //设置请求编码
                if (setting.charset != undefined) {
                    this.charset = setting.charset;
                }
                //设置版本
                if (setting.version != undefined) {
                    //版本号
                    this.version = setting.version;
                }
                return this;
            }
            /**
             * 获取资源
             * @param url
             * @param callback
             * @returns {fox.core.Require}
             */
            get(url, callback, options) {
                //解析路径
                url = this.resolvePath(url);
                let t = this.timeout;
                if (options && options["timeout"]) {
                    t = options["timeout"];
                }
                this.resource.get(url, t, callback);
                return this;
            }
            /**
             * 加载资源
             * @param args
             */
            use(...args) {
                return this.require.apply(this, args);
            }
            /**
             * 加载资源
             * @param args
             */
            require(...args) {
                //定义参数
                let libs = [], options, callback, len;
                len = args.length;
                //获取运行参数
                options = {};
                if (core.Utils.type(args[len - 1]) == "object") {
                    core.Utils.extend(true, options, args[len - 1]);
                    len--;
                }
                //获取回调函数
                if (core.Utils.type(args[len - 1]) == "function") {
                    callback = args[len - 1];
                    len--;
                }
                //解析路径
                for (let i = 0; i < len; i++) {
                    let t = core.Utils.type(args[i]);
                    if (t === "array") {
                        let list = args[i];
                        for (let j = 0; j < list.length; j++) {
                            if (list[j] == undefined) {
                                continue;
                            }
                            let s = this.resolvePath(list[j]);
                            libs.push(s);
                        }
                    }
                    else {
                        let ss = args[i].split(",");
                        for (let j = 0; j < ss.length; j++) {
                            if (ss[j] == undefined) {
                                continue;
                            }
                            let s = this.resolvePath(ss[j]);
                            libs.push(s);
                        }
                    }
                }
                let deferred = new core.Deferred();
                //判断是否加载完成
                if (libs.length == 1 && this.modules.contains(libs[0])) {
                    //获取模块
                    let module = this.modules.get(libs[0]);
                    //判断模块是否已经给定义
                    if (module.status == core.ModuleStatus.Defined) {
                        //获取exports
                        let exports = module.exports;
                        //添加done和fail函数
                        exports.done = function done(fn) {
                            deferred.done(fn);
                        };
                        exports.fail = function fail(fn) {
                            deferred.fail(fn);
                        };
                        try {
                            //调用回调函数
                            if (callback) {
                                callback.call(this, exports);
                            }
                        }
                        catch (ex) {
                            core.logger.error(ex.Message, ex);
                        }
                        try {
                            //通知加载成功
                            deferred.resolve(module);
                        }
                        catch (ex) {
                            core.logger.error(ex.Message, ex);
                        }
                        //返回结果
                        return exports;
                    }
                }
                //定义进程
                let progress = new Progress(libs.length, (status, values) => {
                    if (progress.isOK()) {
                        try {
                            //调用回调函数
                            if (callback) {
                                callback.apply(this, values);
                            }
                        }
                        catch (ex) {
                            core.logger.error(ex.Message, ex);
                        }
                        try {
                            //通知加载成功
                            deferred.resolve(values);
                        }
                        catch (ex) {
                            core.logger.error(ex.Message, ex);
                        }
                    }
                    else {
                        try {
                            //调用回调函数
                            if (callback) {
                                callback.apply(this, values);
                            }
                        }
                        catch (ex) {
                            core.logger.error(ex.Message, ex);
                        }
                        try {
                            //通知加载成功
                            deferred.reject(values);
                        }
                        catch (ex) {
                            core.logger.error(ex.Message, ex);
                        }
                    }
                });
                for (let i = 0; i < libs.length; i++) {
                    //定义任务节点
                    let taskNode = new TaskNode(i, libs[i], options, progress);
                    //加入队列
                    this.queue.push(taskNode);
                }
                if (this.status == Status.Free) {
                    this.execMountTask();
                }
                return deferred;
            }
            /**
             * 执行mount task
             */
            execMountTask() {
                //获取队列长度
                let len = this.queue.length;
                if (len == 0) {
                    //设置空闲状态
                    this.status = Status.Free;
                    return;
                }
                //设置运行状态
                this.status = Status.Busy;
                //获取任务节点
                let node = this.queue.shift();
                //判断任务进程是否有效
                if (node.progress.isOK() == false) {
                    this.execMountTask();
                    return;
                }
                //加载资源
                this.mount(node);
            }
            /**
             * 加载
             * @param taskNode
             */
            mount(taskNode) {
                //获取类型
                let type = this.getFileNamePostfix(taskNode.src);
                if (type == "js") {
                    //尝试获取js加载点
                    let point = taskNode.params["jsPoint"];
                    if (point == undefined) {
                        point = this.head;
                    }
                    this.mountJS(taskNode, point);
                    return;
                }
                else if (type == "css") {
                    //尝试获取js加载点
                    let point = taskNode.params["cssPoint"];
                    if (point == undefined) {
                        point = this.head;
                    }
                    this.mountCSS(taskNode, point);
                    return;
                }
                else {
                    //尝试获取js加载点
                    let point = taskNode.params["htmlPoint"];
                    if (point == undefined) {
                        point = this.doc.body;
                    }
                    this.mountHTML(taskNode, point);
                    return;
                }
            }
            /**
             * 加载css
             * @param task
             * @param point
             */
            mountCSS(task, point) {
                let path = task.src;
                let node = this.doc.createElement("link");
                if (this.charset) {
                    node.charset = this.charset;
                }
                node.setAttribute("crossorigin", true);
                let supportOnload = "onload" in node;
                // for Old WebKit and Old Firefox
                if (this.isOldWebKit || !supportOnload) {
                    let meta = {
                        //定义超时
                        timeout: this.timeout || 3000,
                        //当前消耗时间
                        time: 0,
                        //是否为旧的webkit
                        isOldWebKit: this.isOldWebKit,
                        //定义成功回调函数
                        success: () => {
                            //通知成功
                            task.progress.work(true, task.index, task.src);
                            //继续执行任务
                            this.execMountTask();
                        },
                        //定义错误回调函数
                        error: () => {
                            //css已经加载，node移除以便于节省内存
                            try {
                                point.removeChild(node);
                            }
                            catch (ex) {
                                core.logger.error(ex.message, ex);
                            }
                            //通知失败
                            task.progress.work(false);
                            //继续执行任务
                            this.execMountTask();
                        }
                    };
                    setTimeout(() => {
                        checkCss(node, meta);
                    }, 1); // Begin after node insertion
                }
                else {
                    node.onload = () => {
                        //移除监听
                        node.onload = node.onerror = null;
                        //通知成功
                        task.progress.work(true, task.index, task.src);
                        //继续执行任务
                        this.execMountTask();
                    };
                    node.onerror = () => {
                        //node移除以便于节省内存
                        try {
                            point.removeChild(node);
                        }
                        catch (ex) {
                            core.logger.error(ex.message, ex);
                        }
                        //移除监听
                        node.onload = node.onerror = null;
                        //通知失败
                        task.progress.work(false);
                        //继续执行任务
                        this.execMountTask();
                    };
                }
                //判断是否有上一个节点
                let preNode = this.doc.getElementById(path);
                //如果存在旧的css节点，则删除
                if (preNode) {
                    preNode.parentNode.removeChild(preNode);
                }
                node.async = true;
                node.rel = "stylesheet";
                node.type = "text/css";
                node.id = path;
                //设置href
                node.href = path;
                //加入css脚本
                point.appendChild(node);
            }
            /**
             * 加载html
             * @param task
             * @param evtChain
             */
            mountHTML(task, point) {
                let t = this.timeout;
                if (task.params["timeout"]) {
                    t = task.params["timeout"];
                }
                let path = task.src;
                //获取资源并加载
                this.resource.get(path, t, (code, data) => {
                    if (code == 0) {
                        point.innerHTML = data;
                        //通知成功
                        task.progress.work(true, task.index, data);
                    }
                    else {
                        core.logger.error("code:" + code + ",message:" + data);
                        //通知失败
                        task.progress.work(false);
                    }
                    //继续执行任务
                    this.execMountTask();
                });
            }
            /**
             * 加载js
             * @param path
             * @param point
             * @param evtChain
             */
            mountJS(task, point) {
                let path = task.src;
                //从缓存中获取module
                let module = this.modules.get(path);
                //判断module是否已经存在
                if (module && module.status == core.ModuleStatus.Defined) {
                    //通知任务完成
                    task.progress.work(true, task.index, module.exports);
                    //继续执行任务
                    this.execMountTask();
                    return;
                }
                //定义新的module
                module = new core.Module();
                //设置ID
                module.id = path;
                //保存source
                module.src = path;
                //设置状态
                module.status = core.ModuleStatus.Loading;
                //注册modules
                this.modules.put(path, module);
                let node = this.doc.createElement("script");
                node.setAttribute("crossorigin", 'true');
                if (this.charset) {
                    node.charset = this.charset;
                }
                //判断是否有onload函数
                let supportOnload = "onload" in node;
                if (supportOnload) {
                    node.onload = () => {
                        //移除监听
                        node.onload = node.onerror = null;
                        //判断是否为模块
                        if (module.status == core.ModuleStatus.Loading) {
                            task.progress.work(true, task.index, path);
                        }
                        //继续执行任务
                        this.execMountTask();
                    };
                    node.onerror = () => {
                        //js已经加载，node移除以便于节省内存
                        try {
                            point.removeChild(node);
                        }
                        catch (ex) {
                            core.logger.error(ex.message, ex);
                        }
                        //移除监听
                        node.onload = node.onerror = null;
                        //通知失败
                        task.progress.work(false);
                        //继续执行任务
                        this.execMountTask();
                    };
                }
                else {
                    node.onreadystatechange = () => {
                        if (/loaded|complete/.test(node.readyState)) {
                            //移除监听
                            node.onreadystatechange = null;
                            //判断是否为模块
                            if (module.status == core.ModuleStatus.Loading) {
                                task.progress.work(true, task.index, path);
                            }
                            //继续执行任务
                            this.execMountTask();
                        }
                    };
                }
                //设置为非同步模式
                node.async = true;
                //设置src
                node.src = path;
                //重新定义define函数
                this.win.define = this.createDefine(module, task);
                //加入js脚本
                point.appendChild(node);
            }
            /**
             * 创建define函数
             * @param module
             * @param task
             * @returns {(id:any, deps:any, factory:any)=>undefined}
             */
            createDefine(module, task) {
                //定义require
                let _require = (...args) => {
                    let exports = this.require.apply(this, args);
                    return exports;
                };
                return (...args) => {
                    //设置模块状态
                    module.status = core.ModuleStatus.Defining;
                    let id, deps, factory;
                    //获取参数列表长度
                    let argsLen = args.length;
                    // 如果参数个数为1，代表函数为define(factory)
                    if (argsLen === 1) {
                        factory = args[0];
                        id = undefined;
                    }
                    else if (argsLen === 2) {
                        factory = args[1];
                        // define(deps, factory)
                        if (core.Utils.isArray(args[0])) {
                            deps = args[0];
                            id = undefined;
                        }
                        else {
                            deps = undefined;
                        }
                    }
                    else {
                        id = args[0];
                        deps = args[1];
                        factory = args[2];
                    }
                    if (id) {
                        module.id = id;
                        //加入别名(id作为别名)
                        this.alias[id] = module.src;
                    }
                    //解析依赖关系
                    if (deps) {
                        let remain = deps.length;
                        this.require(deps, function () {
                            if (core.Utils.type(factory) == "function") {
                                module.exports = {};
                                factory.call(module.exports, _require, module.exports, module);
                            }
                            else {
                                module.exports = factory;
                            }
                            //更新状态
                            module.status = core.ModuleStatus.Defined;
                            //通知成功
                            task.progress.work(true, task.index, module.exports);
                        });
                    }
                    else {
                        if (core.Utils.type(factory) == "function") {
                            module.exports = {};
                            factory.call(module.exports, _require, module.exports, module);
                        }
                        else {
                            module.exports = factory;
                        }
                        //更新状态
                        module.status = core.ModuleStatus.Defined;
                        //通知成功
                        task.progress.work(true, task.index, module.exports);
                    }
                };
            }
            /**
             * 解析路径
             * @param uri
             * @returns {string}
             */
            resolvePath(uri) {
                try {
                    // 解析URI
                    uri = this.parserPath(uri, true);
                    //获取后缀
                    let postfix = this.getFileNamePostfix(uri);
                    postfix = postfix.toLowerCase();
                    //格式化文件名
                    if (postfix != "js" && postfix != "css" && postfix != "html") {
                        uri += ".js";
                    }
                    if (uri.indexOf("?") == -1) {
                        uri += "?version=";
                        uri += this.version;
                    }
                    else {
                        uri += "&version=";
                        uri += this.version;
                    }
                    return uri;
                }
                catch (e) {
                    core.logger.error("resolve path[" + uri + "] fail", e);
                    throw e;
                }
            }
            /**
             * 获取文件名后缀
             * @param name
             * @returns {any}
             */
            getFileNamePostfix(name) {
                let lastIndex = name.lastIndexOf("?");
                if (lastIndex == -1) {
                    lastIndex = name.length;
                }
                let index = name.lastIndexOf(".", lastIndex);
                if (index == -1) {
                    return "";
                }
                //获取后缀
                let postfix = name.substring(index + 1, lastIndex);
                return postfix;
            }
            /**
             * 解析路径
             * @param uri
             * @param replace
             */
            parserPath(uri, replace) {
                //绝对路径（http），不解析直接返回
                if (uri.indexOf("http://") == 0) {
                    return uri;
                }
                //绝对路径（https），不解析直接返回
                if (uri.indexOf("https://") == 0) {
                    return uri;
                }
                //绝对路径（file），不解析直接返回
                if (uri.indexOf("file:///") == 0) {
                    return uri;
                }
                // id"/lib/fox/fox.js"，代表路径从根目录起
                if (uri.indexOf("/") == 0) {
                    let path = "", href = this.doc.location.href, pathName = this.doc.location.pathname;
                    if (this.doc.location.protocol == "file:") {
                        let index = href.lastIndexOf("/");
                        path = href.slice(0, index + 1) + uri;
                    }
                    else if (pathName.length == 0) {
                        path = href + uri;
                    }
                    else {
                        let index = href.lastIndexOf(pathName);
                        let host = href.slice(0, index);
                        index = pathName.indexOf("/", 1);
                        let projectName = "";
                        if (index != -1) {
                            projectName = pathName.slice(0, index);
                        }
                        path = host + projectName + "/" + uri;
                    }
                    return path;
                }
                // id"./lib/fox/fox.js"，代表路径从当前目录起
                if (uri.indexOf("./") == 0) {
                    let href = this.doc.location.href, index = href.lastIndexOf("/"), path = href.slice(0, index) + uri.slice(1);
                    return path;
                }
                //是否进行别名替换
                if (replace) {
                    //判断是否为别名
                    let path = this.alias[uri];
                    if (!path) {
                        let index = uri.indexOf("/");
                        if (index == -1) {
                            path = this.base + uri;
                        }
                        else {
                            //判断是否有路径映射
                            let mark = uri.slice(0, index), s = this.paths[mark];
                            if (s) {
                                path = s + uri.slice(index);
                            }
                            else {
                                path = this.base + uri;
                            }
                        }
                    }
                    return this.parserPath(path, false);
                }
                let href = this.doc.location.href;
                let pathName = this.doc.location.pathname;
                let path;
                if (this.doc.location.protocol == "file:") {
                    let index = href.lastIndexOf("/");
                    path = href.slice(0, index + 1) + uri;
                }
                else if (pathName.length == 0) {
                    path = href + uri;
                }
                else {
                    let index = href.lastIndexOf(pathName);
                    let host = href.slice(0, index);
                    index = pathName.indexOf("/", 1);
                    let projectName = "";
                    if (index != -1) {
                        projectName = pathName.slice(0, index);
                    }
                    path = host + projectName + "/" + uri;
                }
                return path;
            }
        }
        core.Require = Require;
        /**
         * 定义require
         * @type {fox.core.Require}
         */
        core.require = new Require();
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by jiangcheng on 2016/08/06.
 */
///<reference path="Logger.ts"/>
///<reference path="Utils.ts"/>
///<reference path="Require.ts"/>
///<reference path="EventChain.ts"/>
///<reference path="Defferred.ts"/>
var fox;
(function (fox) {
    var core;
    (function (core) {
        /**
         * 过滤类型
         */
        let FilterType;
        (function (FilterType) {
            /**
             * before拦截
             */
            FilterType[FilterType["Before"] = 0] = "Before";
            /**
             * mount拦截
             */
            FilterType[FilterType["Mount"] = 1] = "Mount";
            /**
             * ready拦截
             */
            FilterType[FilterType["Ready"] = 2] = "Ready";
            /**
              *  mount拦截
              * */
            FilterType[FilterType["UnMount"] = 3] = "UnMount";
            /**
             * destroy拦截
             */
            FilterType[FilterType["Destroy"] = 4] = "Destroy";
        })(FilterType = core.FilterType || (core.FilterType = {}));
        /**
         * 打开策略
         */
        let OpenPolicy;
        (function (OpenPolicy) {
            /**
             * 替换策略
             */
            OpenPolicy[OpenPolicy["Replace"] = 0] = "Replace";
            /**
             * 增加策略
             */
            OpenPolicy[OpenPolicy["Append"] = 1] = "Append";
        })(OpenPolicy = core.OpenPolicy || (core.OpenPolicy = {}));
        /**
         * 定义路由
         */
        class Router {
            /**
             * 构造函数
             */
            constructor() {
                //保存document
                this.doc = document;
                //route table
                this.routeTable = {};
                //激活页面
                this.activePages = [];
                //历史记录
                this.history = [];
                //历史缓存大小
                this.historyCacheSize = 200;
                //过滤器
                this.filters = [];
                //参数配置
                this.settings = {};
                //初始化mount handlers
                this.mountHandlers = {};
                //创建event chain
                this.eventChain = new core.EventChain();
                //设默认mount handler
                this.addMountHandler("default", new DefaultMountHandler());
            }
            /**
             * 加入mount handler
             * @param type
             * @param handler
             */
            addMountHandler(type, handler) {
                this.mountHandlers[type] = handler;
                return this;
            }
            /**
             * 获取mount handler
             * @param type
             * @returns {any}
             */
            getMountHandler(type) {
                return this.mountHandlers[type];
            }
            /**
             * 移除mount handler
             * @param type
             * @returns {fox.core.Router}
             */
            removeMountHandler(type) {
                if (this.mountHandlers[type]) {
                    delete this.mountHandlers[type];
                }
                return this;
            }
            /**
             * 加入过滤器
             *
             * @param filter
             * @returns {fox.core.Router}
             */
            addFilter(filter) {
                //加入过滤器
                this.filters.push(filter);
                return this;
            }
            /**
             * 移除过滤器
             * @param name
             */
            removeFilter(args) {
                //获取过滤器名称
                let name;
                if (core.Utils.type(args) == "string") {
                    name = args;
                }
                else {
                    name = args["name"];
                }
                //查找待删除过滤器
                let i = 0;
                for (; i < this.filters.length; i++) {
                    if (name == this.filters[i].name) {
                        break;
                    }
                }
                //删除过滤器
                if (i < this.filters.length) {
                    this.filters.splice(i, 1);
                }
                return this;
            }
            /**
             * 执行过滤器
             * @param type
             * @param args
             * @returns {any}
             */
            doFilters(type, ...args) {
                if (type == FilterType.Before) {
                    //按顺序执行
                    for (let i = 0; i < this.filters.length; i++) {
                        if (!this.filters[i].before) {
                            continue;
                        }
                        //获取before方法
                        let before = this.filters[i].before;
                        //调用before方法
                        let res = before.apply(this, args);
                        //判断是否中断执行
                        if (res == false) {
                            return res;
                        }
                    }
                    return true;
                }
                else if (type == FilterType.Mount) {
                    //按顺序执行
                    for (let i = 0; i < this.filters.length; i++) {
                        if (!this.filters[i].mount) {
                            continue;
                        }
                        //获取mount方法
                        let mount = this.filters[i].mount;
                        //调用mount方法
                        let res = mount.apply(this, args);
                        //判断是否中断执行
                        if (res == false) {
                            return res;
                        }
                    }
                    return true;
                }
                else if (type == FilterType.Ready) {
                    //按顺序执行
                    for (let i = 0; i < this.filters.length; i++) {
                        if (!this.filters[i].ready) {
                            continue;
                        }
                        //获取ready方法
                        let ready = this.filters[i].ready;
                        //调用ready方法
                        let res = ready.apply(this, args);
                        //判断是否中断执行
                        if (res == false) {
                            return res;
                        }
                    }
                    return true;
                }
                else if (type == FilterType.UnMount) {
                    //按顺序执行
                    for (let i = 0; i < this.filters.length; i++) {
                        if (!this.filters[i].unMount) {
                            continue;
                        }
                        //获取unMount方法
                        let unMount = this.filters[i].unMount;
                        //调用unMount方法
                        let res = unMount.apply(this, args);
                        //判断是否中断执行
                        if (res == false) {
                            return res;
                        }
                    }
                    return true;
                }
                else {
                    //按顺序执行
                    for (let i = 0; i < this.filters.length; i++) {
                        if (!this.filters[i].destroy) {
                            continue;
                        }
                        //获取destroy方法
                        let destroy = this.filters[i].destroy;
                        //调用destroy方法
                        let res = destroy.apply(this, args);
                        //判断是否中断执行
                        if (res == false) {
                            return res;
                        }
                    }
                    return true;
                }
            }
            /**
             * 加入路由信息
             * @param name
             * @param route
             * @returns {fox.core.Router}
             */
            addRoute(name, route) {
                let unit = {};
                core.Utils.extend(true, unit, route);
                //保存route信息
                this.routeTable[name] = unit;
            }
            /**
             * 移除路由信息
             * @param name
             * @returns {fox.core.Router}
             */
            removeRoute(name) {
                let route = this.routeTable[name];
                if (route) {
                    delete this.routeTable[name];
                }
                return route;
            }
            /**
             * 获取路由信息
             * @param name
             */
            getRoute(name) {
                let route = {};
                core.Utils.extend(true, route, this.routeTable[name]);
                return route;
            }
            /**
             * 加入路由表
             * @param routeTable
             */
            addRouteTable(routeTable) {
                //保存路由表数据
                for (let name in routeTable) {
                    //获取路由信息
                    let route = routeTable[name];
                    //加入路由信息
                    this.addRoute(name, route);
                }
            }
            /**
             * 清空路由表
             */
            clearRouteTable() {
                this.routeTable = {};
            }
            /**
             * 设置默认root id
             * @param rootId
             * @returns {fox.core.Router}
             */
            setDefaultRootId(rootId) {
                this.settings["rootId"] = rootId;
            }
            /**
             * 获取默认root id
             * @returns {any}
             */
            getDefaultRootId() {
                return this.settings["rootId"];
            }
            /**
             * 设置历史缓存的大小
             * @param size
             */
            setHistoryCacheSize(size) {
                this.historyCacheSize = size;
            }
            /**
             * 获取历史缓存大小
             * @returns {number}
             */
            getHistoryCacheSize() {
                return this.historyCacheSize;
            }
            /**
             * 加入历史记录
             * @param id
             * @param data
             * @param rootId
             */
            addHistory(id, data, rootId) {
                //计算历史记录数量超出最大限制的数量
                let n = this.history.length - this.historyCacheSize;
                if (n >= 0) {
                    //移除一半的记录
                    this.history.splice(0, this.history.length / 2);
                }
                //创建历史记录
                let rec = {
                    id: id,
                    data: data,
                    rootId: rootId
                };
                //加入历史记录
                this.history.push(rec);
            }
            /**
             *  移除指定的历史记录
             * @param id
             */
            removeHistory(id) {
                //查找并删除指定的记录
                for (let i = this.history.length - 1; i >= 0; i--) {
                    if (this.history[i].id === id) {
                        this.history.splice(i, 1);
                        break;
                    }
                }
            }
            /**
             * 清空临时
             */
            clearHistory() {
                this.history = [];
            }
            /**
             * 历史回退
             * @param term
             * @returns {Deferred}
             */
            back(term) {
                //如果未指定条件，则默认回退一个
                let len = this.history.length;
                if (len <= 1) {
                    //定义deferred
                    let deferred = new core.Deferred();
                    deferred.reject("");
                    return deferred;
                }
                //无条件则默认后退一步
                if (!term) {
                    //删除记录
                    let index = len - 2;
                    let recs = this.history.splice(index, 2);
                    //标志为回退
                    recs[0].isBack = true;
                    //重新跳转
                    return this.to(recs[0]);
                }
                if (term.id) {
                    let index = -1;
                    for (let i = len - 2; i >= 0; i--) {
                        let node = this.history[i];
                        if (node.id == term.id) {
                            index = i;
                            break;
                        }
                    }
                    //判断是否查找到满足条件的记录
                    if (index >= 0) {
                        let n = len - index;
                        let recs = this.history.splice(index, n);
                        //标志为回退
                        recs[0].isBack = true;
                        //重新跳转
                        return this.to(recs[0]);
                    }
                    else {
                        core.logger.error("can not found the term id:[" + term.id + "]");
                    }
                }
                else if (term.before) {
                    let index = -1;
                    for (let i = 0; i < len; i++) {
                        let node = this.history[i];
                        if (node.id == term.before) {
                            index = i;
                            break;
                        }
                    }
                    index -= 1;
                    //判断是否查找到满足条件的记录
                    if (index >= 0) {
                        let n = len - index;
                        let recs = this.history.splice(index, n);
                        //标志为回退
                        recs[0].isBack = true;
                        //重新跳转
                        return this.to(recs[0]);
                    }
                    else {
                        core.logger.error("can not found the term before:[" + term.before + "]");
                    }
                }
                else {
                    //定义deferred
                    let deferred = new core.Deferred();
                    deferred.reject("");
                    return deferred;
                }
            }
            /**
             * 路由跳转(替换原来内容)
             *
             * @returns {any}
             */
            replace(...args) {
                //获取跳转目标信息
                let target;
                if (args.length == 1 && core.Utils.type(args[0]) == "object") {
                    target = args[0];
                }
                else {
                    target = {};
                    for (let i = 0; i < args.length; i++) {
                        if (i == 0) {
                            target.id = args[i];
                        }
                        else if (i == 1) {
                            target.data = args[i];
                        }
                        else if (i == 2) {
                            target.rootId = args[i];
                        }
                        else if (i == 3) {
                            target.options = args[i];
                        }
                    }
                }
                //加入replace策略
                target.options = target.options === void (0) ? {} : target.options;
                target.options.openPolicy = OpenPolicy.Replace;
                //路由跳转
                return this.to(target);
            }
            /**
             * 路由跳转(替换原来内容)
             *
             * @returns {any}
             */
            append(...args) {
                //获取跳转目标信息
                let target;
                if (args.length == 1 && core.Utils.type(args[0]) == "object") {
                    target = args[0];
                }
                else {
                    target = {};
                    for (let i = 0; i < args.length; i++) {
                        if (i == 0) {
                            target.id = args[i];
                        }
                        else if (i == 1) {
                            target.data = args[i];
                        }
                        else if (i == 2) {
                            target.rootId = args[i];
                        }
                        else if (i == 3) {
                            target.options = args[i];
                        }
                    }
                }
                //加入append策略
                target.options = target.options === void (0) ? {} : target.options;
                target.options.openPolicy = OpenPolicy.Append;
                //路由跳转
                return this.to(target);
            }
            /**
             * 移除append方式加入的页面
             * 如果append页面不在最顶层(可以包含子页面），将不会生效
             *
             *@param boolean
             */
            remove() {
                //无激活页面，不做任何处理
                if (this.activePages.length == 0) {
                    return false;
                }
                //查找最新一个append页面
                let index = -1;
                for (let i = this.activePages.length - 1; i >= 0; i--) {
                    //如果某个pages下面的子页面大于1，则认为是一个append页面
                    if (this.activePages[i].nodes.length > 1) {
                        index = i;
                        break;
                    }
                    //再判断打开方式是否为append
                    let cite = this.activePages[i].nodes[0].cite;
                    if (cite.options && cite.options.openPolicy === OpenPolicy.Append) {
                        index = i;
                        break;
                    }
                }
                //查找不到激活的append页面，不做处理
                if (index == -1) {
                    return false;
                }
                //卸载处理
                return this.unMount("", index);
            }
            /**
             * 路由跳转
             *
             * @returns {any}
             */
            to(...args) {
                //获取跳转目标信息
                let target;
                if (args.length == 1 && core.Utils.type(args[0]) == "object") {
                    target = args[0];
                }
                else {
                    target = {};
                    for (let i = 0; i < args.length; i++) {
                        if (i == 0) {
                            target.id = args[i];
                        }
                        else if (i == 1) {
                            target.data = args[i];
                        }
                        else if (i == 2) {
                            target.rootId = args[i];
                        }
                        else if (i == 3) {
                            target.options = args[i];
                        }
                    }
                }
                //获取路由信息
                let route = this.getRoute(target.id);
                if (!route) {
                    core.logger.error("路由信息未注册[" + target.id + "]");
                    return;
                }
                //如果root id未设置，从路由信息中获取
                if (!target.rootId) {
                    target.rootId = route["rootId"];
                }
                //默认root id
                if (!target.rootId) {
                    target.rootId = this.settings["rootId"];
                }
                //定义引用
                let cite = {
                    id: target.id,
                    rootId: target.rootId,
                    startTime: new Date().getTime(),
                    options: target.options,
                    isBack: target.isBack ? target.isBack : false,
                    data:target.data,
                };
                //执行before过滤器
                let res = this.doFilters(FilterType.Before, target.id, target.data, cite);
                //如果返回false，则不允许跳转
                if (res == false) {
                    return;
                }
                //定义deferred
                let deferred = new core.Deferred();
                //装载
                this.mount(target, route, cite, deferred);
                return deferred;
            }
            /**
             * 装载
             * @param target
             * @param route
             * @param cite
             * @param deferred
             * @returns {boolean}
             */
            mount(target, route, cite, deferred) {
                //获取mount hanlder类型
                let mountHandlerType = cite.options && cite.options.mountHandlerType ? cite.options.mountHandlerType : "default";
                //获取mount handler
                let handler = this.getMountHandler(mountHandlerType);
                //加入事件队列
                this.eventChain.post(() => {
                    //处理mount
                    handler.mount(target.id, route, cite, this, (code, exports, root) => {
                        //正常返回
                        if (code !== 0) {
                            core.logger.error("mount失败");
                            //通知失败
                            deferred.reject(target.id);
                            return;
                        }
                        //设置默认值
                        if (exports == undefined) {
                            exports = {};
                        }
                        cite.root = root;
                        let page = {
                            target: target,
                            exports: exports,
                            cite: cite
                        };
                        //查找root id的pages的索引
                        let index = this.lastIndexOfPages(target.rootId);
                        //获取最新一个page记录节点
                        let lastPages;
                        if (index == -1) {
                            lastPages = {
                                id: target.rootId,
                                nodes: []
                            };
                            //加入激活页面集合
                            this.activePages.push(lastPages);
                        }
                        else {
                            lastPages = this.activePages[index];
                        }
                        lastPages.nodes.push(page);
                        //加入历史记录(append模式不加入历史记录)
                        if (cite.openPolicy != OpenPolicy.Append) {
                            this.addHistory(target.id, target.data, target.rootId);
                        }
                        let curTime = new Date().getTime();
                        core.logger.debug("打开页面[" + target.id + "]耗时:" + (curTime - cite.startTime) + "毫秒");
                        if (exports.ready) {
                            //执行过滤器
                            this.doFilters(FilterType.Ready, exports, target.id, target.data, cite);
                            //执行ready方法
                            try {
                                console.log(exports);
                                exports.ready(target.id, target.data, cite);
                            }
                            catch (ex) {
                                core.logger.error(ex.Message, ex);
                            }
                        }
                        //通知成功
                        deferred.resolve(target.id);
                        //通知下一个
                        this.eventChain.fire();
                    });
                });
                return true;
            }
            /**
             * 释放pages
             * @param pages
             * @returns {boolean}
             */
            releasePages(pages, all, isChild) {
                //获取结束索引
                let endIndex = all == true ? 0 : pages.nodes.length - 1;
                //按后进先出的顺序销毁
                for (let i = pages.nodes.length - 1; i >= endIndex; i--) {
                    //获取引用
                    let cite = pages.nodes[i].cite;
                    //获取exports
                    let exports = pages.nodes[i].exports;
                    if (exports == undefined) {
                        continue;
                    }
                    let target = pages.nodes[i].target;
                    //获取路由
                    let route = this.getRoute(target.id);
                    //获取mount handler type
                    let mountHandlerType = cite.options && cite.options.mountHandlerType ? cite.options.mountHandlerType : "default";
                    //获取mount handler
                    let handler = this.getMountHandler(mountHandlerType);
                    try {
                        handler.unMount(target.id, route, cite, exports, this);
                    }
                    catch (ex) {
                        core.logger.error(ex.Message, ex);
                    }
                    //调用destroy方法
                    if (exports.destroy) {
                        //执行destroy过滤器
                        this.doFilters(FilterType.Destroy, exports, target.id, cite);
                        //获取destroy
                        let destroy = exports.destroy;
                        //调用模块的destroy方法
                        destroy.call(exports, target.id, cite);
                    }
                    //如果是孩子页面，则从记录中移除
                    if (isChild) {
                        this.removeHistory(target.id);
                    }
                    //删除node page
                    pages.nodes.splice(i, 1);
                }
                return true;
            }
            /**
             * 卸载
             * @param rootId
             * @param index
             * @returns {boolean}
             */
            unMount(rootId, index) {
                if (this.activePages.length == 0) {
                    return;
                }
                //根据是否指定active pages的索引，进行分别的处理
                let flag = (index == void (0));
                if (!flag) {
                    rootId = this.activePages[index].id;
                }
                else {
                    index = 0;
                }
                //卸载指定root id下面的所有页面
                for (let i = this.activePages.length - 1; i >= index; i--) {
                    let pages = this.activePages[i];
                    if (rootId == pages.id) {
                        this.releasePages(pages, flag, false);
                        //移除激活页面
                        if (flag || pages.nodes.length == 0) {
                            this.activePages.splice(i, 1);
                        }
                        return true;
                    }
                    else if (this.isChildNode(rootId, pages.id)) {
                        this.releasePages(pages, true, true);
                        //移除激活页面
                        this.activePages.splice(i, 1);
                    }
                }
                return false;
            }
            /**
             * 判断元素是否为父子关系
             * @param root
             * @param node
             */
            isChildNode(root, child) {
                //获取parent node
                let parentNode = core.Utils.type(root) === "string" ? this.doc.getElementById(root) : root;
                //如果parent node不存在，则不存在父子关系
                if (!parentNode) {
                    return false;
                }
                //获取child node
                let childNode = core.Utils.type(child) === "string" ? this.doc.getElementById(child) : child;
                //如果child node不存在，则不存在父子关系
                if (!childNode) {
                    return false;
                }
                //查找
                let parent = childNode.parentNode;
                while (parent != parentNode && parent != this.doc) {
                    parent = parent.parentNode;
                }
                let res = parent == parentNode;
                return res;
            }
            /**
             * 发送消息
             * @param id
             * @param type
             * @param message
             * @returns {any}
             */
            sendMessage(id, type, message) {
                //获取路由信息
                let route = this.getRoute(id);
                if (!route || !route.js) {
                    core.logger.error("路由信息未注册[" + id + "]，或无js模块");
                    return;
                }
                //获取exports
                let exports = core.require.require(route.js);
                //判断exports是否存在
                if (exports && exports.onmessage) {
                    //查找前活动页面
                    let page = this.findPage(id);
                    //获取引用
                    let cite = page ? page.cite : {};
                    //通知
                    let res = exports.onmessage(type, message, cite);
                    return res;
                }
                else {
                    core.logger.error("exports或exports.onMessage未定义");
                }
            }
            /**
             * 查找最后一个满足要求的pages
             * @param rootId
             * @returns number
             */
            lastIndexOfPages(rootId) {
                let index = -1;
                for (let i = this.activePages.length - 1; i >= 0; i--) {
                    if (this.activePages[i].id === rootId) {
                        index = i;
                        break;
                    }
                }
                return index;
            }
            /**
             * 查找page
             * @param rootId
             * @returns number
             */
            findPage(id) {
                for (let i = this.activePages.length - 1; i >= 0; i--) {
                    for (let j = 0; j < this.activePages[i].nodes.length; j++) {
                        let page = this.activePages[i].nodes[j];
                        if (page.target.id === id) {
                            return page;
                        }
                    }
                }
                return void (0);
            }
        }
        core.Router = Router;
        /**
         * 默认mount handler
         */
        class DefaultMountHandler {
            /**
             * 加载
             * @param id
             * @param route
             * @param cite
             * @param router
             * @param callback
             */
            mount(id, route, cite, router, callback) {
                //获取root id
                let rootId = cite.rootId;
                //获取root节点
                let root = document.getElementById(rootId);
                //css node节点数组
                let cssNodes = [];
                //js node节点数组
                let jsNodes = [];
                //html node节点
                let htmlNode;
                //获取第一节点
                let node = core.Utils.getFirstElementChild(root);
                while (node != null) {
                    //获取name
                    let name = node.nodeName;
                    if (name) {
                        //转换为大写
                        name = name.toUpperCase();
                    }
                    if (name == "SCRIPT") {
                        jsNodes.push(node);
                    }
                    else if (name = "LINK") {
                        cssNodes.push(node);
                    }
                    else if (name = "DIV") {
                        htmlNode = node;
                    }
                    node = core.Utils.getNextElementSibling(node);
                }
                let libs = [];
                //1、加入css
                if (route.css) {
                    let list = route.css.split(",");
                    libs = libs.concat(list);
                }
                //2、加入js
                if (route.js) {
                    let list = route.js.split(",");
                    libs = libs.concat(list);
                }
                //获取html资源
                core.require.get(route.html, (code, data) => {
                    //正常返回
                    if (code != 0) {
                        core.logger.error("加載html资源失败");
                        return;
                    }
                    //卸载之前的页面
                    router.unMount(rootId, void (0));
                    //数组参数
                    let args = {
                        'cssPoint': root,
                        'jsPoint': root
                    };
                    //加载css和js
                    core.require.require(libs, (...args) => {
                        //获取export函数
                        let exports;
                        for (let i = 0; i < args.length; i++) {
                            if (core.Utils.type(args[i]) == "object") {
                                exports = args[i];
                                break;
                            }
                        }
                        if (exports && exports.mount) {
                            //执行过滤器
                            router.doFilters(FilterType.Mount, id, cite);
                            //执行mount方法
                            try {
                                exports.mount(id, cite);
                            }
                            catch (ex) {
                                core.logger.error(ex.Message, ex);
                            }
                        }
                        //删除旧的js节点
                        for (let i = 0; root.hasChildNodes() && i < jsNodes.length; i++) {
                            try {
                                root.removeChild(jsNodes[i]);
                            }
                            catch (ex) { }
                        }
                        //删除旧的css节点
                        for (let i = 0; root.hasChildNodes() && i < cssNodes.length; i++) {
                            try {
                                root.removeChild(cssNodes[i]);
                            }
                            catch (ex) { }
                        }
                        //设置html内容
                        if (htmlNode) {
                            //替换html节点
                            htmlNode.innerHTML = data;
                        }
                        else {
                            //创建新的node节点
                            let newHtmlNode = document.createElement("div");
                            newHtmlNode.innerHTML = data;
                            //新增html节点
                            root.appendChild(newHtmlNode);
                        }
                        //调用回调函数
                        callback(0, exports, root);
                    }, args);
                });
            }
            /**
             * 卸载
             * @param id
             * @param route
             * @param rootId
             * @param cite
             * @param exports
             * @param router
             */
            unMount(id, route, cite, exports, router) {
                //执行过滤器
                router.doFilters(FilterType.UnMount, id, cite);
                //执行unMount方法
                if (exports.unMount) {
                    try {
                        exports.unMount(id, cite);
                    }
                    catch (ex) {
                        core.logger.error(ex.Message, ex);
                    }
                }
            }
        }
        /**
         * 导出router
         * @type {fox.core.Router}
         */
        core.router = new Router();
    })(core = fox.core || (fox.core = {}));
})(fox || (fox = {}));
/**
 * Created by jiangcheng on 2016/12/18.
 */
var fox;
(function (fox) {
    /**
     * 检测系统
     * @param ua
     */
    function detect(ua) {
        let os = {};
        let fns = [
            function (os) {
                let windows = ua.match(/(Windows)[\s\/]+/);
                if (windows) {
                    os.windows = true;
                    //是否为支持触摸
                    os.isTouch = ("ontouchstart" in window) || (ua.indexOf("touch") !== -1) || (ua.indexOf("mobile") !== -1);
                }
                return os.windows == true;
            },
            function (os) {
                let android = ua.match(/(Android);?[\s\/]+([\d.]+)?/);
                if (android) {
                    os.android = true;
                    os.version = android[2];
                    os.isBadAndroid = !(/Chrome\/\d/.test(window.navigator.appVersion));
                    //是否为支持触摸
                    os.isTouch = ("ontouchstart" in window) || (ua.indexOf("touch") !== -1) || (ua.indexOf("mobile") !== -1);
                }
                return os.android === true;
            },
            function (os) {
                let iphone = ua.match(/(iPhone\sOS)\s([\d_]+)/);
                if (iphone) {
                    os.ios = os.iphone = true;
                    os.version = iphone[2].replace(/_/g, '.');
                    //是否为支持触摸
                    os.isTouch = ("ontouchstart" in window) || (ua.indexOf("touch") !== -1) || (ua.indexOf("mobile") !== -1);
                }
                else {
                    let ipad = ua.match(/(iPad).*OS\s([\d_]+)/);
                    if (ipad) {
                        os.ios = os.ipad = true;
                        os.version = ipad[2].replace(/_/g, '.');
                        //是否为支持触摸
                        os.isTouch = ("ontouchstart" in window) || (ua.indexOf("touch") !== -1) || (ua.indexOf("mobile") !== -1);
                    }
                }
                return os.ios === true;
            }
        ];
        for (let i = 0; i < fns.length; i++) {
            if (fns[i](os) == true) {
                break;
            }
        }
        return os;
    }
    /**
     * 导出os
     * @type {{}}
     */
    fox.os = detect(window.navigator.userAgent);
})(fox || (fox = {}));
/**
 * Created by jiangcheng on 2016/08/15.
 */
///<reference path="Utils.ts"/>
///<reference path="Resource.ts"/>
///<reference path="Require.ts"/>
///<reference path="Router.ts"/>
///<reference path="Bus.ts"/>
///<reference path="EventChain.ts"/>
///<reference path="EventProxy.ts"/>
///<reference path="OS.ts"/>
var fox;
(function (fox) {
    //工具集合方法
    fox.type = fox.core.Utils.type;
    fox.isFunction = fox.core.Utils.isFunction;
    fox.isArray = fox.core.Utils.isArray;
    fox.makeArray = fox.core.Utils.makeArray;
    fox.merge = fox.core.Utils.merge;
    fox.isWindow = fox.core.Utils.isWindow;
    fox.isPlainObject = fox.core.Utils.isPlainObject;
    fox.extend = fox.core.Utils.extend;
    fox.clone = fox.core.Utils.clone;
    fox.isArrayLike = fox.core.Utils.isArrayLike;
    fox.each = fox.core.Utils.each;
    fox.getInnerText = fox.core.Utils.getInnerText;
    fox.setInnerText = fox.core.Utils.setInnerText;
    fox.getNextElementSibling = fox.core.Utils.getNextElementSibling;
    fox.getFirstElementChild = fox.core.Utils.getFirstElementChild;
    //安装require方法
    fox.require = fox.core.require;
    //安装路由方法
    fox.router = fox.core.router;
    //导出日志
    fox.logger = fox.core.logger;
    //消息代理
    fox.eventproxy = new fox.core.EventProxy();
    //数据篮子
    fox.bus = new fox.core.Bus();
    //事件链
    fox.EventChain = fox.core.EventChain;
    //引用window
    let win = window;
    //引用fox
    let _this = fox;
    /**
     * 配置函数
     */
    function config(setting) {
        let ns = setting.namespace;
        if (ns) {
            if (typeof ns == "string") {
                ns = [ns];
            }
            //循环设置namespace
            fox.core.Utils.each(ns, function (index, na) {
                win[na] = _this;
            });
        }
        //配置require
        fox.core.require.config(setting);
    }
    fox.config = config;
})(fox || (fox = {}));
//# sourceMappingURL=fox-1.2.0.js.map