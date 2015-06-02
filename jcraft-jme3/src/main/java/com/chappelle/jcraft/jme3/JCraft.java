package com.chappelle.jcraft.jme3;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.chappelle.jcraft.Block;
import com.chappelle.jcraft.BlockHelper;
import com.chappelle.jcraft.CubesSettings;
import com.chappelle.jcraft.EntityPlayer;
import com.chappelle.jcraft.GameSettings;
import com.chappelle.jcraft.Vector3Int;
import com.chappelle.jcraft.World;
import com.chappelle.jcraft.profiler.Profiler;
import com.chappelle.jcraft.profiler.ProfilerResult;
import com.chappelle.jcraft.util.AABB;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class JCraft extends SimpleApplication implements ActionListener
{
	private final Vector3Int terrainSize = new Vector3Int(50, 10, 50);
	private int terrainIndex = terrainSize.getX();

	private static JCraft jcraft;
	
	public boolean debugEnabled = false;
	private GameSettings gameSettings;
	private CubesSettings cubesSettings;
	private BlockTerrainControl blockTerrain;
	private BlockHelper blockHelper;
	private Node terrainNode = new Node("terrain");
	private EntityPlayer player;
	public World world;
	private Profiler profiler;
	
	public JCraft(GameSettings gameSettings)
	{
		jcraft = this;
		
		this.gameSettings = gameSettings;
		debugEnabled = gameSettings.debugEnabled;
		this.profiler = new Profiler();
		profiler.profilingEnabled = gameSettings.profilingEnabled;
		settings = new AppSettings(true);
		settings.setWidth(gameSettings.screenWidth);
		settings.setHeight(gameSettings.screenHeight);
		settings.setTitle("JCraft");
		settings.setFrameRate(gameSettings.frameRate);
	}

	public Profiler getProfiler()
	{
		return profiler;
	}
	
	public static JCraft getInstance()
	{
		return jcraft;
	}
	
	@Override
	public void simpleInitApp()
	{
		cam.setFrustumPerspective(45f, (float)cam.getWidth()/cam.getHeight(), 0.01f, 1000f);
		initControls();
		initBlockTerrain();
		cam.lookAtDirection(new Vector3f(1, 0, 1), Vector3f.UNIT_Y);
		
		//Setup sky
		viewPort.setBackgroundColor(ColorRGBA.Black);

		//Setup player
		player = new EntityPlayer(world, cam, blockHelper);
		player.preparePlayerToSpawn();
		rootNode.addControl(new PlayerControl(this, player));

		rootNode.addControl(new HUDControl(this, settings, player));
		rootNode.addControl(new BlockCursorControl(world, blockHelper, assetManager));

		profiler.startSection("root");
		updateStatsView();
		
		System.out.println("****************************************************************************");
		System.out.println("Press F3 to toggle debug, F4 to toggle profiler");
		System.out.println("See key bindings in JCraft class for other controls");
		System.out.println("****************************************************************************");
		System.out.println("\r\n\r\n");
	}
	
	private void toggleDebug()
	{
		debugEnabled = !debugEnabled;
		updateStatsView();
	}
	
	private void toggleProfiling()
	{
		profiler.profilingEnabled = !profiler.profilingEnabled;
	}
	
	private void updateStatsView()
	{
		stateManager.getState(StatsAppState.class).setDisplayStatView(debugEnabled);
		stateManager.getState(StatsAppState.class).setDisplayFps(debugEnabled);
	}

	private void addMapping(String action, Trigger trigger)
	{
		inputManager.addMapping(action, trigger);
		inputManager.addListener(this, action);
	}
	
	private void initControls()
	{
		addMapping("move_left", new KeyTrigger(KeyInput.KEY_A));
		addMapping("move_right", new KeyTrigger(KeyInput.KEY_D));
		addMapping("move_up", new KeyTrigger(KeyInput.KEY_W));
		addMapping("move_down", new KeyTrigger(KeyInput.KEY_S));
		addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
		addMapping("n", new KeyTrigger(KeyInput.KEY_N));
		addMapping("n", new KeyTrigger(KeyInput.KEY_N));
		addMapping("RightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		addMapping("LeftClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        addMapping("1", new KeyTrigger(KeyInput.KEY_1));
        addMapping("2", new KeyTrigger(KeyInput.KEY_2));
        addMapping("3", new KeyTrigger(KeyInput.KEY_3));
        addMapping("4", new KeyTrigger(KeyInput.KEY_4));
        addMapping("5", new KeyTrigger(KeyInput.KEY_5));
        addMapping("6", new KeyTrigger(KeyInput.KEY_6));
        addMapping("7", new KeyTrigger(KeyInput.KEY_7));
        addMapping("8", new KeyTrigger(KeyInput.KEY_8));
        addMapping("9", new KeyTrigger(KeyInput.KEY_9));
        addMapping("0", new KeyTrigger(KeyInput.KEY_0));
        addMapping("t", new KeyTrigger(KeyInput.KEY_T));
        addMapping("g", new KeyTrigger(KeyInput.KEY_G));
        addMapping("u", new KeyTrigger(KeyInput.KEY_U));
        addMapping("f3", new KeyTrigger(KeyInput.KEY_F3));
        addMapping("f4", new KeyTrigger(KeyInput.KEY_F4));
	}

	private void initBlockTerrain()
	{
		cubesSettings = new CubesSettings(this);
		cubesSettings.setDefaultBlockMaterial("Textures/FaithfulBlocks.png");

		blockTerrain = new BlockTerrainControl(this, cubesSettings, new Vector3Int(14, 1, 14));
		world = blockTerrain.world;
		terrainNode.addControl(blockTerrain);
		terrainNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
		rootNode.attachChild(terrainNode);

//		terrainMgr = new BlockTerrainManager(cubesSettings, world);
		
		blockHelper = new BlockHelper(cam, world, settings, terrainNode);
//		blockTerrain.setBlocksFromNoise(new Vector3Int(), terrainSize, 0.5f, Blocks.GRASS);
		world.setBlockArea(new Vector3Int(), terrainSize, Block.grass);
		world.setBlock(6, 10, 4, Block.grass);
		world.setBlock(7, 10, 4, Block.grass);
		world.setBlock(7, 10, 5, Block.grass);
		world.setBlock(7, 10, 6, Block.grass);
		world.setBlock(7, 10, 7, Block.grass);
		world.setBlock(7, 10, 8, Block.grass);
		world.setBlock(7, 11, 4, Block.grass);
		world.setBlock(7, 11, 5, Block.grass);
		world.setBlock(7, 11, 5, Block.grass);
		world.setBlock(7, 12, 4, Block.grass);
		world.setBlock(7, 11, 6, Block.grass);
		world.setBlock(7, 13, 4, Block.grass);
		world.setBlock(7, 13, 5, Block.grass);
		world.setBlock(7, 13, 6, Block.grass);
		world.setBlock(7, 13, 7, Block.grass);
		world.setBlock(7, 12, 5, Block.grass);
		world.setBlock(7, 13, 6, Block.grass);
		world.setBlock(7, 13, 7, Block.grass);
		world.setBlock(7, 12, 5, Block.grass);
		world.setBlock(7, 12, 6, Block.grass);
		world.setBlock(7, 12, 7, Block.grass);
		world.setBlock(7, 11, 7, Block.grass);
		world.setBlock(7, 11, 8, Block.grass);
		world.setBlock(7, 12, 8, Block.grass);
		world.setBlock(7, 13, 8, Block.grass);
		world.setBlock(6, 13, 8, Block.grass);
		world.setBlock(6, 13, 7, Block.grass);
		world.setBlock(6, 13, 6, Block.grass);
		world.setBlock(6, 13, 5, Block.grass);
		world.setBlock(6, 13, 4, Block.grass);
		world.setBlock(6, 12, 4, Block.grass);
		world.setBlock(6, 11, 4, Block.grass);
		world.setBlock(6, 12, 8, Block.grass);
		world.setBlock(6, 11, 8, Block.grass);
		world.setBlock(6, 10, 8, Block.grass);
		world.setBlock(5, 13, 8, Block.grass);
		world.setBlock(5, 13, 7, Block.grass);
		world.setBlock(5, 13, 6, Block.grass);
		world.setBlock(5, 13, 5, Block.grass);
		world.setBlock(5, 13, 4, Block.grass);
		world.setBlock(5, 12, 4, Block.grass);
		world.setBlock(5, 11, 4, Block.grass);
		world.setBlock(5, 10, 4, Block.grass);
		world.setBlock(5, 10, 8, Block.grass);
		world.setBlock(5, 11, 8, Block.grass);
		world.setBlock(5, 12, 8, Block.grass);
		world.setBlock(4, 13, 4, Block.grass);
		world.setBlock(4, 12, 4, Block.grass);
		world.setBlock(4, 11, 4, Block.grass);
		world.setBlock(4, 10, 4, Block.grass);
		world.setBlock(4, 13, 5, Block.grass);
		world.setBlock(4, 13, 6, Block.grass);
		world.setBlock(4, 13, 7, Block.grass);
		world.setBlock(4, 13, 8, Block.grass);
		world.setBlock(4, 12, 8, Block.grass);
		world.setBlock(4, 11, 8, Block.grass);
		world.setBlock(4, 10, 8, Block.grass);
		world.setBlock(4, 12, 7, Block.grass);
		world.setBlock(4, 11, 7, Block.grass);
		world.setBlock(4, 10, 7, Block.grass);
		world.setBlock(4, 10, 5, Block.grass);
		world.setBlock(4, 11, 5, Block.grass);
		world.setBlock(4, 12, 5, Block.grass);
		
	}

	@Override
	public void simpleUpdate(float tpf)
	{
		AABB.getAABBPool().cleanPool();
	}


	
	@Override
	public void onAction(String name, boolean isPressed, float lastTimePerFrame)
	{
		if(name.equals("move_up"))
		{
			player.moveUp(isPressed);
		}
		else if(name.equals("move_right"))
		{
			player.moveRight(isPressed);
		}
		else if(name.equals("move_left"))
		{
			player.moveLeft(isPressed);
		}
		else if(name.equals("move_down"))
		{
			player.moveDown(isPressed);
		}
		else if(name.equals("jump"))
		{
			player.jump();
		}
		else if(name.equals("RightClick") && !isPressed)
		{
			player.placeBlock();
		}
		else if(name.equals("LeftClick") && !isPressed)
		{
			player.breakBlock();
		}
        else if("1".equals(name) && !isPressed)
        {
        	player.selectBlock(1);
        }
        else if("2".equals(name) && !isPressed)
        {
        	player.selectBlock(2);
        }
        else if("3".equals(name) && !isPressed)
        {
        	player.selectBlock(3);
        }
        else if("4".equals(name) && !isPressed)
        {
        	player.selectBlock(4);
        }
        else if("5".equals(name) && !isPressed)
        {
        	player.selectBlock(5);
        }
        else if("6".equals(name) && !isPressed)
        {
        	player.selectBlock(6);
        }
        else if("7".equals(name) && !isPressed)
        {
        	player.selectBlock(7);
        }
        else if("8".equals(name) && !isPressed)
        {
        	player.selectBlock(8);
        }
        else if("9".equals(name) && !isPressed)
        {
        	player.selectBlock(9);
        }
        else if("t".equals(name) && !isPressed)
        {
        	blockTerrain.world.setBlocksFromNoise(new Vector3Int(terrainIndex, 0, 0), terrainSize, 0.8f, Block.grass);
        	terrainIndex += terrainSize.getX();
        }
        else if("g".equals(name) && !isPressed)
        {
        	player.toggleGravity();
        }
        else if("f3".equals(name) && !isPressed)
        {
        	toggleDebug();
        }
        else if("f4".equals(name) && !isPressed)
        {
        	toggleProfiling();
        }
	}
	
    public BitmapFont getGuiFont()
    {
        return guiFont;
    }

    public void setGuiFont(BitmapFont guiFont)
    {
        this.guiFont = guiFont;
    }
    
    public CubesSettings getCubesSettings()
    {
    	return cubesSettings;
    }
    
    public BlockHelper getBlockHelper()
    {
    	return blockHelper;
    }

	@Override
	public void destroy()
	{
		super.destroy();
		gameSettings.save();

		if(profiler.profilingEnabled)
		{
			printProfilingData();
		}
	}

	private void printProfilingData()
	{
		System.out.println("\r\n\r\n");
		System.out.println("**********************************************");
		System.out.println("************* Profiler results ***************");
		System.out.println("**********************************************");
		profiler.endSection();//needed to close the root profiling section
		for(ProfilerResult result : profiler.getProfilingData("root"))
		{
			System.out.println(result.section + " [" + result.elapsedTime + "," + result.maxTime + "]");
		}
	}
	
	public static void main(String[] args)
	{
		Logger.getLogger("").setLevel(Level.SEVERE);

		GameSettings gameSettings = new GameSettings(new File(System.getProperty("user.home")));
		gameSettings.load();

		JCraft app = new JCraft(gameSettings);
		app.setShowSettings(gameSettings.showSettings);
		app.start();
	}
}
