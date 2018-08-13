/**
 * Created by jiangcheng on 2016/11/25.
 */
(function (yufp, window, factory) {
    // 判断是否支持模块定义
    var hasDefine = (typeof define === 'function');
    if (hasDefine) {
        //获取对象
        var exports = factory(yufp);
        //定义模块
        define(exports);
        //安装插件(兼容非模块的访问方式)
        window.yufp.settings = exports;

    } else {
        //获取对象
        var exports = factory(yufp);
        //安装插件
        window.yufp.settings = exports;
    }

}(yufp, window, function (yufp) {

    //定义配置对象
    var settings = {

        /**
         * 设置配置
         * @param args
         */
        config: function (args) {
            yufp.extend(this, args);
        }

    };
    return settings;

}));/**
 * Created by jiangcheng on 2016/11/25.
 */
(function (yufp, window) {
    var validator = {
        /**
         * 数字验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'number': function (rule, value, callback) {
            var reg = /^\d+$/;
            if (value && reg.test(value)) {
                callback();
            } else if (value && !reg.test(value)) {
                callback(new Error('数字类型错误'));
            } else {
                callback();
            }

        },
        /**
         * 年龄验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'age': function (rule, value, callback) {
            var reg = /^\d+$/;
            if (value && reg.test(value)) {
                var _age = parseInt(value);
                if (_age < 200) {
                    callback();
                } else {
                    callback(new Error('年龄不合法'));
                }
            } else if (value && !reg.test(value)) {
                callback(new Error('数字类型错误'));
            } else {
                callback();
            }
        },
        /**
         * 邮编验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'postcode': function (rule, value, callback) {
            var reg = /^[1-9]\d{5}$/;
            if (value && reg.test(value)) {
                callback();
            } else if (value && !reg.test(value)) {
                callback(new Error('邮编格式不正确'));
            } else {
                callback();
            }
        },
        /**
         * ip验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'ip': function (rule, value, callback) {
            var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
            if (value && reg.test(value)) {
                callback()
            } else if (value && !reg.test(value)) {
                callback(new Error('ip地址格式不正确'))
            } else {
                callback()
            }
        },
        /**
         * 固定电话和小灵通验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'telephone': function (rule, value, callback) {
            var reg = /(^\d{3}\-\d{7,8}$)|(^\d{4}\-\d{7,8}$)|(^\d{3}\d{7,8}$)|(^\d{4}\d{7,8}$)|(^\d{7,8}$)/;
            if (value && reg.test(value)) {
                callback()
            } else if (value && !reg.test(value)) {
                callback(new Error('固定电话或小灵通电话格式不正确'))
            } else {
                callback();
            }
        },
        /**
         * 手机号码验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'phone':function(rule, value, callback){
            var reg=/(^\d{3}\-1[3458][0-9]\d{8}$)|(^\d{2}\-1[3458][0-9]\d{8}$)/;
            if(value&&reg.test(value)){
                callback()
            }else if(value&&!reg.test(value)){
                callback(new Error('固定电话格式不正确'))
            }else{
                callback();
            }
        },
        /**
         * 数字和字母验证，只能接受输入项为数字和字母
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'numberAndLetter': function (rule, value, callback) {
            var reg = /(^[A-Za-z0-9]+$)|([A-Za-z]+$)|([0-9]+$)/;
            if (value && reg.test(value)) {
                callback()
            } else if (value && !reg.test(value)) {
                callback(new Error('请输入数字和字母'))
            } else {
                callback();
            }
        },
        /**
         * 手机号码验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'mobile': function (rule, value, callback) {
            var reg = /^1[3-9][0-9]\d{8}$/;
            if (value && reg.test(value)) {
                callback()
            } else if (value && !reg.test(value)) {
                callback(new Error('手机号码不正确'))
            } else {
                callback();
            }
        },
        /**
         * 身份证号码验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'IDCard': function (rule, value, callback) {
            if (!value) {
                callback();
            } else {
                var area = {
                    11: '北京',
                    12: '天津',
                    13: '河北',
                    14: '山西',
                    15: '内蒙古',
                    21: '辽宁',
                    22: '吉林',
                    23: '黑龙江',
                    31: '上海',
                    32: '江苏',
                    33: '浙江',
                    34: '安徽',
                    35: '福建',
                    36: '江西',
                    37: '山东',
                    41: '河南',
                    42: '湖北',
                    43: '湖南',
                    44: '广东',
                    45: '广西',
                    46: '海南',
                    50: '重庆',
                    51: '四川',
                    52: '贵州',
                    53: '云南',
                    54: '西藏',
                    61: '陕西',
                    62: '甘肃',
                    63: '青海',
                    64: '宁夏',
                    65: '新疆',
                    71: '台湾',
                    81: '香港',
                    82: '澳门',
                    91: '国外'
                };
                var Y, JYM;
                var S, M;
                var idcard_array = new Array();
                idcard_array = value.split('');
                if (area[parseInt(value.substr(0, 2))] == null) {
                    callback(new Error('身份证号码地区非法'));
                }
                // 身份号码位数及格式检验
                switch (value.length) {
                    case 15 :
                        if ((parseInt(value.substr(6, 2)) + 1900) % 4 == 0
                            || ((parseInt(value.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(value
                                    .substr(6, 2)) + 1900)
                                % 4 == 0)) {
                            ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
                        } else {
                            ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
                        }
                        if (ereg.test(value))
                            callback();
                        else {
                            callback(new Error('身份证号码出生日日期有误'));
                        }
                        break;
                    case 18 :
                        // 18位身份号码检测
                        // 出生日期的合法性检查
                        // 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
                        // 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
                        if (parseInt(value.substr(6, 4)) % 4 == 0
                            || (parseInt(value.substr(6, 4)) % 100 == 0 && parseInt(value
                                    .substr(6, 4))
                                % 4 == 0)) {
                            ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
                        } else {
                            ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
                        }
                        if (ereg.test(value)) {// 测试出生日期的合法性
                            // 计算校验位
                            S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10]))
                                * 7
                                + (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
                                * 9
                                + (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
                                * 10
                                + (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
                                * 5
                                + (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
                                * 8
                                + (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
                                * 4
                                + (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
                                * 2
                                + parseInt(idcard_array[7])
                                * 1
                                + parseInt(idcard_array[8])
                                * 6
                                + parseInt(idcard_array[9]) * 3;
                            Y = S % 11;
                            M = 'F';
                            JYM = '10X98765432';
                            M = JYM.substr(Y, 1);// 判断校验位
                            if (M == idcard_array[17]) {
                                callback();
                            } else {
                                callback(new Error('身份证号码末位校验位校验出错,请注意x的大小写'));
                            }
                        } else {
                            callback(new Error('身份证号码出生日期有误'));
                        }
                        break;
                    default :
                        callback(new Error('身份证号码位数不对,应该为15位或是18位'));
                        break;
                }
            }
        },
        /**
         * 是否为中文验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'isChnChar': function (rule, value, callback) {
            var reg = /[\u4E00-\u9FA5]/;
            if (value && reg.test(value)) {
                callback()
            } else if (value && !reg.test(value)) {
                callback(new Error('只能输入中文'))
            } else {
                callback();
            }
        },
        /**
         * 输入项收尾空格验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'trim': function (rule, value, callback) {
            if (value != value.trim()) {
                callback(new Error('输入项首尾有空格'))
            } else {
                callback();
            }
        },
        /**
         * 邮箱验证
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'email': function (rule, value, callback) {
            var reg = /[A-Za-z0-9_-]+[@](\S*)(net|com|cn|org|cc|tv|[0-9]{1,3})(\S*)/g;
            if (value && reg.test(value)) {
                callback()
            } else if (value && !reg.test(value)) {
                callback(new Error('电子邮箱格式不正确'))
            } else {
                callback();
            }
        },
        /**
         * 小数验证，输入结果必须为小数
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'digit': function (rule, value, callback) {
            var reg = /^-?\d+(\.\d+)?$/g;
            if (value && reg.test(value)) {
                callback()
            } else if (value && !reg.test(value)) {
                callback(new Error('请输入小数'))
            } else {
                callback();
            }
        },
        /**
         * 非零正整数
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'pInt': function (rule, value, callback) {
            var reg = /^\+?[1-9][0-9]*$/;
            if (value && reg.test(value)) {
                callback()
            } else if (value && !reg.test(value)) {
                callback(new Error('请输入非零正整数'))
            } else {
                callback();
            }
        },
        /**
         * 0 整数和浮点数
         * rule为form表单当前验证的filed对应的验证rule规则
         * value为当前输入框返回值
         * callback为回调函数,验证成功直接回调，验证失败回调函数返回一个带错误信息的Error实例
         * */
        'gZero': function (rule, value, callback) {
            var reg = /^[\+]?[0-9]*\d(\.\d+)?$/;
            if (value && reg.test(value)) {
                callback()
            } else if (value && !reg.test(value)) {
                callback(new Error('请输入非零正整数'))
            } else {
                callback();
            }
        }
    }
    var hasDefine = (typeof define === 'function');
    if (hasDefine) {
        //定义模块
        define(validator);
        //安装插件(兼容非模块的访问方式)
        window.yufp.validator = validator;

    } else {
        //安装插件
        window.yufp.validator = validator;
    }
}(yufp, window));/**
 * 业务工具类
 * created by helin3 2017-12-05
 */
