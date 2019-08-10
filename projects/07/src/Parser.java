import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;


public class Parser {
	private static final String COMMENT = "//";
	private static final String PUSH = "push";
	private static final String POP = "pop";
	private static final String LABEL = "label";
	private static final String GOTO = "goto";
	private static final String IF = "if";
	private static final String FUNCTION = "function";
	private static final String RETURN = "return";
	private static final String CALL = "call";

	public enum Command {
		C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL;
	}

	private String[] arithmeticCommands = new String[] {"add", "sub", "neg", "eq", "gt", "lt", "and", "not"};
	private Scanner scanner;
	private String[] currentCommand;
	private String currentLine;
	private Boolean lineIsProcessed = false;

	public Parser(String file) {
		try {
			scanner = new Scanner(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean hasMoreCommands() {
		while (lineIsProcessed && scanner.hasNextLine()) {
			currentLine = scanner.nextLine().trim();
			if (!currentLine.startsWith(COMMENT)) {
				lineIsProcessed = false;
				return true;
			}
		}
		return false;
	}

	// advance should only be called after a call to hasMoreCommands() returns true.
	public void advance() {
		if (!lineIsProcessed) {
			currentCommand = currentLine.toLowerCase().split(" ");
		}
	}

	public Parser.Command commandType() {
		String type = currentCommand[0];
		if (Arrays.asList(arithmeticCommands).contains(type)) {
			return Command.C_ARITHMETIC;
		}
		if (type.equals(PUSH)) {
			return Command.C_PUSH;
		}
		if (type.equals(POP)) {
			return Command.C_POP;
		}
		if (type.equals(LABEL)) {
			return Command.C_LABEL;
		}
		if (type.equals(GOTO)) {
			return Command.C_GOTO;
		}
		if (type.equals(IF)) {
			return Command.C_IF;
		}
		if (type.equals(FUNCTION)) {
			return Command.C_FUNCTION;
		}
		if (type.equals(RETURN)) {
			return Command.C_RETURN;
		}
		if (type.equals(CALL)) {
			return Command.C_CALL;
		}
		return null;
	}

	public String arg1() {
		switch (this.commandType()) {
			case C_ARITHMETIC:
				return currentCommand[0];
			case C_PUSH:
			case C_POP:
			case C_LABEL:
			case C_GOTO:
			case C_IF:
			case C_FUNCTION:
			case C_CALL:
				return currentCommand[1];
			case C_RETURN:
			default:
				return null;
		}
	}

	public int arg2() {
		switch (this.commandType()) {
			case C_PUSH:
			case C_POP:
			case C_FUNCTION:
			case C_CALL:
				return Integer.parseInt(currentCommand[2]);
			case C_ARITHMETIC:
			case C_LABEL:
			case C_GOTO:
			case C_IF:
			case C_RETURN:
			default:
				return -1;
		}
	}
}
