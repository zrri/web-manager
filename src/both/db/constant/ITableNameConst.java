/**
 * Special Declaration: These technical material reserved as the technical 
 * secrets by Bankit TECHNOLOGY have been protected by the "Copyright Law" 
 * "ordinances on Protection of Computer Software" and other relevant 
 * administrative regulations and international treaties. Without the written 
 * permission of the Company, no person may use (including but not limited to 
 * the illegal copy, distribute, display, image, upload, and download) and 
 * disclose the above technical documents to any third party. Otherwise, any 
 * infringer shall afford the legal liability to the company.
 *
 * 特别声明：本技术材料受《中华人民共和国著作权法》、《计算机软件保护条例》
 * 等法律、法规、行政规章以及有关国际条约的保护，浙江宇信班克信息技术有限公
 * 司享有知识产权、保留一切权利并视其为技术秘密。未经本公司书面许可，任何人
 * 不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使
 * 用，不得向第三方泄露、透露、披露。否则，本公司将依法追究侵权者的法律责任。
 * 特此声明！
 *
 * Copyright(C) 2012 Bankit Tech, All rights reserved.
 */
package both.db.constant;

/**
 * 全局表名常量
 * 
 */
public interface ITableNameConst {

//	/**
//	 * 页面组件信息存储表
//	 */
//	public static final String FOX_SYSTEM_INFO = "FOX_SYSTEM_INFO";
//
//	
//
//	/**
//	 * 柜员流水表记录表
//	 */
//	public static final String FOX_PAD_SEQ = "FOX_PAD_SEQ";
//
//	/**
//	 * PAD端主业务表
//	 */
//	public static final String FOX_BUSI_MAIN = "FOX_BUSI_MAIN";
//	/**
//	 * PAD端子业务表
//	 */
//	public static final String FOX_BUSI_SUBBUSI = "FOX_BUSI_SUBBUSI";
//	/**
//	 * PAD端子业务请求字段明细表
//	 */
//	public static final String FOX_BUSI_FIELD = "FOX_BUSI_FIELD";
//
//	/**
//	 * PAD端待办业务表
//	 */
//	public static final String FOX_BUSI_WAIT = "FOX_BUSI_WAIT";
//
//	/**
//	 * PAD消息中心信息表
//	 */
//	public static final String FOX_PAD_MESSAGECENTER = "FOX_PAD_MESSAGECENTER";
//
//	/**
//	 * 岗位信息表
//	 */
//	public static final String FOX_TELLER_POSTINFO = "FOX_TELLER_POSTINFO";
//
//	/**
//	 * 菜单关系表
//	 */
//	public static final String FOX_TELLER_TRADEMENUTREE = "FOX_TELLER_TRADEMENUTREE";
//
//	/**
//	 * 交易组别信息
//	 */
//	public static final String FOX_TELLER_TRADEGROUPINFO = "FOX_TELLER_TRADEGROUPINFO";
//
//	/**
//	 * 授权表
//	 */
//	public static final String BTOP_AUTH_AUTHINFO = "BTOP_AUTH_AUTHINFO";
//	public static final String BTOP_AUTH_AUTHCENTRE = "BTOP_AUTH_AUTHCENTRE";
//	public static final String BTOP_AUTH_CHECK = "BTOP_AUTH_CHECK";
//	public static final String BTOP_AUTH_PARAMETER = "BTOP_AUTH_PARAMETER";
//	public static final String BTOP_AUTH_RULE = "BTOP_AUTH_RULE";
//	public static final String BTOP_AUTH_STATISTICS = "BTOP_AUTH_STATISTICS";
//	public static final String BTOP_AUTH_AUTHLOG = "BTOP_AUTH_AUTHLOG"; // 已授权登录本
//	public static final String BTOP_AUTH_AUTHCENTER = "BTOP_AUTH_AUTHCENTER"; // 授权中心配置
//	public static final String BTOP_AUTH_AUTHCENTERTELLER = "BTOP_AUTH_AUTHCENTERTELLER"; // 授权中心柜员
//	public static final String BTOP_AUTH_AUTHCENTERORG = "BTOP_AUTH_AUTHCENTERORG"; // 授权机构
//
//	public static final String BTOP_CHARGE_PARAMETER = "BTOP_CHARGE_PARAMETER";
//	public static final String BTOP_CHARGE_RULE = "BTOP_CHARGE_RULE";
//
//	/**
//	 * 复核表
//	 */
//	public static final String BTOP_TRADEREVIEW_RULE = "BTOP_TRADEREVIEW_RULE"; // 复核规则表
//	public static final String BTOP_TRADEREVIEW_PARAMETER = "BTOP_TRADEREVIEW_PARAMETER"; // 复核参数表
//	public static final String BTOP_TRADEREVIEW_FIELDS = "BTOP_TRADEREVIEW_FIELDS"; // 复核字段表
//
//	/**
//	 * 交易任务表
//	 */
//	public static final String BTOP_TASKINFO_TRADE = "BTOP_TASKINFO_TRADE"; // 交易任务表
//	/**
//	 * 交易数据
//	 */
//	public static final String BTOP_TRADE_TRADEDATA = "BTOP_TRADE_TRADEDATA"; // 交易任务表
//
//	/**
//	 * 交易数据大表
//	 */
//	public static final String BTOP_TRADE_TRADEBIGDATA = "BTOP_TRADE_TRADEBIGDATA"; // 交易任务表
//
//	/**
//	 * 交易日志
//	 */
//	public static final String BTOP_TRADE_TRADELOG = "BTOP_TRADE_TRADELOG"; //
//
//	/**
//	 * 通迅日志
//	 */
//	public static final String BTOP_TRADE_TRADEDTL = "BTOP_TRADE_TRADEDTL"; //
//
//	/**
//	 * 
//	 */
//	public static final String BTOP_TASK_AUTHREQ = "BTOP_TASK_AUTHREQ";
//
//	/**
//	 * 统一规则配置表
//	 */
//	public static final String BTOP_CONF_CONDITION = "BTOP_CONF_CONDITION";// 统一配置-条件表
//	public static final String BTOP_CONF_CONFMAPPING = "BTOP_CONF_CONFMAPPING";// 统一配置-业务类型映射表
//	public static final String BTOP_CONF_FIELDMAPPING = "BTOP_CONF_FIELDMAPPING";// 统一配置-字段映射表
//	public static final String BTOP_CONF_PARAMETER = "BTOP_CONF_PARAMETER";// 统一配置-交易配置表
//	public static final String BTOP_CONF_RULE = "BTOP_CONF_RULE";// 统一配置-规则表
//	public static final String BTOP_CONF_RULEMAPPING = "BTOP_CONF_RULEMAPPING";// 统一配置-规则交易映射表
//	public static final String BTOP_CONF_VALUE = "BTOP_CONF_VALUE";// 统一配置-基准值表
//
//	/**
//	 * 互斥表
//	 */
//	public static final String BTOP_MUTEX_ALLOCATIONOFVOUCHER = "BTOP_MUTEX_ALLOCATIONOFVOUCHER";
//	public static final String BTOP_MUTEX_ELEMENT = "BTOP_MUTEX_ELEMENT";
//	public static final String BTOP_MUTEX_ITEMS = "BTOP_MUTEX_ITEMS";
//	public static final String BTOP_MUTEX_MUTEXMENU = "BTOP_MUTEX_MUTEXMENU";
//	public static final String BTOP_MUTEX_RULE = "BTOP_MUTEX_RULE";
//	public static final String BTOP_MUTEX_RULEDETAIL = "BTOP_MUTEX_RULEDETAIL";
//	public static final String BTOP_MUTEX_TELLERITEMS = "BTOP_MUTEX_TELLERITEMS";
//	public static final String BTOP_MUTEX_TELLERVOUCHER = "BTOP_MUTEX_TELLERVOUCHER";
//	public static final String BTOP_MUTEX_VOUCHER = "BTOP_MUTEX_VOUCHER";
//
//	/**
//	 * 报表打印表
//	 */
//	public static final String BTOP_PRINT_CLIENTINFO = "BTOP_PRINT_CLIENTINFO";
//	public static final String BTOP_PRINT_DATASOURCE = "BTOP_PRINT_DATASOURCE";
//	public static final String BTOP_PRINT_ONLINECLIENT = "BTOP_PRINT_ONLINECLIENT";
//	public static final String BTOP_PRINT_PRINTDATA = "BTOP_PRINT_PRINTDATA";
//	public static final String BTOP_PRINT_PRINTERINFO = "BTOP_PRINT_PRINTERINFO";
//	public static final String BTOP_PRINT_PRINTTASK = "BTOP_PRINT_PRINTTASK";
//	public static final String BTOP_PRINT_RECORDER = "BTOP_PRINT_RECORDER";
//	public static final String BTOP_PRINT_PARAMETERS = "BTOP_PRINT_PARAMETERS";
//	public static final String BTOP_PRINT_TASKRECORDER = "BTOP_PRINT_TASKRECORDER";
//
//	/**
//	 * 公共表（交易流水、日志）
//	 */
//
//	public static final String BTOP_PUB_TRANSDTL_HIST = "BTOP_PUB_TRANSDTL_HIST";// 交易明细流水历史表
//	public static final String BTOP_PUB_PARAMUPDATELOG = "BTOP_PUB_PARAMUPDATELOG";
//	public static final String BTOP_PUB_PRINTDATATEMPLATE = "BTOP_PUB_PRINTDATATEMPLATE";// 打印数据模板表
//	public static final String BTOP_PUB_PRINTINFO = "BTOP_PUB_PRINTINFO";
//	public static final String BTOP_PUB_TRANSCONTEXT = "BTOP_PUB_TRANSCONTEXT";// 交易数据流水表
//	public static final String BTOP_PUB_TRANSCONTEXT_HIST = "BTOP_PUB_TRANSCONTEXT_HIST";// 交易数据流水表
//	public static final String BTOP_PUB_TRANSDTL = "BTOP_PUB_TRANSDTL";// 交易明细流水表
//	public static final String BTOP_PUB_TRANSRUNTIMELOG = "BTOP_PUB_TRANSRUNTIMELOG";// 交易运行日志表
//
//	/**
//	 * 排队机表
//	 */
//	public static final String BTOP_QUEUE_COUNTER = "BTOP_QUEUE_COUNTER";
//	public static final String BTOP_QUEUE_COUNTERRULE = "BTOP_QUEUE_COUNTERRULE";
//	public static final String BTOP_QUEUE_COUNTERRULEDETAIL = "BTOP_QUEUE_COUNTERRULEDETAIL";
//	public static final String BTOP_QUEUE_COUNTERSERIAL = "BTOP_QUEUE_COUNTERSERIAL";
//	public static final String BTOP_QUEUE_CUSTOMERTYPE = "BTOP_QUEUE_CUSTOMERTYPE";
//	public static final String BTOP_QUEUE_DEVICE = "BTOP_QUEUE_DEVICE";
//	public static final String BTOP_QUEUE_HOLIDAY = "BTOP_QUEUE_HOLIDAY";
//	public static final String BTOP_QUEUE_MENU = "BTOP_QUEUE_MENU";
//	public static final String BTOP_QUEUE_MENUTEMPLATEDETAIL = "BTOP_QUEUE_MENUTEMPLATEDETAIL";
//	public static final String BTOP_QUEUE_PRIORITYPARAM = "BTOP_QUEUE_PRIORITYPARAM";
//	public static final String BTOP_QUEUE_PRIORITYSERVICE = "BTOP_QUEUE_PRIORITYSERVICE";
//	public static final String BTOP_QUEUE_RULEMENU = "BTOP_QUEUE_RULEMENU";
//	public static final String BTOP_QUEUE_TEMPCOUNTERRULE = "BTOP_QUEUE_TEMPCOUNTERRULE";
//	public static final String BTOP_QUEUE_TEMPLATE = "BTOP_QUEUE_TEMPLATE";
//	public static final String BTOP_QUEUE_TICKET = "BTOP_QUEUE_TICKET";
//	public static final String BTOP_QUEUE_TICKETSERIAL = "BTOP_QUEUE_TICKETSERIAL";
//
//	/**
//	 * 电子登记薄表
//	 */
//	public static final String BTOP_REGISTER_AUTHCONFIG = "BTOP_REGISTER_AUTHCONFIG";
//	public static final String BTOP_REGISTER_CHECKDATARECORD = "BTOP_REGISTER_CHECKDATARECORD";
//	public static final String BTOP_REGISTER_EREGISTER = "BTOP_REGISTER_EREGISTER";
//	public static final String BTOP_REGISTER_GENERALHANDOVER = "BTOP_REGISTER_GENERALHANDOVER";
//	public static final String BTOP_REGISTER_GENERALSIGNIN = "BTOP_REGISTER_GENERALSIGNIN";
//	public static final String BTOP_REGISTER_ACCOUNTANTLOG = "BTOP_REGISTER_ACCOUNTANTLOG";
//	public static final String BTOP_REGISTERITEMS = "BTOP_REGISTERITEMS";
//
//	/**
//	 * 预约表
//	 */
//	public static final String BTOP_RESERVATION_BLACKLIST = "BTOP_RESERVATION_BLACKLIST";
//	public static final String BTOP_RESERVATION_CONFIGURTION = "BTOP_RESERVATION_CONFIGURTION";
//	public static final String BTOP_RESERVATION_INFO = "BTOP_RESERVATION_INFO";
//	public static final String BTOP_RESERVATION_JMALICION = "BTOP_RESERVATION_JMALICION";
//	public static final String BTOP_RESERVATION_MALICIOUS = "BTOP_RESERVATION_MALICIOUS";
//	public static final String BTOP_RESERVATION_MENU = "BTOP_RESERVATION_MENU";
//	public static final String BTOP_RESERVATION_MENUENTRY = "BTOP_RESERVATION_MENUENTRY";
//	public static final String BTOP_RESERVATION_MENURELATIONS = "BTOP_RESERVATION_MENURELATIONS";
//	public static final String BTOP_RESERVATION_RELATION = "BTOP_RESERVATION_RELATION";
//	public static final String BTOP_RESERVATION_WORKHOLIDAY = "BTOP_RESERVATION_WORKHOLIDAY";
//
//	/**
//	 * SEQUENCE表
//	 */
//	public static final String BTOP_SEQ_LEGALPAPERID = "BTOP_SEQ_LEGALPAPERID";
//	public static final String BTOP_SEQ_QUEUE_SMS = "BTOP_SEQ_QUEUE_SMS";
//	public static final String BTOP_SEQ_REGISTER = "BTOP_SEQ_REGISTER";
//	public static final String BTOP_SEQ_RESERVATIONC = "BTOP_SEQ_RESERVATIONC";
//	public static final String BTOP_SEQ_SERIALNO = "BTOP_SEQ_SERIALNO";
//
//	/**
//	 * 柜员信息表
//	 */
//	public static final String BTOP_TELLER_ACCESSAUTH = "BTOP_TELLER_ACCESSAUTH";// 数据权限表
//	public static final String BTOP_TELLER_ACCESSORGMAPPING = "BTOP_TELLER_ACCESSORGMAPPING";// 数据权限表
//	public static final String BTOP_TELLER_APPINFO = "BTOP_TELLER_APPINFO";// 交易应用组表
//																			// 目前分，对公，对私，公用
//	public static final String BTOP_TELLER_CUSTOMPRIVILEGE = "BTOP_TELLER_CUSTOMPRIVILEGE";// 特权信息表
//	public static final String BTOP_TELLER_MENUTREE = "BTOP_TELLER_MENUTREE";
//	public static final String BTOP_TELLER_MENUTREE_EXT = "BTOP_TELLER_MENUTREE_EXT";
//	public static final String BTOP_TELLER_MFORGINFO = "BTOP_TELLER_MFORGINFO";
//	public static final String BTOP_TELLER_MFORGTELLER = "BTOP_TELLER_MFORGTELLER";
//	public static final String BTOP_TELLER_MFTELLERINFO = "BTOP_TELLER_MFTELLERINFO";
//	public static final String BTOP_TELLER_MFTRADEPARAMETER = "BTOP_TELLER_MFTRADEPARAMETER";
//	public static final String BTOP_TELLER_ONLINETELLER = "BTOP_TELLER_ONLINETELLER";
//	public static final String BTOP_TELLER_ORGINFO = "BTOP_TELLER_ORGINFO";// 柜员机构信息定义表
//	public static final String BTOP_TELLER_ORGPRIVILEGE = "BTOP_TELLER_ORGPRIVILEGE";
//	public static final String BTOP_TELLER_POSTINFO = "BTOP_TELLER_POSTINFO";// 柜员岗位信息定义表
//	public static final String BTOP_TELLER_POSTPRIVILEGE = "BTOP_TELLER_POSTPRIVILEGE";
//	public static final String BTOP_TELLER_POSTROLEMAPPING = "BTOP_TELLER_POSTROLEMAPPING";// 岗位角色关联定义表
//	public static final String BTOP_TELLER_ROLEINFO = "BTOP_TELLER_ROLEINFO";// 角色定义表
//	public static final String BTOP_TELLER_ROLETRADEMAPPING = "BTOP_TELLER_ROLETRADEMAPPING";// 角色交易组关联定义表
//	public static final String BTOP_TELLER_SUBSTITUTEINFO = "BTOP_TELLER_SUBSTITUTEINFO";
//	public static final String BTOP_TELLER_TELLER = "BTOP_TELLER_TELLER";
//	public static final String BTOP_TELLER_TELLERINFO = "BTOP_TELLER_TELLERINFO";// 柜员信息定义表
//	public static final String BTOP_TELLER_TELLERLOGINLOG = "BTOP_TELLER_TELLERLOGINLOG";
//	public static final String BTOP_TELLER_TELLERROLEMAPPING = "BTOP_TELLER_TELLERROLEMAPPING"; // 柜员与角色映射表
//	public static final String BTOP_TELLER_TELLERPOSTMAPPING = "BTOP_TELLER_TELLERPOSTMAPPING"; // 柜员与岗位映射表
//	public static final String BTOP_TELLER_TELLERPRIVILEGE = "BTOP_TELLER_TELLERPRIVILEGE";
//	public static final String BTOP_TELLER_TRADECATEGORYINFO = "BTOP_TELLER_TRADECATEGORYINFO";
//	public static final String BTOP_TELLER_CATEGORYTRADEMAPPING = "BTOP_TELLER_CATETRADEMAPPING";
//	public static final String BTOP_TELLER_TRADECODE = "BTOP_TELLER_TRADECODE";// 交易信息定义表
//	public static final String BTOP_TELLER_TRADEGROUP = "BTOP_TELLER_TRADEGROUP";// 交易组定义表
//	public static final String BTOP_TELLER_TRADEPARAMETER = "BTOP_TELLER_TRADEPARAMETER";
//	public static final String BTOP_TELLER_VACATIONINFO = "BTOP_TELLER_VACATIONINFO";
//	public static final String BTOP_TELLERHELP_TRADE = "BTOP_TELLERHELP_TRADE";
//	public static final String BTOP_TELLER_ORGCCYCONFIG = "BTOP_TELLER_ORGCCYCONFIG";// 机构币种配置
//
//	public static final String BTOP_PUB_PARAMEXPAND = "BTOP_PUB_PARAMEXPAND"; // 扩展参数表
//	public static final String BTOP_PUB_PARAMMODULE = "BTOP_PUB_PARAMMODULE"; // 产品参数模块配置表
//	/**
//	 * 交易事件-交易事件定义表
//	 */
//	public static final String BTOP_TRADEEVENT_EVENTINFO = "BTOP_TRADEEVENT_EVENTINFO";
//	public static final String BTOP_TRADEEVENT_MONITOR = "BTOP_TRADEEVENT_MONITOR";
//	public static final String BTOP_TRADEEVENT_MONITORINFO = "BTOP_TRADEEVENT_MONITORINFO";
//	public static final String BTOP_TRADEEVENT_NOTIFYINFO = "BTOP_TRADEEVENT_NOTIFYINFO";
//	public static final String BTOP_TRADEEVENT_NOTIFYMONITOR = "BTOP_TRADEEVENT_NOTIFYMONITOR";
//	public static final String BTOP_TRADEEVENT_PARAMETER = "BTOP_TRADEEVENT_PARAMETER";
//	public static final String BTOP_TRADEEVENT_RULE = "BTOP_TRADEEVENT_RULE";
//
//	public static final String BTOP_VERIFYIDENTITY_PARAMETER = "BTOP_VERIFYIDENTITY_PARAMETER";
//
//	/**
//	 * 统一回单打印
//	 */
//	public static final String BTOP_TRADE_UNIONPRINT = "BTOP_TRADE_UNIONPRINT";//
//
//	/**
//	 * 联网核查表
//	 */
//	public static final String BTOP_PUB_VERIFY = "BTOP_PUB_VERIFY";// 联网核查表
//	/**
//	 * 阅知材料配置表
//	 */
//	public static final String BTOP_USG_LEGALPAPER = "BTOP_USG_LEGALPAPER";
//
//	/**
//	 * 综合签约配置表
//	 */
//	public static final String BTOP_USG_PARAMETERS = "BTOP_USG_PARAMETERS";
//
//	/**
//	 * 综合签约产品配置表
//	 */
//	public static final String BTOP_USG_PRODUCT = "BTOP_USG_PRODUCT";
//	/**
//	 *   
//	 */
//	public static final String BTOP_USG_VOUTOPRO = "BTOP_USG_VOUTOPRO";
//
//	/**
//	 * 第三方存管系统券商列表
//	 */
//	public static final String BTOP_USG_STOCKBROKER = "BTOP_USG_STOCKBROKER";
//
//	/**
//	 * 综合签约交易配置表
//	 */
//	public static final String BTOP_USG_TRANS = "BTOP_USG_TRANS";
//
//	/**
//	 * 综合签约交易流水明细表
//	 */
//	public static final String BTOP_USG_TRANSDTL = "BTOP_USG_TRANSDTL";
//
//	/**
//	 * 综合签约交易流水明细历史表
//	 */
//	public static final String BTOP_USG_TRANSDTL_HIST = "BTOP_USG_TRANSDTL_HIST";
//
//	/**
//	 * 桌面快捷键交易配置
//	 */
//	public static final String BTOP_APP_SHORTCUT = "BTOP_APP_SHORTCUT";
//
//	/**
//	 * 单点登录信息配置
//	 */
//	public static final String BTOP_SSO_BINSYS = "BTOP_SSO_BINSYS";
//
//	/**
//	 * 单点登录账户信息记录表
//	 */
//	public static final String BTOP_SSO_USERINFO = "BTOP_SSO_USERINFO";
//
//	/**
//	 * 取消使用 单点登录登录信息配置
//	 */
//	// 取消使用
//	public static final String BTOP_SSO_TELSIN = "BTOP_SSO_TELSIN";
//
//	/**
//	 * 单点登录登录信息配置
//	 */
//	// 取消使用
//	public static final String BTOP_SSO_TELMAP = "BTOP_SSO_TELMAP";
//
//	/**
//	 * 柜员业务日程
//	 */
//	public static final String BTOP_APP_CALPLA = "BTOP_APP_CALPLA";
//
//	/**
//	 * 消息公告
//	 */
//	public static final String BTOP_BROADCAST_MESSAGE = "BTOP_BROADCAST_MESSAGE";
//
//	/**
//	 * 消息公告机构
//	 */
//	public static final String BTOP_BROADCAST_MESSAGE_ORG = "BTOP_BROADCAST_MESSAGE_ORG";
//
//	/**
//	 * 定时任务记录表
//	 */
//	public static final String BTOP_TIMERTASK_RECORD = "BTOP_TIMERTASK_RECORD";
//
//	/**
//	 * 柜员流水表记录表
//	 */
//	public static final String BTOP_TRADE_SEQ = "BTOP_TRADE_SEQ";
//
//	/**
//	 * 配钞小助手，币种与券别
//	 */
//	public static final String BTOP_APP_CASHCCYCUR = "BTOP_APP_CASHCCYCUR";
//	/**
//	 * 交易任务 主表
//	 */
//	public static final String BTOP_TASK_TASKINFO = "BTOP_TASK_TASKINFO";
//	/**
//	 * 交易任务 复核任务 表
//	 */
//	public static final String BTOP_TASK_REVIEWINFO = "BTOP_TASK_REVIEWINFO";
//
//	/**
//	 * 交易任务授权任务 表
//	 */
//	public static final String BTOP_TASK_AUTHINFO = "BTOP_TASK_AUTHINFO";
//
//	/**
//	 * 民泰银行相关表
//	 */
//	public static final String MT_TELLER_ORGINFO = "MT_TELLER_ORGINFO";// 核心机构信息表
//	public static final String MT_TELLER_ORGINFO_ADD = "MT_TELLER_ORGINFO_ADD";// 核心机构信息辅表
//	public static final String MT_TELLER_TELLERINFO = "MT_TELLER_TELLERINFO";// 核心柜员信息表
//	public static final String MT_TELLER_TELLERINFO_ADD = "MT_TELLER_TELLERINFO_ADD";// 核心柜员信息辅表
//	public static final String MT_TELLER_ROLEINFO = "MT_TELLER_ROLEINFO";// 核心角色信息表
//	public static final String MT_TELLER_TELLERROLEMAPPING = "MT_TELLER_TELLERROLEMAPPING";// 核心柜员与角色映射表
//	public static final String MT_TELLER_ROLEACATEGORYMAPPING = "MT_TELLER_ROLEACATEGORYMAPPING";// 核心角色与角色组关系表
//
//	public static final String MT_TELLER_BOXAMTCONFIG = "MT_TELLER_BOXAMTCONFIG";// 民泰尾箱.
//
//	public static final String BTOP_AUTH_TRADECATEGORYINFO = "BTOP_AUTH_TRADECATEGORYINFO";// 授权交易组别\
//
//	public static final String BTOP_AUTH_CATETRADEMAPPING = "BTOP_AUTH_CATETRADEMAPPING";// 授权交易与交易组关系
//	// 授权-权重表
//	public static final String BTOP_AUTH_PARAMETERWEIGHT = "BTOP_AUTH_PARAMETERWEIGHT";
//	// 授权-常用短语
//	public static final String BTOP_CONF_COMMONPHRASE = "BTOP_CONF_COMMONPHRASE";
//	// 授权-参数表
//	public static final String BTOP_AUTH_CENTERPARAMETER = "BTOP_AUTH_CENTERPARAMETER";
//	// 授权-任务权重
//	public static final String BTOP_TASKINFO_AUTH = "BTOP_TASKINFO_AUTHEX";
//	// 授权-日期
//	public static final String BTOP_PUB_DATEINFO = "BTOP_PUB_DATEINFO";
//	// 授权-IP地址
//	public static final String BTOP_AUTH_IPADDRESS = "BTOP_AUTH_IPADDRESS";
//	// 授权-授权模式
//	public static final String BTOP_AUTH_DATEAUTHMODEL = "BTOP_AUTH_DATEAUTHMODEL";
//	// 授权-授权互斥柜员
//	public static final String BTOP_TELLER_MUTEXINFO = "BTOP_TELLER_MUTEXINFO";
//
//	public static final String BTOP_TELLER_AUTH_PRIVILEGE = "BTOP_TELLER_AUTH_PRIVILEGE";
//
//	public static final String BTOP_TELLER_AUTH_CATEGORY = "BTOP_TELLER_AUTH_CATEGORY";
//
//	public static final String BTOP_TELLER_AUTH_TELLER_INFO = "BTOP_TELLER_AUTH_TELLER_INFO";
//
//	public static final String BTOP_AUTH_TRADEPARAMETER = "BTOP_AUTH_TRADEPARAMETER";
//	// public static final String BTOP_AUTH_AUTHTASKLOG =
//	// "BTOP_AUTH_AUTHTASKLOG";
//
//	public static final String BTOP_AUTH_REVIEW_FIELDS = "BTOP_AUTH_REVIEW_FIELDS";
//	public static final String BTOP_AUTH_PROXYPARAMTER = "BTOP_AUTH_PROXYPARAMTER";
//	public static final String BTOP_TASK_WEIGHT = "PARA_WEIGTH";
//
//	/**
//	 * 监督错误码表
//	 */
//	public static final String BTOP_SUP_ERROR = "BTOP_SUP_ERROR";
//
//	/**
//	 * 监督错误日志表
//	 */
//	public static final String BTOP_SUP_ERRLOG = "BTOP_SUP_ERRLOG";
//
//	/**
//	 * 监督流水表
//	 */
//	public static final String BTOP_SUP_TRANSDTL = "BTOP_SUP_TRANSDTL";
//
//	/**
//	 * 监督参数配置表
//	 */
//	public static final String BTOP_SUP_TRXFACEINFO = "BTOP_SUP_TRXFACEINFO";
//
//	/**
//	 * 异常登记簿
//	 */
//	public static final String BTOP_ERROR_BOOK_MULTIEXCHANGE = "BTOP_ERROR_BOOK_MULTIEXCHANGE";
//
//	/**
//	 * 登录柜员会话信息表
//	 */
//	public static final String BTOP_TELLER_SESSION = "BTOP_TELLER_SESSION";
//
//	/**
//	 * PAD模块区是否校验用户信息和客户信息配置表
//	 */
//	public static final String BTOP_TELLER_SESSION_CHECK = "BTOP_TELLER_SESSION_CHECK";
//	/**
//	 * 调查报告数据表
//	 */
//	public static final String T_RESEARCHREPORT = "T_RESEARCHREPORT";
//
//	/**
//	 * 机构实体
//	 */
//	public static final String FOX_TELLER_ORGINFO = "FOX_TELLER_ORGINFO";
//
//	public static final String FOX_PAD_CREDITCARD = "FOX_PAD_CREDITCARD";
//
//	public static final String FOX_TELLER_ROLEINFO = "FOX_TELLER_ROLEINFO";
//
//	public static final String FOX_TELLER_ROLEASSIGNLOG = "FOX_TELLER_ROLEASSIGNLOG";
//
//	public static final String FOX_TELLER_AUTH = "FOX_TELLER_AUTH";
//
//	public static final String FOX_TELLER_AUTHMAPPING = "FOX_TELLER_AUTHMAPPING";
//
//	public static final String FOX_TELLER_TELLERROLEMAPPING = "FOX_TELLER_TELLERROLEMAPPING";
//
//	public static final String FOX_PAD_LOGINLOG = "FOX_PAD_LOGINLOG";
//
	public static final String FOX_TELLER_SESSION = "FOX_TELLER_SESSION";
//
//	public static final String FOX_TELLER_TELLERINFO = "FOX_TELLER_TELLERINFO";// 用户基础信息表
//
//	public static final String FOX_TELLER_TELLEREXTENDINFO = "FOX_TELLER_TELLEREXTENDINFO";// 用户扩展信息表
//
//	public static final String FOX_TELLER_IMAGE = "FOX_TELLER_IMAGE";
//	/**
//	 * 影像流水关系表
//	 */
//	public static final String IMAGE_WATER_RELATIONS = "IMAGE_WATER_RELATIONS";
//
//	/**
//	 * 令牌信息表
//	 */
//	public static final String FOX_BUSI_TOKEN = "FOX_BUSI_TOKEN";
//
//	/**
//	 * 子女信息表
//	 */
//	public static final String FOX_PAD_CHILDINFO = "FOX_PAD_CHILDINFO";
//
//	/**
//	 * 住房信息表
//	 */
//	public static final String FOX_PAD_HOUSEINFO = "FOX_PAD_HOUSEINFO";
//	/**
//	 * 标准化文本基本信息表
//	 */
//	public static final String FOX_PAD_STANDARIZEDTEXT = "FOX_PAD_STANDARIZEDTEXT";
//	/**
//	 * 二维码相关信息
//	 */
//	public static final String FOX_PAD_QRCODE = "FOX_PAD_QRCODE";
//	/**
//	 * 房贷调查报告 FOX_PAD_INVESTREPORT
//	 */
//	public static final String FOX_PAD_INVESTREPORT = "FOX_PAD_INVESTREPORT";
//
//	/**
//	 * 短信验证码登记表
//	 */
//	public static final String FOX_PAD_SMS = "FOX_PAD_SMS";
//	/**
//	 * 帮助与反馈表
//	 */
//	public static final String FOX_PAD_RSPNEED = "FOX_PAD_RSPNEED";
//
//	/**
//	 * 征信授权详情表
//	 */
//	public static final String T_CREDITAUTH_DETAIL = "T_CREDITAUTH_DETAIL";
//
//	/**
//	 * 征信授权基本表
//	 */
//	public static final String T_CREDITAUTH_BASIC = "T_CREDITAUTH_BASIC";
//	/**
//	 * 业务完成状态表
//	 */
//	public static final String FOX_PAD_STEPOPTIONS = "FOX_PAD_STEPOPTIONS";
//	/**
//	 * 码表
//	 */
//	public static final String CODE_LIBRARY = "CODE_LIBRARY";
//	/**
//	 * 首页产品展示表
//	 */
//	public static final String FOX_PAD_PRODUCTSHOW = "FOX_PAD_PRODUCTSHOW";
//
//	/**
//	 * 单独版征信授权
//	 */
//	public static final String T_CREDITAUTH_ALONE = "T_CREDITAUTH_ALONE";
//
//	public static final String T_RESEARCH_IMAGE = "T_RESEARCH_IMAGE";
//
//	public static final String FOX_TELLER_MODULE = "FOX_TELLER_MODULE"; // 功能模块定义

