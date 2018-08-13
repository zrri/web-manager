define(function(require, exports) {
  exports.ready = function(hashCode, data, cite) {
    //  yufp.lookup.reg("NATIONALITY,PUBLISH_STATUS");
    var vm = yufp.custom.vue({
      el: "#el_test",
      data: {
        userId: '999999'
      },
      methods: {
        login: function() {

        },
        /**
         * 注册全局消息接收函数
         * @param tellerId
         */
        registMesageService: function(tellerId) {

          yufp.service1.unRegisterMessageService("frame");

          //注册
          yufp.service1.registerMessageService("frame", function(name, data) {
            //处理information消息
            if (data.msgType == "#info") {
              //记录地址
              var address = data.content;
              fox.logger.info("1.建立连接,返回标识地址：" + address);
              yufp.service1.request({
                id: "registWebSocket",
                data: {
                  "address": address,
                  "userId": vm.userId
                },
                name: "common/message/registWebsocket",
                callback: function(code, message, content) {
                  if (code == 0) {
                    console.log("2.标识地址记录成功:" + JSON.stringify(content));
                  } else {
                    console.error("全局消息通知注册失败:" + message);
                  }
                }
              });
            } else {

              var content = JSON.parse(data.content);

              var msgName = content.msgName;

              if (msgName){
                fox.eventproxy.trigger(msgName, content.msg);
                console.log(data.content);
              }
              else
                fox.logger.info("websocket消息通知未包含消息名称!!!")
              //fox.layer.open("web socket通知:" + data.content);
            }
          });
        }
      }
    });
  }
});
