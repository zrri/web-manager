/**
 * Created by yourEmail on 2017/11/17.
 */
define(function (require, exports) {

    //page加载完成后调用ready方法
    exports.ready = function (hashCode, data, cite) {
        //创建virtual model
        var vm =  yufp.custom.vue({
            el: "#fx_lookup",
            data: {

            },
            mounted: function(){

            },
            methods: {

            }
        });
    };

    //消息处理
    exports.onmessage = function (type, message) {

    };

    //page销毁时触发destroy方法
    exports.destroy = function (id, cite) {

    }

});