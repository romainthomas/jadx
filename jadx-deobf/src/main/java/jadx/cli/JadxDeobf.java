package jadx.cli;

import java.io.File;
import java.util.ArrayList;
import jadx.api.JadxDecompiler;
import jadx.api.JadxArgs;
import jadx.api.JavaClass;
import jadx.core.utils.exceptions.JadxArgsValidateException;

public class JadxDeobf {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: jadx <apk> <output>");
      System.exit(1);
    }

    JadxArgs jargs = new JadxArgs();
    File input = new File(args[0]);
    ArrayList<File> files = new ArrayList<>(1);
    files.add(input);
    jargs.setInputFiles(files);
    jargs.setShowInconsistentCode(true);
    jargs.setOutDir(new File(args[1]));

    JadxDecompiler jadx = new JadxDecompiler(jargs);
    jadx.load();
    jadx.save();
  }
}
