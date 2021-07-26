package might.vm.wasm.instance;

import might.common.numeric.I32;
import might.vm.wasm.core.structure.Function;
import might.vm.wasm.core.structure.Table;
import might.vm.wasm.model.section.TableType;
import might.vm.wasm.util.Slice;

public class TableInstance implements Table {

    public final TableType type;

    private final Slice<Function> elements;

    public TableInstance(TableType type) {
        this.type = type;
        this.elements = new Slice<>(type.limits.getMin().unsigned().intValue());
        this.elements.set(type.limits.getMin().unsigned().intValue() - 1, null);
    }


    @Override
    public TableType type() {
        return type;
    }

    @Override
    public I32 size() {
        return I32.valueOf(elements.size());
    }

    @Override
    public void grow(I32 grow) {
        int g = grow.unsigned().intValue();

        if (g <= 0) {
            throw new RuntimeException("wrong grow: " + grow);
        }

        int wanna = elements.size() + g;

        if (!type.limits.isValid(I32.valueOf(wanna))) {
            throw new RuntimeException("too large for: " + type.limits);
        }

        elements.set(wanna - 1, null);
    }

    @Override
    public Function getElement(I32 index) {
        return elements.get(index);
    }

    @Override
    public void setElement(I32 index, Function element) {
        elements.set(index.unsigned().intValue(), element);
    }

}
