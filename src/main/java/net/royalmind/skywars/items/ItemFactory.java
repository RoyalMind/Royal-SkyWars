package net.royalmind.skywars.items;

import net.royalmind.skywars.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {

    private String name;
    private List<String> lore = new ArrayList<>();
    private Material material;

    public ItemFactory(final String name, final Material material) {
        this.name = name;
        this.material = material;
    }

    public ItemStack getFinalItem() {
        final ItemStack itemStack = new ItemStack(getMaterial());
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Chat.translate(getLore()));
        itemMeta.setDisplayName(Chat.translate(getName()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }
}