(function (yufp, window, factory) {
    var exports = factory(yufp, window, window.document);
    if (typeof define === 'function') {
        define(exports);
    }
    window.yufp.util = exports;
}(yufp, window, function (yufp, window, document) {

    /**
     * 业务工具类
     * @constructor
     */
    function Utils() {
    }

    /**
     *
     * @param time
     * @param format
     * @returns {*}
     */
    Utils.prototype.dateFormat = function (time, format) {
        if (arguments.length === 0) {
            return null;
        }
        format = format || '{y}-{m}-{d} {h}:{i}:{s}'
        var date;
        if (typeof time === 'object') {
            date = time;
        } else {
            if (('' + time).length === 10) time = parseInt(time) * 1000;
            date = new Date(time);
        }
        var formatObj = {
            y: date.getFullYear(),
            m: date.getMonth() + 1,
            d: date.getDate(),
            h: date.getHours(),
            i: date.getMinutes(),
            s: date.getSeconds(),
            a: date.getDay()
        };
        var time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, function (result, key) {
            var value = formatObj[key];
            if (key === 'a') return ['一', '二', '三', '四', '五', '六', '日'][value - 1];
            if (result.length > 0 && value < 10) {
                value = '0' + value;
            }
            return value || 0;
        });
        return time_str;
    }

    /**
     *
     * 判断当前浏览器类型
     * @author
     * @returns {*}
     */
    Utils.prototype.getExplorer = function () {
        var explorer = window.navigator.userAgent;
        //ie
        if (explorer.indexOf('MSIE') >= 0) {
            return 'ie';
        }
        //firefox
        else if (explorer.indexOf('Firefox') >= 0) {
            return 'Firefox';
        }
        //Chrome
        else if (explorer.indexOf('Chrome') >= 0) {
            return 'Chrome';
        }
        //Opera
        else if (explorer.indexOf('Opera') >= 0) {
            return 'Opera';
        }
        //Safari
        else if (explorer.indexOf('Safari') >= 0) {
            return 'Safari';
        }
    }

    /**
     *
     * 判断当前浏览器类型
     * @param options  导出参数
     * options:{type:'table',ref:table_ref_obj}
     * type 导出类型为table  ref table对应的vue对象ref
     * options:{type:'json',data:{head:[],body:[]}}
     * type 导出类型为json自定义数据  data head为表头,body为数据
     * @author
     * @returns {*}
     */
    Utils.prototype.exportExcelByTable = function (options) {
        var table_ref = options.ref;
        var colums = table_ref.tableColumns;
        var colums_ = new Array();
        var tableColumns = colums.concat([]);
        var collectionHtml = table_ref.$el.getElementsByClassName('el-table__header-wrapper')[0].getElementsByTagName('tr')
        var rowspanIndex = 1;
        var maxrowspan = function (list, parList) {
            if (list && list instanceof Array == true) {
                for (var i = 0; i < list.length; i++) {
                    var obj = list[i];
                    if (obj.children && obj.children instanceof Array == true) {
                        obj.colspan = obj.children.length - 1;
                        if (rowspanIndex < obj.children.length) {
                            parList.map(function (obj_, index_) {
                                if (obj.label != obj_.label) {
                                    obj_.rowspan = (obj_.rowspan == undefined ? 0 : obj_.rowspan) + 1;
                                }
                            });
                            rowspanIndex += 1;
                        }
                        maxrowspan(obj.children, obj.children, i);
                    }
                }
            }
        }
        maxrowspan(tableColumns, tableColumns);
        var getMerge = function (obj, index, rownum, cellNum) {
            var merges_ = {
                s: {//s为开始
                    c: 0,//开始列
                    r: 0//开始取值范围
                },
                e: {//e结束
                    c: 0,//结束列
                    r: 0//结束范围
                }
            }
            if (obj.colspan == 0 && !cellNum) {
                merges_.s.c = index;
                merges_.e.c = index;
            } else if (obj.colspan == 0 && cellNum) {
                merges_.s.c = cellNum;
                merges_.e.c = cellNum;
            } else if (obj.colspan != 0 && !cellNum) {
                merges_.s.c = index;
                merges_.e.c = parseInt(index + obj.colspan);
            } else if (obj.colspan != 0 && cellNum) {
                merges_.s.c = cellNum;
                merges_.e.c = parseInt(cellNum + obj.colspan);
            }
            if (obj.rowspan == 0) {
                merges_.s.r = rownum;
                merges_.e.r = parseInt(rownum + obj.rowspan);
            } else {
                merges_.s.r = rownum;
                merges_.e.r = parseInt(rownum + obj.rowspan);
            }
            return merges_;
        }


        var head = [];
        var merges = [];
        var headSheel = [];
        var letter = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        var rownum = 0;
        var rowspanIndex_ = 1;
        var addIndex = 0;
        var exportRule = function (list, parList, cellNum) {
            if (list && list instanceof Array == true) {
                for (var i = 0; i < list.length; i++) {
                    var obj = list[i];
                    head.push(obj.label);
                    obj.rowspan = obj.rowspan == undefined ? 0 : obj.rowspan;
                    obj.colspan = obj.colspan == undefined ? 0 : obj.colspan;
                    if (obj.children && obj.children instanceof Array == true) {
                        headSheel.push(letter.charAt(parseInt(i + rownum)) + '1');
                        var mg = getMerge(obj, i, rownum);
                        merges.push(mg);
                        rowspanIndex_ += 1;
                        rownum = rownum + 1;
                        addIndex = 0;
                        exportRule(obj.children, obj.children, i);
                        rownum = rownum - 1;
                        addIndex = parseInt(obj.children.length - 1);
                    } else {
                        var mg = getMerge(obj, i + addIndex, rownum, cellNum + i);
                        merges.push(mg);
                        if (cellNum) {
                            headSheel.push(letter.charAt(cellNum + i) + rowspanIndex_);
                        } else {
                            headSheel.push(letter.charAt(parseInt(i + rowspanIndex_ - 1)) + '1');
                        }
                    }
                }
            }
        }
        exportRule(tableColumns, tableColumns);
        //获取列的字段名称
        var getColumnsName = function (name, tableColumns) {
            var key;
            for (var i = 0; i < tableColumns.length; i++) {
                if (tableColumns[i].children && tableColumns[i].children instanceof Array == true) {
                    key = getColumnsName(name, tableColumns[i].children)
                } else {
                    if (name == tableColumns[i].label && tableColumns[i].prop) {
                        key = tableColumns[i].prop;
                        break;
                    }
                }
            }
            return key;
        }
        var headList_ = [];
        for (var i = 0; i < head.length; i++) {
            var key = getColumnsName(head[i], tableColumns);
            if (key) {
                headList_[headList_.length] = key;
            }
        }

        var getColumnsDataCode = function (column, tableColumns) {
            var code;
            for (var i = 0; i < tableColumns.length; i++) {
                if (tableColumns[i].children && tableColumns[i].children instanceof Array == true) {
                    key = getColumnsName(column, tableColumns[i].children)
                } else {
                    if (column == tableColumns[i].prop && tableColumns[i].prop) {
                        code = tableColumns[i].dataCode;
                        break;
                    }
                }
            }
            return code;
        }
        var data = [];
        var tableData = [];
        if (options.importType == 'page') {
            tableData = table_ref.data;
        } else if (options.importType == 'selected') {
            tableData = table_ref.selections;
        } else if (options.importType == 'service') {
            yufp.service.request({
                url: options.url,
                async: false,
                data: options.param,
                method: options.method ? options.method : 'GET',
                callback: function (code, message, response) {
                    tableData = response.data;
                }
            });
        }
        for (var i = 0; i < tableData.length; i++) {
            var o = {};
            var rowData = tableData[i];
            for (var j = 0; j < headList_.length; j++) {
                var k = headList_[j];
                var code = getColumnsDataCode(k, tableColumns);
                if (code) {
                    var val = yufp.lookup.convertKey(code, rowData[k]);
                    o['' + k + ''] = val;
                } else {
                    o['' + k + ''] = rowData[k];
                }

            }
            data.push(o);
        }

        for (var i = 1; i < collectionHtml.length; i++) {
            data.unshift({});
        }
        var wopts = {bookType: 'xlsx', bookSST: true, type: 'binary'};//这里的数据是用来定义导出的格式类型
        var saveAs = function (obj, fileName) {//当然可以自定义简单的下载文件实现方式
            var tmpa = document.createElement('a');
            tmpa.download = fileName || '下载';
            tmpa.href = URL.createObjectURL(obj); //绑定a标签
            tmpa.click(); //模拟点击实现下载
            setTimeout(function () { //延时释放
                URL.revokeObjectURL(obj); //用URL.revokeObjectURL()来释放这个object URL
            }, 100);
        }
        var s2ab = function (s) {
            if (typeof ArrayBuffer !== 'undefined') {
                var buf = new ArrayBuffer(s.length);
                var view = new Uint8Array(buf);
                for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
                return buf;
            } else {
                var buf = new Array(s.length);
                for (var i = 0; i != s.length; ++i) buf[i] = s.charCodeAt(i) & 0xFF;
                return buf;
            }
        }
        var wb = {SheetNames: ['Sheet1'], Sheets: {}, Props: {}};
        data = XLSX.utils.json_to_sheet(data);
        for (var i = 0; i < headSheel.length; i++) {
            data[headSheel[i]] = {t: 's', v: head[i]};
        }
        data['!merges'] = merges;
        wb.Sheets['Sheet1'] = data;
        saveAs(new Blob([s2ab(XLSX.write(wb, wopts))], {type: 'application/octet-stream'}), options.fileName + '.' + (wopts.bookType == 'biff2' ? 'xls' : wopts.bookType));
    }

    Utils.prototype.array2tree = function (data, options) {
        var _options = {id: 'id', pid: 'parentId', root: '0'};
        yufp.extend(_options, options || {});
        var idField = _options.id, pidField = _options.pid;
        var root, children = [];
        if (typeof _options.root === 'object') {
            root = _options.root;
        } else {
            var tempObj = {};
            tempObj[idField] = _options.root;
            root = tempObj;
        }
        var rId = '' + root[idField];
        for (var i = 0, len = data.length; i < len; i++) {
            var d = data[i];
            if (rId === '' + d[idField]) {
                root = d;
            } else if (rId === '' + d[pidField]) {
                children.push(d);
            }
        }
        root.id = root[idField];
        children = root.children ? root.children.concat(children) : children;
        root.children = children;
        for (var i = 0, len = root.children.length; i < len; i++) {
            _options.root = root.children[i];
            root.children[i] = this.array2tree(data, _options);
        }
        return root;
    }

    /**根据数组和对应属性返回满足el-tree的树形数据,
     *id: 对应id,
     *pid: 对应pid,
     *label: 对应展示字段,
     *root: 如果值为空或不存在则计算
     */
    Utils.prototype.genTree = function (data, attr) {
        var root = {};
        if (data == null || data.length == 0) {
            return [];
        }
        if (attr.root == null || attr.root == undefined || attr.root == '') {
            var getRootData = function (data, attributes) {
                var _root = {};
                _root = data[0];
                for (var k = 1; k < data.length; k++) {
                    var i = 1;
                    for (; i < data.length; i++) {
                        if (data[i][attributes.id] == _root[attributes.pid]) {
                            _root = data[i];
                            break;
                        }
                    }
                    if (i == data.length - 1) {
                        break;
                    }
                }
                return _root;
            };

            root.id = getRootData(data, attr)[attr.pid];
        } else if (typeof(attr.root) == 'object') {
            var root = attr.root;
            root.id = root[attr.id] === undefined ? root.id : root[attr.id];
            root.pid = root[attr.pid] === undefined ? root.pid : root[attr.pid];
            root.label = root[attr.label] === undefined ? root.label : root[attr.label];
        } else {
            for (var i in data) {
                if (data[i][attr.id] == attr.root) {
                    root.id = data[i][attr.pid];
                    break;
                }
            }
            root.id = root.id == undefined ? attr.root : root.id;
        }

        var genTreeData = function (data, attr) {
            var ckey = {},
                pkey = {};

            for (var i = 0; i < data.length; i++) {
                var row = data[i];
                row.id = row[attr.id];
                row.pid = row[attr.pid];
                row.label = row[attr.label];
                row.children = [];

                ckey[row.id] = row;
                if (pkey[row.pid]) {
                    pkey[row.pid].push(row);
                } else {
                    pkey[row.pid] = [row];
                }

                var c = pkey[row.id];
                if (c) {
                    row.children = c.concat();
                }

                var p = ckey[row.pid];
                if (p) {
                    p.children.push(row);
                }
            }
            return pkey;
        };

        if (root.label) {
            root.children = genTreeData(data, attr)[root.id];
            return [root];
        }
        return genTreeData(data, attr)[root.id];
    };

    // 实现对象的深度克隆
    Utils.prototype.clone = function (obj) {
        var result = {};
        if (typeof(obj) == 'object') {
            var me = this;
            var objClone = function (o) {
                var t = {};
                for (var k in o) {
                    var copy = o[k];
                    if (typeof(copy) == 'object') {
                        t[k] = me.objClone(copy);
                    } else {
                        t[k] = o[k];
                    }
                }
                return t;
            };
            result = objClone(obj);
        } else {
            alert('clone方法目前只支持对象!');
        }
        return result;
    };

    /**
     * 为url添加token信息
     * @param url
     * @returns {string}
     */
    Utils.prototype.addTokenInfo = function (url) {
        var token = 'access_token=';
        var _url = '';
        if (url == null || url == '') {
            return _url;
        }

        if (!url.indexOf(token) > -1) {
            _url = url + ((url.indexOf('?') > -1) ? '&' : '?') + token + yufp.service.getToken();
        }
        return _url;
    };

    Utils.prototype.download = function (url) {
        if (url) {
            if (url.indexOf('http') <= -1) {
                // 当不包含http时拼接gateway地址
                url = yufp.service.getUrl({
                    url: url
                });
            }
        } else {
            this.$message('必须设置请求url!', '警告');
        }
        //url添加token
        url = this.addTokenInfo(url);
        // 模拟a标签进行下载
        var a = document.createElement('a');
        a.href = url;
        a.click();
    };


    /**
     * @created by zhangkun6
     * @updated by 2018/01/14
     * @description 数字金额格式化(千分位)
     */
    Utils.prototype.moneyFormatter = function (money, num) {
        /*
          * 参数说明：
          * money：要格式化的数字
          * num：保留几位小数
          * */
        num = num > 0 && num <= 20 ? num : 2;
        money = parseFloat((money + '').replace(/[^\d\.-]/g, '')).toFixed(num) + '';
        var l = money.split('.')[0].split('').reverse(),
            r = money.split('.')[1];
        t = '';
        for (i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && i + 1 != l.length ? ',' : '');
        }
        return t.split('').reverse().join('') + '.' + r;
    };

    /**
     * @created by zhangkun6
     * @updated by 2018/01/14
     * @description 数字金额转汉字金额
     */
    Utils.prototype.moneyToUpper = function (money) {
        /*
            * 参数说明：
            * money：要转化的数字
            * */
        //汉字的数字
        var cnNums = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
        //基本单位
        var cnIntRadice = ['', '拾', '佰', '仟'];
        //对应整数部分扩展单位
        var cnIntUnits = ['', '万', '亿', '兆'];
        //对应小数部分单位
        var cnDecUnits = ['角', '分', '毫', '厘'];
        //整数金额时后面跟的字符
        var cnInteger = '整';
        //整型完以后的单位
        var cnIntLast = '元';
        //最大处理的数字
        var maxNum = 999999999999999.9999;
        //金额整数部分
        var integerNum;
        //金额小数部分
        var decimalNum;
        //输出的中文金额字符串
        var chineseStr = '';
        //分离金额后用的数组，预定义
        var parts;
        if (money == '') {
            return '';
        }
        money = parseFloat(money);
        if (money >= maxNum) {
            //超出最大处理数字
            return '';
        }
        if (money == 0) {
            chineseStr = cnNums[0] + cnIntLast + cnInteger;
            return chineseStr;
        }
        //转换为字符串
        money = money.toString();
        if (money.indexOf('.') == -1) {
            integerNum = money;
            decimalNum = '';
        } else {
            parts = money.split('.');
            integerNum = parts[0];
            decimalNum = parts[1].substr(0, 4);
        }
        //获取整型部分转换
        if (parseInt(integerNum, 10) > 0) {
            var zeroCount = 0;
            var IntLen = integerNum.length;
            for (var i = 0; i < IntLen; i++) {
                var n = integerNum.substr(i, 1);
                var p = IntLen - i - 1;
                var q = p / 4;
                var m = p % 4;
                if (n == '0') {
                    zeroCount++;
                } else {
                    if (zeroCount > 0) {
                        chineseStr += cnNums[0];
                    }
                    //归零
                    zeroCount = 0;
                    chineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
                }
                if (m == 0 && zeroCount < 4) {
                    chineseStr += cnIntUnits[q];
                }
            }
            chineseStr += cnIntLast;
        }
        //小数部分
        if (decimalNum != '') {
            var decLen = decimalNum.length;
            for (var i = 0; i < decLen; i++) {
                var n = decimalNum.substr(i, 1);
                if (n != '0') {
                    chineseStr += cnNums[Number(n)] + cnDecUnits[i];
                }
            }
        }
        if (chineseStr == '') {
            chineseStr += cnNums[0] + cnIntLast + cnInteger;
        } else if (decimalNum == '') {
            chineseStr += cnInteger;
        }
        return chineseStr;
    };

    /**
     * @created by zhangkun6
     * @updated by 2018/01/19
     * @description 汉字金额转数字金额
     */
    Utils.prototype.upperToMoney = function (upper) {
        /*
            * 参数说明：
            * upper：要转化的汉字
         */
        //金额数值
        var num = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
        //汉字的数字
        var cnNums = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
        //对应单位的乘积
        var upperMap = [10, 100, 1000];
        //基本单位
        var cnIntRadice = ['拾', '佰', '仟'];
        //对应整数部分扩展单位
        var cnIntUnits = ['万', '亿', '兆'];
        //对应小数部分单位乘积
        var cnDecMap = [0.1, 0.01];
        //对应小数部分单位
        var cnDecUnits = ['角', '分'];
        //金额整数部分
        var integerNum;
        //金额小数部分
        var decimalNum;
        //输出的数字金额字符串
        var moneyNum;
        //金额单位亿之前的数值数组
        var maxArray = [];
        //金额单位亿和万之间的数值数组
        var middleArray = [];
        //金额单位万和元之间的数值数组
        var minArray = [];

        var part = upper.split('元');
        integerNum = part[0];
        decimalNum = part[1].split('');
        if (integerNum.indexOf('亿') !== -1) {
            maxArray = integerNum.split('亿')[0].split('');
            if (integerNum.indexOf('万') !== -1) {
                middleArray = integerNum.split('亿')[1].split('万')[0].split('');
                minArray = integerNum.split('亿')[1].split('万')[1].split('');
            } else {
                minArray = integerNum.split('亿')[1].split('');
            }
        } else if (integerNum.indexOf('万') !== -1) {
            middleArray = integerNum.split('万')[0].split('');
            minArray = integerNum.split('万')[1].split('');
        } else {
            minArray = integerNum.split('');
        }
        var getNum = function (upArray, cnNums, cnRadice, numArray, map) {
            var length = upArray.length;
            var num = 0;
            var sum = 0;
            for (var i = 0; i < length; i++) {
                var index = cnNums.indexOf(upArray[i]);
                var _index = cnRadice.indexOf(upArray[i]);
                if (index !== -1) {
                    num += numArray[index];
                    if (i == (length - 1)) {
                        sum += num;
                    }
                }
                if (_index !== -1) {
                    num *= map[_index];
                    sum += num;
                    num = 0;
                }
            }
            return sum;
        }
        var maxSum = getNum(maxArray, cnNums, cnIntRadice, num, upperMap);
        var middleSun = getNum(middleArray, cnNums, cnIntRadice, num, upperMap);
        var minSun = getNum(minArray, cnNums, cnIntRadice, num, upperMap);
        var cesSum = getNum(decimalNum, cnNums, cnDecUnits, num, cnDecMap);
        moneyNum = maxSum * 100000000 + middleSun * 10000 + minSun + cesSum;
        return moneyNum
    }

    //菜单访问日志工具
    Utils.prototype.logInfo = function (log, url) {
        yufp.service.request({
            url: url,
            method: 'post',
            data: log,
            callback: function (code, msg, response) {
                if (code !== 0 || !response){
					yufp.logger.warn("日志上传失败");
                }
            }
        });
    }

    var utils = new Utils();

    /**
     * 日期默认格式
     * @returns {*}
     */
    Date.prototype.toJSON = function () {
        return utils.dateFormat(this, '{y}-{m}-{d}');
    };

    return utils;
}));/**
 * Created by jiangcheng on 2016/09/21.
 */
