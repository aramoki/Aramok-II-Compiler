package aramokiicompiler;

/**
 *
 * @author Aramoki
 */
public class Variable {
	private String name;
	private int address;

	public Variable() {
	}

	public Variable(String name, int address) {
		this.name = name;
		this.address = address;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
