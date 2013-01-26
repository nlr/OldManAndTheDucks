package oldmanandtheducks;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import nlib.components.BasicComponentRenderable;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import oldmanandtheducks.events.BeatMissedEvent;
import oldmanandtheducks.events.BeatPressedEvent;

public strictfp final class BeatManager extends BasicComponentRenderable {
	
	private final EventBus eventBus;
	
	private final Random random;
	private final Set<Beat> beatsRequired;
	
	private int timeTillNextSequence;
	
	private Animation animationRed;
	private Animation animationBlue;
	private Animation animationYellow;
	private Animation animationGreen;
	
	private Animation animationCross;
	
	private Animation animationRedBox;
	private Animation animationBlueBox;
	private Animation animationYellowBox;
	private Animation animationGreenBox;
	
	private boolean wrongRed;
	private boolean wrongBlue;
	private boolean wrongYellow;
	private boolean wrongGreen;
	
	public BeatManager(long id, EventBus eventBus) {
		
		super(id);
		
		this.eventBus = eventBus;
		
		this.random = new Random();
		this.beatsRequired = new HashSet<Beat>();
		
		this.eventBus.register(this);
	}
	
	@Override
	public void init(GameContainer gameContainer) throws SlickException {
		
		super.init(gameContainer);
		
		this.timeTillNextSequence = Constants.TIME_PER_SEQUENCE;
		
		this.animationRed = new Animation(new SpriteSheet("gfx/Red.png", 96, 96), 100);
		this.animationBlue = new Animation(new SpriteSheet("gfx/Blue.png", 96, 96), 100);
		this.animationYellow = new Animation(new SpriteSheet("gfx/Yellow.png", 96, 96), 100);
		this.animationGreen = new Animation(new SpriteSheet("gfx/Green.png", 96, 96), 100);
		
		this.animationCross = new Animation(new SpriteSheet("gfx/Cross.png", 96, 96), 100);
		
		this.animationRedBox = new Animation(new SpriteSheet("gfx/RedBox.png", 96, 96), 100);
		this.animationBlueBox = new Animation(new SpriteSheet("gfx/BlueBox.png", 96, 96), 100);
		this.animationYellowBox = new Animation(new SpriteSheet("gfx/YellowBox.png", 96, 96), 100);
		this.animationGreenBox = new Animation(new SpriteSheet("gfx/GreenBox.png", 96, 96), 100);
		
		this.wrongRed = false;
		this.wrongBlue = false;
		this.wrongYellow = false;
		this.wrongGreen = false;
	}
	
	@Override
	public void update(GameContainer gameContainer, int delta) throws SlickException {
		
		super.update(gameContainer, delta);
		
		this.timeTillNextSequence -= delta;
		
		if (this.timeTillNextSequence <= 0) {
			
			if (!this.beatsRequired.isEmpty()) {
				
				this.eventBus.post(new BeatMissedEvent());
			}
			
			this.timeTillNextSequence = Constants.TIME_PER_SEQUENCE;
			
			this.nextSequence();
		}
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
		
		super.render(gameContainer, graphics);
		
		float w = 128f;
		
		float x = gameContainer.getScreenWidth() / 2f - w * 2f;
		float y = gameContainer.getScreenHeight() - 128f;
		
		// Red
		graphics.drawAnimation(this.animationRedBox, x, y);
		
		if (this.beatsRequired.contains(Beat.RED)) {
			
			graphics.drawAnimation(this.animationRed, x, y);
		}
		else if (this.wrongRed) {
			
			graphics.drawAnimation(this.animationCross, x, y);
		}
		
		x += w;
		
		// Blue
		graphics.drawAnimation(this.animationBlueBox, x, y);
		
		if (this.beatsRequired.contains(Beat.BLUE)) {
			
			graphics.drawAnimation(this.animationBlue, x, y);
		}
		else if (this.wrongBlue) {
			
			graphics.drawAnimation(this.animationCross, x, y);
		}
		
		x += w;
		
		// Yellow
		graphics.drawAnimation(this.animationYellowBox, x, y);
		
		if (this.beatsRequired.contains(Beat.YELLOW)) {
			
			graphics.drawAnimation(this.animationYellow, x, y);
		}
		else if (this.wrongYellow) {
			
			graphics.drawAnimation(this.animationCross, x, y);
		}
		
		x += w;
		
		// Green
		graphics.drawAnimation(this.animationGreenBox, x, y);
		
		if (this.beatsRequired.contains(Beat.GREEN)) {
			
			graphics.drawAnimation(this.animationGreen, x, y);
		}
		else if (this.wrongGreen) {
			
			graphics.drawAnimation(this.animationCross, x, y);
		}
	}
	
	@Override
	public void destroy(GameContainer gameContainer) throws SlickException {
		
		super.destroy(gameContainer);
		
		this.eventBus.unregister(this);
	}
	
	private void nextSequence() {
		
		this.beatsRequired.clear();
		
		int numberOfBeats = 1 + this.random.nextInt(3);
		
		while (this.beatsRequired.size() < numberOfBeats) {
			
			switch (this.random.nextInt(4)) {
			
			case 0:
				
				this.beatsRequired.add(Beat.RED);
				
				break;
				
			case 1:
				
				this.beatsRequired.add(Beat.BLUE);
				
				break;
				
			case 2:
				
				this.beatsRequired.add(Beat.YELLOW);
				
				break;
				
			case 3:
				
				this.beatsRequired.add(Beat.GREEN);
				
				break;

			default:
				
				this.beatsRequired.add(Beat.RED);
				
				break;
			}
		}
		
		this.wrongRed = false;
		this.wrongBlue = false;
		this.wrongYellow = false;
		this.wrongGreen = false;
	}
	
	@Subscribe
	public void handleBeatPressedEvent(BeatPressedEvent e) {
		
		if (this.beatsRequired.contains(e.getBeat())) {
			
			this.beatsRequired.remove(e.getBeat());
		}
		else {
			
			switch (e.getBeat()) {
			
			case RED:
				
				this.wrongRed = true;
				
				break;
				
			case BLUE:
				
				this.wrongBlue = true;
				
				break;
				
			case YELLOW:
				
				this.wrongYellow = true;
				
				break;
				
			case GREEN:
				
				this.wrongGreen = true;
				
				break;
			}
			
			this.eventBus.post(new BeatMissedEvent());
		}
	}
}