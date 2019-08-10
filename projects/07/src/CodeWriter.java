import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CodeWriter {
	private static final String ADD = "add";
	private static final String SUB = "sub";
	private static final String NEG = "neg";
	private static final String EQ = "eq";
	private static final String GT = "gt";
	private static final String LT = "lt";
	private static final String AND = "and";
	private static final String OR = "or";
	private static final String NOT = "not";

	private String filename;
	private PrintWriter writer;
	private int currentLine;
	private int cmpJumpCount;

	public CodeWriter(File output) {
		try {
			writer = new PrintWriter(output);
			currentLine = 0;
			cmpJumpCount = 0;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void SetFilename(String filename) {
		this.filename = filename;
	}

	public void WriteArithmetic(String command) {
		writeComment(command);
		switch (command) {
			case "add":
				writeLine(mathOpTemplate() + "M=M+D\n");
				break;
			case "sub":
				writeLine(mathOpTemplate() + "M=M-D\n");
				break;
			case "and":
				writeLine(mathOpTemplate() + "M=M&D\n");
				break;
			case "or":
				writeLine(mathOpTemplate() + "M=M|D\n");
				break;
			case "eq":
				writeLine(mathCmpTemplate("JEQ"));
				break;
			case "gt":
				writeLine(mathCmpTemplate("JGT"));
				break;
			case "lt":
				writeLine(mathCmpTemplate("JLT"));
				break;
			case "not":
				writeLine("@SP\nA=M-1\nM=!M\n");
				break;
			case "neg":
				writeLine("D=0\n@SP\nA=M-1\nM=D-M\n");
				break;
		}
	}

	public void WritePushPop(Parser.Command command, String segment, int index) {
		if (command == Parser.Command.C_PUSH) {
			writeComment("push " + segment + " " + index);
			String segmentName;
			boolean isPointer = false;
			switch (segment) {
				case "constant":
					writeLine("@" + index + "\n" + "D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
					return;
				case "pointer":
					isPointer = true;
					switch (index) {
						case 0:
							segmentName = "THIS";
							break;
						case 1:
							segmentName = "THAT";
							break;
					}
					break;
				case "local":
					segmentName = "LCL";
					break;
				case "argument":
					segmentName = "ARG";
					break;
				case "this":
					segmentName = "THIS";
					break;
				case "that":
					segmentName = "THAT";
					break;
				case "static":
					segmentName = filename + "." + index;
					break;
				case "temp":
					segmentName = "R5";
					break;
				default:
					return;
			}
			writeLine(pushTemplate(segmentName, index, isPointer));
		} else if (command == Parser.Command.C_POP) {
			String segmentName;
			boolean isPointer = false;
			switch (segment) {
				case "pointer":
					isPointer = true;
					switch (index) {
						case 0:
							segmentName = "THIS";
							break;
						case 1:
							segmentName = "THAT";
							break;
					}
					break;
				case "local":
					segmentName = "LCL";
					break;
				case "argument":
					segmentName = "ARG";
					break;
				case "this":
					segmentName = "THIS";
					break;
				case "that":
					segmentName = "THAT";
					break;
				case "static":
					segmentName = filename + "." + index;
					break;
				case "temp":
					segmentName = "R5";
					break;
				default:
					return;
			}
			writeLine(popTemplate(segmentName, index, isPointer));
		}
	}

	private void writeLine(String line) {
		writer.println(line);
		currentLine++;
	}

	private void writeComment(String line) {
		writeLine("// " + line);
	}

	// template string for add, sub, and, or.
	private String mathOpTemplate() {
		return "@SP\n"
				+ "AM=M-1\n"
				+ "D=M\n"
				+ "A=A-1\n";
	}

	// template string for eq, gt, lt.
	private String mathCmpTemplate(String type) {
		cmpJumpCount++;
		return "@SP\n"
				+ "AM=M-1\n"
				+ "D=M\n"
				+ "A=A-1\n"
				+ "D=M-D\n"
				+ "@FALSE" + cmpJumpCount + "\n"
				+ "D;" + type + "\n"
				+ "@SP\n"
				+ "A=M-1\n"
				+ "M=-1\n"
				+ "@CONTINUE" + cmpJumpCount + "\n"
				+ "0;JMP\n"
				+ "(FALSE" + cmpJumpCount + ")\n"
				+ "@SP\n"
				+ "A=M-1\n"
				+ "M=0\n"
				+ "(CONTINUE" + cmpJumpCount + ")\n";
	}

	private String pushTemplate(String segment, int index, boolean isPointer) {
		String isPointerTemplate = isPointer ? "" : "@" + index + "\n" + "A=A+D\nD=M\n";
		return "@" + segment + "\n"
				+ "D=M\n"
				+ isPointerTemplate
				+ "@SP\n"
				+ "A=M\n"
				+ "M=D\n"
				+ "@SP\n"
				+ "M=M+1";
	}

	private String popTemplate(String segment, int index, boolean isPointer) {
		String isPointerTemplate = isPointer ? "" : "@" + index + "\n" + "D=A+D\n";
		// store temp variable on R13
		return "@" + segment + "\n"
				+ "D=A\n"
				+ isPointerTemplate
				+ "@R13\n"
				+ "M=D\n"
				+ "@SP\n"
				+ "AM=M-1\n"
				+ "D=M"
				+ "R13\n"
				+ "A=M\n"
				+ "M=D\n";
	}

	public void Close() {
		writer.close();
	}
}
