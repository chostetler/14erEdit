package fourteener.worldeditor.worldeditor.selection;

import org.bukkit.entity.Player;

public class SelectionManager {
	private double positionOne[] = {-1.0, -1.0, -1.0};
	private double positionTwo[] = {-1.0, -1.0, -1.0};
	
	// These two variables make the world editing math a bit easier
	private double mostNegativeCorner[] = new double[3];
	private double mostPositiveCorner[] = new double[3];
	
	public boolean updatePositionOne (double x, double y, double z, Player player) {
		positionOne[0] = x;
		// Use nested ternary operators to clamp y between 0 and 255
		positionOne[1] = y < 0 ? 0 : y > 255 ? 255 : y;
		positionOne[2] = z;
		player.sendMessage("§dFirst position updated to (" + Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z) + "); giving a volume of "
				+ Double.toString(Math.abs(positionOne[0] - positionTwo[0] * Math.signum(positionTwo[0]) + Math.signum(positionTwo[0]))
						* Math.abs(positionOne[1] - positionTwo[1] * Math.signum(positionTwo[1]) + Math.signum(positionTwo[1]))
						* Math.abs(positionOne[2] - positionTwo[2] * Math.signum(positionTwo[2]) + Math.signum(positionTwo[2]))));
		recalculateCorners();
		return true;
	}
	
	public boolean updatePositionTwo (double x, double y, double z, Player player) {
		positionTwo[0] = x;
		// Use nested ternary operators to clamp y between 0 and 255
		positionTwo[1] = y < 0 ? 0 : y > 255 ? 255 : y;
		positionTwo[2] = z;
		player.sendMessage("§dSecond position updated to (" + Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z) + "); giving a volume of "
				+ Double.toString(Math.abs(positionOne[0] - positionTwo[0] * Math.signum(positionTwo[0]) + Math.signum(positionTwo[0]))
						* Math.abs(positionOne[1] - positionTwo[1] * Math.signum(positionTwo[1]) + Math.signum(positionTwo[1]))
						* Math.abs(positionOne[2] - positionTwo[2] * Math.signum(positionTwo[2]) + Math.signum(positionTwo[2]))));
		recalculateCorners();
		return true;
	}
	
	// Check both selection positions have been defined (create a valid region)
	public boolean regionDefined () {
		if (positionOne[0] == -1.0 && positionOne[1] == -1.0 && positionOne[2] == -1.0)
			return false;
		if (positionTwo[0] == -1.0 && positionTwo[1] == -1.0 && positionTwo[2] == -1.0)
			return false;
		return true;
	}
	
	// This recalculates the two corners to help make the math a bit easier
	private void recalculateCorners () {
		// Check both selection positions have been defined first
		if (!regionDefined())
			return;
		
		// Set the X, then Y, then Z
		if (positionOne[0] <= positionTwo[0]) {
			mostNegativeCorner[0] = positionOne[0];
			mostPositiveCorner[0] = positionTwo[0];
		} else {
			mostNegativeCorner[0] = positionTwo[0];
			mostPositiveCorner[0] = positionOne[0];
		}
		
		if (positionOne[1] <= positionTwo[1]) {
			mostNegativeCorner[1] = positionOne[1];
			mostPositiveCorner[1] = positionTwo[1];
		} else {
			mostNegativeCorner[1] = positionTwo[1];
			mostPositiveCorner[1] = positionOne[1];
		}
		
		if (positionOne[2] <= positionTwo[2]) {
			mostNegativeCorner[2] = positionOne[2];
			mostPositiveCorner[2] = positionTwo[2];
		} else {
			mostNegativeCorner[2] = positionTwo[2];
			mostPositiveCorner[2] = positionOne[2];
		}
	}
	
	// Getter for the position
	public double[] getMostNegativeCorner () {
		return mostNegativeCorner;
	}
	
	// Getter for the position
	public double[] getMostPositiveCorner () {
		return mostPositiveCorner;
	}
	
	// Expands the selection in direction by amt
	public boolean expandSelection (double amt, String direction, Player player) {
		if (direction.equalsIgnoreCase("north")) { // -z
			mostNegativeCorner[2] = mostNegativeCorner[2] - amt;
			expandSelectionMessage(player);
			return true;
		} else if (direction.equalsIgnoreCase("south")) { // +z
			mostPositiveCorner[2] = mostPositiveCorner[2] + amt;
			expandSelectionMessage(player);
			return true;
		} else if (direction.equalsIgnoreCase("east")) { // +x
			mostPositiveCorner[0] = mostPositiveCorner[0] + amt;
			expandSelectionMessage(player);
			return true;
		} else if (direction.equalsIgnoreCase("west")) { // -x
			mostNegativeCorner[0] = mostNegativeCorner[0] - amt;
			expandSelectionMessage(player);
			return true;
		} else if (direction.equalsIgnoreCase("up")) { // +y
			mostPositiveCorner[1] = mostPositiveCorner[1] + amt;
			expandSelectionMessage(player);
			return true;
		} else if (direction.equalsIgnoreCase("down")) { // -y
			mostNegativeCorner[1] = mostNegativeCorner[1] - amt;
			expandSelectionMessage(player);
			return true;
		}
		return false;
	}
	
	// Message for the selection expansion
	private void expandSelectionMessage (Player player) {
		player.sendMessage("§dRegion expanded to "
				+ Double.toString(Math.abs(mostNegativeCorner[0] - mostPositiveCorner[0] * Math.signum(mostPositiveCorner[0]) + Math.signum(mostPositiveCorner[0]))
						* Math.abs(mostNegativeCorner[1] - mostPositiveCorner[1] * Math.signum(mostPositiveCorner[1]) + Math.signum(mostPositiveCorner[1]))
						* Math.abs(mostNegativeCorner[2] - mostPositiveCorner[2] * Math.signum(mostPositiveCorner[2]) + Math.signum(mostPositiveCorner[2])))
				+ " blocks.");
	}
	
	public boolean resetSelection () {
		double[] positionOneNew = {-1.0, -1.0, -1.0};
		double[] positionTwoNew = {-1.0, -1.0, -1.0};
		positionOne = positionOneNew;
		positionTwo = positionTwoNew;
		mostNegativeCorner = new double[3];
		mostPositiveCorner = new double[3];
		return true;
	}
}
