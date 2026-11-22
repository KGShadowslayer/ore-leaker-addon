package com.oreleaker.addon;

import com.oreleaker.addon.modules.OreLeaker;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class OreLeakerAddon extends MeteorAddon {
    public static final Category CATEGORY = new Category("Ore Leaker");

    @Override
    public void onInitialize() {
        Modules.get().add(new OreLeaker());
    }
@Override
public void onRegisterCategories() {
    Modules.get().addCategory(CATEGORY);
}
    }

    @Override
    public String getPackage() {
        return "com.oreleaker.addon";
    }
}
