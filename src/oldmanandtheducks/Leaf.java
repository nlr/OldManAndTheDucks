package oldmanandtheducks;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import nlib.components.BasicComponentRenderable;

public strictfp final class Leaf extends BasicComponentRenderable {
	
	private final Random random; 
	
	private Animation animation;
	
	private float x;
	private float y;
	
	private float vx;
	
	public Leaf(long id) {
		
		super(id);
		
		this.random = new Random();
		
		this.vx = 1f - this.random.nextFloat() * 0.01f;
	}
	
	@Override
	public void init(GameContainer gameContainer) throws SlickException {
		
		super.init(gameContainer);
		
		this.x = 256f + this.random.nextInt(1280 + 256);
		this.y = -64f;
		
		String l;
		
		switch (this.random.nextInt(4)) {
		
		case 0:
			
			l = "gfx/Leaf1Color.png";
			
			break;
			
		case 1:
			
			l = "gfx/Leaf2Color.png";
			
			break;
			
		case 2:
			
			l = "gfx/Leaf3Color.png";
			
			break;
			
		case 3:
			
			l = "gfx/Leaf4Color.png";
			
			break;

		default:
			
			l = "gfx/Leaf1Color";
			
			break;
		}
		
		this.animation = new Animation(new SpriteSheet(l, 64, 64), 100);
	}
	
	@Override
	public void update(GameContainer gameContainer, int delta) throws SlickException {
		
		super.update(gameContainer, delta);
		
		this.y += 0.1f * this.random.nextFloat() * this.vx * delta;
		
		this.x -= (0.2f * this.random.nextFloat() - 0.05f) * this.vx * delta;
		
		if (this.y > 800 + 64f) {
			
			this.destroy(gameContainer);
		}
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		
		super.render(gameContainer, graphics);
		
		graphics.drawAnimation(this.animation, x, y);
	}
	
	@Override
	public float getDepth() {
		
		return Constants.DEPTH_LEAF;
	}
}
