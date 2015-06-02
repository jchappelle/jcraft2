package com.chappelle.jcraft;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;

public class CubesSettings
{
	private AssetManager assetManager;
	// private int chunkSizeX = 16;
	// private int chunkSizeY = 256;
	// private int chunkSizeZ = 16;
	private int chunkSizeX = 32;
	private int chunkSizeY = 32;
	private int chunkSizeZ = 32;
	private Material blockMaterial;
	private Material unshadedBlockMaterial;
	private int texturesCountX = 16;
	private int texturesCountY = 16;

	private static CubesSettings INSTANCE;
	
	public static CubesSettings getInstance()
	{
		return INSTANCE;
	}
	
	public CubesSettings(Application application)
	{
		assetManager = application.getAssetManager();
		INSTANCE = this;
	}
	
	public AssetManager getAssetManager()
	{
		return assetManager;
	}

	public int getChunkSizeX()
	{
		return chunkSizeX;
	}

	public void setChunkSizeX(int chunkSizeX)
	{
		this.chunkSizeX = chunkSizeX;
	}

	public int getChunkSizeY()
	{
		return chunkSizeY;
	}

	public void setChunkSizeY(int chunkSizeY)
	{
		this.chunkSizeY = chunkSizeY;
	}

	public int getChunkSizeZ()
	{
		return chunkSizeZ;
	}

	public void setChunkSizeZ(int chunkSizeZ)
	{
		this.chunkSizeZ = chunkSizeZ;
	}

	public Material getBlockMaterial()
	{
		return blockMaterial;
	}

	public void setDefaultBlockMaterial(String textureFilePath)
	{
		setBlockMaterial(new BlockChunk_Material(assetManager, textureFilePath, true));
		setUnshadedBlockMaterial(new BlockChunk_Material(assetManager, textureFilePath, false));
	}
	
	public void setBlockMaterial(Material blockMaterial)
	{
		this.blockMaterial = blockMaterial;
	}

	public int getTexturesCountX()
	{
		return texturesCountX;
	}

	public int getTexturesCountY()
	{
		return texturesCountY;
	}

	public void setTexturesCount(int texturesCountX, int texturesCountY)
	{
		this.texturesCountX = texturesCountX;
		this.texturesCountY = texturesCountY;
	}

	public Material getUnshadedBlockMaterial()
	{
		return unshadedBlockMaterial;
	}

	public void setUnshadedBlockMaterial(Material unshadedBlockMaterial)
	{
		this.unshadedBlockMaterial = unshadedBlockMaterial;
	}
}