(function (yufp, window, factory) {
    // 判断是否支持模块定义
    var hasDefine = (typeof define === 'function');
    if (hasDefine) {
        //获取对象
        var exports = factory(yufp, window);
        //定义模块
        define(exports);
        //安装插件(兼容非模块的访问方式)
        yufp.localStorage = exports;

    } else {
        //获取对象
        var exports = factory(yufp, window);
        //安装插件
        yufp.localStorage = exports;
    }

}(yufp, window, function (yufp, window) {

    /**
     * local storage
     * @constructor
     */
    function LocalStorage() {

    }

    /**
     * Types
     * @type {{stringType: string, numberType: string, booleanType: string, jsonType: string}}
     */
    LocalStorage.Types = {
        stringType: '00',
        numberType: '01',
        booleanType: '02',
        jsonType: '03'
    };


    /**
     * 设置值
     * @param key
     * @param value
     */
    LocalStorage.prototype.put = function put(key, value) {
        //获取key类型
        var keyType = yufp.type(key);
        if (keyType != 'string') {
            console.error('the type of key is not string');
            return;
        }
        //定义参数
        var param;
        //获取value类型
        var valueType = yufp.type(value);
        if (valueType == 'object') {
            var s = JSON.stringify(value);
            param = LocalStorage.Types.jsonType + s;
        } else if (valueType == 'string') {
            param = LocalStorage.Types.stringType + value;
        } else if (valueType == 'number') {
            param = LocalStorage.Types.numberType + value;
        } else if (valueType == 'boolean') {
            param = LocalStorage.Types.booleanType + value;
        } else {
            console.error('the type of value is not supported');
            return;
        }
        //设置值
        window.localStorage.setItem(key, param);
    };

    /**
     * 获取值
     * @param key
     * @returns {*}
     */
    LocalStorage.prototype.get = function get(key) {
        //获取key类型
        var keyType = typeof(key);
        if (keyType != 'string') {
            console.error('the type of key is not string');
            return undefined;
        }
        //获取value
        var param = window.localStorage.getItem(key);

        if (param == null || param == undefined) {
            return param;
        }
        //获取value类型
        var valueType = param.substring(0, 2);
        //获取value
        var value = param.substring(2, param.length);
        if (valueType == LocalStorage.Types.jsonType) {
            value = JSON.parse(value);
        } else if (valueType == LocalStorage.Types.numberType) {
            value = Number(value);
        } else if (valueType == LocalStorage.Types.booleanType) {
            value = Boolean(value);
        }
        return value;
    };

    /**
     * 移除项
     * @param key
     * @returns {undefined}
     */
    LocalStorage.prototype.remove = function remove(key) {
        //获取key类型
        var keyType = typeof(key);
        if (keyType != 'string') {
            console.error('the type of key is not string');
            return undefined;
        }
        window.localStorage.removeItem(key);
    };

    /**
     * 清空所有的项
     * @param key
     * @returns {undefined}
     */
    LocalStorage.prototype.clear = function clear(key) {
        //清空
        window.localStorage.clear();
    };

    //定义local storage
    var localStorage = new LocalStorage();
    //返回local storage
    return localStorage;

}));/**
 * Created by jiangcheng on 2016/09/21.
 */
