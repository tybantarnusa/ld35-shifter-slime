package com.tybprojekt.ld35.game;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.tybprojekt.ld35.game.entities.BubbledEntity;
import com.tybprojekt.ld35.game.entities.Player;

public class Collisions implements ContactListener {
	@Override
	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		
		if (a.getUserData() != null && b.getUserData() != null) {
		
			// Player can interact with their surrounding
			if (a.getUserData().equals("interactor") && b.getUserData().equals("interactable")) {
				interact(a, b);
			}
			
			if (b.getUserData().equals("interactor") && a.getUserData().equals("interactable")) {
				interact(b, a);
			}
		
		}
	}
	
	private void interact(Fixture player, Fixture other) {
		((Player) player.getBody().getUserData()).setNextTo((BubbledEntity) other.getBody().getUserData());
	}
	
	private void removeInteract(Fixture player, Fixture other) {
		((Player) player.getBody().getUserData()).setNextTo(null);
	}

	@Override
	public void endContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		
		if (a.getUserData() != null && b.getUserData() != null) {
		
			// Remove interactability
			if (a.getUserData().equals("interactor") && b.getUserData().equals("interactable")) {
				removeInteract(a, b);
			}
			if (b.getUserData().equals("interactor") && a.getUserData().equals("interactable")) {
				removeInteract(b, a);
			}
		
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
}
