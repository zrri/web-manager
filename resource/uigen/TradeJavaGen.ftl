package ${pkgName};

import java.util.Map;

import cn.com.bankit.phoenix.trade.ui.Trade;
import cn.com.bankit.phoenix.ui.widget.*;

public class ${className} extends ${baseClassName} {
${fields}

/**

构造函数<br>
构造函数是平台初始化交易状态的地方，<b>不建议</b>在构造函数中添加代码，请在init方法中进行初始化设置
@param parent
@param container
*/
public ${className}(Trade parent, Container container) { super(parent, container); }
}