(function (yufp, window, factory) {
    // 判断是否支持模块定义
    var hasDefine = (typeof define === 'function');
    if (hasDefine) {
        //获取对象
        var exports = factory(yufp, window);
        //定义模块
        define(exports);
        //安装插件(兼容非模块的访问方式)
        yufp.sessionStorage = exports;

    } else {
        //获取对象
        var exports = factory(yufp, window);
        //安装插件
        yufp.sessionStorage = exports;
    }

}(yufp, window, function (yufp, window) {

    /**
     * session storage
     * @constructor
     */
    function SessionStorage() {

    }

    /**
     * Types
     * @type {{stringType: string, numberType: string, booleanType: string, jsonType: string}}
     */
    SessionStorage.Types = {
        stringType: '00',
        numberType: '01',
        booleanType: '02',
        jsonType: '03'
    };

    /**
     * 系统配置
     * @type {string}
     */
    SessionStorage.SystemReadyOnly = '_$sys_session_readyOnly';

    /**
     * 判断是否为系统key
     * @param key
     * @returns {boolean}
     */
    SessionStorage.prototype.isSystemKey = function (key) {
        if (key && key.indexOf('_$sys_session_') == 0) {
            return true;
        }
        return false;
    };

    /**
     * 设置值
     * @param key
     * @param value
     * @param readyOnly
     */
    SessionStorage.prototype.put = function put(key, value, readyOnly) {
        //获取key类型
        var keyType = yufp.type(key);
        if (keyType != 'string') {
            console.error('the type of key is not string');
            return;
        }

        //判断key值是否和系统保留值冲突
        if (this.isSystemKey(key)) {
            console.error('the key name is illegal');
            return;
        }

        //获取只读key配置
        var readyOnlyKeys = window.sessionStorage.getItem(SessionStorage.SystemReadyOnly);
        if (!readyOnlyKeys) {
            readyOnlyKeys = [];
        } else {
            readyOnlyKeys = JSON.parse(readyOnlyKeys);
        }

        //判断key是否为只读
        if (readyOnlyKeys.indexOf(key) != -1) {
            console.error('the key is ready only');
            return;
        }


        //定义参数
        var param;
        //获取value类型
        var valueType = yufp.type(value);
        if (valueType == 'object') {
            var s = JSON.stringify(value);
            param = SessionStorage.Types.jsonType + s;
        } else if (valueType == 'string') {
            param = SessionStorage.Types.stringType + value;
        } else if (valueType == 'number') {
            param = SessionStorage.Types.numberType + value;
        } else if (valueType == 'boolean') {
            param = SessionStorage.Types.booleanType + value;
        } else {
            console.error('the type of value is not supported');
            return;
        }

        //保存只读key集合
        if (readyOnly && readyOnly == true) {
            readyOnlyKeys.push(key);
            readyOnlyKeys = JSON.stringify(readyOnlyKeys);
            window.sessionStorage.setItem(SessionStorage.SystemReadyOnly, readyOnlyKeys);

        }
        //设置值
        window.sessionStorage.setItem(key, param);
    };

    /**
     * 获取值
     * @param key
     * @returns {*}
     */
    SessionStorage.prototype.get = function get(key) {
        //获取key类型
        var keyType = typeof(key);
        if (keyType != 'string') {
            console.error('the type of key is not string');
            return undefined;
        }
        //获取value
        var param = window.sessionStorage.getItem(key);

        if (param == null || param == undefined) {
            return param;
        }
        //获取value类型
        var valueType = param.substring(0, 2);
        //获取value
        var value = param.substring(2, param.length);
        if (valueType == SessionStorage.Types.jsonType) {
            value = JSON.parse(value);
        } else if (valueType == SessionStorage.Types.numberType) {
            value = Number(value);
        } else if (valueType == SessionStorage.Types.booleanType) {
            value = Boolean(value);
        }
        return value;
    };

    /**
     * 移除项
     * @param key
     * @returns {undefined}
     */
    SessionStorage.prototype.remove = function remove(key) {
        //获取key类型
        var keyType = typeof(key);
        if (keyType != 'string') {
            console.error('the type of key is not string');
            return undefined;
        }

        //判断key值是否和系统保留值冲突
        if (this.isSystemKey(key)) {
            console.error('the key name is illegal');
            return undefined;
        }
        //获取只读key配置
        var readyOnlyKeys = window.sessionStorage.getItem(SessionStorage.SystemReadyOnly);
        if (!readyOnlyKeys) {
            readyOnlyKeys = [];
        } else {
            readyOnlyKeys = JSON.parse(readyOnlyKeys);
        }

        //判断key是否为只读
        if (readyOnlyKeys.indexOf(key) != -1) {
            console.error('the key is ready only');
            return;
        }

        window.sessionStorage.removeItem(key);
    };

    /**
     * 清空所有的项
     * @returns {undefined}
     */
    SessionStorage.prototype.clear = function clear() {

        //获取只读key配置
        var readyOnlyKeys = window.sessionStorage.getItem(SessionStorage.SystemReadyOnly);
        if (!readyOnlyKeys) {
            readyOnlyKeys = [];
        } else {
            readyOnlyKeys = JSON.parse(readyOnlyKeys);
        }

        //临时缓存
        var tmpCache = {};
        //保存ready only key
        for (var i = 0; i < readyOnlyKeys.length; i++) {
            var key = readyOnlyKeys[i];
            var value = window.sessionStorage.getItem(key);
            tmpCache[key] = value;
        }

        //清空
        window.sessionStorage.clear();

        if (readyOnlyKeys) {
            //还原ready only key
            for (var i = 0; i < readyOnlyKeys.length; i++) {
                var key = readyOnlyKeys[i];
                var value = tmpCache[key];
                window.sessionStorage.setItem(key, value);
            }
            //保存ready only keys
            readyOnlyKeys = JSON.stringify(readyOnlyKeys);
            window.sessionStorage.setItem(SessionStorage.SystemReadyOnly, readyOnlyKeys);
        }
    };

    //定义session storage
    var sessionStorage = new SessionStorage();
    //返回session storage
    return sessionStorage;

}));/**
 * yufp service xy
 * Created by helin3 on 2017/11/25.
 */
(function (yufp, window, factory) {
    var exports = factory(yufp, window, window.document);
    if (typeof define === 'function') {
        define(exports);
    }
    window.yufp.service = exports;
}(yufp, window, function (yufp, window, document) {

        /**
         * 定义Service
         * @constructor
         */
        function Service() {
            this.options = {
                method: 'POST',   //默认POST，支持4种访问类型 GET/POST/PUT/DELETE
                async: true,      //异步请求
                data: {},         //请求数据
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                    // 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                },      //http请求头
                dataType: 'json', //默认返回数据类型
                timeout: 9000,    //默认超时时间
                cache: false,     //是否缓存
                needToken: true,  //是否传认证Token值去后台
                callback: false   //回调方法
            }
            this._filters = []    //过滤器集合
            this.basePath = ''    //应用名
            this.tokenId = 'Authorization'; //TOKEN 名
            this.tokenVal = ''    //TOKEN 值
        }

        /**
         *
         * @param options
         */
        Service.prototype.request = function (options) {
            var me = this;
            var _options = yufp.extend({}, me.options, options)
            _options.url = me.getUrl(_options);

            var deferred = new yufp.core.Deferred();
            var event = {
                data: _options.data
            }
            //before过滤
            if (me._doFilter(0, event) === false) {
                event.code = event.code ? event.code : 2;
                deferred.reject(event.code, event.message, event.data);
                if (_options.callback) {
                    _options.callback(event.code, event.message, event.data);
                }
                return deferred;
            }
            _options.data = event.data.data
            _options.headers = yufp.extend({}, event.data.headers || {}, _options.headers)
            if (_options.needToken){
                _options.headers[me.tokenId] = 'Bearer ' + me.getToken()
            }
            _options.type = options.method;
            _options.async = options.async;
            _options.success = function (data, status, xhr) {
                //-------------------------------- recorder 模式 -----------------------------------------
                if(yufp.settings.recorderModel && yufp.settings.recorderScope.indexOf('yufp.service')!=-1) {
                    yufp.logger.info('[service receive]  url:' + _options.url + ' ,response:' + JSON.stringify(data));
                }
                //-------------------------------- recorder 模式 -----------------------------------------

                //定义过滤事件
                var event = { code: 0, message: 'success', data: data };
                //after过滤器
                //过滤器中断调用处理
                if (me._doFilter(1, event) === false) {
                    var code = event.code ? event.code : 1;
                    //通知调用失败
                    deferred.reject(code, event.message, event.data);
                    if (_options.callback) {
                        _options.callback(code, event.message, event.data);
                    }
                    return;
                }
                //通知调用成功
                deferred.resolve(event.code, event.message, event.data);
                if (_options.callback) {
                    _options.callback(event.code, event.message, event.data);
                }
            }
            _options.error = function (xhr, status) {
                var msg = xhr.responseText;
                msg = !msg ? status : msg

                //定义过滤事件
                var event = { code: 1, message: msg, xhr: xhr };
                //exception过滤
                if (me._doFilter(2, event) === false) {
                    //通知调用失败
                    deferred.reject(event.code, event.message, event.data);
                    return;
                }

                event.code = event.code ? event.code : 1;
                //通知调用失败
                deferred.reject(event.code, event.message, event.data);
                //判断是否存在回调函数
                if (_options.callback) {
                    _options.callback(event.code, event.message, event.data);
                }
            }
            yufp.core.ajax(_options)
        }

        /**
         * 获取最终访问的完整URL
         * @param param {url }
         * @returns {string}
         */
        Service.prototype.getUrl = function (param) {
            param.url = param.url ? param.url : param.name
            if(!param.url){
                throw new Error('未设置请求URL')
            }
            param.url = param.url.charAt(0) == '/' ? param.url : '/' + param.url

            var url = yufp.settings.ssl ? 'https://' : 'http://'
            url += yufp.settings.url
            url += this.basePath ? this.basePath : ''
            if (param.url && param.url.indexOf('http') > -1) {
                url = param.url
            } else {
                url += param.url
            }
            return url
        }

        Service.prototype.getToken = function () {
            return yufp.sessionStorage.get('UFP-' + this.tokenId) || ''
        }

        Service.prototype.putToken = function (token) {
            yufp.sessionStorage.put('UFP-' + this.tokenId, token)
        }

        Service.prototype.removeToken = function () {
            yufp.sessionStorage.remove('UFP-' + this.tokenId)
        }

        Service.prototype.addFilter = function (obj) {
            if (typeof obj != 'object') {
                yufp.logger.error('filter args must been json object');
                return;
            }
            if (!obj.name) {
                yufp.logger.error('filter args must have name attribute');
                return;
            }
            if (!obj.before || typeof obj.before !== 'function') {
                yufp.logger.error('filter args must have before function');
                return;
            }
            if (!obj.after || typeof obj.after !== 'function') {
                yufp.logger.error('filter args must have after function');
                return;
            }
            if (!obj.exception || typeof obj.exception !== 'function') {
                yufp.logger.error('filter args must have exception function');
                return;
            }
            this._filters.push(obj);
        }

        Service.prototype.removeFilter = function (obj) {
            var name = typeof obj == 'string' ? obj : obj.name;
            var i = 0
            for (; i < this._filters.length; i++) {
                if (name == this._filters[i].name) {
                    break;
                }
            }
            if (i < this._filters.length) {
                this._filters.splice(i, 1);
            }
        };

        Service.prototype._doFilter = function (type, event) {
            var fname = 'exception'
            fname = type == 0 ? 'before' : fname
            fname = type == 1 ? 'after' : fname
            for (var i = 0; i < this._filters.length; i++) {
                if (!this._filters[i][fname]) {
                    continue;
                }
                var res = this._filters[i][fname](event);
                if (res === false) {
                    return false;
                }
            }
        };

        return new Service();
    })
);
/**
 * 公共数据字典管理器
 * 依赖：custom/plugins/yufp.service.js
 * created by helin3 2017-12-04
 */
