package de.luckycrew.halloween.item;

import java.util.List;

import info.u_team.u_team_core.creativetab.UCreativeTab;
import info.u_team.u_team_core.item.UItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class ItemCandy extends UItem {
	
	public ItemCandy(String name, UCreativeTab tab) {
		super(name, tab);
		this.setMaxDamage(20);
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!stack.hasTagCompound()) {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setInteger("cooldown", 0);
			stack.setTagCompound(compound);
		} else {
			NBTTagCompound compound = stack.getTagCompound();
			int i = compound.getInteger("cooldown");
			if (i > 0) {
				compound.setInteger("cooldown", i - 1);
			}
			stack.setTagCompound(compound);
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		NBTTagCompound compound = stack.getTagCompound();
		if (compound.getInteger("cooldown") == 0) {
			compound.setInteger("cooldown", 200);
			player.motionY = 0.88D;
			float f = player.rotationYaw * 0.017453292F;
			player.motionX -= (double) (MathHelper.sin(f) * 2.2F);
			player.motionZ += (double) (MathHelper.cos(f) * 2.2F);
			stack.damageItem(1, player);
		}
		return stack;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !ItemStack.areItemsEqual(oldStack, newStack);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
		if (stack.hasTagCompound()) {
			NBTTagCompound compound = stack.getTagCompound();
			int seconds = compound.getInteger("cooldown") / 20;
			if (seconds == 0) {
				tooltip.add(I18n.format("item.candy.cooldown.no"));
			} else {
				tooltip.add(I18n.format("item.candy.cooldown.yes", seconds));
			}
		}
	}
	
}
