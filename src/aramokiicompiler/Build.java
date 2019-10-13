package aramokiicompiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Aramok
 */
public class Build extends JPanel {

	private JTextPane textpane;
	private File file;
	private JScrollPane scroll;

	public float font_size = 13.0f;

	Parameters P;

	String code_end_op;
	String data_end_op;
	TextLineNumber tln;

	public Build() {
		P = new Parameters();
		setLayout(new BorderLayout());
		add(scroll = new JScrollPane(textpane = new JTextPane()), BorderLayout.CENTER);
		textpane.setFont(new Font("Courier new", Font.PLAIN, 13));
		scroll.setRowHeaderView(tln = new TextLineNumber(textpane));
	}

	BufferedReader reader;
	String line;
	int mode = 0;
	String code, data, sidezero;
	String[] sidetext = new String[2];
	ArrayList<Variable> label_model;
	int number_not_compiled_line;
	boolean currentproc;
	String procname;
	String procinfo;
	boolean curproc = false;
	boolean addproc = false;

	public void compile() {
		curproc = false;
		AramokIICompiler.R.code.resetprocs();
		AramokIICompiler.R.navigator.p.resetprocs();
		code_end_op = new String();
		data_end_op = new String();
		textpane.setText("");
		codeline = 0;
		dataline = 0;
		memory_model = new ArrayList<>();
		label_model = new ArrayList<>();
		number_not_compiled_line = 0;
		try {
			reader = new BufferedReader(new FileReader(new File("program.aasm")));
			line = reader.readLine().toLowerCase();
			while (line != null) {
				try {
					number_not_compiled_line++;
					if (mode == 2) {
						compilecode(line);
						if (line.contains("PROC")) {
							AramokIICompiler.R.code.addProc(line.substring(line.indexOf(";") + 5, line.length()));
							procname = line.substring(line.indexOf(";") + 5, line.length());
							procinfo = "Procedure " + procname + "\n";
							curproc = true;
							addproc = false;
						} else if ((line.contains(";@") || line.contains(";;")) && curproc) {
							procinfo += line.replaceAll(";", "").replaceAll("->", "\n").trim() + "\n";
							addproc = true;
						} else {
							curproc = false;
							if (addproc) {
								AramokIICompiler.R.navigator.p.addproc(procname, procinfo);
							}
							addproc = false;
						}
					} else if (mode == 1) {
						compiledata(line);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(AramokIICompiler.R, "Error at line : \"" + number_not_compiled_line
							+ "\" \n" + line.substring(0, line.indexOf(";")), "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (Pattern.compile("\\.data\\b", Pattern.CASE_INSENSITIVE).matcher(line).find()) {
					mode = 1;
				} else if (Pattern.compile("\\.code\\b", Pattern.CASE_INSENSITIVE).matcher(line).find()) {
					mode = 2;
				}
				line = reader.readLine();
			}
		} catch (FileNotFoundException ex) {
			error();
		} catch (IOException ex) {
			error("Error spliting code and data parameters");
		}

		for (int ii = 0; ii < label_model.size(); ii++) {
			code_end_op = code_end_op.replaceAll(label_model.get(ii).getName(),
					fill16(decToBin(label_model.get(ii).getAddress() + "")));
		}

		for (int ij = 0; ij < memory_model.size(); ij++) {
			String rep = fill16(decToBin(memory_model.get(ij).getAddress() + ""));
			String xrep = fill16(rep.substring(0, rep.length() - 8));
			code_end_op = code_end_op.replaceAll(("#" + memory_model.get(ij).getName()).trim(), xrep);
		}

		for (int ij = 0; ij < memory_model.size(); ij++) {
			code_end_op = code_end_op.replaceAll(memory_model.get(ij).getName() + "#",
					fill16(decToBin(memory_model.get(ij).getAddress() + "")));
		}

		for (int ij = 0; ij < memory_model.size(); ij++) {
			code_end_op = code_end_op.replaceAll(memory_model.get(ij).getName(),
					fill16(decToBin(memory_model.get(ij).getAddress() + "")));
		}

		code_end_op = code_end_op.replaceAll("offset", "").replaceAll("xrlbl", "").replaceAll("xrvar", "")
				.replaceAll(":", "").replaceAll("  ", " ");
		textpane.setText("" + code_end_op + "\n\n\n\n\n" + data_end_op + "\n");
		code_end_op = code_end_op.replaceAll(" ", "").replaceAll("\n\n", "\n");

		textpane.setText(textpane.getText() + "\nCompile operation done \n\t" + number_not_compiled_line + "\nCode:\t"
				+ codeline / 2 + "\nData:\t" + dataline + "\n\n\n");

		for (Variable label : label_model) {
			AramokIICompiler.R.navigator.p.addlabel(label.getName(),
					"Label refers to: \n" + Integer.toBinaryString(label.getAddress()) + ".b\n"
							+ Integer.toHexString(label.getAddress()) + ".h\n" + label.getAddress());
		}

		for (Variable variable : memory_model) {
			AramokIICompiler.R.navigator.p.addvariable(variable.getName(),
					"Byte address : \n" + Integer.toBinaryString(variable.getAddress()) + ".b\n"
							+ Integer.toHexString(variable.getAddress()) + ".h\n" + variable.getAddress());
		}

	}

	int codeline = 0;
	int dataline = 0;

	public void compilecode(String line) {

		String x_opcode;

		Matcher opcode_m = Pattern.compile(P.OPCODE_REGEX, Pattern.CASE_INSENSITIVE).matcher(line);

		int endline;
		if (line.contains(";")) {
			endline = line.indexOf(";");
		} else {
			endline = line.length();
		}

		if (opcode_m.find()) {
			x_opcode = line.substring(0, opcode_m.end()).trim(); // opcode
			for (String[] op : P.opcode) {
				if (op[0].equals(x_opcode)) {
					write(op[1] + " ");
					// double side
					if (Integer.parseInt(op[2]) == 2) {
						String[] side = line.substring(opcode_m.end(), endline).trim().split(",");
						String ls, rs;
						ls = getType(side[0], 0);
						rs = getType(side[1], 1);
						switch (ls + "" + rs) {
						case "rr":
							write("00 " + sidetext[0] + fill16(sidetext[1]) + "\n");
							break;
						case "rm":
							write("01 " + sidetext[0] + " " + sidetext[1] + "\n");
							break;
						case "ri":
							write("10 " + sidetext[0] + " " + sidetext[1] + "\n");
							break;
						case "mr":
							write("11 " + sidetext[1] + " " + sidetext[0] + "\n");
							break;
						}
					} else if (Integer.parseInt(op[2]) == 1) {
						String side = line.substring(opcode_m.end(), endline).trim();
						String ss = getType(side, 0);
						switch (ss + ss) {
						case "rr":
							if (Integer.parseInt(op[3]) == 1) {
								write("00 " + " 000 00000000 " + "00000" + sidetext[0] + "\n");
							} else {
								write("00 " + sidetext[0] + " 00000000" + " 00000000" + "\n");
							}
							break;
						case "mm":
							if (Integer.parseInt(op[3]) == 1) {
								write("01 000 " + sidetext[0] + "\n");
							} else {
								write("11 000 " + sidetext[0] + "\n");
							}
							break;
						case "ii":
							if (Integer.parseInt(op[3]) == 1) {
								write("10 000 " + sidetext[0] + "\n");
							} else {
								write("00 000 " + sidetext[0] + "\n");
							}
						}
					} else if (Integer.parseInt(op[2]) == 0) {
						write("00 000 00000000 00000000" + "\n");
					}
					break;
				}
			}
		} else if (line.trim().endsWith(":")) {
			// write ("\n"+ line.trim().replaceFirst(":", "") + "label shows" + (codeline) +
			// ": \n");
			label_model.add(new Variable(line.trim().replaceFirst(":", ""), codeline / 2));
			System.out.println(line + "" + codeline + " " + number_not_compiled_line);
		}
		// code += line+ "\n";
	}

	ArrayList<Variable> memory_model = new ArrayList<>();

	public void compiledata(String line) {
		Matcher byte_m = Pattern.compile("\\bbyte\\b", Pattern.CASE_INSENSITIVE).matcher(line);
		String byter;
		String[] each_bytes_maybe;
		if (byte_m.find()) {
			// write2(line.substring(0, byte_m.start()).trim() + " shows " + dataline +
			// "\n");
			byter = line.substring(byte_m.end(), line.length()).trim();
			memory_model.add(new Variable(line.substring(0, byte_m.start()).trim(), dataline));
		} else {
			byter = line.trim();
		}

		each_bytes_maybe = byter.split(",");
		for (String bayt : each_bytes_maybe) {
			Matcher isstring = Pattern.compile("\"", Pattern.CASE_INSENSITIVE).matcher(bayt);
			if (isstring.find()) {
				String feelfree = bayt.replaceAll("\"", " ").trim();
				char sequence;
				for (int i = 0; i < feelfree.length(); i++) {
					sequence = feelfree.charAt(i);
					int ascii = (int) sequence;
					write2("@" + Integer.toHexString(dataline) + "\n");
					write2("" + fill8(decToBin(ascii + "").trim()) + "\n");
					dataline++;
				}
			} else if (bayt.contains(".h") | bayt.contains(".d") | bayt.contains(".b")) {
				write2("@" + Integer.toHexString(dataline) + "\n");
				if (bayt.contains(".b")) {
					write2("" + fill8(bayt.replaceFirst(".b", "").trim()) + "\n");
				} else if (bayt.contains(".h")) {
					write2("" + fill8(hexToBin(bayt).trim()) + "\n");
				} else if (bayt.contains(".d")) {
					write2("" + fill8(decToBin(bayt).trim()) + "\n");
				}
				dataline++;
			}

		}

	}

	public String getType(String side, int a) {
		String sidetype = "";
		Matcher reg_m = Pattern.compile(P.REGISTER_REGEX, Pattern.CASE_INSENSITIVE).matcher(side);
		Matcher ptr_m = Pattern.compile(P.POINTER_REGEX, Pattern.CASE_INSENSITIVE).matcher(side);
		if (reg_m.find()) {
			String x_reg = side.substring(reg_m.start(), reg_m.end()).trim();
			for (String[] reg : P.register) {
				if (reg[0].equals(x_reg)) {
					sidetext[a] = reg[1];
					break;
				}
			}

			sidetype = "r"; // means register
		} else if (ptr_m.find()) {
			String ohey = side.replaceAll("\\bptr\\b", "100000000");
			String x_ptr = side.substring(ptr_m.start(), ptr_m.end()).trim();
			for (String[] ptr : P.pointer) {
				if (ptr[0].equals(x_ptr)) {
					sidetext[a] = "10000000 " + ptr[1];
				}
			}
			sidetype = "m";
		} else if (side.contains(".h") | side.contains(".d") | side.contains(".b") | side.contains("offset")) {
			sidetype = "i";
			if (side.contains(".b")) {
				side = side.replaceFirst(".b", "");
				sidetext[a] = fill16(side.trim());
			} else if (side.contains(".h")) {
				side = hexToBin(side);
				sidetext[a] = fill16(side.trim());
			} else if (side.contains(".d")) {
				side = decToBin(side);
				sidetext[a] = fill16(side.trim());
			} else if (side.contains("offset")) {
				sidetext[a] = side;
			}
		} else {
			if (side.trim().startsWith(":")) {
				sidetext[a] = " xrlbl:" + side + ":";
			} else {
				sidetext[a] = " xrvar:" + side + ":";
			}
			sidetype = "m";
		}
		return sidetype;
	}

	public String getOpcode(String reg) {
		for (int i = 0; i < P.opcode.length; i++) {
			if (P.opcode[i][0].equals(reg)) {
				return P.opcode[i][1];
			}
		}
		return null;
	}

	private String hexToBin(String hex) {
		String bin = "";
		String binFragment = "";
		int iHex;
		hex = hex.trim();
		hex = hex.replaceFirst(".h", "");

		for (int i = 0; i < hex.length(); i++) {
			iHex = Integer.parseInt("" + hex.charAt(i), 16);
			binFragment = Integer.toBinaryString(iHex);

			while (binFragment.length() < 4) {
				binFragment = "0" + binFragment;
			}
			bin += binFragment;
		}
		return bin;
	}

	private String decToBin(String dec) {
		dec = dec.replaceFirst(".d", "").trim();

		return Integer.toBinaryString(Integer.parseInt(dec));
	}

	public String fill8(String str) {
		int fillval = 8;
		String returnval = "";
		for (int i = 0; i < fillval - str.length(); i++) {
			returnval += "0";
		}
		return returnval + str;
	}

	public String fill16(String str) {
		int fillval = 16;
		String returnval = "";
		for (int i = 0; i < fillval - str.length(); i++) {
			returnval += "0";
		}
		return returnval + str;
	}

	public void error() {
		System.out.println("error");
	}

	public void error(String s) {
		System.out.println(s);
	}

	public void write(String s) {
		code_end_op += s;
		codeline++;
	}

	public void write2(String s) {
		data_end_op += s;
	}

	public void saveDocument() {
		try {
			FileWriter fw = new FileWriter(new File("rom.txt"), false);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(code_end_op);
			bw.close();
			System.out.println("Document Saved");

		} catch (Exception ex) {
			System.out.println(file + "");
			System.out.print("Saving Document error\n");
		}

		try {
			FileWriter fw = new FileWriter(new File("mem.txt"), false);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(data_end_op);
			bw.close();
			System.out.println("Document Saved");

		} catch (Exception ex) {
			System.out.println(file + "");
			System.out.print("Saving Document error\n");
		}

	}

	public JTextPane getTextpane() {
		return textpane;
	}

	public void notify(String notif) {
		textpane.setText(textpane.getText() + notif);
	}

	public void showerror(String notif) {

		JOptionPane.showMessageDialog(AramokIICompiler.R, notif, "Error", JOptionPane.ERROR_MESSAGE);

	}
}