(function (yufp, window, factory) {
    var exports = factory(yufp, window, window.document);
    if (typeof define === 'function') {
        define(exports);
    }
    window.yufp.lookup = exports;
}(yufp, window, function (yufp, window, document) {

    /**
     * 统一数据字典管理器
     * @constructor
     */
    function Lookup() {
        var _options = {
            lookupMgr: {},      //内存字典对象
            remoteUrl: backend.adminService+'/api/adminsmlookupitem/weblist', //远程URL
            remoteParamName: 'codeType', //远程参数名
            codeKey: 'key',     //对应后台字段key
            codeValue: 'value', //对应后台字段value
            limit: false,       //是否开启字典长度超过limitlength长度转存
            limitLength: 100,   //字典长度超过100，直接存储于localstorage
            prefix: 'YUFP-LIMIT-TYPE-', //超长字典前缀
            //loadingQueue: {},      //加载中队列，暂且未考虑
            localPath: [           //本地字典路径
                './custom/common/app.data.lookup.js'
            ]
        };
        yufp.extend(this, _options);
        this.loadLocal();
    }

    /**
     * private
     * 加载所有本地字典
     * @param callback 回调方法(可选参数)
     */
    Lookup.prototype.loadLocal = function (callback) {
        var me = this, pathArr = me.localPath;
        yufp.require.require(pathArr, function () {
            for (var i=0, len=pathArr.length;i<len;i++){
                var local = yufp.require.use(pathArr[i]);
                yufp.extend(me.lookupMgr, local.localLookup);
            }
            if (yufp.type(callback) === 'function'){
                callback.call(me);
            }
        });
    }

    /**
     * private
     * 加载指定远程数据字典
     * @param types  String
     *    GENDER_TYPE
     *    GENDER_TYPE,USER_STATUS,SYSTEM_STATUS
     */
    Lookup.prototype.loadRemote = function (types, callback) {
        var me = this;
        //TODO 暂且未考虑字典请求中队列
        me.forceLoad(types, function (data) {
            var typeArr = types.split(',');
            for (var i=0,len = typeArr.length;i<len;i++){
                var type = typeArr[i];
                if (!me.limit) {
                    me.lookupMgr[type] = data[type] || [];
                } else {
                    if(data[type].length < me.limitLength){
                        me.lookupMgr[type] = data[type];
                    } else {
                        me.storagePut(me.prefix + type, data[type]);
                    }
                }
            }
        });
    }

    /**
     * private
     * 强制刷新远程字典
     * @param types
     * @param callback
     */
    Lookup.prototype.forceLoad = function (types, callback) {
        var me = this;
        var param = {};
        param[me.remoteParamName] = types;
        yufp.service.request({
            url: me.remoteUrl,
            method: 'get',
            data: param,
            callback: function (code, msg, response) {
                if (code == 0 && response && yufp.type(callback) == 'function'){
                    callback.call(this, response.data);
                }
            }
        });
    }

    Lookup.prototype.queueHas = function (type) {
        return this.loadingQueue[type] ? true : false;
    }

    Lookup.prototype.queuePush = function (type) {
        this.loadingQueue[type] = true;
    }

    Lookup.prototype.queuePop = function (type) {
        delete this.loadingQueue[type];
        return type;
    }

    Lookup.prototype.storagePut = function (type, array) {
        yufp.sessionStorage.put(this.prefix + type, JSON.stringify(array));
    }

    Lookup.prototype.storageGet = function (type) {
        var me = this;
        var lookup = yufp.sessionStorage.get(this.prefix + type);
        if (lookup) {
            lookup = JSON.parse(lookup);
        } else {
            lookup = undefined;
        }
        return lookup;
    }

    /**
     * 字段转换方法
     * @param type 字典类型
     * @param sourceVal 要转换的值
     * @param source 源字段
     * @param target 目标字段
     * @returns {*}
     */
    Lookup.prototype.convert = function (type, sourceVal, source, target) {
        var me = this, targetVal = sourceVal;
        var lookup = me.lookupMgr[type] || me.storageGet[type];
        if (!lookup) {
            yufp.logger.warn('【' + type + '】字典未加载！');
            return targetVal;
        }
        for (var i=0,len=lookup.length;i<len;i++) {
            var item = lookup[i];
            if (item[source] === sourceVal) {
                targetVal = item[target];
                break;
            }
        }
        return targetVal;
    }

    /**
     * 将字典注册到字典管理器中
     * @param types string,必须
     *    示例：GENDER_TYPE
     *      或：GENDER_TYPE,USER_STATUS,SYSTEM_STATUS
     * @param callback function,可选
     */
    Lookup.prototype.reg = function (types, callback) {
        var me = this;
        var allArr = types.split(','), needArr = [];
        for (var i=0,len=allArr.length;i<len;i++) {
            var type = allArr[i];
            if (!me.lookupMgr[type] && !me.storageGet[type]){
                needArr.push(type);
            }
        }
        if (needArr.length > 0) {
            me.loadRemote.call(this, needArr.join(','), callback);
        }
    }

    /**
     * 将数据字典绑定到对象上
     * @param type 字典类型
     * @param callback 回调方法参数即是字典数组对象
     */
    Lookup.prototype.bind = function (type, callback) {
        var me = this;
        if (yufp.type(callback) != 'function') {
            yufp.logger.warn('【' + type + '】字典bind方法参数错误');
        }
        var lookup = this.lookupMgr[type] || me.storageGet[type];
        if (lookup) {
            callback.call(this, lookup);
        } else {
            me.forceLoad(type, function (data) {
                me.lookupMgr[type] = lookup = data[type];
                callback.call(this, lookup);
            });
        }
    }

    /**
     * 根据字典类别查找
     * @param type 要查找的字典类型
     * @param isArray 是否返回数组（可选参数）, true: 是，false: 否； 默认true
     */
    Lookup.prototype.find = function (type, isArray) {
        var me = this;
        isArray = isArray === false ? false : true;
        var lookup = this.lookupMgr[type] || me.storageGet[type];
        if (!isArray) {
            lookup = !lookup ? {} : me.array2Map(lookup);
        }
        return lookup;
    }

    /**
     * 数组转Map
     * @param lookup
     * @returns {Map}
     */
    Lookup.prototype.array2Map = function (lookup) {
        var me = this;
        lookup = lookup ? lookup.reduce(function(acc, cur){
            acc[cur[me.codeKey]] = cur[me.codeValue]
            return acc
        }, {}) : {};
        return lookup;
    }

    /**
     * 字典码转换为字典值
     * @param type 字典类型
     * @param key 字典码
     * @returns {*} 字典值
     */
    Lookup.prototype.convertKey = function (type, key) {
        return this.convert(type, key, this.codeKey, this.codeValue);
    }

    /**
     * 字典值转换为字典码
     * @param type 字典类型
     * @param value 字典值
     * @returns {*} 字典码
     */
    Lookup.prototype.convertValue = function (type, value) {
        return this.convert(type, value, this.codeValue, this.codeKey);
    }

    /**
     * 批量字典码转换为字典值
     * @param type 字典类型
     * @param keys 字典码，多个用逗号分隔
     * @param sep 分隔符（可选参数），默认','
     * @returns {*}
     */
    Lookup.prototype.convertMultiKey = function (type, keys, sep) {
        var me = this;
        var lookup = me.lookupMgr[type] || me.storageGet[type];
        if (!lookup) {
            yufp.logger.warn('【' + type + '】字典未加载！');
            return targetVal;
        }
        var sep = !sep ? ',' : sep;
        var keyArr = keys.split(sep), values = [];
        for (var k=0,kLen=keyArr.length;k<kLen;k++) {
            values.push(me.convertKey(type, keyArr[k]));
        }
        return values.join(sep);
    }

    /**
     * 批量字典值转换为字典码
     * @param type 字典类型
     * @param values 字典值，多个用逗号分隔
     * @param sep 分隔符（可选参数），默认','
     * @returns {*}
     */
    Lookup.prototype.convertMultiValue = function (type, values, sep) {
        var me = this;
        var lookup = me.lookupMgr[type] || me.storageGet[type];
        if (!lookup) {
            yufp.logger.warn('【' + type + '】字典未加载！');
            return targetVal;
        }
        var sep = !sep ? ',' : sep;
        var keyArr = values.split(sep), keys = [];
        for (var k=0,kLen=keyArr.length;k<kLen;k++) {
            keys.push(me.convertValue(type, keyArr[k]));
        }
        return keys.join(sep);
    }

    return new Lookup();
}));
/**
 * @created by helin3 2017-11-30
 * @updated by
 * @description 会话信息
 * 依赖：custom/plugins/yufp.service.js
 *      custom/plugins/yufp.sessionstorage.js
 */
