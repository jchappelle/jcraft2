package com.chappelle.jcraft;

import com.chappelle.jcraft.blocks.BlockStone;
import com.chappelle.jcraft.blocks.BlockDoor;
import com.chappelle.jcraft.blocks.BlockGlass;
import com.chappelle.jcraft.blocks.BlockGrass;
import com.chappelle.jcraft.blocks.PickedBlock;
import com.chappelle.jcraft.blocks.BlockTorch;
import com.chappelle.jcraft.shapes.BlockShape_Cube;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class Block
{
	public static final Block[] blocksList = new Block[4096];
	
	public static enum Face
	{
		Top, Bottom, Left, Right, Front, Back;

		public static Block.Face fromNormal(Vector3f normal)
		{
			return fromNormal(Vector3Int.fromVector3f(normal));
		}

		public static Block.Face fromNormal(Vector3Int normal)
		{
			int x = normal.getX();
			int y = normal.getY();
			int z = normal.getZ();
			if(x != 0)
			{
				if(x > 0)
				{
					return Block.Face.Right;
				}
				else
				{
					return Block.Face.Left;
				}
			}
			else if(y != 0)
			{
				if(y > 0)
				{
					return Block.Face.Top;
				}
				else
				{
					return Block.Face.Bottom;
				}
			}
			else if(z != 0)
			{
				if(z > 0)
				{
					return Block.Face.Front;
				}
				else
				{
					return Block.Face.Back;
				}
			}
			return null;
		}

	};

	public static final Block grass = new BlockGrass(1);
	public static final Block glass = new BlockGlass(2);
	public static final Block door = new BlockDoor(3, true);
	public static final Block torch = new BlockTorch(4);
	public static final Block stone = new BlockStone(5);
	
	private BlockShape[] shapes = new BlockShape[] { new BlockShape_Cube() };
	private BlockSkin[] skins;

	/** ID of the block. */
	public final int blockId;
	
	public Block(int blockId, BlockSkin... skins)
	{
		this.skins = skins;
		this.blockId = blockId;
		
		blocksList[blockId] = this;
	}
	
	protected void setShapes(BlockShape... shapes)
	{
		this.shapes = shapes;
	}

	public BlockShape getShape(Chunk chunk, Vector3Int location)
	{
		return shapes[getShapeIndex(chunk, location)];
	}

	protected int getShapeIndex(Chunk chunk, Vector3Int location)
	{
		return 0;
	}

	public BlockSkin getSkin(Chunk chunk, Vector3Int location, Face face)
	{
		return skins[getSkinIndex(chunk, location, face)];
	}

	protected int getSkinIndex(Chunk chunk, Vector3Int location, Face face)
	{
		if(skins.length == 6)
		{
			return face.ordinal();
		}
		return 0;
	}

	public boolean isRemovable()
	{
		return true;
	}
	
	public boolean isAffectedByGravity()
	{
		return false;
	}
	
	public Geometry makeBlockGeometry()
	{
		return null;
	}

	public boolean isValidPlacementFace(Face face)
	{
		return true;
	}

	public void onBlockPlaced(World world, Vector3Int location, Vector3f contactNormal, Vector3f cameraDirectionAsUnitVector)
	{
		// TODO Auto-generated method stub
		
	}

	public void onBlockRemoved(World world, Vector3Int location)
	{
		// TODO Auto-generated method stub
		
	}

	public void onBlockActivated(World world, PickedBlock pickedBlock)
	{
		// TODO Auto-generated method stub
		
	}

	public void onNeighborRemoved(World world, Vector3Int location, Vector3Int neighborLocation)
	{
		// TODO Auto-generated method stub
		
	}

	public void onEntityWalking(World world, Vector3Int location)
	{
		// TODO Auto-generated method stub
		
	}
	
    public boolean smothersBottomBlock()
    {
        return true;
    }
    
    public boolean isSolid()
    {
    	return true;
    }

	public boolean isTransparent()
	{
		return false;
	}

	public int getBlockLightValue()
	{
		return 0;
	}
	
	public boolean isActionBlock()
	{
		return false;
	}
	
	public boolean useNeighborLight()
	{
		return true;
	}
}
