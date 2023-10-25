package com.wadoo.hyperion.common.items;

import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ModuleItem extends Item {
    public Supplier<? extends EntityType<?>> type;

    public ModuleItem(Properties properties, Supplier<? extends EntityType<?>> type) {
        super(properties);
        this.type = type;
    }

    public EntityType<?> getType() {
        return this.type.get();
    }
}
