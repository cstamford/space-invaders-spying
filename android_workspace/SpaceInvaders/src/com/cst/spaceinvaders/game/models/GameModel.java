package com.cst.spaceinvaders.game.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import android.util.Log;

import com.cst.spaceinvaders.game.SpaceInvadersGame;
import com.cst.spaceinvaders.game.data.Asset;
import com.cst.spaceinvaders.game.entities.Enemy;
import com.cst.spaceinvaders.game.entities.Entity;
import com.cst.spaceinvaders.game.entities.Player;
import com.cst.spaceinvaders.game.entities.Projectile;
import com.cst.spaceinvaders.game.enumerable.AssetManifest;
import com.cst.spaceinvaders.game.enumerable.EnemyDirection;
import com.cst.spaceinvaders.game.views.GameView;

public class GameModel
{
	private static final int BASE_MOVE_SPEED = 48;
	private static final int BASE_PROJECTILE_VELOCITY = 15;
	private static final int BASE_ENEMY_SPEED_CHANGE = 25;
	private static final int PLAYER_SHOOT_FREQUENCY = 500;
	private static final int SCORE_PER_LEVEL = 250;
	private static final int SCORE_PER_ENEMY = 50;
	
	private GameView m_view;
	
	private EnemyDirection m_enemyDirection;
	
	private int m_enemyUpdateFrequency;
	private int m_width;
	private int m_height;
	private int m_level;
	private int m_score;
	
	private boolean m_inputMoveLeft;
	private boolean m_inputFire;
	private boolean m_inputMoveRight;
	
	private boolean m_playerDied;
	
	private Semaphore m_semaphore;
	
	private long m_lastTickUpdate;
	private long m_lastPlayerProjectile;
	private long m_lastEnemyProjectile;
	
	private Player m_player;
	
	private List<Projectile> m_playerProjectiles;
	private List<Projectile> m_enemyProjectiles;
	private List<Enemy> m_enemyList;
	
	private Random m_rng;
	
	public GameModel(GameView view, int width, int height)
	{
		m_view = view;
		m_width = width;
		m_height = height;
		changeScore(0);
		
		// The starting direction of the enemies
		m_enemyDirection = EnemyDirection.DIRECTION_RIGHT;
		
		m_player = new Player(SpaceInvadersGame.getAsset(AssetManifest.ASSET_PLAYER));
		m_enemyList = new ArrayList<Enemy>();
		
		m_playerProjectiles = new ArrayList<Projectile>();
		m_enemyProjectiles = new ArrayList<Projectile>();
		
		// A semaphore (acting as a mutex) to stop the controller accessing the entity list
		// while the model is updating.
		m_semaphore = new Semaphore(1);
		
		// The update timers
		m_lastTickUpdate = System.currentTimeMillis();
		m_lastPlayerProjectile = System.currentTimeMillis();
		m_lastEnemyProjectile = System.currentTimeMillis();
		
		// Random number generator
		m_rng = new Random();
		
		try
		{
			changeLevel(1);
		}
		catch (Exception e)
		{
			// This exception is impossible, because another thread cannot access this object
			// before it has been created.
			Log.e(SpaceInvadersGame.LOG_TAG, "This exception is impossible");
			Log.e(SpaceInvadersGame.LOG_TAG, e.toString());
		}
	}
	
	// Tells the model to move the player left
	public void onClickLeft()
	{
		m_inputMoveLeft = true;
	}
	
	// Tells the model to fire a projectile
	public void onClickFire()
	{		
		m_inputFire = true;
	}
	
	// Tells the model to move the player right
	public void onClickRight()
	{
		m_inputMoveRight = true;
	}
	
	// Updates the model
	public void updateModel() throws InterruptedException
	{
		// Acquire the semaphore. This semaphore acts as a mutex to ensure thread-safety.
		// No modification to the model must be allowed while the view is retrieving the
		// model list.
		m_semaphore.acquire();
		
		handlePlayerInput();
		
		moveEnemies();
		
		moveEnemyProjectiles();
		handleEnemyProjectileCollisions();
		
		movePlayerProjectiles();
		handlePlayerProjectileCollisions();
		
		handleEnemyShooting();

		// Release the semaphore.
		m_semaphore.release();
		
		// If the player has died
		if (m_playerDied)
			m_view.onGameEnd(m_level, m_score);
		
		// If all enemies are dead
		if (m_enemyList.size() == 0)
		{
			changeScore(m_score + SCORE_PER_LEVEL);
			changeLevel(m_level + 1);
		}
	}
	
