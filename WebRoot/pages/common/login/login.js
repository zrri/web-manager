/**
 * @created by jiangcheng 2017-11-15
 * @updated by
 * @description 登录页
 */
define(function (require, exports) {
    /**
     * 页面加载完成时触发
     * @param hashCode 路由ID
     * @param data 传递数据对象
     * @param cite 页面站点信息
     */
    exports.ready = function (hashCode, data, cite) {
        var loginFn = function () {

            if($('#username').val()==''){
                $('#msg').text('请输入用户名!').show();
                $('#username').focus();
                return;
            }
            if($('#password').val()==''){
                $('#msg').text('请输入密码!').show();
                $('#password').focus();
                return;
            }
            $('#msg').hide();

            var data = {
                username: $('#username').val(),
                password: $('#password').val(),
                grant_type: "password"
            };
            var headers = {
                "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                "Authorization": "Basic d2ViX2FwcDo="
            };
            yufp.service.request({
                needToken: false,
                url: backend.uaaService+'/oauth/token',
                method: 'post',
                headers: headers,
                data: data,
                callback: function (code, message, response) {
                    if (response && response.code == '0') {
                        var data = response && response.access_token;
                        yufp.service.putToken(data);
                        yufp.session.loadUserSession(function () {
                            yufp.router.to("frame");
                        });
                    } else {
                        var msg = response && response.message ? response.message : '登录失败，请联系系统管理员！';
                        $('#msg').text(msg).show();
                    }
                }
            });
        }
        $("#submitBtn").click(function(){
            loginFn();
        });
    };
});