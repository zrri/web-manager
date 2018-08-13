package both.commoncontext;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.com.bankit.phoenix.context.Context;
import cn.com.bankit.phoenix.context.ContextField;
import cn.com.bankit.phoenix.context.ContextMetadata;
import cn.com.bankit.phoenix.context.IContext;
import cn.com.bankit.phoenix.context.IContext.SynchronizePolicy;
import cn.com.bankit.phoenix.context.annotation.Metadata;
import cn.com.bankit.phoenix.context.annotation.MetadataGroup;
import cn.com.bankit.phoenix.context.bus.ContextBus;
import cn.com.bankit.phoenix.context.exception.ContextFieldNotExistException;
import cn.com.bankit.phoenix.context.util.StaticContextMetadataGenerator;

/**
 * TellerInfo
 * @author 浙江宇信班克信息技术有限公司
 * @version 1.0.0 2012-2-22 上午9:15:56
 */
public final class TellerInfo {
	/**
     * 上下文名称
     */
    private static final String NAME = "TellerInfo";

    /**
     * 同步锁
     */
    private static Lock lock = new ReentrantLock();
    
    /**
     * 查找上下文实例
     */
    private static IContext lookupContext(String url) {
            // 获取context bus
            ContextBus contextBus = ContextBus.getInstance();
            // 查找上下文
            IContext context = contextBus.lookup(NAME);
            if (context == null) {
                    lock.lock();
                    try {
                            context = contextBus.lookup(NAME);
                            if (context == null) {
                                    // 定义公共上下文的id和同步方式
                                    context = new Context(NAME, constructMetadata(), constructFields(), SynchronizePolicy.SYNCHONIZ);
                                    // 装载上下文
                                    contextBus.mount(context);
                            }
                    } finally {
                            lock.unlock();
                    }
            }
            return context;
    }

	/**
	 * 构造公共上下文field
	 * 
	 * @return
	 */
	private static ContextField[] constructFields()
	{
		ContextField[] contextFields = StaticContextMetadataGenerator.getFieldsFromAnnotations(TellerInfo.class);
		return contextFields;
	}
	
	private static List<ContextMetadata> constructMetadata() {
		List<ContextMetadata> metadata = StaticContextMetadataGenerator.getMetadataFromAnnotations(TellerInfo.class);
		return metadata;
	}
    