	// Handle the player's input
	private void handlePlayerInput()
	{
		if (m_inputMoveLeft)
		{
			// Make sure we don't go off screen
			if (m_player.getX() - BASE_MOVE_SPEED > 0)
				m_player.move(-BASE_MOVE_SPEED, 0);
			
			m_inputMoveLeft = false;
		}
		
		if (m_inputFire)
		{
			long currentTime = System.currentTimeMillis();
			
			if (currentTime - m_lastPlayerProjectile > PLAYER_SHOOT_FREQUENCY)
			{
				m_playerProjectiles.add(createProjectileAt(m_player, -BASE_PROJECTILE_VELOCITY));
				m_lastPlayerProjectile = currentTime;
			}
			
			m_inputFire = false;
		}
		
		if (m_inputMoveRight)
		{
			// Make sure we don't go off screen
			if (m_player.getX() + m_player.getAsset().m_width + BASE_MOVE_SPEED < m_width)
				m_player.move(BASE_MOVE_SPEED, 0);
			
			m_inputMoveRight = false;
		}
	}
	
	// Move all enemies in the world
	private void moveEnemies()
	{
		long currentTime = System.currentTimeMillis();
		
		// Check for movement
		if (currentTime - m_lastTickUpdate > m_enemyUpdateFrequency)
		{
			EnemyDirection newDirection = m_enemyDirection;
			int directionX = 0;
			int directionY = 0;
			
			Iterator<Enemy> iter = m_enemyList.iterator();

			// Determine direction
			while (iter.hasNext() && newDirection == m_enemyDirection)
			{
				Enemy enemy = iter.next();
				
				if (m_enemyDirection == EnemyDirection.DIRECTION_RIGHT)
				{
					if (enemy.getX() + enemy.getAsset().m_height + BASE_MOVE_SPEED >= m_width)
					{
						directionY = 64;
						newDirection = EnemyDirection.DIRECTION_LEFT;
					}
				}
				else
				{
					if (enemy.getX() - BASE_MOVE_SPEED <= 0)
					{
						directionY = 64;
						newDirection = EnemyDirection.DIRECTION_RIGHT;
					}
				}
			}
			
			// If the direction is the same
			if (newDirection == m_enemyDirection)
			{
				// Move in the appropriate direction
				if (m_enemyDirection == EnemyDirection.DIRECTION_RIGHT)
				{
					directionX = BASE_MOVE_SPEED;
				}
				else
				{
					directionX = -BASE_MOVE_SPEED;
				}
			}
			// Don't move along x-, just y-
			else
			{
				m_enemyDirection = newDirection;
			}
			
			iter = m_enemyList.iterator();
		
			// Move the enemies
			while (iter.hasNext())
			{
				Enemy enemy = iter.next();
				
				enemy.move(directionX, directionY);
				
				// Check to see if the enemy is below the player
				// If it is, the player is now dead
				if (enemy.getY() + enemy.getAsset().m_height > m_player.getY())
					m_playerDied = true;
			}
			
			m_lastTickUpdate = currentTime;
		}
	}
	
	// Create a projectile at the provided entity's location.
	private Projectile createProjectileAt(Entity entity, int velocity)
	{
		Asset projectileAsset = SpaceInvadersGame.getAsset(AssetManifest.ASSET_PROJECTILE);
		
		Projectile projectile = new Projectile(
				projectileAsset,
				entity.getX() + (entity.getAsset().m_width / 2 ) - (projectileAsset.m_width / 2),
				entity.getY() - projectileAsset.m_height,
				velocity);
		
		return projectile;
	}
	
	// Move player projectiles
	private void movePlayerProjectiles()
	{
		Iterator<Projectile> iter = m_playerProjectiles.iterator();

		while (iter.hasNext())
		{
			Projectile projectile = iter.next();
			
			// If it is out of bounds, remove it from the list
			if (isProjectileOutOfBounds(projectile))
			{
				iter.remove();
				continue;
			}
			else
			{
				projectile.move(0, projectile.getVelocity());
			}
		}
	}
	
	// Move enemy projectiles
	private void moveEnemyProjectiles()
	{
		Iterator<Projectile> iter = m_enemyProjectiles.iterator();

		// Determine direction
		while (iter.hasNext())
		{
			Projectile projectile = iter.next();
			
			// If it is out of bounds, remove it from the list
			if (isProjectileOutOfBounds(projectile))
			{
				iter.remove();
				continue;
			}
			else
			{
				projectile.move(0, projectile.getVelocity());
			}
		}
	}
	
