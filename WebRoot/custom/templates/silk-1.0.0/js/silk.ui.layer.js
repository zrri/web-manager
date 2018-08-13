/**
 * Created by jiangcheng on 2016/10/30.
 */

"use strict";
(function ($, window, factory) {
    // 判断是否支持模块定义
    var hasDefine = (typeof define === 'function');
    if (hasDefine) {
        //获取对象
        var exports = factory(window.document);
        //定义模块
        define(exports);
        //安装插件(兼容非模块的访问方式)
        window.$.layer = exports;

    } else {
        //获取对象
        var exports = factory(window.document);
        //安装插件
        window.$.layer = exports;
    }

}($, window, function (document) {

    var doc = document, query = 'querySelectorAll', claname = 'getElementsByClassName', S = function (s) {
        return doc[query](s);
    };

    //默认配置
    var config = {
        type: 0
        , shade: true
        , shadeClose: true
        , fixed: true
        , skin: 'dialog'
        , anim: 'scale' //默认动画类型
        , color: 'yellow'
    };

    var ready = {
        extend: function (obj) {
            var newObj = JSON.parse(JSON.stringify(config));
            for (var i in obj) {
                newObj[i] = obj[i];
            }
            return newObj;
        },
        timer: {}, end: {}, parent:{},node:{}
    };

    //点触事件
    ready.touch = function (elem, fn) {
        elem.addEventListener('tap', function (e) {
            fn.call(this, e);
        }, false);
    };

    var index = 0, classs = ['sk-m-layer'], Layer = function (options) {
        var that = this;
        that.config = ready.extend(options);
        that.view();
    };

    /**
     * 构建layer
     */
    Layer.prototype.view = function () {
        var that = this, config = that.config, layerBox = doc.createElement('div');

        that.id = layerBox.id = classs[0] + index;
        layerBox.setAttribute('class', classs[0] + ' ' + classs[0] + (config.type || 0));
        layerBox.setAttribute('index', index);

        if (!config.fixed) {
            config.top = config.hasOwnProperty('top') ? config.top : 100;
            config.style = config.style || '';
            config.style += ' top:' + ( doc.body.scrollTop + config.top) + 'px';
        }

        if(config.shade){
            var shapeLayer = doc.createElement('div');
            var style=(typeof config.shade === 'string' ? config.shade : '');
            shapeLayer.setAttribute("style",style);
            shapeLayer.setAttribute("class","sk-m-layershade");
            //加入layer
            layerBox.appendChild(shapeLayer);
        }

        var mainDiv = doc.createElement('div');
        mainDiv.setAttribute("class","sk-m-layermain");
        if(!config.fixed){
            mainDiv.setAttribute("style","position:static;");
        }
        layerBox.appendChild(mainDiv);

        var sectionDiv=doc.createElement('div');
        sectionDiv.setAttribute("class","sk-m-layersection");
        mainDiv.appendChild(sectionDiv);

        var wrapDiv=doc.createElement('div');

        //设置class
        var s="sk-m-layerchild";
        if(config.skin){
            s+=(' sk-m-layer-' + config.skin);
        }
        if(config.anim){
            s+=(" sk-m-anim-"+config.anim);
        }
        if(config.className){
            s+=(" "+config.className);
        }
        wrapDiv.setAttribute("class",s);

        //设置class
        if(config.style){
           wrapDiv.setAttribute("style",config.style);
        }
        sectionDiv.appendChild(wrapDiv);

        //自定义弹出框
        if(config.type === 1){
            //创建内容
            var content=this.view4custom();

            if(typeof content === "string") {
                //设置html
                wrapDiv.innerHTML = content;
            }else{
               try{
                 var parentNode=content.parentNode;
                 ready.parent[index]=parentNode;
                 ready.node[index]=content;
                 wrapDiv.appendChild(content);
               }catch(ex){
                   console.logger.error(ex.message,ex);
               }
            }

        }
        //loading
        else if(config.type === 2){
            //创建html内容
            var html=this.view4cover();
            //设置html
            wrapDiv.innerHTML=html;
        }
        //image页面
        else if(config.type === 3){
            //创建内容
            var content=this.view4image();
            wrapDiv.appendChild(content);
        }
        else if(config.type == 4){
            config.shadeClose=false;
            //创建内容
            var content=this.view4progress();
            wrapDiv.appendChild(content);
        }

        //默认类型
        else{
            //创建html内容
            var html=this.view4dialog();
            //设置html
            wrapDiv.innerHTML=html;
        }

        //加入body
        document.body.appendChild(layerBox);
        var elem = that.elem = S('#' + that.id)[0];

        //layer打开时候调用
        try {
            config.success && config.success(elem, index);
        }catch(ex){
            console.logger.error(ex.message,ex);
        }

        that.index = index++;
        that.action(config, elem);

    };

    /**
     * 构建layer for dialog
     */
    Layer.prototype.view4dialog = function () {
        var config = this.config;
        //标题区域
        var title = (function () {
            var tType = typeof config.title === 'object';
            return config.title
                ? '<div class="sk-m-layertitle" style="' + (tType ? config.title[1] : '') + '">' + (tType ? config.title[0] : config.title) + '</div>'
                : '';
        }());

        var empty=config.content === void(0) || config.content.length == 0;

        //按钮区域
        var button = (function () {
            typeof config.btn === 'string' && (config.btn = [config.btn]);
            var btns = (config.btn || []).length, btndom='';
            if (btns === 0 || !config.btn) {
                return '';
            }
            for(var i=0;i<btns;i++){
              if(empty && i == 0){
                  btndom+='<span first type='+i+'>' + config.btn[i] + '</span>';
              }else{
                  btndom+='<span type='+i+'>' + config.btn[i] + '</span>';
              }

            }
            return '<div class="sk-m-layerbtn">' + btndom + '</div>';
        }());

        var content='';
        if(empty){
             content='<div class="sk-m-layercont sk-m-layercont-empty"></div>';
        }else{
             content='<div class="sk-m-layercont">' + config.content + '</div>';
        }

        var html=title+content+button;
        return html;
    };

    /**
     * 构建layer for cover
     */
    Layer.prototype.view4cover = function () {
        var config = this.config;
        var html = '<div class="sk-m-layercont"><i></i><i class="sk-m-layerload"></i><i></i><p>' + (config.content || '') + '</p></div>';
        return html;
    };

    /**
     * 构建layer for custom
     */
    Layer.prototype.view4custom = function () {
        var config = this.config;

        if(typeof config.content === "string"){
            //判断是否为ID
            var firstChar=config.content.charAt(0);
            if(firstChar==='#'){
                var div=$(config.content)[0];
                var node=div.children[0];
                return node;
            }else{
                return config.content;
            }
        }else{
            return config.content;
        }
    };

    /**
     * 构建layer for image
     */
    Layer.prototype.view4image = function () {
        var config = this.config;

        if(typeof config.content === "string"){
            var imgNode=doc.createElement('img');
            imgNode.setAttribute("src",config.content);
            return imgNode;
        }else{
            return config.content;
        }
    };

    /**
     * 构建layer for progress
     */
    Layer.prototype.view4progress = function () {
        var config = this.config;

        var progressDiv = doc.createElement("div");
        var clsName="sk-m-layerprogress sk-m-layer-color-"+config.color;
        progressDiv.setAttribute("class",clsName);

        var progressBarDiv=doc.createElement("div");
        progressBarDiv.id=this.id+"progBar";
        progressBarDiv.setAttribute("class","sk-m-layerprogress-bar");
        progressDiv.appendChild(progressBarDiv);

        var divStripes=doc.createElement("div");
        divStripes.setAttribute("class","sk-m-layerprogress-stripes");
        progressBarDiv.appendChild(divStripes);

        var divPercentage=doc.createElement("div");
        divPercentage.id=this.id+"progPer";
        divPercentage.setAttribute("class","sk-m-layerprogress-percentage");
        progressBarDiv.appendChild(divPercentage);

        var fragment=doc.createDocumentFragment();
        fragment.appendChild(progressDiv);
        if(config.content){
            var spanDiv=doc.createElement("span");
            spanDiv.innerHTML=config.content;
            fragment.appendChild(spanDiv);
        }
        return fragment;
    };

    /**
     * 定义action
     * @param config
     * @param elem
     */
    Layer.prototype.action = function (config, elem) {
        var that = this;

        //自动关闭
        if (config.time) {
            ready.timer[that.index] = setTimeout(function () {
                layer.close(that.index);
            }, config.time * 1000);
        }

        if(config.action === void(0)){
            config.action = [];
            if(config.yes){
              config.action.push(config.yes);
            }
            if(config.no){
              config.action.push(config.node);
            }

        }

        //确认取消
        var btn = function () {
            var type = this.getAttribute('type');
            var typeIndex=parseInt(type);

            config.action[typeIndex]? config.action[typeIndex](that.index):layer.close(that.index);;
            if(typeIndex == config.action.length-1){
               layer.close(that.index);
            }
        };
        if (config.btn) {
            var btns = elem[claname]('sk-m-layerbtn')[0].children, btnlen = btns.length;
            for (var ii = 0; ii < btnlen; ii++) {
                ready.touch(btns[ii], btn);
            }
        }

        //点遮罩关闭
        if (config.shade && config.shadeClose) {
            var shade = elem[claname]('sk-m-layershade')[0];
            ready.touch(shade, function () {
                layer.close(that.index, config.end);
            });
        }

        //记录end方法，close时调用
        config.end && (ready.end[that.index] = config.end);
    };

    /**
     * 自定义view
     */
    Layer.prototype.customView = function () {
        var that = this, config = that.config, layerBox = doc.createElement('div');

        that.id = layerBox.id = classs[0] + index;
        layerBox.setAttribute('class', classs[0] + ' ' + classs[0] + (config.type || 0));
        layerBox.setAttribute('index', index);

        if (!config.fixed) {
            config.top = config.hasOwnProperty('top') ? config.top : 100;
            config.style = config.style || '';
            config.style += ' top:' + ( doc.body.scrollTop + config.top) + 'px';
        }


       if(config.shade){
          var shapeLayer = doc.createElement('div');
          var style=(typeof config.shade === 'string' ? config.shade : '');
          shapeLayer.setAttribute("style",style);
          shapeLayer.setAttribute("class","sk-m-layershade");
          //加入layer
          layerBox.appendChild(shapeLayer);
       }

        var mainDiv = doc.createElement('div');
        mainDiv.setAttribute("class","sk-m-layermain");
        if(!config.fixed){
            mainDiv.setAttribute("style","position:static;");
        }
        layerBox.appendChild(mainDiv);

        var sectionDiv=doc.createElement('div');
        sectionDiv.setAttribute("class","sk-m-layersection");
        mainDiv.appendChild(sectionDiv);

        var wrapDiv=doc.createElement('div');

        var s="sk-m-sk-m-layerwrap";
        if(config.anim){
            s+=(" sk-m-anim-"+config.anim);
        }
        wrapDiv.setAttribute("class",s);
        sectionDiv.appendChild(wrapDiv);

        var child=sk.$(config.content);
        child.isShown();

        var parent=child[0].parentNode;
        wrapDiv.appendChild(child[0]);

        //加入body
        document.body.appendChild(layerBox);

        var elem = that.elem = S('#' + that.id)[0];
        that.index = index++;

        //记录
        ready.children[index]={
            parent:parent,
            child:child[0]
        };
        config.success && config.success(elem,that.index);

    };

    var layer = {
        //默认layer
        Dialog:0,
        //自定义layer
        Custom:1,
        //cover layer
        Cover:2,
        //图片layer
        Image:3,
        //progress layer
        Progress:4,

        v: '1.0',
        index: index,

        /**
         * 打开layer
         * @param options
         * @returns {number|*}
         */
        open: function(options){
            if(typeof options === "string"){
                var content=options;
                options={
                    content: content
                    ,btn: '确定'
                }
            }

            var o = new Layer(options || {});
            return o.index;
        },

        /**
         * 关闭layer
         * @param index
         */
        close: function(index){
            var ibox = S('#'+classs[0]+index)[0];
            if(!ibox) {
                return;
            }

            try{
                if(ready.parent[index] && ready.node[index]){
                    ready.parent[index].appendChild(ready.node[index]);
                }
                delete ready.parent[index];
                delete ready.node[index];
            }catch(ex){
                console.logger.error(ex.message,ex)
            }

            ibox.innerHTML = '';
            doc.body.removeChild(ibox);
            clearTimeout(ready.timer[index]);
            delete ready.timer[index];
            typeof ready.end[index] === 'function' && ready.end[index]();
            delete ready.end[index];
        },

        /**
         * 关闭所有layer层
         */
        closeAll: function(){
            var boxs = doc[claname](classs[0]);
            for(var i = 0, len = boxs.length; i < len; i++){
                layer.close((boxs[0].getAttribute('index')|0));
            }
        },

        /**
         * 更新layer
         * @param index
         */
        update:function(index,param){
            var bar = S('#'+classs[0]+index+"progBar")[0];
            if(!bar) {
                return;
            }
            var text=S('#'+classs[0]+index+"progPer")[0];

            bar.style.width=param+"%";
            text.innerHTML=param+"%";
        }
    };
    return layer;
}));
