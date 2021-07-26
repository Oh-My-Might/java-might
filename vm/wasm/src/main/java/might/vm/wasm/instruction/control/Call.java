package might.vm.wasm.instruction.control;

import might.common.numeric.ISize;
import might.vm.wasm.core.WasmReader;
import might.vm.wasm.core.structure.Function;
import might.vm.wasm.core.structure.ModuleInstance;
import might.vm.wasm.error.Assertions;
import might.vm.wasm.instruction.Instruction;
import might.vm.wasm.instruction.Operate;
import might.vm.wasm.model.Dump;
import might.vm.wasm.model.Local;
import might.vm.wasm.model.index.FunctionIndex;
import might.vm.wasm.model.section.CodeSection;

public class Call implements Operate {

    @Override
    public Dump read(WasmReader reader) {
        return reader.readFunctionIndex();
    }

    @Override
    public void operate(ModuleInstance mi, Dump args) {
        Assertions.requireNonNull(args);
        Assertions.requireType(args, FunctionIndex.class);

        FunctionIndex index = ((FunctionIndex) args);

        Function function = mi.getFunction(index);

        callFunction(mi, function);
    }

    public void callFunction(ModuleInstance mi, Function function) {
        if (!function.isInternal()) {
            // 有本地函数内容，对于模块来说是外部的
            callExternalFunction(mi, function);
        } else {
            callInternalFunction(mi, function);
        }
    }

    private void callInternalFunction(ModuleInstance mi, Function function) {
        CodeSection code = function.getCodeSection();
        mi.enterBlock(Instruction.CALL, function.type(), code.expression);

        // 分配本地变量
        for (int i = 0; i < code.locals.length; i++) {
            Local local = code.locals[i];
            local.pushLocal(mi);
        }
    }

    private void callExternalFunction(ModuleInstance mi, Function function) {
        ISize[] args = mi.popISizes(function.type().parameters.length);
        ISize[] results = function.call(args);
        mi.pushISizes(results);
    }

}
