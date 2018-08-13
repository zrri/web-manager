define(function (require, exports) {

    var List = [];

    var index = 5;
    Mock.Random.increment(1000)
    for (var i = 0; i < index; i++) {
        List[i] = Mock.mock({
            id: '@increment(2)',
            title: '@ctitle(8, 15)',
            create_at: '@date',
            'status|1': ['published', 'draft', 'deleted'],
            pageviews: '@integer(300, 5000)',
            remark: '@ctitle(15, 100)',
            EVALUATION_PERIOD_ID: '@date',
            'OPEN_FLAG|1': ['O', 'N', 'C'],
            'MEASURE|1': ['已', '未'],
            'LOCK|1': ['已', '未']
        })
    }

    function paramUrl2Obj(url) {
        var search = url.split('?')[1]
        if (!search) {
            return {}
        }
        return JSON.parse('{"' + decodeURIComponent(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"').replace(/\n/g, '\\n') + '"}')
    }

    function paramBody2Obj(body) {
        if (!body) {
            return {}
        }
        return JSON.parse('{"' + decodeURIComponent(body).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"').replace(/\n/g, '\\n') + '"}')
    }

    exports.subList = function(config){
        var reqData = paramUrl2Obj(config.url);
        // var page = reqData.page;
        // var size = reqData.size;
        // var condition = reqData.condition ? JSON.parse(reqData.condition) : {};
        // var create_at = condition.create_at;
        // var type = condition.type;
        // var title = condition.title;
        // var sort = condition.sort;
        var id = reqData.id;

        var filterData = List.filter(function(item, index){
            return item.id == id;
        })

        return {
            data: filterData
        };
    }
});

