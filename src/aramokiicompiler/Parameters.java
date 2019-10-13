package aramokiicompiler;

/**
 *
 * @author Aramoki
 */
public class Parameters {
	static String[][] opcode, register, pointer;
	public static String OPCODE_REGEX, REGISTER_REGEX, POINTER_REGEX;

	public Parameters() {

		opcode = new String[][] { 
				{ "nop", "00000", "0", "0" },
				// {"out" , "00001" , "2" , "0"} ,
				{ "adc", "00010", "1", "0" }, { "adz", "00011", "1", "0" },
				{ "and", "00100", "2", "0" }, { "or", "00101", "2", "0" }, { "xor", "00110", "2", "0" },
				{ "not", "00111", "1", "0" },
				{ "rnd", "01000", "1", "0" }, { "mov", "01001", "2", "0" }, { "in", "01010", "1", "0" },
				// {"dmov" , "01011" , "2" , ""} ,
				{ "add", "01100", "2", "0" }, { "sub", "01101", "2", "0" }, { "inc", "01110", "1", "0" },
				{ "dec", "01111", "1", "0" },
				{ "dly", "10000", "1", "1" },
				// { "out" , "10001" ,"2" , ""} ,
				{ "int", "10010", "1", "0" }, { "out", "10011", "1", "1" },
				{ "test", "10100", "2", "0" }, { "jmp", "10101", "1", "0" }, { "call", "10110", "1", "0" },
				{ "ret", "10111", "0", "0" },
				{ "jz", "11000", "1", "0" }, { "jnz", "11001", "1", "0" }, { "jc", "11010", "1", "0" },
				{ "jnc", "11011", "1", "0" },
				// { "cmp" , "11100" ,"2" } ,
				{ "cmp", "11101", "2", " " }
				// { "int" , "11110" ,"2" } ,
				// { "rnd" , "11111" ,"2" } ,
		};

		register = new String[][] { { "ax", "000" }, { "bx", "001" }, { "cx", "010" }, { "dx", "011" }, { "ex", "100" },
				{ "fx", "101" }, { "gx", "110" }, { "hx", "111" }, };

		pointer = new String[][] {
				// ptr removed
				{ "abx", "00000000" }, { "cdx", "00000001" }, { "efx", "00000010" }, { "ghx", "00000011" }, };

		StringBuilder opcode_builder = new StringBuilder("");
		opcode_builder.append("(");
		for (String[] op : opcode) {
			opcode_builder.append("\\b").append(op[0]).append("\\b").append("|");
		}
		opcode_builder.deleteCharAt(opcode_builder.length() - 1);
		opcode_builder.append(")");
		OPCODE_REGEX = opcode_builder.toString();

		StringBuilder register_builder = new StringBuilder("");
		register_builder.append("(");
		for (String[] reg : register) {
			register_builder.append("\\b").append(reg[0]).append("\\b").append("|");
		}
		register_builder.deleteCharAt(register_builder.length() - 1);
		register_builder.append(")");
		REGISTER_REGEX = register_builder.toString();

		StringBuilder pointer_builder = new StringBuilder("");
		pointer_builder.append("(");
		for (String[] ptr : pointer) {
			pointer_builder.append("\\b").append(ptr[0]).append("\\b").append("|");
		}
		pointer_builder.deleteCharAt(pointer_builder.length() - 1);
		pointer_builder.append(")");
		POINTER_REGEX = pointer_builder.toString();
	}

}