	// Handle collisions between player projectiles and the enemies.
	private void handlePlayerProjectileCollisions()
	{
		Iterator<Projectile> iter = m_playerProjectiles.iterator();
		
		while (iter.hasNext())
		{
			boolean collision = false;
			Projectile projectile = iter.next();
			
			Iterator<Enemy> enemyIter = m_enemyList.iterator();
			
			while (enemyIter.hasNext())
			{
				Enemy enemy = enemyIter.next();
				
				// If there is a collision, remove the enemy
				if (projectile.collidesWith(enemy))
				{
					collision = true;
					enemyIter.remove();
					continue;
				}
			}
			
			// If there is a collision, remove the projectile
			// Also increase the enemy update frequency
			if (collision)
			{
				m_enemyUpdateFrequency -= BASE_ENEMY_SPEED_CHANGE + (m_level * 5);
				changeScore(m_score + SCORE_PER_ENEMY);
				iter.remove();
				continue;
			}
		}
	}
	
	// Handle collisions between enemy projectiles and the players.
	private void handleEnemyProjectileCollisions() throws InterruptedException
	{
		boolean collision = false;
		Iterator<Projectile> iter = m_enemyProjectiles.iterator();
		
		while (iter.hasNext() && !collision)
		{
			Projectile projectile = iter.next();
			
			if (projectile.collidesWith(m_player))
			{
				iter.remove();
				collision = true;
				continue;
			}
		}
		
		if (collision)
			m_playerDied = true;
	}
	
	// Handles the creation of enemy projectiles
	private void handleEnemyShooting()
	{
		long currentTime = System.currentTimeMillis();
		
		// Scale the probability of an enemy shooting based on the last time it shot.
		int timeScale = (int)((currentTime - m_lastEnemyProjectile) / 1000);
		
		// Get a random number between 0 and 100
		int randomNumber = m_rng.nextInt(100);
		
		int lowBound = 50 - timeScale;
		int highBound = 50 + timeScale;
		
		if (randomNumber >= lowBound && randomNumber <= highBound)
		{
			int enemyAtIndex = 0;
			
			// Get the index of a random enemy in the list
			if (m_enemyList.size() > 1)
				enemyAtIndex = m_rng.nextInt(m_enemyList.size() - 1);
			
			m_enemyProjectiles.add(createProjectileAt(
							m_enemyList.get(enemyAtIndex),
							BASE_PROJECTILE_VELOCITY));
			
			m_lastEnemyProjectile = currentTime;
		}
		
	}
	
	// Restarts the level
	private void restartLevel() throws InterruptedException
	{
		// Acquire the semaphore. This semaphore acts as a mutex to ensure thread-safety.
		// No modification to the model must be allowed while the view is retrieving the
		// model list.
		m_semaphore.acquire();
		
		Asset enemyAsset = SpaceInvadersGame.getAsset(AssetManifest.ASSET_ENEMY1A);
		Asset playerAsset = SpaceInvadersGame.getAsset(AssetManifest.ASSET_PLAYER);
		
		m_player.setPosition((m_width / 2) - (playerAsset.m_width / 2), m_height - 32 - playerAsset.m_height);
		m_playerProjectiles.clear();
		
		m_enemyList.clear();
		m_enemyProjectiles.clear();
		
		// Populate the world with enemies
		for (int x = 0; x < 5; ++x)
		{
			for (int y = 0; y < 5; ++y)
			{
				m_enemyList.add(new Enemy(
						enemyAsset,
						256 + (x*128),
						256 + (y*128)));
			}
		}
		
		m_playerDied = false;
		
		// The frequency in ms that enemies will move
		m_enemyUpdateFrequency = 1000 - m_level*25;
		
		
		m_semaphore.release();
	}
	
	// Check if a projectile is out of the game world.
	private boolean isProjectileOutOfBounds(Projectile projectile)
	{			
		return projectile.getY() < 0 - projectile.getAsset().m_height || projectile.getY() > m_height;
	}
	
	private void changeScore(int newScore)
	{
		m_score = newScore;
		m_view.onScoreChange(m_score);
	}
	
	private void changeLevel(int newLevel) throws InterruptedException
	{
		m_level = newLevel;
		m_view.onLevelChange(newLevel);
		restartLevel();
	}
	
	// Get a list of drawable entities.
	public List<Entity> getEntityList() throws InterruptedException
	{
		List<Entity> entList = new ArrayList<Entity>();
		
		// Acquire the semaphore. This semaphore acts as a mutex to ensure thread-safety.
		// No modification to the model must be allowed while the view is retrieving the
		// model list.
		m_semaphore.acquire();
		
		if (m_player != null)
			entList.add(m_player);
		
		if (m_playerProjectiles != null)
			entList.addAll(m_playerProjectiles);
		
		if (m_enemyList != null)
			entList.addAll(m_enemyList);
		
		if (m_enemyProjectiles != null)
			entList.addAll(m_enemyProjectiles);
		
		// Release the semaphore
		m_semaphore.release();
		
		return entList;
	}
}