(function (yufp, window, factory) {
    var exports = factory(yufp, window, window.document);
    if (typeof define === 'function') {
        define(exports);
    }
    window.yufp.session = exports;
}(yufp, window, function (yufp, window, document) {

    /**
     * 会话信息对象
     * @constructor
     */
    function LocalSession() {
        var _settings = {
            settings: {
                logoutUrl: backend.uaaService + '/api/logout',     //注销URL
                userUrl: backend.uaaService + '/api/session/info',   //会话URL
                userJsonRoot: '',                   //用户返回数据节点,如：'data.user'
                userStoreKey: 'YUFP-SESSION-USER',  //会话存储前缀
                userMapping: {                      //用户后端数据模型映射
                    userId: 'userId',               //用户ID
                    userName: 'userName',           //用户姓名
                    userCode: 'loginCode',          //用户登录码
                    userAvatar: 'userAvatar',       //用户头像
                    logicSys: 'logicSys',           //逻辑系统Object
                    roles: 'roles',                 //角色数组Object
                    org: 'org',                     //机构Object
                    dpt: 'dpt',                     //部门Object
                    instu: 'instu',                 //金融机构Object
                    upOrg: 'upOrg',                 //上级机构Object
                    upDpt: 'upDpt'                  //上级部门Object
                },

                menuUrl: backend.uaaService + '/api/account/menuandcontr', //菜单远程URL
                menuRootPid: '0',                     //菜单根节点父级Id
                menuJsonRoot: 'menus',                //菜单返回数据节点,如：'data.menus'
                menuStoreOgKey: 'YUFP-SESSION-MENUS-OG',
                menuStoreKey: 'YUFP-SESSION-MENUS',   //菜单存储前缀
                menuMapping: {                        //菜单后端数据模型映射
                    mId: 'menuId',                    //菜单ID
                    mText: 'menuName',                //菜单名称
                    mPid: 'upMenuId',                 //上级菜单ID
                    mIcon: 'menuIcon',                //菜单图标
                    routeId: 'funcId',                //菜单功能ID
                    routeUrl: 'funcUrl'               //菜单功能URL
                },

                ctrlUrl: backend.uaaService + '/api/session/menuandcontr', //控制点远程URL
                ctrlJsonRoot: 'ctrls',                //控制点返回数据节点,如：'data.ctrls'，控制点数据，查询需按菜单ID、功能ID排序返回
                ctrlStoreOgKey: 'YUFP-SESSION-STRLS-OG',
                ctrlStoreKey: 'YUFP-SESSION-STRLS',
                ctrlMapping: {
                    mId: 'menuId',         //菜单ID
                    rId: 'funcId',         //菜单功能ID
                    cId: 'ctrlCode',       //控制点CODE
                    cText: 'ctrlName'      //控制点名称
                }

            }
        };
        yufp.extend(this, _settings);
    };

    /**
     * private 存储数据
     * @param key
     * @param array
     */
    var storagePut = function (key, array) {
        yufp.sessionStorage.put(key, JSON.stringify(array));
    };

    /**
     * private 获取数据
     * @param key
     */
    var storageGet = function (key) {
        var me = this;
        var obj = yufp.sessionStorage.get(key);
        if (obj) {
            obj = JSON.parse(obj);
        } else {
            obj = undefined;
        }
        return obj;
    };

    /**
     * private 移除数据
     * @param key
     */
    var storageRemove = function (key) {
        yufp.sessionStorage.remove(key);
    }

    /**
     * private 获取namespace数据
     * @param obj 待获取对象
     * @param ns namespace，如：'json.data'
     * @returns {*}
     */
    var getObjectKey = function (obj, ns) {
        if (!ns) {
            return obj;
        }
        var keys = ns.split('.');
        for (var i = 0, len = keys.length; i < len; i++) {
            if (!obj) {
                break;
            }
            obj = obj[keys[i]];
        }
        return obj;
    };

    /**
     * 加载会话用户数据
     * @param callback
     */
    LocalSession.prototype.loadUserSession = function (callback) {
        var processFn = function (data) {
            var _this = this, userMapping = _this.settings.userMapping;
            for (var key in userMapping) {
                _this[key] = data[userMapping[key]] || '';
            }
            _this.user = data;
            storagePut(_this.settings.userStoreKey, _this.user);
        };

        var _this = this;
        var userStore = storageGet(_this.settings.userStoreKey);
        var menuStoreOg = storageGet(_this.settings.menuStoreOgKey);
        var ctrlStoreOg = storageGet(_this.settings.ctrlStoreOgKey);
        if (userStore && menuStoreOg) {
            processFn.call(_this, userStore);
            _this.processMenus(menuStoreOg);
            _this.processCtrls(ctrlStoreOg);
            typeof callback === 'function' ? callback.call(_this) : '';
            return;
        }

        yufp.service.request({
            url: _this.settings.userUrl,
            method: 'get',
            callback: function (code, message, data) {
                var data = getObjectKey(data, _this.settings.userJsonRoot);
                if (code == '0' && data) {
                    processFn.call(_this, data);
                }
                _this.loadMenus(callback);
                //_this.loadCtrls(); //由于控制点数据和菜单一起加载则无需要单独加载
            }
        });
    };

    /**
     * 移除会话信息
     * @param already 服务已登出
     */
    LocalSession.prototype.logout = function (already) {
        var settings = this.settings;
        if (already) {
            yufp.service.request({
                url: settings.logoutUrl,
                method: 'POST',
                callback: function (code, message, response) {
                    /*if (code != 0 || (response && response.code != 0)) {
                        var errMsg = response && response.message ? response.message : '系统错误，请联系系统管理员！';
                        var vm = yufp.custom.vue({});
                        vm.$message({ message: errMsg, type: 'warning' });
                    }*/
                }
            });
        }
        yufp.service.removeToken();
        yufp.router.to('login');
        storageRemove(settings.userStoreKey);
        storageRemove(settings.menuStoreOgKey);
        storageRemove(settings.menuStoreKey);
        storageRemove(settings.ctrlStoreOgKey);
        storageRemove(settings.ctrlStoreKey);
    };

    /**
     * 当前会话页
     */
    LocalSession.prototype.getCurrentRoute = function () {
        return yufp.service.getToken() ? 'frame' : ''
    };

    /**
     * 加载菜单数据
     */
    LocalSession.prototype.loadMenus = function (callback) {
        var _this = this;
        yufp.service.request({
            url: _this.settings.menuUrl,
            method: 'get',
            callback: function (code, message, data) {
                var menuData = getObjectKey(data, _this.settings.menuJsonRoot);
                if (code == '0' && menuData) {
                    _this.processMenus(menuData);
                    var ctrlData = getObjectKey(data, _this.settings.ctrlJsonRoot);
                    if (ctrlData) {
                        _this.processCtrls(ctrlData);
                    }
                    typeof callback === 'function' ? callback.call(_this) : '';
                }
            }
        });
    };

    /**
     * 加工处理菜单数据
     * @param data
     */
    LocalSession.prototype.processMenus = function (data) {
        var _this = this, mm = _this.settings.menuMapping;
        _this.orginalMenus = JSON.parse(JSON.stringify(data));
        var leafMenus = [], nonLeafMenus = [];
        for (var i = 0, len = data.length; i < len; i++) {
            var obj = data[i];
            for (var key in mm) {
                if (mm[key] != key) {
                    obj[key] = obj[mm[key]] || '';
                    delete obj[mm[key]];
                }
            }
            obj.routeId && obj.routeUrl ? leafMenus.push(obj) : nonLeafMenus.push(obj);
        }
        var routeTable = {};
        for (var i = 0, len = leafMenus.length; i < len; i++) {
            var obj = leafMenus[i], flag = false;
            routeTable[obj.routeId] = {html: obj.routeUrl + '.html', js: obj.routeUrl + '.js'};
            for (var j = 0, jlen = nonLeafMenus.length; j < jlen; j++) {
                var pObj = nonLeafMenus[j];
                if ('' + obj.mPid == '' + pObj.mId) {
                    pObj.children = pObj.children ? pObj.children : [];
                    pObj.children.push(obj);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                nonLeafMenus.unshift(obj);
            }
        }
        yufp.router.addRouteTable(routeTable);
        var root = yufp.util.array2tree(nonLeafMenus, {id: 'mId', pid: 'mPid', root: _this.settings.menuRootPid});
        if (root.children && root.children.length > 0) {
            root.children[0].isIndex = true;
        }
        _this.menus = root.children;
        storagePut(_this.settings.menuStoreOgKey, _this.orginalMenus);
        storagePut(_this.settings.menuStoreKey, _this.menus);
    };

    /**
     * 获取树结构的菜单数据
     * @returns {*}
     */
    LocalSession.prototype.getMenuTree = function () {
        return this.menus || storageGet(this.settings.menuStoreKey);
    };

    /**
     * 获取数组结构的菜单数据
     * @returns {*}
     */
    LocalSession.prototype.getMenus = function () {
        return this.orginalMenus || storageGet(this.settings.menuStoreOgKey);
    };

    /**
     * 根据菜单ID返回菜单对象数据
     * @param menuId
     */
    LocalSession.prototype.getMenuById = function (menuId) {
        var settings = this.settings;
        for (var i = 0, len = this.orginalMenus.length; i < len; i++) {
            var menu = this.orginalMenus[i];
            if (menu[settings.menuMapping.mId] == menuId) {
                return menu;
            }
        }
    };

    /**
     * 加载控制权限数据
     * 注：若控制权限数据和菜单一起加载，则无需调用此方法
     */
    LocalSession.prototype.loadCtrls = function (callback) {
        var _this = this;
        yufp.service.request({
            url: _this.settings.ctrlUrl,
            method: 'get',
            callback: function (code, message, data) {
                var data = getObjectKey(data, _this.settings.ctrlJsonRoot);
                if (code == '0' && data) {
                    _this.processCtrls(data);
                }
                typeof callback === 'function' ? callback.call(_this) : '';
            }
        });
    };

    /**
     * 加载处理控制点权限,后台需要排序
     * @param data
     */
    LocalSession.prototype.processCtrls = function (data) {
        if (!data || data.length < 1) {
            return;
        }
        var _this = this, mm = _this.settings.ctrlMapping;
        _this.orginalCtrls = JSON.parse(JSON.stringify(data));
        var ctrlObj = {}, lastId = '', lastObj = {};
        for (var i = 0, len = data.length; i < len; i++) {
            var obj = data[i];
            for (var key in mm) {
                if (mm[key] != key) {
                    obj[key] = obj[mm[key]] || '';
                    delete obj[mm[key]];
                }
            }
            if (lastId == obj.mId) {
                lastObj[obj.cId] = 1;
            } else {
                if (lastId !== '') {
                    ctrlObj[lastId] = lastObj;
                }
                lastId = obj.mId;
                lastObj = {};
                lastObj[obj.cId] = 1;
            }
        }
        if (lastId !== '') {
            ctrlObj[lastId] = lastObj;
        }
        _this.ctrls = ctrlObj;
        storagePut(_this.settings.ctrlStoreOgKey, _this.orginalCtrls);
        storagePut(_this.settings.ctrlStoreKey, _this.ctrls);
    };

    /**
     * 检查是否有控制点权限，有权限返回false，无权限返回true
     * @param ctrlCode 控制点代码
     * @param menuId 菜单ID，可选值，默认取当前页签ID，无需提供
     * @param isView 是否视图菜单，可选值，默认false，配置为true时，若menuId配置为'',
     *               则取得menuId强制按视图方式获取
     */
    LocalSession.prototype.checkCtrl = function (ctrlCode, menuId, isView) {
        var ctrls = this.ctrls;
        menuId = menuId ? menuId : '';
        if (!menuId) {
            //TODO 视图菜单时，获取控制点ID逻辑暂且未定
            menuId = isView ? '' : yufp.frame.tab().url;
        }
        if (!ctrlCode || !ctrls || !menuId) {
            return false;
        }
        if (ctrls && menuId && ctrls[menuId] && ctrls[menuId][ctrlCode]) {
            return false;
        }
        return true;
    };

    return new LocalSession();
}));
/**
 * 首页&框架公共管理
 * created by wy 2017-12-04
 */
(function (yufp, window, factory) {
    var exports = factory(yufp, window, window.document);
    if (typeof define === 'function') {
        define(exports);
    }
    window.yufp.frame = exports;
}(yufp, window, function (yufp, window, document) {

    /**
     * 框架管理（页签、公共设置）
     * @constructor
     */
    function Frame() {
        var _options = {
            maxOpenTabs: 10,     //页签最大打开数量，默认10,0:不限制
            isMenuRefresh: false,        //重复打开是否刷新，默认false
            tabRefresh: true,       //是否开启页签右键刷新功能，默认true
            tabDoubleClickRefresh: true,       //是否开启页签双击刷新功能，默认true
            tabsTargetId: 'yu-idxTabs',      //页签按钮标签ID
            tabBoxTargetId: 'yu-idxTabBox',      //页签容器标签ID
            tabDropdownMenuBtId: 'yu-idxTabDMenuBt',      //页签下拉菜单按钮id
            tabDropdownMenuWidth: 140,       //页签下拉菜单所占空间宽度
            tabButtonOutWidth: 34,   //页签按钮内外边距宽
            model: 0,    //当前首页模式标记
            models: ['frame', 'frameRight', 'frameTop'],     //菜单模式
            heightDifference: [35, 35, 123],      //高度差，frame：35、frameRight：35、frameTop：123
            idxMenuSwiper: null,     //菜单Swiper对象
            sysTools: {
                ownerId: 'yu-sysTools',//所属容器id
                tools: [{
                    show: true,
                    text: '收缩/展开',
                    icon: 'el-icon-yx-menu',
                    className: 'sidebar-toggler',
                    id: 'yu-sidebar'
                },
                    {
                        show: true,
                        text: '切换模式',
                        icon: 'el-icon-yx-switch-1',
                        className: '',
                        id: 'yu-switch'
                    },
                    {
                        show: true,
                        text: '设置主题',
                        icon: 'el-icon-yx-themes-1',
                        className: '',
                        id: 'yu-themes'
                    },
                    {
                        show: false,
                        text: '常用功能',
                        icon: 'el-icon-yx-star-empty',
                        className: '',
                        id: 'yu-common'
                    },
                    {
                        show: true,
                        text: '修改密码',
                        icon: 'el-icon-yx-key2',
                        className: '',
                        id: 'yu-modifyPassword'
                    },
                    {
                        show: false,
                        text: '选择语言',
                        icon: 'el-icon-yx-sphere',
                        className: '',
                        id: 'yu-language'
                    }]
            },      //系统工具
            modifyPassword: {
                modifyPassword: {
                    html: 'pages/common/frame/modifyPassword.html',
                    js: 'pages/common/frame/modifyPassword.js'
                }
            },      //修改密码-页面路由
            TEMP_loginUser: {
                name: 'YUFP',
                role: [
                    {id: 'Administrator', code: 'Administrator', name: 'Administrator'}
                ],
                picUrl: 'themes/default/images/user_default_pic.png'
            },       //用户登录信息，临时
            themes: [
                {id: 'default', name: '默认'},
                {id: 'orange', name: '橙色'},
                {id: 'simple', name: '简约'}
            ],      //UI主题
            language: [
                {id: 'zh', name: '中文'},
                {id: 'en', name: 'English'}
            ]       //语言
        };
        yufp.extend(this, _options);
    };

    /**
     * Frame初始化
     */
    Frame.prototype.init = function () {
        setUserInfo();
        setSysTools();
        checkTabSize();
        $(window).resize(function () {
            checkTabSize();
        });
    };

    /**
     * private
     * 页签size适配(页签面容区域高度)
     */
    var checkTabSize = function () {
        var _this = yufp.frame;
        $('#' + _this.tabBoxTargetId).height(_this.size().height);      //页签内容区域高度
        $('#' + _this.tabBoxTargetId + '>div').height(_this.size().height);      //页签内容区域高度
        checkTabs();
    };

    /**
     * private
     * 页签按钮显示适配
     */
    var checkTabs = function () {
        var _this = yufp.frame;
        //获取单个tab按钮宽度
        var _getW = function (o) {
            return o ? $(o).width() + _this.tabButtonOutWidth : 0;
        };
        //获取当前tab按钮容器宽度
        var _getSW = function () {
            return $('#' + _this.tabsTargetId + '>div').width();
        };
        //获取tab按钮可显示的最大空间宽度
        var _getMaxW = function () {
            return $('#' + _this.tabsTargetId).width() - _this.tabDropdownMenuWidth - 10;
        };
        //获取显示、隐藏页签按钮
        var _getTab = function (flag) {
            return flag == 'hidden' ? $('#' + _this.tabsTargetId + '>div>a:hidden') : $('#' + _this.tabsTargetId + '>div>a:gt(0):not(.ck)').not('a:hidden');
        }
        if (_getSW() > _getMaxW()) {
            _getTab().each(function (i, o) {
                $(o).hide();
                if (_getSW() < _getMaxW()) {
                    return false
                }
            });
        }
        else {
            _getTab('hidden').each(function (i, o) {
                if ((_getSW() + _getW(o)) < _getMaxW()) {
                    $(o).show();
                }
            });
        }
    };

    /**
     * private
     * 获取菜单模式
     */
    var getModel = function () {
        var _this = yufp.frame;
        if (document.getElementById('frameTop')) {
            _this.model = 2;
        }
        else if (document.getElementById('frameRight')) {
            _this.model = 1;
        }
        else {
            _this.model = 0;
        }
        return _this.model;
    };

    /**
     * private
     * 系统工具按钮初始化
     */
    var setSysTools = function () {
        var _this = yufp.frame;
        if (_this.sysTools.ownerId) {
            var _tools = '';
            $(_this.sysTools.tools).each(function (i, o) {
                if (o.show) {
                    _tools += '<span id="' + o.id + '" class="' + o.className + ' ' + o.icon + '" title="' + o.text + '"></span>';
                }
            });
            $('#' + _this.sysTools.ownerId).append(_tools);

            //菜单收缩展开
            $('#yu-sidebar').bind('click', function () {
                setTimeout(function () {
                    _this.idxMenuSwiper.resizeFix();//重置菜单容器滚动尺寸
                }, 50);
            });

            //切换模式
            $('#yu-switch').bind('click', function () {
                yufp.router.to(_this.models[(_this.model + 1) >= _this.models.length ? 0 : _this.model + 1]);
            });

            //选择语言
            $('#yu-language').bind('click', function (e) {
                var el = this;
                var getXY = function (o) {
                    var x = o.offsetLeft;
                    var y = o.offsetTop + o.clientHeight + 5;
                    do {
                        o = o.offsetParent;
                        x += o.offsetLeft;
                    } while (o.tagName != 'BODY');
                    return {x: x - 25 + 'px', y: y + 'px'};
                };
                var e = event || window.event;
                if ($('#yu-languageList').size() > 0) {
                    $('#yu-languageList').css({left: getXY(el).x, top: getXY(el).y}).fadeToggle();
                }
                else {
                    var domStr = '<div id="yu-languageList" class="yu-languageList"><i></i>';
                    for (var i = 0; i < _this.language.length; i++) {
                        domStr += '<span language="' + _this.language[i].id + '" title="' + _this.language[i].name + '">' + _this.language[i].name + '</span>';
                    }
                    domStr += '</div>';
                    $('body').append(domStr).find('#yu-languageList').css({
                        left: getXY(el).x,
                        top: getXY(el).y
                    }).fadeIn()
                        .bind('mouseleave', function () {
                            $(this).hide();
                        })
                        .find('span').bind('click', function () {
                        var language = $(this).attr('language');
                        $('#yu-languageList').hide();
                        alert(language);
                    });
                }
            });

            //设置主题
            $('#yu-themes').bind('click', function (e) {
                var el = this;
                var getXY = function (o) {
                    var x = o.offsetLeft;
                    var y = o.offsetTop + o.clientHeight + 5;
                    do {
                        o = o.offsetParent;
                        x += o.offsetLeft;
                    } while (o.tagName != 'BODY');
                    return {x: x - 25 + 'px', y: y + 'px'};
                };
                var e = event || window.event;
                if ($('#yu-themesList').size() > 0) {
                    $('#yu-themesList').css({left: getXY(el).x, top: getXY(el).y}).fadeToggle();
                }
                else {
                    var domStr = '<div id="yu-themesList" class="yu-themesList"><i></i>';
                    for (var i = 0; i < _this.themes.length; i++) {
                        domStr += '<span class="' + _this.themes[i].id + '" themes="' + _this.themes[i].id + '" title="' + _this.themes[i].name + '"><i></i>' + _this.themes[i].name + '</span>';
                    }
                    domStr += '</div>';
                    $('body').append(domStr).find('#yu-themesList').css({left: getXY(el).x, top: getXY(el).y}).fadeIn()
                        .bind('mouseleave', function () {
                            $(this).hide();
                        })
                        .find('span').bind('click', function () {
                        var themes = 'default';//$(this).attr('themes');
                        $('link[rel=stylesheet]').each(function (i, o) {
                            for (var i = 0; i < _this.themes.length; i++) {
                                if (o.href.indexOf('themes/' + _this.themes[i].id) > 0) {
                                    o.href = o.href.replace('themes/' + _this.themes[i].id, 'themes/' + themes);
                                    // save themes data
                                    //……………………………………
                                }
                            }
                        });
                        $('#yu-themesList').hide();
                    });
                }
                /*$(document).bind('click',function () {
                    $('#yu-themesList').hide();
                });*/
            });

            //常用功能
            $('#yu-common').bind('click', function () {

            });

            //修改密码
            $('#yu-modifyPassword').bind('click', function () {
                yufp.router.addRouteTable(_this.modifyPassword);
                var options = {
                    id: 'modifyPassword',
                    title: '修改密码',
                    key: 'custom_20180108174856'
                };
                _this.addTab(options);
            });

        }
        // 注销退出
        $('#yu-exit').click(function () {
            _this.exit();
        });
    };

    /**
     * private
     * 用户信息设置
     */
    var setUserInfo = function () {
        var name = yufp.session.userName || yufp.frame.TEMP_loginUser.name;
        var roles = yufp.session.roles || yufp.frame.TEMP_loginUser.role;
        var role = [];
        for (var i = 0, len = roles.length; i < len; i++) {
            role.push(roles[i].name);
        }
        var pic = yufp.session.userAvatar || yufp.frame.TEMP_loginUser.picUrl;
        document.getElementById('userName').innerHTML += name;
        document.getElementById('userRole').innerHTML += '(' + role.join(',') + ')';
        document.getElementById('userPic').src = pic;
        document.getElementById('userPic').title = name + '/' + role;
    };

    /**
     * private
     * 页签下拉菜单
     * @param key  页签标识
     * @param title  页签标题
     */
    var tabDropdownMenu = function (key, title) {
        var _this = yufp.frame;
        var _add = function (k, t) {
            var _p = document.createElement('span');
            _p.innerHTML = title;
            _p.title = title;
            _p.setAttribute('data-key', key);
            if (k != 0) {
                var _i = document.createElement('i');
                _i.title = '关闭';
                _p.appendChild(_i);
                _i.addEventListener('click', function () {
                    $(_p).remove();
                    _this.removeTab(k);
                    _display();
                    return;
                });
            }
            $('#' + _this.tabDropdownMenuBtId + '>div').append(_p);
            _display();
            _p.addEventListener('click', function () {
                _this.focusTab(k);
            });
        };

        var _init = function () {
            var _a = document.createElement('a');
            _a.id = _this.tabDropdownMenuBtId;
            _a.href = 'javascript:void(0)';
            var _d = document.createElement('div');
            var _p = document.createElement('span');
            _p.innerHTML = '关闭全部';
            _p.title = '关闭全部';
            _d.appendChild(_p);
            _a.appendChild(_d);
            $('#' + _this.tabsTargetId).append(_a);
            _p.addEventListener('click', function () {
                _this.removeAllTab();
                $('#' + _this.tabDropdownMenuBtId + ' span:gt(1)').remove();
                _display();
            });
        };

        var _display = function () {
            $('#' + _this.tabDropdownMenuBtId + '>div>span').size() > 2 ? $('#' + _this.tabDropdownMenuBtId).show() : $('#' + _this.tabDropdownMenuBtId).hide();
        };

        if ($('#' + _this.tabDropdownMenuBtId).size() < 1) {
            _init(key, title);
        }

        if ($('#' + _this.tabDropdownMenuBtId + ' span[data-key="' + key + '"').size() < 1) {
            _add(key, title);
        }
    };

    /**
     * private
     * 移除页签下拉菜单子项
     * @param key  页签标识
     */
    var removeTabDropdownMenu = function (key) {
        var _this = yufp.frame;
        $('#' + _this.tabDropdownMenuBtId + ' span[data-key="' + key + '"').remove();
        $('#' + _this.tabDropdownMenuBtId + ' span').size() > 2 ? $('#' + _this.tabDropdownMenuBtId).show() : $('#' + _this.tabDropdownMenuBtId).hide();
    };

    /**
     * private
     * 页签右键菜单
     * @param key  页签标识
     */
    var tabContextmenu = function (key) {
        var _this = yufp.frame;
        var e = event || window.event;
        //清除默认右键菜单
        document.all ? e.returnValue = false : e.preventDefault();
        //显示右键菜单
        var _show = function () {
            key == 0 ? $('.yu-tabContextmenu a:eq(1)').hide() : $('.yu-tabContextmenu a:eq(1)').show();
            $('.yu-tabContextmenu').attr('data-key', key).css({
                left: e.clientX + 'px',
                top: e.clientY + 'px'
            }).fadeIn('fast');
        };
        //隐藏右键菜单
        var _hide = function () {
            $('.yu-tabContextmenu').fadeOut('fast');
        };
        //创建菜单
        var _create = function () {
            var contextmenuTab, refreshThisMenu, closeThisMenu, closeOtherMenu, closeAllMenu;
            contextmenuTab = document.createElement('div');
            contextmenuTab.className = 'yu-tabContextmenu';
            closeThisMenu = document.createElement('a');
            closeOtherMenu = document.createElement('a');
            closeAllMenu = document.createElement('a');
            closeThisMenu.innerHTML = '关闭当前';
            closeOtherMenu.innerHTML = '关闭其它';
            closeAllMenu.innerHTML = '关闭全部';
            closeThisMenu.href = 'javascript:void(0)';
            closeOtherMenu.href = 'javascript:void(0)';
            closeAllMenu.href = 'javascript:void(0)';
            if (_this.tabRefresh) {
                refreshThisMenu = document.createElement('a');
                refreshThisMenu.innerHTML = '刷新当前';
                refreshThisMenu.href = 'javascript:void(0)';
                contextmenuTab.appendChild(refreshThisMenu);
                //刷新当前
                $(refreshThisMenu).bind('click', function () {
                    var _id = $('#' + _this.tabsTargetId + ' a[data-key="' + $(contextmenuTab).attr('data-key') + '"]').attr('data-url');
                    _this.focusTab($(contextmenuTab).attr('data-key'));
                    _this.refreshTab($(contextmenuTab).attr('data-key'));
                });
            }
            contextmenuTab.appendChild(closeThisMenu);
            contextmenuTab.appendChild(closeOtherMenu);
            contextmenuTab.appendChild(closeAllMenu);
            document.body.appendChild(contextmenuTab);
            $(contextmenuTab).bind('mouseleave', function () {
                _hide();
            });
            $(document).bind('click', function () {
                _hide();
            });
            _show();

            //关闭当前
            $(closeThisMenu).bind('click', function () {
                _this.removeTab($(contextmenuTab).attr('data-key'));
            });
            //关闭其它
            $(closeOtherMenu).bind('click', function () {
                _this.removeOtherTab($(contextmenuTab).attr('data-key'));
            });
            //关闭全部
            $(closeAllMenu).bind('click', function () {
                _this.removeAllTab();
            });
        };
        //如果已经创建菜单，侧仅显示即可
        $('.yu-tabContextmenu').size() > 0 ? _show() : _create();
    };

    /**
     * 获取工作区域尺寸
     *@returns {width,height}
     */
    Frame.prototype.size = function () {
        getModel();
        return {
            width: window.top.document.body.clientWidth - $('.page-sidebar').width(),
            height: window.top.document.body.clientHeight - this.heightDifference[this.model]
        };
    };

    /**
     * 添加页签
     * @param options {id,title,key,data}
     * @param id  路由id，对应data-url
     * @param title  页签标题，对应菜单标题
     * @param [key]  页签标识，可选
     * @param [data]  参数对象，可选
     *  @return {string} key
     */
    Frame.prototype.addTab = function (options) {
        var _this = this;
        var id, title, key, data;
        if (!options || options.id == undefined) {
            return;
        }
        id = options.id;
        title = options.title ? options.title : 'Tab Title';
        key = options.key ? options.key : 'other_' + _this.getTimestamp();
        data = options.data ? options.data : '';
        var tabId = 'tabBox_' + key;
        //该路由页面已打开，则激活
        if ($('#' + tabId).size() > 0) {
            _this.focusTab(key);
        } else {
            if ($('#' + _this.tabsTargetId + '>div>a').length >= _this.maxOpenTabs && _this.maxOpenTabs != 0) {
                //打开数量超过最大，移除“首页”页签后的第1个页签
                _this.removeTab($('#' + _this.tabsTargetId + '>div>a:eq(1)').attr('data-key'));
            }
            var _c = key == 0 ? '' : '<i title="关闭"></i>';
            var _a = '<a class="ck" href="javascript:void(0);" title="' + title + '" data-url="' + id + '"  data-key="' + key + '">' + title + _c + '</a>';
            $('#' + _this.tabsTargetId + '>div').find('a[class=ck]').removeClass('ck').end().append(_a).find('a[data-key="' + key + '"]')
                .bind('click', function () {
                    _this.focusTab($(this).attr('data-key'));
                })
                .bind('dblclick', function () {
                    _this.tabDoubleClickRefresh ? _this.refreshTab($(this).attr('data-key')) : '';
                })
                .bind('contextmenu', function () {
                    tabContextmenu($(this).attr('data-key'));
                })
                .data('data', data)
                .find('i').bind('click', function () {
                _this.removeTab($(this).parent().attr('data-key'));
            });
            var _d = '<div class="ck" data-key="' + key + '" id="' + tabId + '" style="height:' + _this.size().height + 'px;overflow:auto;overflow-x: hidden;"></div>';
            $('#' + _this.tabBoxTargetId).find('div[class=ck]').removeClass('ck').end().append(_d);
            _this.focusMenu(key);
            tabDropdownMenu(key, title);
            yufp.bus.put('_frame', key, id);//保存路由ID
            yufp.router.to(id, data, tabId);//路由跳转
            checkTabs();
        }
        return key;
    };

    /**
     * 激活页签
     * @param key  页签标识
     */
    Frame.prototype.focusTab = function (key) {
        var _this = this;
        $('#' + _this.tabsTargetId + '>div>a[data-key="' + key + '"]').show().addClass('ck').siblings().removeClass('ck');
        $('#' + _this.tabBoxTargetId + '>div[data-key="' + key + '"]').addClass('ck').siblings().removeClass('ck');
        _this.focusMenu(key);
        //重复打开菜单刷新当前router
        if (_this.isMenuRefresh) {
            _this.refreshTab(key);
        }
        checkTabs();
    };

    /**
     * 刷新页签
     * @param key  页签标识
     * @param id  或路由id
     */
    Frame.prototype.refreshTab = function (key) {
        var _this = this;
        var key = key ? key : _this.tab().key;
        var tab = _this.tab(key);
        yufp.router.to(tab.url, tab.data, tab.boxId);
    }

    /**
     * 移除页签
     * @param [key]  页签标识,可选，无key时关闭当前页签
     */
    Frame.prototype.removeTab = function (key) {
        var _this = this;
        key = key ? key : $('#' + _this.tabsTargetId + '>div>a[class=ck]').attr('data-key');
        //var id=yufp.bus.get('_frame',key);
        //var rootId='root_'+id;
        yufp.router.unMount(_this.tab(key).boxId);

        var $a = $('#' + _this.tabsTargetId + '>div>a[data-key="' + key + '"]');
        if ($a.hasClass('ck')) {
            //如果是关闭当前，则激活前一个页签
            $a.prev().click();
        }
        $a.remove();
        $('#' + _this.tabBoxTargetId + '>div[data-key="' + key + '"]').remove();
        $('a.yu-isMenu[data-key="' + key + '"]').attr('data-key', '');
        removeTabDropdownMenu(key);
        checkTabs();
    };

    /**
     * 移除全部页签
     */
    Frame.prototype.removeAllTab = function () {
        var _this = this;
        var $a = $('#' + _this.tabsTargetId + '>div>a:not(:first)');
        $a.each(function (i, o) {
            _this.removeTab($(o).attr('data-key'));
        });
    };

    /**
     * 移除其它页签
     * @param key  页签标识
     */
    Frame.prototype.removeOtherTab = function (key) {
        var _this = this;
        var $a = $('#' + _this.tabsTargetId + '>div>a:not(:first)');
        $a.each(function (i, o) {
            if ($(o).attr('data-key') != key) {
                _this.removeTab($(o).attr('data-key'));
            }
        });
    };

    /**
     * 获取页签参数
     * @param key  页签标识，无key时，返回当前tab
     * @returns {key,url(router id),title,data}
     */
    Frame.prototype.tab = function (key) {
        var _this = this;
        var key = key || $('#' + _this.tabsTargetId + '>div>a[class=ck]').attr('data-key');
        var $tab = $('#' + _this.tabsTargetId + '>div>a[data-key=' + key + ']');
        return {
            key: key,    //页签标识，默认为当前页签
            url: $tab.attr('data-url'),      //页签url(或路由id)
            title: $tab.attr('title'),      //页签标题,
            data: $tab.data('data'),     //页签数据
            index: $tab.index(),     //页签索引
            boxId: $('#' + _this.tabBoxTargetId + '>div[data-key=' + key + ']').attr('id')     //页签内容容器id
        };
    };

    /**
     * 菜单激活，高亮
     * @param key  页签标识
     */
    Frame.prototype.focusMenu = function (key) {
        var _this = this;
        if (_this.model == 2) {
            $('.yu-topFirstMenus').removeClass('ck');
            $('a.yu-isMenu[data-key="' + $('#' + _this.tabsTargetId + '>div>a[class*=ck]').first().attr('data-key') + '"]').parents().children('.yu-topFirstMenus').addClass('ck');
        }
        else {
            $('.page-sidebar-menu li').removeClass('active').removeClass('open');
            $('a.yu-isMenu[data-key="' + $('#' + _this.tabsTargetId + '>div>a[class*=ck]').first().attr('data-key') + '"]').parents('li').addClass('active').addClass('open');
        }
    };

    /**
     * 菜单单击事件
     * @param menusObj  菜单结点对象
     * @param callback  回调function
     */
    Frame.prototype.menuClick = function (menusObj, callback) {
        var _this = this;
        $(menusObj).bind('click', function () {
            //如果已经打开则激活
            var cMenu = $(this), dataKey = cMenu.attr('data-key');
            if (dataKey) {
                _this.focusTab(dataKey);
                _this.focusMenu(dataKey);
            } else {
                // var key = 'menu_'+_this.getTimestamp();//菜单、页签、页签容器对应标识
                dataKey = 'menu_' + cMenu.attr('data-mId');
                cMenu.attr('data-key', dataKey);
                var options = {
                    id: cMenu.attr('data-url'),
                    path: cMenu.attr('data-path'),
                    title: cMenu.text(),
                    key: dataKey
                };
                var log = {
                    "userId": yufp.session.userId,
                    "orgId": yufp.session.org.code,
                    "menuId": options.key,
                    "operFlag": '访问',
                    "logTypeId": '7',
                    "beforeValue": '',
                    "afterValue": '',
                    "content": '访问菜单:' + options.title + '路径:' + options.path,
                };
                yufp.util.logInfo(log, '/example/log/menu');
                _this.addTab(options);
            }
            if (callback && yufp.type(callback) == 'function') {
                callback.call(this);
            }
        });
    };

    /**
     * 获取时间戳标识
     *  @return {string}
     */
    Frame.prototype.getTimestamp = function () {
        return (new Date()).valueOf();
    };

    /**
     * 注销登录
     * @param key  页签标识
     */
    Frame.prototype.exit = function () {
        yufp.session.logout();
    };

    return new Frame();
}));/*这里本项目得工具类方法放置的地方*/

define(function (require, exports) {
    //记录exports
    yufp.custom = exports;

    /**
     * vue扩展定义方法
     * @param args
     * @returns {*}
     */
    exports.vue = function (args) {
        return new Vue(args);
    };

    /**
     * 获取有效数据
     * @param data
     */
    exports.getData = function (data) {

        var resData = {};

        for (var key in data) {

            //以m_开头的数据为page中的元数据、以$开头的数据为virtual model的保留数据
            if (key.indexOf('m_') === 0 || key.indexOf('$') === 0) {
                continue;
            }
            //获取value
            var val = data[key];
            //忽略function
            if (typeof val === 'function') {
                continue;
            }
            resData[key] = data[key];
        }
        return resData;
    }

    /**
     * 设置数据
     * @param srcData
     * @param targetData
     */
    exports.setData = function (srcData, targetData) {
        for (var key in srcData) {
            targetData[key] = srcData[key];
        }
        return targetData;
    }

    /**
     * 获取可见区域
     * @returns {{width: number, height: number}}
     */
    exports.viewSize = function viewSize() {
        return yufp.frame.size();
    }


})