	/**
	 * 重置所有的字段值
	 */
	public static void reset() {
		// 查找上下文
		IContext context = lookupContext(NAME);
		// 获取field集合
		java.util.Map<String, ContextField> fieldMap = context.getFields();
		// 循环清空field value
		for (String key : fieldMap.keySet()) {
			try {
				// 获取context field
				ContextField contextField = fieldMap.get(key);
				// 获取默认值
				Object defaultValue = contextField.getDefaultValue();
				// 重置默认值
				context.setFieldValue(key, defaultValue);
			} catch (ContextFieldNotExistException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 清除所有的字段值
	 * 
	 * @deprecated 使用clearAll
	 */
	public static void ClearAll() {
		clearAll();
	}
	
	/**
     * 清除所有的字段值
     */
    public static void clearAll()
    {
		// 查找上下文
		IContext context = lookupContext(NAME);
		// 获取field集合
		java.util.Map<String, ContextField> fieldMap = context.getFields();
		// 循环清空field value
		for (String key : fieldMap.keySet()) {
			try {
				// 清空field value
				context.setFieldValue(key, null);
			} catch (ContextFieldNotExistException e) {
				throw new RuntimeException(e);
			}
		}	
    }

	
	/**
	 * 获取柜员号
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "柜员号"),
    })
    public static java.lang.String get_TellerId()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("TellerId");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置柜员号
	 */
    public static void set_TellerId(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("TellerId", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取柜员机构号
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "柜员机构号"),
    })
    public static java.lang.String get_OrgId()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("OrgId");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置柜员机构号
	 */
    public static void set_OrgId(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("OrgId", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取终端号
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "终端号"),
    })
    public static java.lang.String get_DeviceCode()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("DeviceCode");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置终端号
	 */
    public static void set_DeviceCode(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("DeviceCode", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取柜员名称
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "柜员名称"),
    })
    public static java.lang.String get_TellerName()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("TellerName");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置柜员名称
	 */
    public static void set_TellerName(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("TellerName", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取工作日期
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "工作日期"),
    })
    public static java.lang.String get_WorkDate()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("WorkDate");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置工作日期
	 */
    public static void set_WorkDate(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("WorkDate", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取地区号
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "地区号"),
    })
    public static java.lang.String get_ZoneNo()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("ZoneNo");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置地区号
	 */
    public static void set_ZoneNo(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("ZoneNo", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取服务器信息
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "服务器信息"),
    })
    public static java.lang.String get_ServerInfo()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("ServerInfo");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置服务器信息
	 */
    public static void set_ServerInfo(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("ServerInfo", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取柜员服务状态
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "柜员服务状态"),
    })
    public static java.lang.Integer get_Status()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.Integer) context.getFieldValue("Status");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置柜员服务状态
	 */
    public static void set_Status(java.lang.Integer value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("Status", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取机构名称
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "机构名称"),
    })
    public static java.lang.String get_OrgName()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("OrgName");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置机构名称
	 */
    public static void set_OrgName(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("OrgName", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取柜面类型
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "柜面类型"),
    })
    public static java.lang.String get_CounterType()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("CounterType");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置柜面类型
	 */
    public static void set_CounterType(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("CounterType", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取未完成待办数量
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "未完成待办数量"),
    })
    public static java.lang.Integer get_UnfinishedTaskCount()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.Integer) context.getFieldValue("UnfinishedTaskCount");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置未完成待办数量
	 */
    public static void set_UnfinishedTaskCount(java.lang.Integer value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("UnfinishedTaskCount", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取柜员的账务机构
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "柜员的账务机构"),
    })
    public static java.lang.String get_AccountOrgId()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("AccountOrgId");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置柜员的账务机构
	 */
    public static void set_AccountOrgId(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("AccountOrgId", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取柜面状态
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "柜面状态"),
    })
    public static java.lang.String get_CounterStatus()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("CounterStatus");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置柜面状态
	 */
    public static void set_CounterStatus(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("CounterStatus", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取排队人数
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "排队人数"),
    })
    public static java.lang.String get_WaitingNum()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("WaitingNum");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置排队人数
	 */
    public static void set_WaitingNum(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("WaitingNum", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取密码
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "密码"),
    })
    public static java.lang.String get_Password()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("Password");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置密码
	 */
    public static void set_Password(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("Password", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取登录类型
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "登录类型"),
    })
    public static java.lang.String get_LoginType()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("LoginType");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置登录类型
	 */
    public static void set_LoginType(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("LoginType", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取岗位编号
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "岗位编号"),
    })
    public static java.lang.String get_PostId()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("PostId");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置岗位编号
	 */
    public static void set_PostId(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("PostId", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取所有上级机构
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "所有上级机构"),
    })
    public static java.util.Map get_AllSuperOrgInfoMap()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.util.Map) context.getFieldValue("AllSuperOrgInfoMap");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置所有上级机构
	 */
    public static void set_AllSuperOrgInfoMap(java.util.Map value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("AllSuperOrgInfoMap", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取传输密钥
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "传输密钥"),
    })
    public static java.lang.String get_TransKey()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("TransKey");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置传输密钥
	 */
    public static void set_TransKey(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("TransKey", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取年
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "年"),
    })
    public static java.lang.String get_yyyy()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("yyyy");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置年
	 */
    public static void set_yyyy(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("yyyy", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取月
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "月"),
    })
    public static java.lang.String get_MM()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("MM");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置月
	 */
    public static void set_MM(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("MM", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取日
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "日"),
    })
    public static java.lang.String get_dd()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("dd");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置日
	 */
    public static void set_dd(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("dd", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取分行号联社号
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "分行号联社号"),
    })
    public static java.lang.String get_BranchID()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("BranchID");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置分行号联社号
	 */
    public static void set_BranchID(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("BranchID", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取授权级别
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "授权级别"),
    })
    public static java.lang.String get_authlevel()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("authlevel");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置授权级别
	 */
    public static void set_authlevel(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("authlevel", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取柜员授权中心ID
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "柜员授权中心ID"),
    })
    public static java.lang.String get_authcenterno()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("authcenterno");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置柜员授权中心ID
	 */
    public static void set_authcenterno(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("authcenterno", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取岗位限额
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "岗位限额"),
    })
    public static java.util.Map get_postAmt()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.util.Map) context.getFieldValue("postAmt");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置岗位限额
	 */
    public static void set_postAmt(java.util.Map value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("postAmt", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取前置机地址
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "前置机地址"),
    })
    public static java.lang.String get_vtaddrip()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("vtaddrip");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置前置机地址
	 */
    public static void set_vtaddrip(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("vtaddrip", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取尾箱号
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "尾箱号"),
    })
    public static java.lang.String get_cashbox()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("cashbox");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置尾箱号
	 */
    public static void set_cashbox(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("cashbox", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取身份证号码
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "身份证号码"),
    })
    public static java.lang.String get_idcardno()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("idcardno");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置身份证号码
	 */
    public static void set_idcardno(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("idcardno", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取昨天
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "昨天"),
    })
    public static java.lang.String get_yesterday()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("yesterday");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置昨天
	 */
    public static void set_yesterday(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("yesterday", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取机构级别
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "机构级别"),
    })
    public static java.lang.String get_orglevel()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("orglevel");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置机构级别
	 */
    public static void set_orglevel(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("orglevel", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取机构类别
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "机构类别"),
    })
    public static java.lang.String get_orgType()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("orgType");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置机构类别
	 */
    public static void set_orgType(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("orgType", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取民泰上级清算机构
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "民泰上级清算机构"),
    })
    public static java.lang.String get_mtsjjgm()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("mtsjjgm");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置民泰上级清算机构
	 */
    public static void set_mtsjjgm(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("mtsjjgm", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取机构开通币种
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "机构开通币种"),
    })
    public static java.util.List get_orgccylist()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.util.List) context.getFieldValue("orgccylist");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置机构开通币种
	 */
    public static void set_orgccylist(java.util.List value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("orgccylist", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取国家列表
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "国家列表"),
    })
    public static java.util.List get_sgjdList()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.util.List) context.getFieldValue("sgjdList");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置国家列表
	 */
    public static void set_sgjdList(java.util.List value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("sgjdList", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取所有可以操作的机构
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "所有可以操作的机构"),
    })
    public static java.lang.String get_allAccessOrgs()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("allAccessOrgs");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置所有可以操作的机构
	 */
    public static void set_allAccessOrgs(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("allAccessOrgs", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取数据权限类型
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "数据权限类型"),
    })
    public static java.lang.String get_AccessPowerType()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("AccessPowerType");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置数据权限类型
	 */
    public static void set_AccessPowerType(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("AccessPowerType", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取Sessionid
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "Sessionid"),
    })
    public static java.lang.String get_sessionid()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("sessionid");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置Sessionid
	 */
    public static void set_sessionid(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("sessionid", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取授权单笔额度
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "授权单笔额度"),
    })
    public static java.lang.String get_authSingleAmount()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("authSingleAmount");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置授权单笔额度
	 */
    public static void set_authSingleAmount(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("authSingleAmount", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取授权总额度
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "授权总额度"),
    })
    public static java.lang.String get_authAmount()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("authAmount");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置授权总额度
	 */
    public static void set_authAmount(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("authAmount", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取授权累计额度
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "授权累计额度"),
    })
    public static java.lang.String get_authSumAmount()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.String) context.getFieldValue("authSumAmount");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置授权累计额度
	 */
    public static void set_authSumAmount(java.lang.String value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("authSumAmount", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取是否无纸化客户端
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "是否无纸化客户端"),
    })
    public static java.lang.Boolean get_isPaperlessClient()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.Boolean) context.getFieldValue("isPaperlessClient");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置是否无纸化客户端
	 */
    public static void set_isPaperlessClient(java.lang.Boolean value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("isPaperlessClient", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取无纸化终端连接状态
	 */
	@MetadataGroup({
			@Metadata(key = "外部可读", value = "true"),
			@Metadata(key = "外部可写", value = "true"),
			@Metadata(key = "业务名称", value = "无纸化终端连接状态"),
    })
    public static java.lang.Boolean get_paperlessConnstate()
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            return (java.lang.Boolean) context.getFieldValue("paperlessConnstate");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 设置无纸化终端连接状态
	 */
    public static void set_paperlessConnstate(java.lang.Boolean value)
    {
        try
        {
            // 查找上下文
            IContext context = lookupContext(NAME);
            context.setFieldValue("paperlessConnstate", value);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
