package might.vm.wasm.core.structure;

import might.common.numeric.ISize;
import might.vm.wasm.model.type.GlobalType;

public interface Global {

    /**
     * 全局数据类型
     */
    GlobalType type();

    /**
     * 获取全局参数
     */
    ISize get();

    /**
     * 设置全局参数
     */
    void set(ISize value);

}
