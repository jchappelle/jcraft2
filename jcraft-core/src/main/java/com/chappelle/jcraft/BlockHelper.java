package com.chappelle.jcraft;

import com.chappelle.jcraft.blocks.PickedBlock;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class BlockHelper
{
    private World world;
    private AppSettings settings;
    private Camera cam;
    private Node blocksNode;

    public BlockHelper(Camera camera, World world, AppSettings settings, Node blocksNode)
    {
        this.world = world;
        this.settings = settings;
        this.cam = camera;
        this.blocksNode = blocksNode;
    }

    private CollisionResults getRayCastingResults(Node node, Integer rayLength)
    {
        Vector3f origin = cam.getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();
        Ray ray = new Ray(origin, direction);
        if(rayLength != null)
        {
            ray.setLimit(rayLength);
        }
        CollisionResults results = new CollisionResults();
        node.collideWith(ray, results);
        return results;
    }

    /**
     * Returns the pointed at block location in chunk space. The coordinates are relative to the chunk
     * and the BlockNavigator is used to get the pointed block location. This method should be used
     * when manipulating blocks in the Cubes api.
     * @param getNeighborLocation Whether to return the neighbor block location. Useful for adding blocks.
     * @return A Vector3Int representing the pointed at block location in chunk space
     */
    public Vector3Int getPointedBlockLocationInChunkSpace(boolean getNeighborLocation)
    {
        CollisionResults results = getRayCastingResults(blocksNode, 15);
        if (results.size() > 0)
        {
            Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
            return BlockNavigator.getPointedBlockLocation(world, collisionContactPoint, getNeighborLocation);
        }
        return null;
    }

    /**
     * Returns the world space coordinates for the block that is being pointed at with the crosshairs.
     * It does this by doing an infinite length ray cast
     * @return A Vector3Int representing the world space coordinates for the pointed at block.
     */
    public Vector3Int getPointedBlockLocationInWorldSpace()
    {
        return toVector3Int(getPointedBlockLocationInWorldSpaceVector());
    }

    public PickedBlock pickBlock()
    {
        return pickBlock(false);
    }
    
    public PickedBlock pickNeighborBlock()
    {
        return pickBlock(true);
    }
    
    private PickedBlock pickBlock(boolean getNeighborLocation)
    {
        CollisionResults results = getRayCastingResults(blocksNode, null);
        if (results.size() > 0)
        {
            Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
            Vector3Int blockLocation = BlockNavigator.getPointedBlockLocation(world, collisionContactPoint, getNeighborLocation);
            Block block = world.getBlock(blockLocation);
            if(!getNeighborLocation && block == null)//If we are getting a neighbor then the block could be null, otherwise we don't allow null
            {
                return null;
            }
            return new PickedBlock(block, blockLocation, results, cam.getDirection());
        }
        return null;
    }

    private Vector3f getPointedBlockLocationInWorldSpaceVector()
    {
        CollisionResults results = getRayCastingResults(blocksNode, null);
        if (results.size() > 0)
        {
            Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
            Vector3f collisionLocation = Util.compensateFloatRoundingErrors(collisionContactPoint);
            return collisionLocation;
        }
        return null;
    }

    public static Vector3Int toVector3Int(Vector3f vector)
    {
        if(vector == null)
        {
            return null;
        }
        vector = Util.compensateFloatRoundingErrors(vector);
        return new Vector3Int((int)vector.getX(), (int)vector.getY(), (int)vector.getZ());
    }

    public static Vector3f toVector(Vector3Int point)
    {
        return new Vector3f(point.getX(), point.getY(), point.getZ());
    }

    public float getDistanceToPointedLocation(Vector3f playerLocation)
    {
        Vector3f pointedBlock = getPointedBlockLocationInWorldSpaceVector();
        if(pointedBlock == null)
        {
            return 0;
        }
        else
        {
            return playerLocation.distance(pointedBlock);
        }
    }

    public Block getSteppedOnBlock(Vector3f playerLocation)
    {
        Vector3f origin = playerLocation;
        Vector3f direction = new Vector3f(0.0f, -1.0f, 0.0f);
        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        blocksNode.collideWith(ray, results);

        if(results.size() > 0)
        {
            Vector3f collisionContactPoint = results.getClosestCollision().getContactPoint();
            Vector3Int blockLocation = BlockNavigator.getPointedBlockLocation(world, collisionContactPoint, false);
            if(blockLocation != null)
            {
                return world.getBlock(blockLocation);
            }
        }
        return null;
    }

}
