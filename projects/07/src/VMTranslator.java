import java.io.File;
import java.util.ArrayList;

public class VMTranslator {

	public static ArrayList<String> getVMFiles(String path) {
		ArrayList<String> vmFiles = new ArrayList<String>();
		File file = new File(path);

		if (file.isFile()) {
			if (path.endsWith(".vm")) {
				vmFiles.add(path);
			}
		} else if (file.isDirectory()) {
			File[] dirFiles = file.listFiles();
			for (File f: dirFiles) {
				if (f.getName().endsWith(".vm")) {
					vmFiles.add(f.getName());
				}
			}
		}
		return vmFiles;
	}

	public static void translateFile(String vmFile) {
		String asmFilepath = vmFile.substring(0, vmFile.lastIndexOf(".")) + ".asm";
		File asmFile = new File(asmFilepath);

		Parser parser = new Parser(vmFile);
		CodeWriter writer = new CodeWriter(asmFile);

		Parser.Command commandType = null;

		while (parser.hasMoreCommands()) {
			parser.advance();
			commandType = parser.commandType();
			if (commandType == Parser.Command.C_ARITHMETIC) {
				writer.WriteArithmetic(parser.arg1());
			} else if (commandType == Parser.Command.C_PUSH || commandType == Parser.Command.C_POP) {
				writer.WritePushPop(commandType, parser.arg1(), parser.arg2());
			}
		}
		writer.Close();
	}

	public static void main(String[] args) {
		String path = args[0];
		ArrayList<String> vmFiles = getVMFiles(path);

		for (String vmFile: vmFiles) {
			translateFile(vmFile);
		}
	}
}
