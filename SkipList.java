import java.util.ArrayList;
import java.util.Random;

public class SkipList {

	private final int infinity = Integer.MAX_VALUE;

	private Node root;

	private int maximumHeight;

	private Random random;

	public SkipList(int size) {

		maximumHeight = (int) (Math.log(size) / Math.log(2));

		random = new Random();

		Node negativeInfinity = null;
		Node positiveInfinity = null;

		for (int i = 0; i < maximumHeight; i++) {

			positiveInfinity = new Node(null, positiveInfinity, getInfinity(), i);

			negativeInfinity = new Node(positiveInfinity, negativeInfinity, -getInfinity(), i);
		}

		root = negativeInfinity;

	}

	public boolean contains(int value) {
		return contains(value, getRoot());
	}

	public boolean contains(int value, Node currentNode) {

		if (currentNode == null) {
			return false;
		} else if (currentNode.getValue() == value) {
			return true;
		} else if (currentNode.getRight().getValue() > value) {
			return contains(value, currentNode.getDown());
		} else {
			return contains(value, currentNode.getRight());
		}

	}

	public void add(int value) {

		if (contains(value)) {
			return;
		}

		int maximumLevel = getMaximumLevel();

		ArrayList<Node> nearValues = getNearValues(value, false);

		Node newNode = null;

		for (int i = nearValues.size() - 1; i >= 0; i--) {

			Node currentNode = nearValues.get(i);

			if (currentNode.getLevel() <= maximumLevel) {
				newNode = new Node(currentNode.getRight(), newNode, value, currentNode.getLevel());
				currentNode.setRight(newNode);
			}
		}

	}

	public void remove(int value) {

		if (!contains(value))
			return;

		ArrayList<Node> nearValues = getNearValues(value, true);

		for (int i = nearValues.size() - 1; i >= 0; i--) {

			Node currentNode = nearValues.get(i);

			currentNode.setRight(currentNode.getRight().getRight());

		}

	}

	public Integer getMinimum() {

		Node currentNode = getRoot();

		while (currentNode.getLevel() != 0) {
			currentNode = currentNode.getDown();
		}

		if (currentNode.getRight().getValue() == getInfinity()) {
			return null;
		} else {
			return currentNode.getRight().getValue();
		}

	}

	public Integer getMaximum() {

		Node currentNode = getRoot();

		while (true) {
			if (currentNode.getRight().getValue() == getInfinity()) {

				if (currentNode.getLevel() == 0) {

					if (currentNode.getValue() == -getInfinity()) {
						return null;
					} else {
						return currentNode.getValue();
					}

				} else {
					currentNode = currentNode.getDown();
				}

			} else {
				currentNode = currentNode.getRight();
			}
		}
	}

	public Node getRoot() {
		return root;
	}

	public Integer maximumLessThan(int value) {
		if (contains(value))
			return value;

		ArrayList<Node> nearValues = getNearValues(value, false);

		int maximumValue = -getInfinity();

		for (Node node : nearValues) {
			if (node.getValue() != -getInfinity() && node.value > maximumValue)
				maximumValue = node.getValue();
		}

		return (maximumValue == -getInfinity()) ? null : maximumValue;

	}

	private int getRandomNumber(int n) {
		return random.nextInt(n);
	}

	private int getInfinity() {
		return infinity;
	}

	public int getMaximumHeight() {
		return maximumHeight;
	}

	private int getMaximumLevel() {

		int maximumLevel = 0;

		while (true) {

			int randomNumber = getRandomNumber(2);

			if (randomNumber == 1 && maximumLevel < getMaximumHeight()) {
				maximumLevel += 1;
			} else {
				return maximumLevel;
			}

		}

	}

	private ArrayList<Node> getNearValues(int value, boolean checkForEquality) {

		Node currentNode = getRoot();

		ArrayList<Node> nearValues = new ArrayList<Node>();

		while (currentNode != null) {

			if (checkForEquality) {

				if (currentNode.getValue() < value && currentNode.getRight().getValue() == value) {
					nearValues.add(currentNode);
					currentNode = currentNode.getDown();
				} else {
					if (currentNode.getValue() < value && currentNode.getRight().getValue() > value) {
						currentNode = currentNode.getDown();
					} else {
						currentNode = currentNode.getRight();
					}
				}

			} else {

				if (currentNode.getValue() < value && currentNode.getRight().getValue() > value) {
					nearValues.add(currentNode);
					currentNode = currentNode.getDown();
				} else {
					currentNode = currentNode.getRight();
				}
			}
		}

		return nearValues;
	}

	static class Node implements Comparable<Node> {

		private Node right, down;
		private int value, level;

		public Node(Node right, Node down, int value, int level) {
			this.right = right;
			this.down = down;
			this.value = value;
			this.level = level;
		}

		public Node getRight() {
			return right;
		}

		public Node getDown() {
			return down;
		}

		public int getValue() {
			return value;
		}

		public int getLevel() {
			return level;
		}

		public void setRight(Node right) {
			this.right = right;
		}

		public void setDown(Node down) {
			this.down = down;
		}

		@Override
		public String toString() {
			return "Node [right=" + right + ", down=" + down + ", value=" + value + ", level=" + level + "]";
		}

		@Override
		public int compareTo(Node o) {

			return getValue() - o.getValue();
		}

	}

}
