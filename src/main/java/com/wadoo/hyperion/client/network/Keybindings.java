package com.wadoo.hyperion.client.network;

import com.mojang.blaze3d.platform.InputConstants;
import com.wadoo.hyperion.Hyperion;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class Keybindings {
    public static final Keybindings INSTANCE = new Keybindings();

    private Keybindings() {}

    private static final String CATEGORY = "key.categories." + Hyperion.MODID;

    public final KeyMapping AGOL_INPUT_1 = new KeyMapping(
            "key." + Hyperion.MODID + ".agol_input_1",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_C, -1),
            CATEGORY
    );

    public final KeyMapping AGOL_INPUT_2 = new KeyMapping(
            "key." + Hyperion.MODID + ".agol_input_2",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_V, -1),
            CATEGORY
    );

    public final KeyMapping AGOL_INPUT_3 = new KeyMapping(
            "key." + Hyperion.MODID + ".agol_input_3",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_B, -1),
            CATEGORY
    );

    public final KeyMapping AGOL_INPUT_4 = new KeyMapping(
            "key." + Hyperion.MODID + ".agol_input_4",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_N, -1),
            CATEGORY
    );

    public final KeyMapping AGOL_INPUT_5 = new KeyMapping(
            "key." + Hyperion.MODID + ".agol_input_5",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_M, -1),
            CATEGORY
    );

}
