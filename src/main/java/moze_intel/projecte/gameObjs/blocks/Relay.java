package moze_intel.projecte.gameObjs.blocks;

import moze_intel.projecte.PECore;
import moze_intel.projecte.gameObjs.tiles.RelayMK1Tile;
import moze_intel.projecte.gameObjs.tiles.RelayMK2Tile;
import moze_intel.projecte.gameObjs.tiles.RelayMK3Tile;
import moze_intel.projecte.gameObjs.tiles.TileEmc;
import moze_intel.projecte.utils.Constants;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Relay extends BlockDirection
{
	private int tier;
	
	public Relay(int tier) 
	{
		super(Material.rock);
		this.setUnlocalizedName("pe_relay_MK" + Integer.toString(tier));
		this.setLightLevel(Constants.COLLECTOR_LIGHT_VALS[tier - 1]);
		this.setHardness(10.0f);
		this.tier = tier;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();

			switch (tier)
			{
				case 1:
					player.openGui(PECore.instance, Constants.RELAY1_GUI, world, x, y, z);
					break;
				case 2:
					player.openGui(PECore.instance, Constants.RELAY2_GUI, world, x, y, z);
					break;
				case 3:
					player.openGui(PECore.instance, Constants.RELAY3_GUI, world, x, y, z);
					break;
			}
		}
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entLiving, ItemStack stack)
	{
		setFacingMeta(world, x, y, z, ((EntityPlayer) entLiving));
		
		TileEntity tile = world.getTileEntity(pos);
		
		if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("ProjectEBlock") && tile instanceof TileEmc)
		{
			stack.getTagCompound().setInteger("x", pos.getX());
			stack.getTagCompound().setInteger("y", pos.getY());
			stack.getTagCompound().setInteger("z", pos.getZ());
			
			tile.readFromNBT(stack.getTagCompound());
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) 
	{
		if (tier == 1)
		{
			return new RelayMK1Tile();
		}
		
		if (tier == 2) 
		{
			return new RelayMK2Tile();
		}
		
		if (tier == 3) 
		{
			return new RelayMK3Tile();
		}
		
		return null;
	}
}