	//-----------------管理端表名常量--------------------------------
	public static final String FOX_MGR_AUTH_ORGINFO = "FOX_MGR_AUTH_ORGINFO";	//机构信息基础表
	public static final String FOX_MGR_AUTH_TELLERINFO = "FOX_MGR_AUTH_TELLERINFO"; // 用户基础信息表
	public static final String FOX_MGR_AUTH_TELLERROLEMAPPING = "FOX_MGR_AUTH_TELLERROLEMAPPING"; // 用户与角色映射关系表
	public static final String FOX_MGR_AUTH_ROLEINFO = "FOX_MGR_AUTH_ROLEINFO"; // 角色信息表
	public static final String FOX_MGR_AUTH_FUNCGROUPINFO = "FOX_MGR_AUTH_FUNCGROUPINFO"; // 功能组信息表
	public static final String FOX_MGR_AUTH_FUNCMENUTREE = "FOX_MGR_AUTH_FUNCMENUTREE"; // 功能菜单树信息表
	public static final String FOX_MGR_AUTH_FUNC = "FOX_MGR_AUTH_FUNC"; // 功能定义表
	public static final String FOX_MGR_AUTH_ROLEFUNCMAPPING = "FOX_MGR_AUTH_ROLEFUNCMAPPING"; // 角色与功能映射表
	public static final String FOX_MGR_AUTH_GROUPFUNCMAPPING = "FOX_MGR_AUTH_GROUPFUNCMAPPING"; // 功能组与功能映射关系表
	public static final String FOX_MGR_AUTH_TELLEREXTENDINFO = "FOX_MGR_AUTH_TELLEREXTENDINFO"; // 用户扩展信息表
	public static final String FOX_MGR_AUTH_ONLINETELLER = "FOX_MGR_AUTH_ONLINETELLER"; // 用户在线表
	
	
	//------------------PAD端表名常量------------------------------------------
	public static final String FOX_PAD_TELLER_ORGINFO = "FOX_PAD_TELLER_ORGINFO";//机构信息表
	public static final String FOX_PAD_FUNCTION = "FOX_PAD_FUNCTION";//功能信息表
	public static final String FOX_PAD_TELLER_ROLEINFO = "FOX_PAD_TELLER_ROLEINFO";//角色信息表
	public static final String FOX_PAD_TELLER_ROLEFUNC = "FOX_PAD_TELLER_ROLEFUNC";//功能角色映射表
	public static final String FOX_PAD_TELLER_TELLERROLE = "FOX_PAD_TELLER_TELLERROLE";//员工角色映射表信息表
	public static final String FOX_PAD_TELLER_TELLERINFO = "FOX_PAD_TELLER_TELLERINFO";//员工信息表
	
	
	
	
	
	
	
	
	
	
	//-----------------定時任务表名常量------------------------------
	public static final String JOB_CONFIG = "JOB_CONFIG";
	public static final String JOB_EXECUTE_LOG = "JOB_EXECUTE_LOG";
}
