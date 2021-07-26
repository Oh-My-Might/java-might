package might.vm.wasm.instruction.numeric;

import might.common.numeric.I32;
import might.common.numeric.I64;
import might.vm.wasm.core.WasmReader;
import might.vm.wasm.core.structure.ModuleInstance;
import might.vm.wasm.instruction.Operate;
import might.vm.wasm.model.Dump;

public class I64ExtendI32U implements Operate {

    @Override
    public Dump read(WasmReader reader) {
        return null;
    }

    @Override
    public void operate(ModuleInstance mi, Dump args) {
        I32 value = mi.popI32();
        byte[] bytes = value.bytes();
        // 无符号拓展，内置方法都是有符号的
        I64 u64 = I32.valueOf(new byte[] {
            bytes[0], bytes[1], bytes[2], bytes[3]
        }).u64();
        mi.pushI64(u64);
    }

}
